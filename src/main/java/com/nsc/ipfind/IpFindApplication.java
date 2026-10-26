package com.nsc.ipfind;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

/**
 * IP定位服务主启动类
 * 提供获取当前城市的REST接口服务
 * 排除数据源自动配置，专注于IP定位功能
 */
@SpringBootApplication
@MapperScan("com.nsc.ipfind.mapper")
public class IpFindApplication {

    public static void main(String[] args) {
        SpringApplication.run(IpFindApplication.class, args);
        System.out.println("===========================================");
        System.out.println("http://localhost:8080/login.html");
    }
}
