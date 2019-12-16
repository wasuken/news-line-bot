create table sites(
	   url varchar(400) primary key,
	   title varchar(200)
);
create table posts(
	   url varchar(400) primary key,
	   from_url varchar(400),
	   title varchar(200),
	   description text,
	   created_at timestamp default CURRENT_TIMESTAMP
);
