package com.atquil.jwt_oauth2.config.userConfig;

import jakarta.servlet.*;
import org.springframework.boot.web.servlet.MultipartConfigFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.unit.DataSize;

@Configuration
public class FileUploadConfig {

    @Bean
    public MultipartConfigElement multipartConfigElement() {
        MultipartConfigFactory factory = new MultipartConfigFactory();

        // Set the maximum file size
        factory.setMaxFileSize(DataSize.ofMegabytes(50)); // 50MB

        // Set the maximum request size
        factory.setMaxRequestSize(DataSize.ofMegabytes(50)); // 50MB

        return factory.createMultipartConfig();
    }
}
