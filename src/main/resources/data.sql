SET REFERENTIAL_INTEGRITY FALSE;

TRUNCATE TABLE FILMS_GENRES;

TRUNCATE TABLE FILMS_DIRECTORS;

TRUNCATE TABLE FRIENDS;

TRUNCATE TABLE LIKES;

TRUNCATE TABLE FILMS RESTART IDENTITY;

TRUNCATE TABLE USERS RESTART IDENTITY;

TRUNCATE TABLE GENRES RESTART IDENTITY;

TRUNCATE TABLE DIRECTORS RESTART IDENTITY;

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

INSERT INTO FILMS (NAME , DESCRIPTION , RELEASEDATE , DURATION , RATING_ID)
VALUES ('Тень', '30-ые годы XX века, город Нью-Йорк...', '1994-07-01', 108 , 3),
		('Звёздные войны: Эпизод 4 – Новая надежда', 'Татуин. Планета-пустыня. Уже постаревший рыцарь Джедай ...', '1997-05-25', 121 , 2),
		('Зеленая миля', 'Пол Эджкомб — начальник блока смертников в тюрьме «Холодная гора» ...', '1999-12-06', 189 , 4),
		('Гадкий я', 'Гадкий снаружи, но добрый внутри Грю намерен, тем не менее, ...', '2010-06-27', 95 , 2);

INSERT INTO USERS (EMAIL , LOGIN , NAME , BIRTHDAY)
VALUES ('Capitan@yandex.ru', 'Capitan', 'Capitan', '2001-01-01'), 	-- 1
		('Jack@yandex.ru', 'Jack', 'Jack', '2002-02-02'), 			-- 2
		('Sparrow@yandex.ru', 'Sparrow', 'Sparrow', '2003-03-03'); 	-- 3

INSERT INTO DIRECTORS (NAME)
VALUES ('Джордж Лукас'), 	-- 1
		('Фрэнк Дарабонт'), -- 2
		('Рассел Малкэй'),	-- 3
		('Пьер Коффан'),	-- 4
		('Крис Рено');		-- 5

INSERT INTO LIKES (FILM_ID, USER_ID)
VALUES (1, 1), (1, 3),
		(2, 1), (2, 2), (2, 3),
		(3, 2),
		(4, 1), (4, 2), (4, 3);

INSERT INTO FRIENDS (USER_ID, FRIEND_ID, STATUS_ID)
VALUES (1, 2, 1), (1, 3, 1),
		(2, 1, 2),
		(3, 1, 2), (3, 2, 1);

INSERT INTO FILMS_GENRES (FILM_ID, GENRE_ID)
VALUES (1, 2), (1, 4), (1, 6),
		(2, 2), (2, 4), (2, 6),
		(3, 1), (3, 2), (3, 4),
		(4, 1), (4, 3), (4, 4), (4, 6);

INSERT INTO FILMS_DIRECTORS (FILM_ID, DIRECTOR_ID)
VALUES (1, 1), (1, 3),
        (2, 1),
        (3, 2),
        (4, 4), (4, 5);
