package com.yf.summarize.summarize;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import tk.mybatis.spring.annotation.MapperScan;

@SpringBootApplication
@MapperScan(basePackages = "com.yf.summarize.summarize.mapper")
public class SummarizeApplication {

    public static void main(String[] args) {
        SpringApplication.run(SummarizeApplication.class, args);
    }

}
