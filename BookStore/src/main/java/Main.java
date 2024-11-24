package com.books.BookStore.example;

import com.books.BookStore.example.DI.AppConfig;
import org.apache.catalina.Context;
import org.apache.catalina.LifecycleException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;
import org.apache.catalina.startup.Tomcat;

import javax.servlet.ServletException;
import javax.servlet.ServletRegistration;
import java.io.File;

public class Main {
	public static void main(String[] args) throws ServletException, LifecycleException {
		try {
			Tomcat tomcat = new Tomcat();
			tomcat.setPort(8080);
			tomcat.getConnector();

			Context context = tomcat.addContext("", new File("src/main/resources").getAbsolutePath());

			AnnotationConfigWebApplicationContext springContext = new AnnotationConfigWebApplicationContext();
			springContext.scan("com.books.BookStore");

			DispatcherServlet dispatcherServlet = new DispatcherServlet(springContext);
			ServletRegistration.Dynamic registration = (ServletRegistration.Dynamic) tomcat.addServlet(context, "dispatcher", dispatcherServlet);
			registration.setLoadOnStartup(1);
			registration.addMapping("/*");

			tomcat.start();
			tomcat.getServer().await();
		} catch (Exception e) {
			System.err.println("Ошибка при запуске сервера:");
			e.printStackTrace();
		}
	}
}
