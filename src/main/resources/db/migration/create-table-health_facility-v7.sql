create table if not exists health_facility
(
  health_facility_id int auto_increment
    primary key,
  health_facility_code int not null,
  health_facility_name varchar(255) not null,
  district_id int not null,
  taluka_id int not null,
  health_block_id int not null,
  health_facility_type_id int not null,
  constraint health_facility_district_district_id_fk
    foreign key (district_id) references district (district_id),
  constraint health_facility_health_facility_type_facility_type_id_fk
    foreign key (health_facility_type_id) references health_facility_type (facility_type_id)
);