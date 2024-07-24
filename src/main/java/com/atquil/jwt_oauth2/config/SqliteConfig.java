//package com.atquil.jwt_oauth2.config;
//
//import org.hibernate.cfg.*;
//import org.springframework.beans.factory.annotation.*;
//import org.springframework.context.annotation.*;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.jdbc.datasource.*;
//
//import javax.sql.*;
//@Configuration
//public class SqliteConfig {
//    @Autowired Environment env;
//
//
//    @Bean
//    public DataSource dataSource() {
//        final DriverManagerDataSource dataSource = new DriverManagerDataSource();
//        dataSource.setDriverClassName(env.getProperties().getProperty("driverClassName"));
//        dataSource.setUrl(env.getProperties().getProperty("url"));
//        dataSource.setUsername(env.getProperties().getProperty("user"));
//        dataSource.setPassword(env.getProperties().getProperty("password"));
//
//        return dataSource;
//    }
//}
