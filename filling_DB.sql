-- DML скрипты
INSERT INTO Product (maker, model, type) VALUES
    ('A', 'x1', 'PC'),
    ('A', 'y1', 'Laptop'),
    ('A', 'z1', 'Printer'),
    ('B', 'x2', 'PC'),
    ('B', 'y2', 'Laptop'),
    ('B', 'z2', 'Printer'),
    ('C', 'x3', 'PC'),
    ('C', 'y3', 'Laptop'),
    ('C', 'z3', 'Printer');

-- Заполнение таблицы pc
INSERT INTO PC (code, model, speed, ram, hd, cd, price) VALUES
    (1, 'x1', 2400, 512, 80.0, '12x', 899.99),
    (2, 'x2', 2800, 512, 160.0, '16x', 1199.99),
    (3, 'x3', 3000, 1024, 250.0, '24x', 1499.99);

-- Заполнение таблицы laptop
INSERT INTO Laptop (code, model, speed, ram, hd, screen, price) VALUES
    (1, 'y1', 2000, 512, 60.0, 15.4, 1199.99),
    (2, 'y2', 2400, 1024, 80.0, 15.6, 1499.99),
    (3, 'y3', 2800, 1024, 100.0, 17.0, 1799.99);

-- Заполнение таблицы printer
INSERT INTO Printer (code, model, color, type, price) VALUES
    (1, 'z1', 'n', 'Laser', 399.99),
    (2, 'z2', 'y', 'Jet', 199.99),
    (3, 'z3', 'y', 'Matrix', 299.99);