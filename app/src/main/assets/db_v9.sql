ALTER TABLE tbl_bible RENAME TO tbl_bible_tamil;
CREATE TABLE "tbl_languages" (`id` INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, `title` varchar(528) DEFAULT null,`suffix` varchar(528) DEFAULT null, `is_installed` NUMERIC DEFAULT '0',`download_url` varchar(1000) NOT NULL ,`created_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP);
INSERT INTO tbl_languages(title, suffix, is_installed, download_url) VALUES("English", "english",0 ,'https://s3.ap-south-1.amazonaws.com/onebible.in/tbl_bible_english.sql.zip');
INSERT INTO tbl_languages(title, suffix, is_installed, download_url) VALUES("Hindi", "hindi", 0,'https://s3.ap-south-1.amazonaws.com/onebible.in/tbl_bible_hindi.sql.zip');
INSERT INTO tbl_languages(title, suffix, is_installed, download_url) VALUES("Tamil", "tamil", 1,'https://s3.ap-south-1.amazonaws.com/onebible.in/tbl_bible_tamil.sql.zip');
update tbl_bible_tamil set verse='வாலவயதின் குமாரர் பலவான் கையிலுள்ள அம்புகளுக்கு ஒப்பாயிருக்கிறார்கள்.' where book_id=18 and chapter_id=127 and verse_id= 5;
update tbl_bible_tamil set verse='திரும்பிவா, திரும்பிவா, சூலமித்தியே! நாங்கள் உன்னைப் பார்க்கும்படிக்கு, திரும்பிவா, திரும்பிவா. சூலமித்தியில் நீங்கள் என்னத்தைப் பார்க்கிறீர்கள்? அவள் இரண்டு சேனையின் கூட்டத்துக்குச் சமானமானவள்.' where book_id=21 and chapter_id=6 and verse_id= 13;
UPDATE tbl_bible_tamil SET verse = REPLACE(verse, 'லுூ', 'லூ');