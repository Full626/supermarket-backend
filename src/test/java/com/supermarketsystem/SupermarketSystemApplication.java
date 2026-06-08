package com;  // 改为 com

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.mapper")  // 添加Mapper扫描
public class SupermarketSystemApplication {
    public static void main(String[] args) {
        SpringApplication.run(SupermarketSystemApplication.class, args);
    }
}