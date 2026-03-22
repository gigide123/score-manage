package com.example.scoremanage;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.example.scoremanage.mapper") // 扫描 Mapper 接口
public class ScoreManageApplication {
    public static void main(String[] args) {
        SpringApplication.run(ScoreManageApplication.class, args);
    }
}