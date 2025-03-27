create table if not exists role
(
	role_id int auto_increment
		primary key,
	role_name varchar(100) not null
);