package com.example.demo.services;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Stream;

import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.FileSystemUtils;
import org.springframework.web.multipart.MultipartFile;
import java.nio.file.StandardCopyOption;

import java.io.OutputStream;
import java.io.InputStream;
import java.io.FileOutputStream;

@Service
public class FilesStorageServiceImpl implements FilesStorageService {

  private final Path root = Paths.get("uploads");

  @Override
  public void init() {
    try {
      Files.createDirectory(root);
    } catch (IOException e) {
      throw new RuntimeException("Could not initialize folder for upload!");
    }
  }

  @Override
  public void save(MultipartFile file, String username) {
    try{
      Files.copy(file.getInputStream(), this.root.resolve(username+"/bases de datos/"+file.getOriginalFilename()));

      /*InputStream is = file.getInputStream();
      byte[] buffer = new byte[is.available()];
      is.read(buffer);
      is.close();
      File targetFile = new File("uploads\\"+file.getOriginalFilename());
      OutputStream os = new FileOutputStream(targetFile);
      os.write(buffer);
      os.flush();
      os.close();*/
      System.out.println("The file was created.");
    } catch(Exception e){
      System.out.println("Could not store the file. Error: " + e.getMessage());
    }
  }

  @Override
  public Resource load(String filename, String username, boolean isDB) {
    try {
      Path file = root.resolve(username+"/consultas/"+filename);
      if(isDB){
        file = root.resolve(username+"/bases de datos/"+filename);
      }
      System.out.println("check load "+isDB+" "+file.toString());
      Resource resource = new UrlResource(file.toUri());
      System.out.println("check file "+filename+" "+file.toUri().toString());
      if (resource.exists() || resource.isReadable()) {
        return resource;
      } else {
        throw new RuntimeException("Could not read the file!");
      }
    } catch (MalformedURLException e) {
      throw new RuntimeException("Error: " + e.getMessage());
    }
  }

  public void delete(File file) {
    try{
      FileSystemUtils.deleteRecursively(file.toPath());
      System.out.println("The file was deleted.");
    } catch(Exception e){
      System.out.println("Could not delete the file. Error: " + e.getMessage());
    }
  }

  /*@Override
  public void deleteAll() {
    FileSystemUtils.deleteRecursively(root.toFile());
  }*/

  @Override
  public Stream<Path> loadAll(String username, boolean isDB) {
    try {
      System.out.println("LoadAll, isDB?");
      Path filepath;
      if(isDB){
        filepath = this.root.resolve(username+"/bases de datos");
      }
      else{
        filepath = this.root.resolve(username+"/consultas");
      }
      return Files.walk(filepath, 1).filter(path -> !path.equals(filepath)).map(filepath::relativize);
    } catch (IOException e) {
      throw new RuntimeException("Could not load the files!");
    }
  }
}