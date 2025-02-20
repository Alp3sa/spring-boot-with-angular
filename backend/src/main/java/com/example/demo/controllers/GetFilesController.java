package com.example.demo.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestMethod;
import com.example.demo.models.*;
import com.example.demo.utils.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Arrays;

import com.example.demo.repositories.*;
import com.example.demo.services.*;
import com.example.demo.messages.*;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpHeaders;

import java.io.File;

import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;
import java.util.stream.Collectors;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import java.io.FileInputStream;
import java.io.IOException;
import org.springframework.core.io.ByteArrayResource;

import javax.servlet.http.HttpServletRequest;
import com.example.demo.security.JwtUtils;

@Controller
public class GetFilesController {
    @Autowired
    FilesStorageService storageService;

    @Autowired
    private HttpServletRequest request;

    @GetMapping("/getFiles")
	public String index() {
        return "forward:/index.html";
    }

    @GetMapping("/queries")
    public ResponseEntity<List<FileInfo>> getListQueries() {
        System.out.println("getting queries");

        String username = JwtUtils.getUserNameFromRequest(request);

        List<FileInfo> fileInfos = storageService.loadAll(username, false).map(path -> {
            String filename = path.getFileName().toString();
            String url = MvcUriComponentsBuilder
                .fromMethodName(GetFilesController.class, "getQuery", path.getFileName().toString(), false).build().toString();
    
            return new FileInfo(filename, url);
        }).collect(Collectors.toList());
    
        return ResponseEntity.status(HttpStatus.OK).body(fileInfos);
    }
    
    @GetMapping("/queries/{filename:.+}")
    @ResponseBody
    public ResponseEntity<Resource> getQuery(@PathVariable String filename, boolean isDB) {
        System.out.println("Recuperando consulta: "+filename);

        String username = JwtUtils.getUserNameFromRequest(request);

        Resource file = storageService.load(filename, username, isDB);
        return ResponseEntity.ok()
        .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getFilename() + "\"").body(file);
    }
    
    @RequestMapping(value="/delete/query", method = RequestMethod.DELETE)
    public ResponseEntity<Message> deleteQuery(@RequestParam("filename") String filename) {
        System.out.println("deleting query");

        String username = JwtUtils.getUserNameFromRequest(request);

        try{
            File f = new File("uploads\\"+username+"\\consultas\\"+filename);
            String fileExtension = filename.substring(filename.lastIndexOf('.')+1, filename.length());
            System.out.println(filename+" "+fileExtension);
            if(f.exists()){
                //Delete file
                storageService.delete(f);
                return ResponseEntity.status(HttpStatus.OK).body(new Message(filename+" ha sido borrado."));
            }
            else{
                return ResponseEntity.status(HttpStatus.OK).body(new Message(filename+" no existe."));
            }
        }
        catch(Exception e){
            return ResponseEntity.status(HttpStatus.OK).body(new Message("Error: "+filename+" no puede ser borrado."));
        }
    }

}