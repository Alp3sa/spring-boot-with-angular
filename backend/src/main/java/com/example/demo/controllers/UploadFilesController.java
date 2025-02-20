package com.example.demo.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestMethod;
import com.example.demo.models.*;
import com.example.demo.utils.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;
import java.util.Arrays;
import org.hibernate.Session;

import com.example.demo.repositories.*;
import com.example.demo.services.*;
import com.example.demo.messages.*;
import java.util.Optional;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpHeaders;

import java.io.File;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;
import java.util.stream.Collectors;
import org.springframework.core.io.Resource;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import org.springframework.jdbc.support.rowset.SqlRowSetMetaData;

import org.springframework.transaction.annotation.Transactional;
import com.fasterxml.jackson.databind.*;

import javax.servlet.http.HttpServletRequest;
import com.example.demo.security.JwtUtils;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.apache.commons.lang3.StringUtils;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.FileNotFoundException;

@Controller
public class UploadFilesController {
    @Autowired
    FilesStorageService storageService;

    @Autowired
    private DatabaseService databaseService;

    @Autowired
    private DatabaseTableService databaseTableService;

    @Autowired
    private DatabaseFieldService databaseFieldService;

    @Autowired
    private DatabaseRelationService databaseRelationService;

    @Autowired
    private HttpServletRequest request;

    @Autowired
	UserRepository userRepository;

    @GetMapping("/uploadFile")
	public String index() {
        return "forward:/index.html";
    }

    @GetMapping("/databases")
    public ResponseEntity<List<FileInfo>> getListDatabases() {
        System.out.println("getting databases");

        String username = JwtUtils.getUserNameFromRequest(request);

        List<FileInfo> fileInfos = storageService.loadAll(username, true).map(path -> {
            String filename = path.getFileName().toString();
            String url = MvcUriComponentsBuilder
                .fromMethodName(UploadFilesController.class, "getDatabase", path.getFileName().toString(), true).build().toString();
    
            return new FileInfo(filename, url);
        }).collect(Collectors.toList());
    
        return ResponseEntity.status(HttpStatus.OK).body(fileInfos);
    }

    @RequestMapping(value="/delete/database", method = RequestMethod.DELETE)
    public ResponseEntity<Message> deleteDatabase(@RequestParam("filename") String filename) throws UsernameNotFoundException {
        System.out.println("deleting database");

        String username = JwtUtils.getUserNameFromRequest(request);
        User user = userRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("User Not Found with username: " + username));
        Long userid = user.getId();

        try{
            File f = new File("uploads\\"+username+"\\bases de datos\\"+filename);
            Runtime rt = Runtime.getRuntime();
            String fileExtension = filename.substring(filename.lastIndexOf('.')+1, filename.length());
            if(f.exists()){
                //Delete file
                storageService.delete(f);
                //Delete references
                Optional<Database> oldDB = databaseService.findByName(filename, userid);
                if(oldDB.isPresent()){
                    databaseService.delete(oldDB.get().getId());
                }
                // Delete database already loaded (MySQL)
                if("sql".equals(fileExtension)){
                    String databaseName = filename.substring(0, filename.lastIndexOf('.'));
                    Process pr1 = rt.exec("cmd /c \"cd C:\\xampp-v7.4.8\\mysql\\bin && mysql -u root -h localhost -e \"drop database "+username+"_"+databaseName+"\"");
                    pr1.waitFor();
                }
                return ResponseEntity.status(HttpStatus.OK).body(new Message(filename+" has been deleted."));
            }
            else{
                return ResponseEntity.status(HttpStatus.OK).body(new Message(filename+" doesnt exist."));
            }
        }
        catch(Exception e){
            return ResponseEntity.status(HttpStatus.OK).body(new Message("Error: "+filename+" cannot be deleted."));
        }
    }

    @RequestMapping(value="/upload", method = RequestMethod.POST)
    @Transactional
    public ResponseEntity<Message> uploadFile(@RequestParam("file") MultipartFile file) throws UsernameNotFoundException {
        System.out.println("uploading file");
        String message = "";
        String filename = file.getOriginalFilename();
        try{
            Runtime rt = Runtime.getRuntime();
            String username = JwtUtils.getUserNameFromRequest(request);
            User user = userRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("User Not Found with username: " + username));
            Long userid = user.getId();
            if(!filename.substring(0,filename.lastIndexOf('.')).matches("[0-9A-Za-z]+")){
                return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(new Message("El nombre del fichero sólo debe contener letras y números, además de la extensión (sql, mdb o accdb)."));
            }

            File f = new File("uploads\\"+username+"\\bases de datos\\"+filename);
            boolean dbExist = f.exists();
            if(dbExist){
                //Delete references
                Optional<Database> oldDB = databaseService.findByName(filename,userid);
                if(oldDB.isPresent()){
                    databaseService.delete(oldDB.get().getId());
                }
                //Delete file
                storageService.delete(f);
                System.out.println("The old file has been deleted.");
            }
            storageService.save(file, username);
            String fileExtension = filename.substring(filename.lastIndexOf('.')+1, filename.length());
            System.out.println(fileExtension);
            if("sql".equals(fileExtension)){
                String databaseFullName = filename.substring(0, filename.length());
                String databaseName = filename.substring(0, filename.lastIndexOf('.'));
                if(dbExist){
                    //Delete database already loaded
                    Process pr1 = rt.exec("cmd /c \"cd C:\\xampp-v7.4.8\\mysql\\bin && mysql -u root -h localhost -e \"drop database "+username+"_"+databaseName+"\"");
                    pr1.waitFor();
                }
                
                Process pr2 = rt.exec("cmd /c \"cd C:\\xampp-v7.4.8\\mysql\\bin && mysql -u root -h localhost -e \"create database "+username+"_"+databaseName+"\" && mysql -u root -h localhost "+username+"_"+databaseName+" < \""+f.getAbsolutePath()+"\"\"");
                pr2.waitFor();
                
                DriverManagerDataSource dataSource = new DriverManagerDataSource();
                dataSource.setDriverClassName("com.mysql.cj.jdbc.Driver");
                dataSource.setUrl("jdbc:mysql://localhost:3306/"+username+"_"+databaseName);
                dataSource.setUsername("root");
                
                /* -Get table names
                    String sql = "SHOW TABLES";*/
                /* -Get column and fields
                    String sql = "SELECT * FROM tabla_ejemplo1";*/
                /* -Get constraints-relationships
                select COLUMN_NAME, CONSTRAINT_NAME, REFERENCED_COLUMN_NAME, REFERENCED_TABLE_NAME
                from information_schema.KEY_COLUMN_USAGE
                where TABLE_NAME = 'tabla_ejemplo2';*/
                /*while (sqlRowSet.next()) {
                    //System.out.println("*"+sqlRowSet.getString(1)+" "+sqlRowSet.getString(2)+" "+sqlRowSet.getString(3));
                }*/

                /* -Get field names and types
                SqlRowSetMetaData sqlrsmd = sqlRowSet.getMetaData();
                for(int i=1;i<=sqlrsmd.getColumnCount();i++){
                    System.out.println(sqlrsmd.getColumnTypeName(i)+" "+sqlrsmd.getColumnTypeName(i));
                }*/

                //Save database info
                Database database = new Database(databaseFullName, userid);
                Database myDatabase = databaseService.save(database);
                //Preparing hasmap to set relationships
                List<List<String>> fieldList = new ArrayList<>();
                // Save tables
                JdbcTemplate template = new JdbcTemplate(dataSource);
                SqlRowSet sqlRowTablesSet = template.queryForRowSet("SHOW TABLES");
                System.out.println("check tables");
                while (sqlRowTablesSet.next()) {
                    System.out.println("^^^^^^^^^^^Table "+sqlRowTablesSet.getString(1));
                    DatabaseTable databaseTable = new DatabaseTable(myDatabase.getId(), sqlRowTablesSet.getString(1));
                    System.out.println("-"+databaseTable.getTableName());
                    DatabaseTable mydatabaseTable = databaseTableService.save(databaseTable);
                    // Save fields
                    SqlRowSet sqlRowFieldsSet = template.queryForRowSet("SELECT * FROM "+databaseTable.getTableName());
                    SqlRowSetMetaData sqlFieldsrsmd = sqlRowFieldsSet.getMetaData();
                    System.out.println("check fields");
                    for(int i=1;i<=sqlFieldsrsmd.getColumnCount();i++){
                        DatabaseField databaseField = new DatabaseField(mydatabaseTable.getId(), sqlFieldsrsmd.getColumnName(i), sqlFieldsrsmd.getColumnTypeName(i));
                        DatabaseField myDatabaseField = databaseFieldService.save(databaseField);
                        fieldList.add(Arrays.asList(mydatabaseTable.getId().toString(), mydatabaseTable.getTableName(), myDatabaseField.getId().toString(), myDatabaseField.getFieldName()));
                    }
                }
                System.out.println("check relations");
                /*
                // Save relations using information_schema.KEY_COLUMN_USAGE 
                SqlRowSet sqlRowRelationshipsSet = template.queryForRowSet("SELECT COLUMN_NAME, TABLE_NAME, CONSTRAINT_NAME, REFERENCED_COLUMN_NAME, REFERENCED_TABLE_NAME "+
                "FROM information_schema.KEY_COLUMN_USAGE "+
                "WHERE CONSTRAINT_SCHEMA = '"+username+"_"+databaseName+"' AND REFERENCED_TABLE_SCHEMA IS NOT NULL");
                
                while (sqlRowRelationshipsSet.next()) {
                    System.out.println("next relation");
                    DatabaseRelation databaseRelation = new DatabaseRelation();
                    for (int i = 0; i < fieldList.size(); i++) {
                        System.out.println("next field "+i);
                        List<String> myField = fieldList.get(i);
                        if(sqlRowRelationshipsSet.getString(2).equals(myField.get(1)) && sqlRowRelationshipsSet.getString(1).equals(myField.get(3))){
                            databaseRelation.setDatabaseTableId(Long.parseLong(myField.get(0)));
                            databaseRelation.setDatabaseFieldId(Long.parseLong(myField.get(2)));
                        }
                        else if(sqlRowRelationshipsSet.getString(5).equals(myField.get(1)) && sqlRowRelationshipsSet.getString(4).equals(myField.get(3))){
                            databaseRelation.setDatabaseReferencedTableId(Long.parseLong(myField.get(0)));
                            databaseRelation.setDatabaseReferencedFieldId(Long.parseLong(myField.get(2)));
                        }
                    }
                    databaseRelationService.save(databaseRelation);
                }*/

                //Save relacions using BufferedReader and reading the file itself.
                String databaseTableName="";
                String databaseFieldName;
                String databaseReferencedTableName;
                String databaseReferencedFieldName;
                BufferedReader sqlreader = null;
                try {
                    sqlreader = new BufferedReader(new FileReader(new File("uploads\\"+username+"\\bases de datos\\"+filename)));
                    String line;
                    while((line = sqlreader.readLine()) != null) {
                        if(line.indexOf("ALTER TABLE")!=-1){
                            databaseTableName=line.substring(StringUtils.ordinalIndexOf(line, "`", 1)+1,StringUtils.ordinalIndexOf(line, "`", 2));
                        }
                        if(line.indexOf("ADD CONSTRAINT")!=-1 && line.indexOf("FOREIGN KEY")!=-1){
                            databaseFieldName=line.substring(StringUtils.ordinalIndexOf(line, "`", 3)+1,StringUtils.ordinalIndexOf(line, "`", 4));
                            databaseReferencedTableName=line.substring(StringUtils.ordinalIndexOf(line, "`", 5)+1,StringUtils.ordinalIndexOf(line, "`", 6));
                            databaseReferencedFieldName=line.substring(StringUtils.ordinalIndexOf(line, "`", 7)+1,StringUtils.ordinalIndexOf(line, "`", 8));
                            //Get ids from tables and fields and save relation
                            DatabaseRelation databaseRelation = new DatabaseRelation();
                            for (int i = 0; i < fieldList.size(); i++) {
                                List<String> myField = fieldList.get(i);
                                if(databaseTableName.equals(myField.get(1)) && databaseFieldName.equals(myField.get(3))){
                                    databaseRelation.setDatabaseTableId(Long.parseLong(myField.get(0)));
                                    databaseRelation.setDatabaseFieldId(Long.parseLong(myField.get(2)));
                                }
                                else if(databaseReferencedTableName.equals(myField.get(1)) && databaseReferencedFieldName.equals(myField.get(3))){
                                    databaseRelation.setDatabaseReferencedTableId(Long.parseLong(myField.get(0)));
                                    databaseRelation.setDatabaseReferencedFieldId(Long.parseLong(myField.get(2)));
                                }
                            }
                            databaseRelationService.save(databaseRelation);
                            System.out.println("Relation: "+databaseTableName+" ("+databaseFieldName+") -> "+databaseReferencedTableName+" ("+databaseReferencedFieldName+")");
                            //Reset vars
                            databaseTableName="";
                        }
                    }
                }
                catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                catch (IOException e) {
                    e.printStackTrace();
                }
                finally {
                    if (sqlreader != null) {
                        try {
                            sqlreader.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
                
                System.out.println("finished");
            }
            else if(fileExtension.matches("accdb|mdb")){
                String databaseURL = "jdbc:ucanaccess://"+f.getAbsolutePath()+";sysSchema=true;immediatelyReleaseResources=true";
                Connection connection = DriverManager.getConnection(databaseURL);
                try {
                    //Save database info
                    String databaseFullName = filename.substring(0, filename.length());
                    Database database = new Database(databaseFullName, userid);
                    Database myDatabase = databaseService.save(database);
                    
                    //Preparing hasmap to set relationships
                    List<List<String>> fieldList = new ArrayList<>();

                    //Save tables
                    Statement statement = connection.createStatement();
                    ResultSet rsTables = statement.executeQuery("SELECT name FROM sys.MSysObjects WHERE Id>0 AND Flags=0");
                    while (rsTables.next()) {
                        DatabaseTable databaseTable = new DatabaseTable(myDatabase.getId(), rsTables.getString(1));
                        DatabaseTable myDatabaseTable = databaseTableService.save(databaseTable);
                        //Save fields
                        statement = connection.createStatement();
                        ResultSet rsFields = statement.executeQuery("SELECT * FROM "+rsTables.getString(1));
                        if(rsFields.next()) {
                            ResultSetMetaData rsmd = rsFields.getMetaData();
                            for (int i = 1; i <= rsmd.getColumnCount(); i++) {
                                DatabaseField databaseField = new DatabaseField(myDatabaseTable.getId(), rsmd.getColumnName(i), rsmd.getColumnTypeName(i));
                                DatabaseField myDatabaseField = databaseFieldService.save(databaseField);
                                fieldList.add(Arrays.asList(myDatabaseTable.getId().toString(), myDatabaseTable.getTableName(), myDatabaseField.getId().toString(), myDatabaseField.getFieldName()));
                            }
                        }
                    }

                    statement = connection.createStatement();
                    ResultSet rsRelationships = statement.executeQuery("SELECT * FROM sys.MSysRelationships WHERE grbit=0 AND szRelationship IS NOT NULL");
                    while (rsRelationships.next()) {
                        DatabaseRelation databaseRelation = new DatabaseRelation();
                        for (int i = 0; i < fieldList.size(); i++) {
                            List<String> myField = fieldList.get(i);
                            if(rsRelationships.getString("szObject").equals(myField.get(1)) && rsRelationships.getString("szColumn").equals(myField.get(3))){
                                databaseRelation.setDatabaseTableId(Long.parseLong(myField.get(0)));
                                databaseRelation.setDatabaseFieldId(Long.parseLong(myField.get(2)));
                            }
                            else if(rsRelationships.getString("szReferencedObject").equals(myField.get(1)) && rsRelationships.getString("szReferencedColumn").equals(myField.get(3))){
                                databaseRelation.setDatabaseReferencedTableId(Long.parseLong(myField.get(0)));
                                databaseRelation.setDatabaseReferencedFieldId(Long.parseLong(myField.get(2)));
                            }
                        }
                        databaseRelationService.save(databaseRelation);
                    }
                    System.out.println("finished");

                    /* -Get table names:
                    String sql = "SELECT name FROM sys.MSysObjects WHERE Id>0 AND Flags=0";*/
                    /* -Get ResultSet and column names:
                    String sql = "SELECT * FROM tabla_ejemplo1";*/
                    /* -Get relationships:
                    String sql = "SELECT * FROM sys.MSysRelationships WHERE grbit=0";*/
                    /*while (result.next()) {
                        System.out.println("*"+result.getInt(1));
                    }*/

                    /* -Get column names and types:
                    ResultSetMetaData rsmd = result.getMetaData();
                    for (int i = 1; i <= rsmd.getColumnCount(); i++) {
                        System.out.println(rsmd.getColumnName(i)+" "+rsmd.getColumnTypeName(i));
                    }*/
                    /* -Get fields:
                    while (result.next()) {
                        String nombre = result.getString("nombre");
                        String apellidos = result.getString("apellidos");
                        System.out.println("*"+nombre+" "+apellidos);
                    }*/
                    /* -Get relationships:
                    while (result.next()) {
                        String tablaReferencial = result.getString("szObject");
                        String tablaReferenciada = result.getString("szReferencedObject");
                        String campoReferencial = result.getString("szColumn");
                        String campoReferenciado = result.getString("szReferencedColumn");
                        String indiceCampoReferencial = result.getString("ccolumn");
                        String indiceCampoReferenciado = result.getString("icolumn");
                        System.out.println("*"+tablaReferencial+" ("+campoReferencial+") - "+tablaReferenciada+" ("+campoReferenciado+")");
                    }
                    */
                    statement.close();
                    System.out.println("check connection "+connection.isClosed()+" "+statement.isClosed());
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
                connection.close();
            }
            message = filename+" ha sido subido a su directorio personal.";
            System.out.println("Save totally finished");
            return ResponseEntity.status(HttpStatus.OK).body(new Message(message));
        } catch (Exception e) {
          System.out.println(e);
          message = "No se pudo subir el fichero: " + filename + "!";
          System.out.println("Save totally finished with errors");
          return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(new Message(message));
        }
    }

    @GetMapping("/database/{database:.+}")
    @ResponseBody
    public ResponseEntity<Resource> getDatabase(@PathVariable String database, boolean isDB) {
        String username = JwtUtils.getUserNameFromRequest(request);
        System.out.println("Recuperando fichero: "+database);
        Resource file = storageService.load(database, username, true);
        return ResponseEntity.ok()
            .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getFilename() + "\"").body(file);
    }
}