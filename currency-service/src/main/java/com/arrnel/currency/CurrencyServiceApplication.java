package com.arrnel.currency;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableFeignClients
@SpringBootApplication
public class CurrencyServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(CurrencyServiceApplication.class, args);
    }

}
