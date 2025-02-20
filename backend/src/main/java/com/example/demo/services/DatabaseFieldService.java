package com.example.demo.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.models.*;
import com.example.demo.repositories.*;

@Service
public class DatabaseFieldService {
    @Autowired
    private DatabaseFieldRepository databaseFieldRepository;

    public Optional<DatabaseField> findById(Long id) {
        return databaseFieldRepository.findById(id);
    }

    public DatabaseField save(DatabaseField databaseField) {
        return databaseFieldRepository.save(databaseField);
    }

    public Optional<List<DatabaseField>> findAll(Long userid) {
        return databaseFieldRepository.findAll(userid);
    }

    public void delete(long id) {
        databaseFieldRepository.deleteById(id);
    }
}