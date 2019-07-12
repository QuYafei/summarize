package com.yf.summarize.summarize;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.scheduling.annotation.EnableScheduling;
import tk.mybatis.spring.annotation.MapperScan;

@SpringBootApplication
@MapperScan(basePackages = "com.yf.summarize.summarize.mapper")
@EnableScheduling
@EnableCaching
public class SummarizeApplication {

    public static void main(String[] args) {
        SpringApplication.run(SummarizeApplication.class, args);
    }

}
