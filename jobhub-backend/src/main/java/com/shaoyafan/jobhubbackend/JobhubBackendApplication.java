package com.shaoyafan.jobhubbackend;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@SpringBootApplication
@MapperScan("com.shaoyafan.jobhubbackend.mapper")
@EnableAspectJAutoProxy(proxyTargetClass = true, exposeProxy = true)
public class JobhubBackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(JobhubBackendApplication.class, args);
    }

}
