create table clients (
    id varchar(64) primary key,
    first_name varchar(100) not null,
    last_name varchar(100),
    email varchar(255) not null unique,
    phone varchar(32) not null unique,
    password_hash varchar(255) not null,
    registration_date timestamp with time zone not null,
    total_classes_attended integer not null default 0,
    allergies text,
    is_permanent_client boolean not null default false
);

create table chefs (
    id varchar(64) primary key,
    name varchar(160) not null,
    specialization varchar(160) not null,
    rating numeric(2,1) not null default 0,
    total_reviews integer not null default 0,
    bio text
);

create table cooking_classes (
    id varchar(64) primary key,
    title varchar(200) not null,
    description text not null,
    date_time timestamp with time zone not null,
    duration integer not null,
    max_participants integer not null,
    available_seats integer not null,
    chef_id varchar(64) not null references chefs(id),
    class_type varchar(80) not null,
    price numeric(10,2) not null,
    status varchar(40) not null default 'SCHEDULED'
);

create table rental_packages (
    id varchar(64) primary key,
    package_name varchar(40) not null unique,
    description text not null,
    price numeric(10,2) not null,
    available_count integer not null
);

create table bookings (
    id varchar(64) primary key,
    client_id varchar(64) not null references clients(id),
    class_id varchar(64) not null references cooking_classes(id),
    booking_date timestamp with time zone not null,
    status varchar(40) not null,
    rental_package_id varchar(64) not null references rental_packages(id),
    cancellation_date timestamp with time zone,
    penalty_points integer not null default 0,
    allergies text
);

create unique index ux_active_booking_per_client_class
    on bookings(client_id, class_id)
    where status = 'CONFIRMED';

create table reviews (
    id varchar(64) primary key,
    booking_id varchar(64) not null unique references bookings(id),
    client_id varchar(64) not null references clients(id),
    chef_id varchar(64) not null references chefs(id),
    rating integer not null check (rating between 1 and 5),
    comment varchar(500),
    review_date timestamp with time zone not null
);

insert into chefs (id, name, specialization, rating, total_reviews, bio) values
('chef_anna', 'Анна Смирнова', 'Итальянская кухня', 4.8, 24, 'Шеф с опытом ресторанной кухни и авторскими паста-классами.'),
('chef_ivan', 'Иван Петров', 'Французская кухня', 4.6, 18, 'Специализируется на соусах, выпечке и классических техниках.'),
('chef_maria', 'Мария Волкова', 'Азиатская кухня', 4.9, 31, 'Ведет практические классы по вок, лапше и балансам вкуса.');

insert into rental_packages (id, package_name, description, price, available_count) values
('pkg_basic', 'Basic', 'Фартук', 300.00, 20),
('pkg_standard', 'Standard', 'Фартук + набор ножей', 500.00, 15),
('pkg_premium', 'Premium', 'Фартук, ножи и полный рабочий набор', 700.00, 10);

insert into cooking_classes (id, title, description, date_time, duration, max_participants, available_seats, chef_id, class_type, price) values
('class_pasta_basics', 'Итальянская паста', 'Основы приготовления пасты и двух классических соусов.', now() + interval '1 day', 3, 12, 12, 'chef_anna', 'novice', 2500.00),
('class_risotto', 'Ризотто без ошибок', 'Техника ризотто, бульон, текстура и подача.', now() + interval '3 days', 3, 10, 10, 'chef_anna', 'intermediate', 2900.00),
('class_french_sauces', 'Французские соусы', 'Базовые соусы и работа с температурой.', now() + interval '5 days', 3, 8, 8, 'chef_ivan', 'advanced', 3200.00),
('class_wok', 'Вок и азиатская лапша', 'Подготовка ингредиентов и быстрые техники жарки.', now() + interval '6 days', 3, 12, 12, 'chef_maria', 'novice', 2700.00),
('class_tiramisu', 'Тирамису', 'Классический итальянский десерт: один слот для теста.', now() + interval '7 days', 2, 1, 1, 'chef_anna', 'novice', 1800.00);
