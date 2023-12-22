package com;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@SpringBootApplication
@EnableWebMvc
@EnableScheduling
@ComponentScan(basePackages = { "com.config.audit", "other.packages.if.any" })SD
public class ChineseChessAPI {

  public static void main(String[] args) {
    SpringApplication.run(ChineseChessAPI.class, args);
  }
}
