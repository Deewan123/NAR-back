create table if not exists state
(
  state_id int not null primary key auto_increment,
  state_name varchar(100) null,
  country_id int null,

);

alter table state
    add constraint state_country_country_id_fk
        foreign key (country_id) references country (country_id);


insert into state values (21,'ODISHA',1);