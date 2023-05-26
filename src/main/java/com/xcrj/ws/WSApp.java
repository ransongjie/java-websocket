package com.xcrj.ws;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class WSApp{
    public static void main(String[] args) {
        SpringApplication.run(WSApp.class, args);
        System.out.println("WSApp 启动");
    }
}