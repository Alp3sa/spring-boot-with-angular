package com.example.demo.repositories;

import com.example.demo.models.DatabaseField;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.Optional;
import java.util.List;

@Repository
public interface DatabaseFieldRepository extends JpaRepository<DatabaseField, Long> {
    @Query("SELECT db FROM databasefield db, databasetable dbtable, databaseinfo dbinfo WHERE dbtable.id=db.databaseTableId AND dbinfo.id=dbtable.databaseId AND dbinfo.user_id = :userid")
	Optional<List<DatabaseField>> findAll(Long userid);
}