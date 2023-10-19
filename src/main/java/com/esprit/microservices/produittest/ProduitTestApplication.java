package com.esprit.microservices.produittest;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableEurekaClient
@SpringBootApplication
@EnableFeignClients
public class ProduitTestApplication {

    public static void main(String[] args) {
        SpringApplication.run(ProduitTestApplication.class, args);
    }

}
