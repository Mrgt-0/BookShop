package com.books.BookStore.example.DI;

import com.books.BookStore.example.Controller.BookStoreController;
import com.books.BookStore.example.Model.BookStore;
import com.books.BookStore.example.Model.BookStoreSerializable;
import com.books.BookStore.example.Service.BookStoreService;
import com.mysql.cj.jdbc.MysqlDataSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;

import javax.sql.DataSource;

@Configuration
@ComponentScan(basePackages = "com.books.BookStore.example")
@PropertySource("classpath:application.properties")
public class AppConfig {
    @Bean
    public static PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer() {
        return new PropertySourcesPlaceholderConfigurer();
    }

    @Bean
    public DataSource dataSource(){
        MysqlDataSource dataSource = new MysqlDataSource();
        dataSource.setUrl("jdbc:mysql://localhost:3306/BookStore");
        dataSource.setUser("root");
        dataSource.setPassword("Tessy_Sammy28*");
        return dataSource;
    }
}
