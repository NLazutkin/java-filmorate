# java-filmorate
Template repository for Filmorate project.

## Схема базы данных 

![Схема базы данных проекта Filmorate](/assets/images/Filmorate.png)

## Запросы на языке SQL для модели User

### 1. Найти пользователя

#### findUser(Long userId)
```
SELECT u.*
FROM User AS u
WHERE u.id = {userId};
```

### 2. Добавить друга

#### addFriend(Long userId, Long friendId)
```
INSERT INTO Friends (user_id, friend_id, status_id)
VALUE ({userId}, {friend_id}, 2);
```

### 3. Удалить друга

#### deleteFriend(Long userId, Long friendId)
```
DELETE FROM Friends
WHERE user_id = {userId} AND friend_id = {friend_id};
```

### 4. Найти всех друзей пользователя

#### findFriends(Long userId)
```
SELECT u2.*
FROM User AS u1
JOIN Friends AS f ON u1.id = f.user_id
JOIN User AS u2 ON f.friend_id = u2.id
JOIN Status AS s ON f.status_id = s.status_id
WHERE u1.id = {userId} AND s.status = 'accepted';
```

### 5. Найти общих друзей двух пользователей

#### findOther(Long userId, Long otherId)
```
SELECT u2.*
FROM User AS u1
JOIN Friends AS f ON u1.id = f.user_id
JOIN User AS u2 ON f.friend_id = u2.id
JOIN Status AS s ON f.status_id = s.status_id
WHERE u1.id = {userId} 
      AND s.status = 'accepted'
      AND u2.id IN (SELECT ou2.id
                    FROM User AS ou1
                    JOIN Friends AS of ON ou1.id = of.user_id
                    JOIN User AS ou2 ON of.friend_id = ou2.id
                    JOIN Status AS os ON of.status_id = os.status_id
                    WHERE ou1.id = {otherId} 
                          AND os.status = 'accepted');
```

### 6. Получить список всех пользователей

#### getUsers()
```
SELECT * FROM User
```

### 7. Создать пользователя

#### create(User user)
```
INSERT INTO User (email, login, name, birthday)
VALUE ({user.getEmail()}, {user.getLogin()}, {user.getName()}, {user.getBirthday()});
```

### 8. Обновить данные пользователя

#### update(User user)
```
UPDATE User 
SET email = {user.getEmail()}, 
    login = {user.getLogin()}, 
    name = {user.getName()}, 
    birthday = {user.getBirthday()}
WHERE id = {user.getId()}
```

## Запросы на языке SQL для модели Film

### 1. Пользователь ставит лайк фильму

#### addLike(Long filmId, Long userId) 
```
INSERT INTO Film_User (film_id, user_id)
VALUE ({filmId}, {userId});
```

### 2. Пользователь удаляет лайк с фильма

#### deleteLike(Long filmId, Long userId)
```
DELETE FROM Film_User
WHERE film_id = {filmId} AND user_id = {userId};
```

### 3. Получить список N популярных фильмов

#### findPopular(Integer count)
```
SELECT f.id,
       f.name AS title,
       f.description,
       f.releaseDate,
       f.duration,
       r.name AS AMP_Rating
FROM Film AS f
JOIN Rating AS r ON f.rating_id = r.rating_id;
JOIN (SELECT fu.film_id AS liked_film_id,
             COUNT(fu.user_id) AS likes
      FROM Film_User AS fu
      GROUP BY fu.film_id
      ORDER BY likes DESC) AS liked_films ON f.id = liked_films.liked_film_id
LIMIT {count};
```

### 4. Получить список всех фильмов

#### findAll()
```
SELECT f.id,
       f.name AS title,
       f.description,
       f.releaseDate,
       f.duration,
       r.name AS AMP_Rating
FROM Film AS f
JOIN Rating AS r ON f.rating_id = r.rating_id;
```

### 5. Добавить данные фильма
#### create(Film film)
```
INSERT INTO Film (name, 
                  description, 
                  releaseDate, 
                  duration, 
                  rating)
VALUE ({film.getName()}, 
       {film.getDescription()}, 
       {film.getReleaseDate()}, 
       {film.getDuration()},
       {film.getRating()});
```

### 6. Обновить данные фильма

#### update(Film film)
```
UPDATE Film 
SET name = {film.getName()}, 
    description = {film.getDescription()}, 
    releaseDate = {film.getReleaseDate()}, 
    duration = {film.getDuration()}, 
    rating = {film.getRating()}, 
WHERE id = {film.getId()}
```

### 7. Добавить жанр фильму

#### addGenre(Long filmId, Long genreId)
```
INSERT INTO Film_Genre (film_id, genre_id)
VALUE ({filmId}, {genreId});
```

#### addGenre(Long filmId, String genre)
```
INSERT INTO Film_Genre (film_id, genre_id)
SELECT {filmId}, g.genre_id
FROM Genre AS g
WHERE g.name = {genre};
```

### 8. Получить список людей поставивших лайк фильму

#### findFilmLikers(Long filmId)
```
SELECT u.name AS full_name
FROM Film AS f
JOIN Film_User AS fu ON f.id = fu.film_id;
JOIN User AS u ON fu.user_id = u.id;
WHERE f.id = {filmId}
LIMIT {count};
```

### 9. Получить количество лайков фильма

#### findFilmLikesCount(Long filmId)
```
SELECT f.name,
       COUNT(fu.user_id) AS likes
FROM Film AS f
JOIN Film_User AS fu ON f.id = fu.film_id;
WHERE f.id = {filmId}
GROUP BY f.name
```

### 10. Получить список фильмов которые лайкнул пользователь

#### findUserLikedFilms(Long userId)
```
SELECT f.name
FROM User AS u
JOIN Film_User AS fu ON u.id = fu.user_id;
JOIN Film AS f ON fu.film_id = f.id;
WHERE u.id = {userId}
```
