create table users(
	id bigint not null auto_increment,
	name varchar(100) not null,
	login varchar(100) not null,
	password varchar(255) not null unique,
	
	primary key (id)
)