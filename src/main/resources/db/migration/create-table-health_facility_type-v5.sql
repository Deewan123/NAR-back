create table if not exists health_facility_type
(
  facility_type_id int auto_increment
    primary key,
  facility_type__name varchar(255) not null
);