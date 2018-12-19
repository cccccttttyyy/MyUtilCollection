package com.tsrain.springBootUtils;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.tsrain.springBootUtils.mapper")
public class SpringBootUtilsApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringBootUtilsApplication.class, args);
    }

}

