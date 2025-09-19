package com.nsc.ipfind;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

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
        System.out.println("IP城市定位服务启动成功！");
        System.out.println("访问地址：http://39.97.174.238:8080/api/location/current");
        System.out.println("===========================================");
    }
}
