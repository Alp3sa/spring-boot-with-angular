package com.example.demo.repositories;

import com.example.demo.models.DatabaseTable;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.Optional;
import java.util.List;

@Repository
public interface DatabaseTableRepository extends JpaRepository<DatabaseTable, Long> {
    @Query("SELECT db FROM databasetable db, databaseinfo dbinfo WHERE dbinfo.id=db.databaseId AND dbinfo.user_id = :userid")
	Optional<List<DatabaseTable>> findAll(Long userid);
}