package com.debug.pmp.server;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ImportResource;

/**
 * @author gentleman_qiang
 */
@SpringBootApplication
@MapperScan(basePackages = "com.debug.pmp.model.mapper")
@ImportResource(value = {"classpath:spring/spring-dubbo.xml"})
public class MainApplication extends SpringBootServletInitializer {

    public static void main(String[] args){
        ConfigurableApplicationContext run = SpringApplication.run(MainApplication.class, args);
    }
}
