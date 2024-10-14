 # Filmorate

<u>**Filmorate** — это учебный проект, представляющий собой бэкенд-сервис для поиска и оценки фильмов.</u>

### Стек технологий и опыт разработки

В процессе разработки использовались следующие технологии:

- **Java 11**
- **Spring Boot**
- **Spring Data JPA**
-  **H2 Database**
- **Lombok**
- **REST API** 
- **JUnit 5** 
- **Maven**
- **Postman**
- **GitHub**

## Функционал
### Фильмы:
- Добавить фильм.
- Обновить фильм.
- Найти фильм по id.
- Получить список всех фильмов.
- Удалить фильм.
- Поставить лайк.
- Удалить лайк.
- Отобразить популярные фильмы.
- Поиск фильмов по названию и/или режиссёру.

### Пользователи:
- Добавить пользователя.
- Обновить пользователя.
- Найти пользователя по id.
- Получить список пользователей.
- Удалить пользователя.
- Добавить пользователя в друзья.
- Удалить пользователя из друзей.
- Найти общих друзей с другим пользователем.
- Получить список рекомендаций по фильмам.

### Жанры:
- Добавить жанр.
- Обновить жанр.
- Найти жанр по id.
- Получить список всех жанров.
- Удалить жанр.

### Рейтинг (MPA):
- Добавить рейтинг.
- Обновить рейтинг.
- Найти рейтинг по id.
- Получить список всех рейтингов.
- Удалить рейтинг.

### Отзывы на фильм:
- Добавить отзыв.
- Обновить отзыв.
- Получить список отзывов о фильме.
- Найти отзыв по id.
- Поставить лайк отзыву.
- Поставить дизлайк отзыву.
- Удалить лайк.
- Удалить дизлайк.
- Удалить отзыв.

### Режиссёры:
- Добавить режиссёра.
- Обновить данные режиссёра.
- Получить список режиссёров.
- Найти режиссёра по id.
- Удалить режиссёра.

### Новостная лента:
- Добавить событие.
- Получить историю событий пользователя по id.

## Этапы разработки

### Разработка приложения велась в 4 этапа.

**1 этап (Индивидуальный)**: 
   - Реализована базовая архитектура приложения.
   - Созданы модели данных `User` и `Film`.
   - Организована логика хранения данных в памяти приложения.
   - Настроена валидация пользовательских данных.
   - Включено логирование операций.
   - Написаны юнит-тесты для проверки работы с данными в памяти.

**2 этап (Индивидуальный)**:
   - Оптимизирована архитектура приложения.
   - Логика разделена на слои бизнес-логики и системы хранения данных.
   - Внедрены зависимости с помощью Spring Framework.
   - Реализованы контроллеры для обработки HTTP-запросов (эндпоинтов).
   - Добавлено расширенное логирование для отслеживания операций.

**3 этап (Индивидуальный)**:
   - Проведена интеграция с базой данных.
   - Разработаны DAO-классы для взаимодействия с сущностями `User`, `Film`, `Genre`, `MPA`.
   - Написаны SQL-запросы для работы с базой данных.
   - Обновлены тесты для проверки работы с данными из БД.

**4 этап (Групповой)**:
   - Проведено первоначальное планирование, проектирование, разработка, ревью, тестирование, презентация заказчику.
   - Добавлены ключевые функции, такие как: система отзывов, поиск, общие фильмы, рекомендации, лента событий, популярные фильмы, фильмы по режиссерам, удаление пользователей и фильмов.
   - Разработаны DAO-классы для работы с сущностями `Director`, `Review`, `Feed`.
   - Созданы основные таблицы (`directors`, `reviews`, `feeds`) и соединительные таблицы (`films_directors`, `reviews_like`).
   - Написаны дополнительные SQL-запросы для обеспечения работы с новыми сущностями.
   - Обновлены тесты для проверки новых функций и работы с данными из БД.

## ER-диаграмма

![Схема базы данных проекта Filmorate](/assets/images/Filmorate_final.png)

<details><summary><strong><span style="font-size: 20px;">Пояснения к таблицам БД</span></strong></summary>
   
### Film
- Информация о фильмах

### Rating
- Информация о рейтингах фильмов

### Film_Genre
- Соединительная таблица для фильмов и их жанров

### Genre
- Информация о жанрах фильмов

### User
- Информация о пользователях приложения

### Film_User
- Соединительная таблица для фильмов и пользователях, оценивших фильм

### Friends
- Соединительная таблица для пользователей и их друзей (других пользователей)

### Statuses
- Информация о статусе запроса "дружбы" между пользователями

### Directors
- Информация о режиссёрах

### Film_Directors
- Соединительная таблица для фильма и его режиссёра

### Feeds
- Информация о последних событиях на платформе 

### Reviews
- Информация об отзыве к фильму

### Reviews_likes
- Соединительная таблица для отзыва и лайков/дизлайков
</details>

<details><summary><strong><span style="font-size: 20px;">Примеры SQL-запросов для модели Film</span></strong></summary>
   
### 1. Добавить фильм 
#### create(Film film)
```sql
INSERT INTO films(name,
                  description,
                  releaseDate,
                  duration,
                  rating_id)
VALUES ({film.getName()}, 
       {film.getDescription()}, 
       {film.getReleaseDate()}, 
       {film.getDuration()},
       {film.getRating()});
```

### 2. Обновить фильм
#### update(Film film)
```sql
UPDATE Film 
SET name = {film.getName()}, 
    description = {film.getDescription()}, 
    releaseDate = {film.getReleaseDate()}, 
    duration = {film.getDuration()}, 
    rating = {film.getRating()}, 
WHERE id = {film.getId()};
```

### 3. Найти фильм по id
#### findFilm(Long filmId)
```sql
SELECT * 
FROM films 
WHERE id = {filmId};
```

### 4. Получить список всех фильмов
#### Collection<Film> findAll()
```sql
SELECT * 
FROM films;
```

### 5. Удалить фильм
#### delete(Long filmId)
```sql
DELETE FROM films 
WHERE id = {filmId};
```

### 6. Поставить лайк
#### addLike(Film film, User user)
```sql
INSERT INTO likes(film_id, user_id) 
VALUES ({film.getId()}, {user.getId()});
```

### 7. Удалить лайк
#### deleteLike(Film film, User user)
```sql
DELETE FROM likes 
WHERE film_id = {film.getId()} AND user_id = {user.getId()};
```

### 8. Отобразить популярные фильмы
#### findPopular(Integer count)
```sql
SELECT f.* FROM films AS f  
LEFT JOIN (SELECT film_id, count(l.user_id) likes
           FROM likes AS l  
           GROUP BY l.film_id
           ORDER BY count(l.user_id) DESC 
           LIMIT {count})
AS liked_films ON f.id = liked_films.film_id  
ORDER BY liked_films.likes DESC;
```

### 9. Поиск фильмов по режиссёру
#### findDirectorFilms(Long directorId)
```sql
SELECT f.* FROM films_directors AS fd 
LEFT JOIN films AS f ON fd.film_id = f.id
WHERE fd.director_id = {directorId};
```

### 10. Получить количество лайков у фильма
#### getLikes(Long filmId)
```sql
SELECT user_id FROM likes
WHERE film_id = {filmId};
```
</details>

<details><summary><strong><span style="font-size: 20px;">Примеры SQL-запросов для модели User</span></strong></summary>
   
### 1. Добавить пользователя
#### create(User user)
```sql
INSERT INTO users(email, login, name, birthday)
VALUES ({user.getEmail()},
        {user.getLogin()},
        {user.getName()},
        {user.getBirthday()});
```

### 2. Обновить пользователя
#### update(User newUser)
```sql
UPDATE users
SET email = {newUser.getEmail()},
    login = {newUser.getLogin()},
    name = {newUser.getName()},
    birthday = {newUser.getBirthday()}
WHERE id = newUser.getId();
```

### 3. Найти пользователя по id
#### findUser(Long userId)
```sql
SELECT *
FROM users WHERE id = {userId};
```

### 4. Получить список пользователей
#### getUsers()
```sql
SELECT u.id, u.email, u.login, u.name, u.birthday, GROUP_CONCAT(f.friend_id) AS friends
FROM users u  
LEFT JOIN friends f ON u.id = f.user_id
LEFT JOIN statuses AS s ON f.status_id = s.id AND s.name = 'Подтверждённая'
GROUP BY u.id, u.email, u.login, u.name, u.birthday;
```

### 5. Удалить пользователя
#### delete(Long id)
```sql
DELETE FROM users
WHERE id = {id};
```

### 6. Добавить пользователя в друзья
#### addFriend(Long userId, Long friendId)
```sql
INSERT INTO friends(user_id, friend_id, status_id)
VALUES ({user_id}, {friend_id}, {status_id});
```

### 7. Удалить пользователя из друзей
#### deleteFriend(Long userId, Long friendId)
```sql
DELETE FROM friends
WHERE user_id = {userId} AND friend_id = {friendId};
```
</details>

<details><summary><strong><span style="font-size: 20px;">Примеры SQL-запросов для модели Genre</span></strong></summary>

### 1. Добавить жанр
#### create(Genre genre)
```sql
INSERT INTO genres(name)
VALUES ({genre.getName()});
```

### 2. Найти жанр по id
#### findGenre(Long genreId)
```sql
SELECT *
FROM genres
WHERE id = genreId;
```

### 3. Получить список всех жанров
#### findAll()
```sql
SELECT *
FROM genres;
```
</details>

<details><summary><strong><span style="font-size: 20px;">Примеры SQL-запросов для модели Mpa</span></strong></summary>

### 1. Добавить рейтинг
#### create(Mpa mpa)
```sql
INSERT INTO ratings(name, description)
VALUES ({mpa.getName()},
        {mpa.getDescription()});
```

### 2. Найти рейтинг по id
#### findMpa(Long mpaId)
```sql
SELECT * FROM ratings
WHERE id = {mpaId};
```

### 3. Получить список всех рейтингов
#### findAll()
```sql
SELECT *
FROM ratings;
```
</details>

<details><summary><strong><span style="font-size: 20px;">Примеры SQL-запросов для модели Review</span></strong></summary>

### 1. Добавить отзыв
#### create(Review review)
```sql
INSERT INTO reviews(user_id, film_id, content, isPositive, useful)
VALUES ({review.getUserId()},
        {review.getFilmId()},
        {review.getContent()},
        {review.getIsPositive()},
        {review.getUseful()});
```

### 2. Получить список отзывов о фильме
#### reviewsByFilmId(Long filmId, Integer count)
```sql
SELECT *
FROM reviews 
WHERE film_id = {filmId}
LIMIT {count};
```

### 3. Поставить лайк отзыву
#### increaseUseful(Long reviewId)
```sql
UPDATE reviews
SET useful = useful + 1
WHERE id = {reviewId};
```

### 4. Поставить дизлайк отзыву
#### decreaseUseful(Long reviewId)
```sql
UPDATE reviews
SET useful = useful - 1
WHERE id = {reviewId};
```
</details>

<details><summary><strong><span style="font-size: 20px;">Примеры SQL-запросов для модели Director</span></strong></summary>

### 1. Добавить режиссёра
#### create(Director director)
```sql
INSERT INTO directors(name)
VALUES ({director.getName});
```

### 2. Получить список режиссёров
#### findAll()
```sql
SELECT *
FROM directors;
```

### 3. Найти режиссёра по id
#### findDirector(Long directorId)
```sql
SELECT * FROM directors
WHERE id = {directorId};
```

### 4. Удалить режиссёра
#### delete(Long directorId)
```sql
DELETE FROM directors
WHERE id = {directorId};
```
</details>

<details><summary><strong><span style="font-size: 20px;">Примеры SQL-запросов для модели Feed</span></strong></summary>

### 1. Добавить событие
#### addEvent(Feed feed)
```sql
INSERT INTO events (user_id, timestamp, event_type, operation, entity_id)
VALUES ({feed.getUserId()},
                    {feed.getTimestamp()},
                    {feed.getEventType().name()},
                    {feed.getOperation().name()},
                    {feed.getEntityId()});
```

### 2. Получить историю событий пользователя по id
#### getEventsByUserId(Long userId)
```sql
SELECT * FROM events
WHERE user_id = {userId}
ORDER BY timestamp ASC;
```
</details>