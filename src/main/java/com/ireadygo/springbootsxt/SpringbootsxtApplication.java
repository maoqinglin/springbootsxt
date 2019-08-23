package com.ireadygo.springbootsxt;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ImportResource;

//@ImportResource(locations = {"classpath:beans.xml"})
@SpringBootApplication
public class SpringbootsxtApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringbootsxtApplication.class, args);
    }

}
