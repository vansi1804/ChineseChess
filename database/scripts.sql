-- drop database chinese_chess;
create database chinese_chess ;
use chinese_chess;


---------------------------------------------------------------------------
insert into pieces(id,name,is_red,image,start_col,start_row) values
(1,'Binh',0,null,1,4),
(2,'Binh',0,null,3,4),
(3,'Binh',0,null,5,4),
(4,'Binh',0,null,7,4),
(5,'Binh',0,null,9,4),
(6,'Pháo',0,null,2,3),
(7,'Pháo',0,null,8,3),
(8,'Xe',0,null,1,1),
(9,'Xe',0,null,9,1),
(10,'Mã',0,null,2,1),
(11,'Mã',0,null,8,1),
(12,'Tượng',0,null,3,1),
(13,'Tượng',0,null,7,1),
(14,'Sĩ',0,null,4,1),
(15,'Sĩ',0,null,6,1),
(16,'Tướng',0,null,5,1),
(17,'Binh',1,null,1,7),
(18,'Binh',1,null,3,7),
(19,'Binh',1,null,5,7),
(20,'Binh',1,null,7,7),
(21,'Binh',1,null,9,7),
(22,'Pháo',1,null,2,8),
(23,'Pháo',1,null,8,8),
(24,'Xe',1,null,1,10),
(25,'Xe',1,null,9,10),
(26,'Mã',1,null,2,10),
(27,'Mã',1,null,8,10),
(28,'Tượng',1,null,3,10),
(29,'Tượng',1,null,7,10),
(30,'Sĩ',1,null,4,10),
(31,'Sĩ',1,null,6,10),
(32,'Tướng',1,null,5,10);
---------------------------------------------------
select * from pieces;
---------------------------------------------------
select * from roles;

