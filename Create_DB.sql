-- 1

create database Tecnuque;

use Tecnuque;

create table Product(
maker varchar(10) not null,
model varchar(50) not null,
type varchar(50) not null,
primary key(model)
);

use Tecnuque;

create table PC(
code int not null primary key,
model varchar(50) not null,
speed smallint not null,
ram smallint not null,
hd real not null,
cd varchar(10) not null,
price decimal(10, 2),
foreign key(model) references product(model)
);

use Tecnuque;

create table Laptop(
code int not null primary key,
model varchar(50) not null,
speed smallint not null,
ram smallint not null,
hd real not null,
price decimal(10, 2),
screen tinyint not null,
foreign key(model) references product(model)
);

use Tecnuque;

create table Printer(
code int not null primary key,
model varchar(50) not null,
color varchar(1) not null,
type varchar(10) not null,
price decimal(10, 2),
foreign key(model) references product(model)
);

    




