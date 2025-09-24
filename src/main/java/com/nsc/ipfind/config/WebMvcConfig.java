package com.nsc.ipfind.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.io.File;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    @Value("${file.upload.path:/home/www/wwwroot/java-app/uploads/}")
    private String uploadPath;

    @Value("${file.access.path:/uploads/}")
    private String accessPath;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        System.out.println("=== WebMvcConfig.addResourceHandlers 被调用 ===");

        // 处理访问路径，确保格式正确
        String normalizedAccessPath = accessPath;
        if (!normalizedAccessPath.startsWith("/")) {
            normalizedAccessPath = "/" + normalizedAccessPath;
        }
        if (!normalizedAccessPath.endsWith("/**")) {
            if (normalizedAccessPath.endsWith("/")) {
                normalizedAccessPath += "**";
            } else {
                normalizedAccessPath += "/**";
            }
        }

        // 处理文件路径
        String normalizedUploadPath = uploadPath;
        if (!normalizedUploadPath.endsWith("/")) {
            normalizedUploadPath += "/";
        }

        // 确保目录存在
        File uploadDir = new File(normalizedUploadPath);
        if (!uploadDir.exists()) {
            boolean created = uploadDir.mkdirs();
            System.out.println("创建上传目录: " + created + ", 路径: " + uploadDir.getAbsolutePath());
        }

        // 构造资源位置
        String location = "file:" + uploadDir.getAbsolutePath() + "/";

        System.out.println("=== 静态资源映射详情 ===");
        System.out.println("ResourceHandler: " + normalizedAccessPath);
        System.out.println("ResourceLocations: " + location);
        System.out.println("上传目录绝对路径: " + uploadDir.getAbsolutePath());
        System.out.println("上传目录是否存在: " + uploadDir.exists());
        System.out.println("========================");

        // 添加资源处理器
        registry.addResourceHandler(normalizedAccessPath)
                .addResourceLocations(location)
                .setCachePeriod(0);
    }
}
