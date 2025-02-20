package com.example.demo.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.models.*;
import com.example.demo.repositories.*;

@Service
public class DatabaseTableService {
    @Autowired
    private DatabaseTableRepository databaseTableRepository;

    public Optional<DatabaseTable> findById(Long id) {
        return databaseTableRepository.findById(id);
    }

    public DatabaseTable save(DatabaseTable databaseTable) {
        return databaseTableRepository.save(databaseTable);
    }

    public Optional<List<DatabaseTable>> findAll(Long userid) {
        return databaseTableRepository.findAll(userid);
    }

    public void delete(long id) {
        databaseTableRepository.deleteById(id);
    }
}