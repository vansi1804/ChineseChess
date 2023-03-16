-- drop database chinese_chess;
create database chinese_chess;
use chinese_chess;

create table piece(
	id int auto_increment,
    name varchar(20),
    is_red bit,
	image varchar(1000),
    start_col int,
    start_row int,
    primary key(id)
);

create table role(
	id int auto_increment,
    name nvarchar(50),
	primary key(id)
);

create table levels(
	id int auto_increment,
    name nvarchar(50),
	primary key(id)
);

create table player(
	id bigint auto_increment,
    role_id int,
    email varchar(50),
    phone_number varchar(15),
    name nvarchar(200),
    avata varchar(1000),
    elo_score bigint,
    levels_id int,
    primary key(id),
   foreign key (role_id) references role(id),
   foreign key (levels_id) references levels(id)
);

create table game(
	id bigint auto_increment,
	time varchar(20),
	player1_id bigint,
	player2_id bigint,
	result bigint,
    primary key(id),
   foreign key (player1_id) references player(id),
   foreign key (player2_id) references player(id)
);

create table move_history(
	id bigint auto_increment,
	game_id bigint,
	turn bigint,
	player_id bigint,
	piece_id int,
	from_col int,
	from_row int,
	to_col int,
	to_row int,
    primary key(id),
   foreign key (game_id) references game(id),
   foreign key (player_id) references player(id),
   foreign key (piece_id) references piece(id)
);

---------------------------------------------------------------------------
insert into piece values(1,'Binh',0,'',1,4);
insert into piece values(2,'Binh',0,'',3,4);
insert into piece values(3,'Binh',0,'',5,4);
insert into piece values(4,'Binh',0,'',7,4);
insert into piece values(5,'Binh',0,'',9,4);
insert into piece values(6,'Pháo',0,'',2,3);
insert into piece values(7,'Pháo',0,'',8,3);
insert into piece values(8,'Xe',0,'',1,1);
insert into piece values(9,'Xe',0,'',9,1);
insert into piece values(10,'Mã',0,'',2,1);
insert into piece values(11,'Mã',0,'',8,1);
insert into piece values(12,'Tượng',0,'',3,1);
insert into piece values(13,'Tượng',0,'',7,1);
insert into piece values(14,'Sĩ',0,'',4,1);
insert into piece values(15,'Sĩ',0,'',6,1);
insert into piece values(16,'Tướng',0,'',5,1);
insert into piece values(17,'Binh',1,'',1,7);
insert into piece values(18,'Binh',1,'',1,7);
insert into piece values(19,'Binh',1,'',1,7);
insert into piece values(20,'Binh',1,'',1,7);
insert into piece values(21,'Binh',1,'',1,7);
insert into piece values(22,'Pháo',1,'',1,8);
insert into piece values(23,'Pháo',1,'',1,8);
insert into piece values(24,'Xe',1,'',1,10);
insert into piece values(25,'Xe',1,'',9,10);
insert into piece values(26,'Mã',1,'',2,10);
insert into piece values(27,'Mã',1,'',8,10);
insert into piece values(28,'Tượng',1,'',3,10);
insert into piece values(29,'Tượng',1,'',7,10);
insert into piece values(30,'Sĩ',1,'',4,10);
insert into piece values(31,'Sĩ',1,'',6,10);
insert into piece values(32,'Tướng',1,'',5,10);
---------------------------------------------------
select * from piece;
---------------------------------------------------
insert into role values(1,'adnin',0);
---------------------------------------------------
select * from role;

