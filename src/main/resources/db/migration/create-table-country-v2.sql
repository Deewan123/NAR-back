create table if not exists country
(
  country_id Integer not null
    primary key auto_increment,
  country_name varchar(150) not null
);

insert into country values (1,'INDIA');