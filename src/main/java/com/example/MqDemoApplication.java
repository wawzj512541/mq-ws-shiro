package com.example;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan({"com.example.web.dao","com.example.shiro.*.dao"})
public class MqDemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(MqDemoApplication.class, args);
    }

}
