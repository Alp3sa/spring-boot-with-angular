package com.example.demo.models;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import javax.annotation.PostConstruct;

@Component
@ConfigurationProperties
public class Properties {
    @Value("${trust.store}")
    public String prueba;

    @PostConstruct
    public void init(){
        System.out.println("Holaaaaaaaa "+prueba);
    }
}