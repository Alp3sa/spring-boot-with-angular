package com.example.demo.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.models.*;
import com.example.demo.repositories.*;

@Service
public class DatabaseRelationService {
    @Autowired
    private DatabaseRelationRepository databaseRelationRepository;

    public Optional<DatabaseRelation> findById(Long id) {
        return databaseRelationRepository.findById(id);
    }

    public void save(DatabaseRelation databaseRelation) {
        databaseRelationRepository.save(databaseRelation);
    }

    public List <DatabaseRelation> findAll() {
        return databaseRelationRepository.findAll();
    }

    public void delete(long id) {
        databaseRelationRepository.deleteById(id);
    }
}