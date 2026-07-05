alter table clients add column refresh_token varchar(255);
create unique index ux_clients_refresh_token on clients(refresh_token);

insert into rental_packages (id, package_name, description, price, available_count) values
('pkg_own', 'Own', 'Со своим оборудованием', 0.00, 999);