package com.books.BookStore.example;

import com.books.BookStore.example.DI.AppConfig;
import org.apache.catalina.Context;
import org.apache.catalina.LifecycleException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;
import org.apache.catalina.startup.Tomcat;

import javax.servlet.ServletException;
import javax.servlet.ServletRegistration;
import java.io.File;

public class Main {
	public static void main(String[] args) throws ServletException, LifecycleException {
		try {
			String webappDirLocation = "src/main/resources/webapp/";
			Tomcat tomcat = new Tomcat();
			tomcat.setPort(8080);

			tomcat.addWebapp("", new File(webappDirLocation).getAbsolutePath());

			tomcat.start();
			System.out.println("Веб-приложение запущено на http://localhost:8080/");
			tomcat.getServer().await();
		} catch (Exception e) {
			System.err.println("Ошибка при запуске сервера:");
			e.printStackTrace();
		}
	}
}
