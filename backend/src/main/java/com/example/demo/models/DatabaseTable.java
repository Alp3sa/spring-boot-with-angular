package com.example.demo.models;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import org.springframework.stereotype.Component;
import java.util.List;
import com.example.demo.models.DatabaseField;
import javax.persistence.Transient;

@Entity(name = "databasetable")
@Component
public class DatabaseTable {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private Long databaseId;
	private String tableName;
	@Transient
	private boolean exportable;
	@Transient
	private String sheetName;
	@Transient
	private List<DatabaseField> databaseFields;
	@Transient
	private String queryConditions;

	public DatabaseTable() {
		this(null);
	}

	public DatabaseTable(Long id) {
		this.setId(id);
	}

	public DatabaseTable(Long databaseId, String tableName) {
		this.databaseId=databaseId;
		this.tableName=tableName;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getDatabaseId() {
		return databaseId;
	}

	public void setDatabaseId(Long databaseId) {
		this.databaseId = databaseId;
	}

	public String getTableName() {
		return tableName;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	public List<DatabaseField> getDatabaseFields() {
		return databaseFields;
	}

	public void setDatabaseFields(List<DatabaseField> databaseFields) {
		this.databaseFields = databaseFields;
	}

	public boolean getExportable() {
		return exportable;
	}

	public void setExportable(boolean exportable) {
		this.exportable = exportable;
	}

	public String getSheetName() {
		return sheetName;
	}

	public void setSheetName(String sheetName) {
		this.sheetName = sheetName;
	}
	
	public String getQueryConditions() {
		return queryConditions;
	}

	public void setQueryConditions(String queryConditions) {
		this.queryConditions = queryConditions;
	}
}