package com.example.demo.services;

import java.nio.file.Path;
import java.util.stream.Stream;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;
import java.io.File;

public interface FilesStorageService {
  public void init();

  public void save(MultipartFile file, String username);

  public Resource load(String filename, String username, boolean isDB);

  public void delete(File file);

  //public void deleteAll();

  public Stream<Path> loadAll(String username, boolean isDB);
}