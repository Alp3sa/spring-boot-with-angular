package com.example.demo.models;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import org.springframework.stereotype.Component;

@Entity(name = "databaserelation")
@Component
public class DatabaseRelation {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private Long databaseTableId;
	private Long databaseFieldId;
	private Long databaseReferencedTableId;
	private Long databaseReferencedFieldId;

	public DatabaseRelation() {
		this(null);
	}

	public DatabaseRelation(Long id) {
		this.setId(id);
	}

	public DatabaseRelation(Long databaseTableId, Long databaseFieldId, Long databaseReferencedTableId, Long databaseReferencedFieldId) {
		this.databaseTableId=databaseTableId;
		this.databaseFieldId=databaseFieldId;
		this.databaseReferencedTableId=databaseReferencedTableId;
		this.databaseReferencedFieldId=databaseReferencedFieldId;
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
	
	public Long getDatabaseFieldId() {
		return databaseFieldId;
	}

	public void setDatabaseFieldId(Long databaseFieldId) {
		this.databaseFieldId = databaseFieldId;
	}
	
	public Long getDatabaseReferencedTableId() {
		return databaseReferencedTableId;
	}

	public void setDatabaseReferencedTableId(Long databaseReferencedTableId) {
		this.databaseReferencedTableId = databaseReferencedTableId;
	}
	
	public Long getDatabaseReferencedFieldId() {
		return databaseReferencedFieldId;
	}

	public void setDatabaseReferencedFieldId(Long databaseReferencedFieldId) {
		this.databaseReferencedFieldId = databaseReferencedFieldId;
	}
}