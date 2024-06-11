create table Usuarios(
UUID_usuario VARCHAR2(150),
nom_usuario VARCHAR2(50),
contrasena VARCHAR2(50)
);

drop table Usuarios
select * from Usuarios

create table Tickets(
UUID_TICKET VARCHAR2(150) not null,
num_ticket int not null,
titulo VARCHAR2(20 BYTE) not null,
descripcion	VARCHAR2(100) not null,
autor VARCHAR2(50) not null,
email_autor	VARCHAR2(50) not null,
fecha_ticket VARCHAR2(50) not null,
estado	VARCHAR2(50) not null,
fecha_fin_ticket VARCHAR2(50) not null
);

select * from Ticket