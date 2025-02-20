package com.example.demo.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.models.*;
import com.example.demo.repositories.*;

@Service
public class DatabaseService {
    @Autowired
    private DatabaseRepository databaseRepository;

    public Optional<Database> findById(Long id) {
        return databaseRepository.findById(id);
    }

    public Optional<Database> findByName(String name, Long userid) {
        return databaseRepository.findByName(name, userid);
    }

    public Database save(Database database) {
        return databaseRepository.save(database);
    }

    public Optional<List<Database>> findAll(Long userid) {
        return databaseRepository.findAll(userid);
    }

    public void delete(long id) {
        databaseRepository.deleteById(id);
    }
}