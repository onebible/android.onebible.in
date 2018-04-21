alter table tbl_bible add column `is_favourited` NUMERIC DEFAULT '0';
alter table tbl_favourites add column "book_id" int(2) DEFAULT NULL;
alter table tbl_favourites add column "chapter_id" int(3) DEFAULT NULL;
alter table tbl_favourites add column "verse_id" int(3) DEFAULT NULL;