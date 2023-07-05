-- drop database chinese_chess;
create database chinese_chess ;
use chinese_chess;


---------------------------------------------------
select * from chinese_chess.matches;
---------------------------------------------------
select * from chinese_chess.move_histories;
---------------------------------------------------
SELECT * FROM chinese_chess.pieces;
---------------------------------------------------
SELECT * FROM chinese_chess.players;
---------------------------------------------------
select * from chinese_chess.ranks;
---------------------------------------------------
select * from chinese_chess.roles;
---------------------------------------------------
select * from chinese_chess.training;
---------------------------------------------------
select * from chinese_chess.users;
---------------------------------------------------
select * from chinese_chess.vips;

---------------------------------------------------
insert into pieces(id,name,is_red,image,start_col,start_row) values
(1,'Binh',1,null,1,7),
(2,'Binh',1,null,3,7),
(3,'Binh',1,null,5,7),
(4,'Binh',1,null,7,7),
(5,'Binh',1,null,9,7),
(6,'Pháo',1,null,2,8),
(7,'Pháo',1,null,8,8),
(8,'Xe',1,null,1,10),
(9,'Xe',1,null,9,10),
(10,'Mã',1,null,2,10),
(11,'Mã',1,null,8,10),
(12,'Tượng',1,null,3,10),
(13,'Tượng',1,null,7,10),
(14,'Sĩ',1,null,4,10),
(15,'Sĩ',1,null,6,10),
(16,'Tướng',1,null,5,10),
(17,'Binh',0,null,1,4),
(18,'Binh',0,null,3,4),
(19,'Binh',0,null,5,4),
(20,'Binh',0,null,7,4),
(21,'Binh',0,null,9,4),
(22,'Pháo',0,null,2,3),
(23,'Pháo',0,null,8,3),
(24,'Xe',0,null,1,1),
(25,'Xe',0,null,9,1),
(26,'Mã',0,null,2,1),
(27,'Mã',0,null,8,1),
(28,'Tượng',0,null,3,1),
(29,'Tượng',0,null,7,1),
(30,'Sĩ',0,null,4,1),
(31,'Sĩ',0,null,6,1),
(32,'Tướng',0,null,5,1);
---------------------------------------------------
insert into move_histories(to_col,to_row,turn,match_id,piece_id) values
('5', '8', '1', '1', '6'),
('5', '3', '2', '1', '23'),
('3', '8', '3', '1', '10'),
('7', '3', '4', '1', '27'),
('1', '9', '5', '1', '8'),
('8', '1', '6', '1', '25'),
('4', '9', '7', '1', '8'),
('8', '7', '8', '1', '25'),
('4', '2', '9', '1', '8'),
('1', '3', '10', '1', '26'),
('1', '9', '11', '1', '9'),
('2', '10', '12', '1', '22'),
('2', '3', '13', '1', '7'),
('8', '3', '14', '1', '25'),
('7', '2', '15', '1', '8'),
('2', '1', '16', '1', '24'),
('7', '3', '17', '1', '7'),
('9', '3', '18', '1', '29'),
('5', '4', '19', '1', '6'),
('5', '2', '20', '1', '31'),
('9', '3', '21', '1', '7'),
('8', '1', '22', '1', '25'),
('8', '9', '23', '1', '9'),
('6', '1', '24', '1', '25'),
('8', '2', '25', '1', '8'),
('2', '5', '26', '1', '24'),
('9', '1', '27', '1', '7'),
('9', '1', '28', '1', '25'),
('8', '1', '29', '1', '8'),
('8', '1', '30', '1', '25'),
('8', '1', '31', '1', '9');
--------------------------------

