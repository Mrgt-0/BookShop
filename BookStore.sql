CREATE DATABASE IF NOT EXISTS BookStore;

USE BookStore;
CREATE TABLE IF NOT EXISTS Book(
bookId INT AUTO_INCREMENT PRIMARY KEY,
title VARCHAR(50) NOT NULL,
author VARCHAR(50) NOT NULL,
status VARCHAR(100) NOT NULL,
publish_date DATE NOT NULL,
price DECIMAL,
description LONGTEXT
);

USE BookStore;
CREATE TABLE IF NOT EXISTS Order_book(
orderId INT AUTO_INCREMENT PRIMARY KEY,
status VARCHAR(20) NOT NULL,
order_date DATE NOT NULL,
order_price DECIMAL
);

USE BookStore;
CREATE TABLE IF NOT EXISTS Request(
requestId INT AUTO_INCREMENT PRIMARY KEY,
bookId INT NOT NULL,
FOREIGN KEY(bookId) REFERENCES Book(bookId)
);


INSERT INTO Book(title, author, status, publish_date, price, description) VALUES
	("Война и мир", "Лев Николаевич Толстой", "IN_STOCK", "1873-1-10", 500, "Роман-эпопея Льва Толстого, посвящённый жизни российского общества во времена правления Александра I."),
	("Преступление и наказание", "Федор Михайлович Достоевский", "IN_STOCK", "1866-7-22", 450, "В романе рассказывается об отчуждении студента Раскольникова, который решает совершить идеальное преступление."),
	("Анна Каренина", "Лев Николаевич Толстой", "OUT_OF_STOCK", "1875-11-9", 390, "Это трагическая история любви замужней женщины и офицера Алексея Вронского на фоне счастливого брака Константина Левина и Кити Щербацкой.");
    