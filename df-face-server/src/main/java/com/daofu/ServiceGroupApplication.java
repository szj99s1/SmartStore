package com.daofu;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * @author lc
 * @version 1.0
 * @date 2018-12-05 16:17
 **/
@EnableTransactionManagement
@MapperScan("com.daofu.*.dao")
@SpringBootApplication
public class ServiceGroupApplication {
    public static void main(String[] args) {
        SpringApplication.run(ServiceGroupApplication.class, args);
    }

}