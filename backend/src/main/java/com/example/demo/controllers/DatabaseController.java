package com.example.demo.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestMethod;
import com.example.demo.models.*;
//import com.example.demo.utils.*;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;
import java.util.Arrays;
import org.hibernate.Session;
import org.hibernate.Transaction;

import com.example.demo.repositories.*;
import com.example.demo.services.*;
import com.example.demo.messages.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.context.annotation.Configuration;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import java.util.Optional;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PathVariable;
import java.util.Map;
import java.util.HashMap;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpHeaders;
import java.net.URI;

import java.io.FileOutputStream;
import java.nio.file.Paths;
import java.io.File;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;
import java.util.stream.Collectors;
import org.springframework.core.io.Resource;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import org.springframework.jdbc.support.rowset.SqlRowSetMetaData;

import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import com.example.demo.security.JwtUtils;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

@Controller
@Configuration
@EnableConfigurationProperties({AppProperties.class, Properties.class})
@RequestMapping(value="/database")
public class DatabaseController {
    private AppProperties appProperties;
    @Autowired
    private Properties properties = new Properties();
    
    @Autowired
    private DatabaseService databaseService;

    @Autowired
    private DatabaseTableService databaseTableService;

    @Autowired
    private DatabaseFieldService databaseFieldService;

    @Autowired
    private DatabaseRelationService databaseRelationService;

    @Autowired
    private HttpServletRequest request;

    @Autowired
	UserRepository userRepository;

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @RequestMapping(value="/createFile", method = RequestMethod.GET)
	public String index() {
        System.out.println("Check createFile");
        return "forward:/index.html";
    }

    @ResponseBody
    @RequestMapping(value="/getListDatabases", method = RequestMethod.GET)
    public Optional<List<Database>> findAllDatabases() throws UsernameNotFoundException {
        System.out.println("Consultando bases de datos");
        String username = JwtUtils.getUserNameFromRequest(request);
        // Get userid using its name.
        User user = userRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("User Not Found with username: " + username));
        Long userid = user.getId();
        return databaseService.findAll(userid);
    }
}