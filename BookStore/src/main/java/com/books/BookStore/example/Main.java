package com.books.BookStore.example;

import jakarta.servlet.Servlet;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import org.apache.catalina.Context;
import org.apache.catalina.LifecycleException;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;
import org.apache.catalina.startup.Tomcat;

import java.io.File;

public class Main {
	public static void main(String[] args) throws ServletException, LifecycleException {
		try {
			Tomcat tomcat = new Tomcat();
			tomcat.setPort(8080);
			tomcat.getConnector();

			File base = new File(System.getProperty("java.io.tmpdir"));
			Context ctx = tomcat.addWebapp("", base.getAbsolutePath());

			AnnotationConfigWebApplicationContext springContext = new AnnotationConfigWebApplicationContext();
			springContext.scan("com.books.BookStore.example");

			ServletContext servletContext = ctx.getServletContext();
			String servletName = "myServlet";
			String servletMapping = "/myServlet";
			Servlet dispatcherServlet = new DispatcherServlet();

			Tomcat.addServlet(ctx, servletName, dispatcherServlet);
			ctx.addServletMappingDecoded(servletMapping, servletName);

			tomcat.start();
			tomcat.getServer().await();
		} catch (Exception e) {
			System.err.println("Ошибка при запуске сервера:");
			e.printStackTrace();
		}
	}
}
