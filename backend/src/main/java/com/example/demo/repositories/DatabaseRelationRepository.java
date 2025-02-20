package com.example.demo.repositories;

import com.example.demo.models.DatabaseRelation;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;

@Repository
public interface DatabaseRelationRepository extends JpaRepository<DatabaseRelation, Long> {

}