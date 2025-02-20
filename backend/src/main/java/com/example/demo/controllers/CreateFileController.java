package com.example.demo.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestMethod;
import com.example.demo.models.*;
import com.example.demo.utils.*;
//import com.example.demo.utils.*;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;
import java.util.Arrays;
import org.hibernate.Session;
import org.hibernate.Transaction;

import com.example.demo.repositories.*;
import com.example.demo.services.*;
import com.example.demo.messages.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.context.annotation.Configuration;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import java.util.Optional;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PathVariable;
import java.util.Map;
import java.util.HashMap;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpHeaders;
import java.net.URI;

import java.io.FileOutputStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.io.File;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFFont;
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

import org.springframework.core.io.UrlResource;

import java.net.MalformedURLException;

import javax.servlet.http.HttpServletRequest;
import com.example.demo.security.JwtUtils;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

@Controller
@Configuration
@EnableConfigurationProperties({AppProperties.class, Properties.class})
public class CreateFileController {
    private AppProperties appProperties;
    @Autowired
    private Properties properties = new Properties();
    
    @Autowired
    private HttpServletRequest request;
    
    @Autowired
	UserRepository userRepository;

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @GetMapping("/createFile")
	public String index() {
        return "forward:/index.html";
    }

    @RequestMapping("/add")
	public String index2() {
        return "forward:/index.html";
    }

    @RequestMapping("/update/{id}")
	public String index2(@PathVariable("id") Long id) {
        return "forward:/index.html";
    }

    @RequestMapping(value="/list", method = RequestMethod.GET)
	public String index3() {
        //return service.findById(1L);
        
        return "forward:/index.html";
        /*System.out.println("check redirect");
		HttpHeaders headers = new HttpHeaders();
        headers.setLocation(URI.create("people/add"));
        return new ResponseEntity<>(headers, HttpStatus.MOVED_PERMANENTLY);*/
    }

    @RequestMapping(value="/gen", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity genFile(@RequestBody List<Object> dataToExport) throws UsernameNotFoundException {
        String username = JwtUtils.getUserNameFromRequest(request);
        User user = userRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("User Not Found with username: " + username));
        Long userid = user.getId();

        ObjectMapper mapper = new ObjectMapper();
        List<Database> databasesToExport = new ArrayList();
        List<DatabaseTable> tablesToExport = new ArrayList();
        List<DatabaseField> fieldsToExport = new ArrayList();
        String filename = "ejemplo0";
        try{
            if(dataToExport.get(0)!=null){
                filename = dataToExport.get(0).toString();
            }
            List databases = (List) dataToExport.get(1);
            List tables = (List) dataToExport.get(2);
            List fields = (List) dataToExport.get(3);
            System.out.println("check databases size "+databases.size());
            for(int i = 0;i<databases.size();i++){
                databasesToExport.add(mapper.readValue(mapper.writeValueAsString(databases.get(i)),Database.class));
            }
            System.out.println("check tables size "+tables.size());
            for(int i = 0;i<tables.size();i++){
                tablesToExport.add(mapper.readValue(mapper.writeValueAsString(tables.get(i)),DatabaseTable.class));
            }
            System.out.println("check fields size "+fields.size());
            for(int i = 0;i<fields.size();i++){
                fieldsToExport.add(mapper.readValue(mapper.writeValueAsString(fields.get(i)),DatabaseField.class));
            }
        } catch(Exception e){
            e.printStackTrace();
        }

        try{
            if(tablesToExport.size()>0 && fieldsToExport.size()>0){
                XSSFWorkbook workbook = new XSSFWorkbook();
                //Setting styles
                FicheroUtils ficheroUtils = new FicheroUtils();
                XSSFFont headerFont = ficheroUtils.crearTipoDeFuente(workbook, "bold", null, "11", null);
                XSSFCellStyle headerCellStyle = ficheroUtils.estiloCabeceraPorDefecto(workbook, headerFont);

                //Creating the sheets
                List<Sheet> sheets = new ArrayList();
                int sheetIndex = 0;
                int columnIndex;
                for(DatabaseTable databaseTable : tablesToExport){
                    List<DatabaseField> fields = databaseTable.getDatabaseFields();
                    if(fields!=null && databaseTable.getExportable()){//Check if table has been selected
                        //Adding new sheet
                        sheets.add(workbook.createSheet((sheetIndex+1)+". "+databaseTable.getSheetName()));
                        //Creating header
                        Row headerRow = sheets.get(sheetIndex).createRow(0);
                        Cell cell;
                        columnIndex = 0;
                        List<String> columnNames = new ArrayList();
                        for(DatabaseField databaseField : fields){
                            if(databaseTable.getId().equals(databaseField.getDatabaseTableId())){
                                cell = headerRow.createCell(columnIndex);
                                cell.setCellValue(databaseField.getFieldName());
                                cell.setCellStyle(headerCellStyle);
                                columnIndex++;
                                columnNames.add(databaseField.getFieldName());
                            }
                        }
                        
                        //Creating body
                        String databaseName = Database.getDatabaseName(databasesToExport, databaseTable.getDatabaseId());
                        String fileExtension = Database.getDabaseType(databaseName);
                        //Setting query conditions
                        String queryConditions = "";
                        System.out.println("***"+databaseTable.getQueryConditions()+"**"+(!"".equals(databaseTable.getQueryConditions())));
                        if(databaseTable.getQueryConditions()!=null && !"".equals(databaseTable.getQueryConditions().trim())){
                            queryConditions = " WHERE "+databaseTable.getQueryConditions();
                        }

                        if("sql".equals(fileExtension)){//MySQL databases
                            DriverManagerDataSource dataSource = new DriverManagerDataSource();
                            dataSource.setDriverClassName("com.mysql.cj.jdbc.Driver");
                            dataSource.setUrl("jdbc:mysql://localhost:3306/"+username+"_"+databaseName.substring(0,databaseName.lastIndexOf('.')));
                            dataSource.setUsername("root");

                            String fieldNamesToSelect = columnNames.get(0);
                            for(int column = 1; column<columnNames.size(); column++){
                                fieldNamesToSelect += ", "+columnNames.get(column);
                            }
                            String sql = "SELECT "+fieldNamesToSelect+" FROM "+databaseTable.getTableName()+queryConditions;
                            JdbcTemplate template = new JdbcTemplate(dataSource);
                            SqlRowSet rs;
                            try{
                                rs = template.queryForRowSet(sql);
                            } catch(Exception e){
                                System.out.println("Error de sintaxis.");
                                //return "Alguna de las condiciones establecidas no tiene la sintaxis correcta.";
                                return ResponseEntity.ok().body("Alguna de las condiciones establecidas no tiene la sintaxis correcta.");
                            }
                            int numRow = 1;
                            while (rs.next()) {
                                headerRow = sheets.get(sheetIndex).createRow(numRow++);
                                columnIndex = 0;
                                while(true){
                                    try{
                                        String str = rs.getObject(columnIndex+1).toString();
                                        System.out.println("*"+str+" ");
                                        cell = headerRow.createCell(columnIndex);
                                        cell.setCellValue(str);
                                        columnIndex++;
                                    }
                                    catch(Exception e){
                                        System.out.println("Error getting values on column "+columnIndex+": "+e);
                                        break;
                                    }
                                }
                            }

                            /* -Get table names
                                String sql = "SHOW TABLES";*/
                            /* -Get column and fields
                                String sql = "SELECT * FROM tabla_ejemplo1";*/
                            /* -Get constraints-relationships
                            select COLUMN_NAME, CONSTRAINT_NAME, REFERENCED_COLUMN_NAME, REFERENCED_TABLE_NAME
                            from information_schema.KEY_COLUMN_USAGE
                            where TABLE_NAME = 'tabla_ejemplo2';*/
                            /*
                            JdbcTemplate template = new JdbcTemplate(dataSource);
                            SqlRowSet sqlRowTablesSet = template.queryForRowSet(query);
                            while (sqlRowSet.next()) {
                                //System.out.println("*"+sqlRowSet.getString(1)+" "+sqlRowSet.getString(2)+" "+sqlRowSet.getString(3));
                            }*/

                            /* -Get field names and types
                            SqlRowSetMetaData sqlrsmd = sqlRowSet.getMetaData();
                            for(int i=1;i<=sqlrsmd.getColumnCount();i++){
                                System.out.println(sqlrsmd.getColumnTypeName(i)+" "+sqlrsmd.getColumnTypeName(i));
                            }*/
                        } else if("mdb".equals(fileExtension)){//Access databases
                            File f = new File("uploads\\"+username+"\\bases de datos\\"+databaseName);
                            String databaseURL = "jdbc:ucanaccess://"+f.getAbsolutePath()+";singleconnection=true;sysSchema=true";
                            try (Connection connection = DriverManager.getConnection(databaseURL)) {
                                Statement statement = connection.createStatement();
                                System.out.println("check conection "+columnNames.size());
                                String fieldNamesToSelect = columnNames.get(0);
                                for(int column = 1; column<columnNames.size(); column++){
                                    fieldNamesToSelect += ", "+columnNames.get(column);
                                }
                                System.out.println("SELECT "+fieldNamesToSelect+" FROM "+databaseTable.getTableName()+" WHERE user_id="+userid+queryConditions);
                                String sql = "SELECT "+fieldNamesToSelect+" FROM "+databaseTable.getTableName()+queryConditions;
                                ResultSet rs = statement.executeQuery(sql);
                                int numRow = 1;
                                while (rs.next()) {
                                    headerRow = sheets.get(sheetIndex).createRow(numRow++);
                                    columnIndex = 0;
                                    while(true){
                                        try{
                                            String str = rs.getObject(columnIndex+1).toString();
                                            System.out.println("*"+str+" ");
                                            cell = headerRow.createCell(columnIndex);
                                            cell.setCellValue(str);
                                            columnIndex++;
                                        }
                                        catch(Exception e){
                                            System.out.println("Error getting values on column "+columnIndex+": "+e);
                                            break;
                                        }
                                    }
                                }
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
                                connection.close();
                            } catch (SQLException ex) {
                                ex.printStackTrace();
                            }
                        }
                        //Adjust the width of the column based on its title, except in case it's too short
                        for (int e = 0; e < columnNames.size() + 3; e++) {
                            sheets.get(sheetIndex).autoSizeColumn(e);
                            if (sheets.get(sheetIndex).getColumnWidth(e) < 4000) {
                                sheets.get(sheetIndex).setColumnWidth(e, 4000);
                            }
                        }
                        sheetIndex++;
                    }
                }
                // Creating the file
                if(sheets.size()>0){
                    FileOutputStream fos = new FileOutputStream(
                        Paths.get("").toAbsolutePath().toString().replace("/", "\\\\") + "\\uploads\\"+username+"\\consultas\\" + filename + ".xlsx");
                    workbook.write(fos);
                    fos.close();
                }
                else {
                    System.out.println("El fichero no se generó porque no contiene datos. Seleccione las hojas a generar en el menú de opciones e incluya al menos un campo.");
                    //return "El fichero no se generó porque no contiene datos. Seleccione las hojas a generar en el menú de opciones e incluya al menos un campo en alguna de las tablas.";
                    return ResponseEntity.ok().body("El fichero no se generó porque no contiene datos. Seleccione las hojas a generar en el menú de opciones e incluya al menos un campo en alguna de las tablas.");
                }
            }
            else{
                System.out.println("No hay tablas ni campos.");
                return ResponseEntity.ok().body("No hay tablas ni campos.");
            }
        } catch (Exception e) {
            System.out.println("Error al crear el fichero.");
            e.printStackTrace();
            return ResponseEntity.ok().body("Error al crear el fichero.");
        }
        
        try {
            Path path = Paths.get(Paths.get("").toAbsolutePath().toString().replace("/", "\\\\") + "\\uploads\\"+username+"\\consultas\\" + filename + ".xlsx");
            System.out.println("Leyendo el fichero: "+path.toUri().toString());
            Resource resource = new UrlResource(path.toUri());
      
            if (resource.exists() || resource.isReadable()) {
                return  ResponseEntity.ok().body(filename + ".xlsx");
                //return  ResponseEntity.ok().body("https://localhost:8084/queries/" + filename + ".xlsx");
                /*return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + path.toString() + "\"").body(resource);*/  
            } else {
                return ResponseEntity.ok().body("No es posible abrir el fichero, puede que haya sido borrado.");
            }
        } catch (MalformedURLException e) {
            return ResponseEntity.ok().body("Error al leer el fichero.");
        }
    }

    @Autowired
    FilesStorageService storageService;

    @RequestMapping("/assets/{path}")
	public String getAssets(@PathVariable("id") Long id) {
        return "forward:/assets/{path}";
    }
}