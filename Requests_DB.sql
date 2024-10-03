-- 2

-- 1)
SELECT model, speed, hd
FROM PC
WHERE price < 500;

-- 2)
SELECT DISTINCT maker
FROM Product
WHERE type = 'Printer';

-- 3)
SELECT model, ram, screen
FROM Laptop
WHERE price > 1000;

-- 4)
SELECT *
FROM Printer
WHERE color = 'Y';

-- 5)
SELECT code, speed, hd
FROM PC
WHERE cd IN ('12x', '24x') AND price < 600;

-- 6)
SELECT p.maker, l.speed
FROM Laptop l
JOIN Product p ON l.model = p.model
WHERE l.hd >= 100;

-- 7)
SELECT p.model, p.price
FROM Product p
LEFT JOIN PC pc ON p.model = pc.model
LEFT JOIN Laptop l ON p.model = l.model  
LEFT JOIN Printer pr ON p.model = pr.model
WHERE p.maker = 'B';

-- 8)
SELECT DISTINCT maker
FROM Product p
WHERE p.type = 'PC'
AND p.maker NOT IN (SELECT maker FROM Product WHERE type = 'Laptop');

-- 9)
SELECT DISTINCT p.maker
FROM Product p
JOIN PC pc ON p.model = pc.model
WHERE pc.speed >= 450;

-- 10)
SELECT model, price
FROM Printer
WHERE price = (SELECT MAX(price) FROM Printer);

-- 11)
SELECT AVG(speed) AS avg_pc_speed
FROM PC;

-- 12)
SELECT AVG(speed) AS avg_laptop_speed
FROM Laptop
WHERE price > 1000;

-- 13)
SELECT AVG(pc.speed) AS avg_pc_speed
FROM PC pc
JOIN Product p ON pc.model = p.model
WHERE p.maker = 'A';

-- 14)
SELECT speed, AVG(price) AS avg_price
FROM PC
GROUP BY speed;

-- 15)
SELECT hd
FROM PC
GROUP BY hd
HAVING COUNT(*) >= 2;    

-- 16)
SELECT p1.model AS model1, p2.model AS model2, p1.speed, p1.ram
FROM PC p1, PC p2
WHERE p1.model > p2.model
  AND p1.speed = p2.speed
  AND p1.ram = p2.ram;
  
-- 17)
SELECT p.type, l.model, l.speed
FROM Laptop l
JOIN Product p ON l.model = p.model
WHERE l.speed < ALL (SELECT speed FROM PC);

-- 18)
SELECT p.maker, pr.price
FROM Printer pr
JOIN Product p ON pr.model = p.model
WHERE pr.color = 'Y'
AND pr.price = (SELECT MIN(price) FROM Printer WHERE color = 'Y');

-- 19)
SELECT p.maker, AVG(l.screen) AS avg_screen_size
FROM Laptop l
JOIN Product p ON l.model = p.model
GROUP BY p.maker;

-- 20)
SELECT p.maker, COUNT(DISTINCT pc.model) AS num_models
FROM Product p
JOIN PC pc ON p.model = pc.model
GROUP BY p.maker
HAVING COUNT(DISTINCT pc.model) >= 3;

-- 21)
SELECT p.maker, MAX(pc.price) AS max_price
FROM Product p
JOIN PC pc ON p.model = pc.model
GROUP BY p.maker;

-- 22)
SELECT speed, AVG(price) AS avg_price
FROM PC
WHERE speed > 600
GROUP BY speed;

-- 23)
SELECT DISTINCT p.maker
FROM Product p
JOIN PC pc ON p.model = pc.model
JOIN Laptop l ON p.model = l.model
WHERE pc.speed >= 750 AND l.speed >= 750;

-- 24)
SELECT p.model
FROM (
  SELECT model, MAX(price) AS max_price
  FROM PC
  GROUP BY model
  UNION ALL
  SELECT model, MAX(price) AS max_price
  FROM Laptop
  GROUP BY model
  UNION ALL
  SELECT model, MAX(price) AS max_price
  FROM Printer
  GROUP BY model
) AS all_products
JOIN (
  SELECT MAX(price) AS overall_max_price
  FROM (
    SELECT price FROM PC
    UNION ALL
    SELECT price FROM Laptop 
    UNION ALL
    SELECT price FROM Printer
  ) AS all_prices
) AS overall_max
ON all_products.max_price = overall_max.overall_max_price

-- 25)
SELECT DISTINCT p.maker
FROM Product p
JOIN Printer pr ON p.model = pr.model
JOIN PC pc ON p.model = pc.model
WHERE pc.ram = (SELECT MIN(ram) FROM PC)
AND pc.speed = (SELECT MAX(speed) FROM PC WHERE ram = (SELECT MIN(ram) FROM PC));
