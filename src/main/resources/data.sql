SET REFERENTIAL_INTEGRITY FALSE;

TRUNCATE TABLE FILMS_GENRES;

TRUNCATE TABLE FRIENDS;

TRUNCATE TABLE LIKES;

TRUNCATE TABLE FILMS RESTART IDENTITY;

TRUNCATE TABLE USERS RESTART IDENTITY;

TRUNCATE TABLE GENRES RESTART IDENTITY;

TRUNCATE TABLE RATINGS RESTART IDENTITY;

TRUNCATE TABLE STATUSES RESTART IDENTITY;

SET REFERENTIAL_INTEGRITY TRUE;

INSERT INTO RATINGS (NAME, DESCRIPTION)
VALUES ('G',      'У фильма нет возрастных ограничений'), 										-- 1
		('PG',    'Детям рекомендуется смотреть фильм с родителями'),							-- 2
		('PG-13', 'Детям до 13 лет просмотр не желателен'), 									-- 3
		('R',     'Лицам до 17 лет просматривать фильм можно только в присутствии взрослого'),  -- 4
		('NC-17', 'Лицам до 18 лет просмотр запрещён'); 										-- 5

INSERT INTO GENRES (NAME)
VALUES ('Комедия'), 		-- 1
		('Драма'), 			-- 2
		('Мультфильм'),		-- 3
		('Триллер'),		-- 4
		('Документальный'), -- 5
		('Боевик');		    -- 6

INSERT INTO STATUSES (NAME)
VALUES ('Подтверждённая'), 		-- 1
		('Неподтверждённая'); 	-- 2