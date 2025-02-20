package com.example.demo.models;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties("app")
public class AppProperties {
    public static String puerto;
    
    public void setPuerto(String puerto) {
        this.puerto = puerto;
    }
    public String getPuerto() {
        return puerto;
    }
    
    public static Objeto objeto = new Objeto();

    public void setObjeto(Objeto objeto) {
        this.objeto = objeto;
    }
    public Objeto getObjeto() {
        return objeto;
    }

    public static class Objeto{
        public String prueba;

        public void setPrueba(String prueba) {
            this.prueba = prueba;
        }
        public String getPrueba() {
            return prueba;
        }
    }
}