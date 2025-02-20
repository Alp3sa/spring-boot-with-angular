package com.example.demo.models;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import org.springframework.stereotype.Component;
import javax.persistence.Column;

import java.util.List;

@Entity(name = "databaseinfo")
@Component
public class Database {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String databaseName;
	private Long user_id;

	public Database() {
	}

	public Database(Long id) {
		this.setId(id);
	}

	public Database(String databaseName) {
		this.databaseName=databaseName;
	}

	public Database(String databaseName, Long user_id) {
		this.databaseName=databaseName;
		this.user_id=user_id;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getDatabaseName() {
		return databaseName;
	}

	public void setDatabaseName(String databaseName) {
		this.databaseName = databaseName;
	}

	public Long getUserId() {
		return user_id;
	}

	public void setUserId(Long user_id) {
		this.user_id = user_id;
	}

	public static String getDatabaseName(List<Database> listDatabases, Long id){
		for(Database db : listDatabases){
			System.out.println("db id: "+id+" "+db.getId()+" "+(id.equals(db.getId())));
			if(id.equals(db.getId())){
				return db.getDatabaseName();
			}
		}
		return null;
	}

	public static String getDabaseType(String databaseName){
        String fileExtension = databaseName.substring(databaseName.lastIndexOf('.')+1, databaseName.length());
        if("sql".equals(fileExtension)){
            return "sql";
        } else if(fileExtension.matches("accdb|mdb")){
            return "mdb";
        }
        return null;
    }
}