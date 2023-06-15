-- drop database chinese_chess;
create database chinese_chess ;
use chinese_chess;


---------------------------------------------------------------------------
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
select * from pieces;
---------------------------------------------------
select * from roles;

