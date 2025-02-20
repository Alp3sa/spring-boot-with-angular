package com.example.demo.models;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import org.springframework.stereotype.Component;
import javax.persistence.Transient;

@Entity(name = "databasefield")
@Component
public class DatabaseField {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private Long databaseTableId;
	private String fieldName;
	private String fieldType;
	@Transient
	private Boolean selected;

	public DatabaseField() {
		this(null);
	}

	public DatabaseField(Long id) {
		this.setId(id);
	}

	public DatabaseField(Long databaseTableId, String fieldName, String fieldType) {
		this.databaseTableId=databaseTableId;
		this.fieldName=fieldName;
		this.fieldType=fieldType;
		this.selected=false;
	}

	public DatabaseField(Long databaseTableId, String fieldName, String fieldType, Boolean selected) {
		this.databaseTableId=databaseTableId;
		this.fieldName=fieldName;
		this.fieldType=fieldType;
		this.selected=selected;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getDatabaseTableId() {
		return databaseTableId;
	}

	public void setDatabaseTableId(Long databaseTableId) {
		this.databaseTableId = databaseTableId;
	}

	public String getFieldName() {
		return fieldName;
	}

	public void setFieldName(String fieldName) {
		this.fieldName = fieldName;
	}

	public String getFieldType() {
		return fieldType;
	}

	public void setFieldType(String fieldType) {
		this.fieldType = fieldType;
	}

	public Boolean getSelected() {
		return selected;
	}

	public void setSelected(Boolean selected) {
		this.selected = selected;
	}
}