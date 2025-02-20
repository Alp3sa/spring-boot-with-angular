package com.example.demo.repositories;

import com.example.demo.models.Database;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.Optional;
import java.util.List;

@Repository
public interface DatabaseRepository extends JpaRepository<Database, Long> {
    @Query("SELECT db FROM databaseinfo db WHERE db.user_id = :userid")
	Optional<List<Database>> findAll(Long userid);

    @Query("SELECT db FROM databaseinfo db WHERE db.databaseName = :name AND db.user_id = :userid")
	Optional<Database> findByName(String name, Long userid);
}