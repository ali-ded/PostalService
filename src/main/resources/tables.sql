drop schema if exists postal_service cascade;

create schema postal_service;

-- Создание таблицы для хранения информации о клиентах.
create table postal_service.clients
(
    id				bigserial primary key,
    surname			varchar(30) not null,
    first_name		varchar(20) not null,
	patronymic		varchar(30),
    email           varchar(60),
    phone_number    bigint unique not null
)
;

-- Создание таблицы отделений почтовой службы.
create table postal_service.departments
(
	id				serial primary key,
	description		varchar(200) unique not null
)
;

-- Создание таблицы возможных статусов отправки посылки.
create table postal_service.parcel_statuses
(
	id			smallserial primary key,
	status		varchar(30) unique not null
)
;

-- Установка возможных состояний статусов отправки посылки.
insert into postal_service.parcel_statuses(status)
values
	('Новый'),
	('Доставлена'),
	('Просрочена')
;

-- Создание таблицы для отправленных посылок.
create table postal_service.deliveries
(
	id						bigserial primary key,
	id_client				bigint not null references postal_service.clients (id) on delete cascade,
	id_department_sender	int not null references postal_service.departments (id) on delete cascade,
	id_department_recipient	int not null references postal_service.departments (id) on delete cascade,
	recipient_phone			bigint not null,
    recipient_surname		varchar(30) not null,
    recipient_first_name	varchar(20) not null,
	recipient_patronymic	varchar(30),
	id_parcel_status		smallint not null references postal_service.parcel_statuses (id) on delete cascade default 1,
	date_time_creation		timestamp not null default current_timestamp,
	date_time_status_change timestamp default current_timestamp
)
;

-- Создание таблицы возможных статусов отправки уведомления.
create table postal_service.notification_statuses
(
	id			smallserial primary key,
	status		varchar(30) unique not null
)
;

-- Установка возможных состояний статусов отправки уведомления.
insert into postal_service.notification_statuses(status)
values
	('Новый'),
	('Отправлено')
;

-- Создание таблицы для уведомлений об отправке.
create table postal_service.notifications
(
	id			bigserial primary key,
	message		text not null,
	id_status	smallint not null references postal_service.notification_statuses (id) on delete cascade default 1 
)
;