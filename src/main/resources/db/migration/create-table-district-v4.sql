create table if not exists district
(
  district_id int not null
    primary key auto_increment,
  district_name varchar(255) not null,
  district_code int not null,
  mdds_code int not null,
  state_id int not null,
  constraint district_state_state_id_fk
    foreign key (state_id) references state (state_id)
);