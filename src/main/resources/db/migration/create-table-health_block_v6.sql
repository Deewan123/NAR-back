create table if not exists health_block
(
  health_block_id int auto_increment
    primary key,
  health_block_code int not null,
  health_block_name varchar(255) not null,
  district_id int not null,
  taluka_id int not null,
  mdds_code int not null,
  constraint health_block_district_district_id_fk
  foreign key (district_id) references district (district_id)
);