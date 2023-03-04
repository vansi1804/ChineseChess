-- DROP DATABASE chinese_chess;
CREATE DATABASE chinese_chess;
use chinese_chess;

CREATE TABLE piece(
	id INT,
    name VARCHAR(20),
    is_red BIT,
	image VARCHAR(1000),
    start_col INT,
    start_row INT,
    PRIMARY KEY(id)
);

CREATE TABLE role(
	id INT,
    name NVARCHAR(50),
	PRIMARY KEY(id)
);

CREATE TABLE level(
	id INT,
    name NVARCHAR(50),
	PRIMARY KEY(id)
);

CREATE TABLE player(
	id BIGINT,
    role_id INT,
    name NVARCHAR(200),
    avata VARCHAR(1000),
    elo_score BIGINT,
    level_id int,
    PRIMARY KEY(id),
    FOREIGN KEY (role_id) REFERENCES role(id),
    FOREIGN KEY (level_id) REFERENCES level(id)
);

CREATE TABLE game(
	id BIGINT,
	time varchar(20),
	player1_id BIGINT,
	player2_id BIGINT,
	result BIGINT,
    PRIMARY KEY(id),
    FOREIGN KEY (player1_id) REFERENCES player(id),
    FOREIGN KEY (player2_id) REFERENCES player(id)
);

CREATE TABLE move_history(
	id BigInt,
	game_id BIGINT,
	turn BIGINT,
	player_id BIGINT,
	piece_id INT,
	from_col INT,
	from_row INT,
	to_col INT,
	to_row INT,
    PRIMARY KEY(id),
    FOREIGN KEY (game_id) REFERENCES game(id),
    FOREIGN KEY (player_id) REFERENCES player(id),
    FOREIGN KEY (piece_id) REFERENCES piece(id)
);

---------------------------------------------------------------------------
INSERT INTO piece values(1,'Binh',0,'',1,4);
INSERT INTO piece values(2,'Binh',0,'',3,4);
INSERT INTO piece values(3,'Binh',0,'',5,4);
INSERT INTO piece values(4,'Binh',0,'',7,4);
INSERT INTO piece values(5,'Binh',0,'',9,4);
INSERT INTO piece values(6,'Pháo',0,'',2,3);
INSERT INTO piece values(7,'Pháo',0,'',8,3);
INSERT INTO piece values(8,'Xe',0,'',1,1);
INSERT INTO piece values(9,'Xe',0,'',9,1);
INSERT INTO piece values(10,'Mã',0,'',2,1);
INSERT INTO piece values(11,'Mã',0,'',8,1);
INSERT INTO piece values(12,'Tượng',0,'',3,1);
INSERT INTO piece values(13,'Tượng',0,'',7,1);
INSERT INTO piece values(14,'Sĩ',0,'',4,1);
INSERT INTO piece values(15,'Sĩ',0,'',6,1);
INSERT INTO piece values(16,'Tướng',0,'',5,1);
INSERT INTO piece values(17,'Binh',1,'',1,7);
INSERT INTO piece values(18,'Binh',1,'',1,7);
INSERT INTO piece values(19,'Binh',1,'',1,7);
INSERT INTO piece values(20,'Binh',1,'',1,7);
INSERT INTO piece values(21,'Binh',1,'',1,7);
INSERT INTO piece values(22,'Pháo',1,'',1,8);
INSERT INTO piece values(23,'Pháo',1,'',1,8);
INSERT INTO piece values(24,'Xe',1,'',1,10);
INSERT INTO piece values(25,'Xe',1,'',9,10);
INSERT INTO piece values(26,'Mã',1,'',2,10);
INSERT INTO piece values(27,'Mã',1,'',8,10);
INSERT INTO piece values(28,'Tượng',1,'',3,10);
INSERT INTO piece values(29,'Tượng',1,'',7,10);
INSERT INTO piece values(30,'Sĩ',1,'',4,10);
INSERT INTO piece values(31,'Sĩ',1,'',6,10);
INSERT INTO piece values(32,'Tướng',1,'',5,10);
-----------------------------------------------------------------------------------------


