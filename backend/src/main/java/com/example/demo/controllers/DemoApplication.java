package com.example.demo.controllers;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import com.example.demo.models.*;
import com.example.demo.repositories.*;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
//import com.example.demo.config.JdbcConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import javax.annotation.PostConstruct;
import org.springframework.context.ApplicationContext;
import org.springframework.beans.factory.annotation.Autowired;

@SpringBootApplication
@ComponentScan(basePackages = {"com.example.demo"})
@EnableJpaRepositories(basePackages = "com.example.demo.repositories")
@EntityScan(basePackages = "com.example.demo.models")
public class DemoApplication {
	public static void main(String[] args) {
		/*AnnotationConfigApplicationContext contexto=
                new AnnotationConfigApplicationContext(JdbcConfiguration.class);*/
        //Persona p= new Persona("Alvaro","Gómez");
        //PersonaRepository pRepositorio= contexto.getBean(PersonaRepository.class);
		//pRepositorio.save(p);
		SpringApplication.run(DemoApplication.class, args);
	}
}