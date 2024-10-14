 # Filmorate

<u>**Filmorate** � ��� ������� ������, �������������� ����� ������-������ ��� ������ � ������ �������.</u>

### ���� ���������� � ���� ����������

� �������� ���������� �������������� ��������� ����������:

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

## ����������
### ������:
- �������� �����.
- �������� �����.
- ����� ����� �� id.
- �������� ������ ���� �������.
- ������� �����.
- ��������� ����.
- ������� ����.
- ���������� ���������� ������.
- ����� ������� �� �������� �/��� ��������.

### ������������:
- �������� ������������.
- �������� ������������.
- ����� ������������ �� id.
- �������� ������ �������������.
- ������� ������������.
- �������� ������������ � ������.
- ������� ������������ �� ������.
- ����� ����� ������ � ������ �������������.
- �������� ������ ������������ �� �������.

### �����:
- �������� ����.
- �������� ����.
- ����� ���� �� id.
- �������� ������ ���� ������.
- ������� ����.

### ������� (MPA):
- �������� �������.
- �������� �������.
- ����� ������� �� id.
- �������� ������ ���� ���������.
- ������� �������.

### ������ �� �����:
- �������� �����.
- �������� �����.
- �������� ������ ������� � ������.
- ����� ����� �� id.
- ��������� ���� ������.
- ��������� ������� ������.
- ������� ����.
- ������� �������.
- ������� �����.

### ��������:
- �������� ��������.
- �������� ������ ��������.
- �������� ������ ���������.
- ����� �������� �� id.
- ������� ��������.

### ��������� �����:
- �������� �������.
- �������� ������� ������� ������������ �� id.

## ����� ����������

### ���������� ���������� ������ � 4 �����.

**1 ���� (��������������)**: 
   - ����������� ������� ����������� ����������.
   - ������� ������ ������ `User` � `Film`.
   - ������������ ������ �������� ������ � ������ ����������.
   - ��������� ��������� ���������������� ������.
   - �������� ����������� ��������.
   - �������� ����-����� ��� �������� ������ � ������� � ������.

**2 ���� (��������������)**:
   - �������������� ����������� ����������.
   - ������ ��������� �� ���� ������-������ � ������� �������� ������.
   - �������� ����������� � ������� Spring Framework.
   - ����������� ����������� ��� ��������� HTTP-�������� (����������).
   - ��������� ����������� ����������� ��� ������������ ��������.

**3 ���� (��������������)**:
   - ��������� ���������� � ����� ������.
   - ����������� DAO-������ ��� �������������� � ���������� `User`, `Film`, `Genre`, `MPA`.
   - �������� SQL-������� ��� ������ � ����� ������.
   - ��������� ����� ��� �������� ������ � ������� �� ��.

**4 ���� (���������)**:
   - ��������� �������������� ������������, ��������������, ����������, �����, ������������, ����������� ���������.
   - ��������� �������� �������, ����� ���: ������� �������, �����, ����� ������, ������������, ����� �������, ���������� ������, ������ �� ����������, �������� ������������� � �������.
   - ����������� DAO-������ ��� ������ � ���������� `Director`, `Review`, `Feed`.
   - ������� �������� ������� (`directors`, `reviews`, `feeds`) � �������������� ������� (`films_directors`, `reviews_like`).
   - �������� �������������� SQL-������� ��� ����������� ������ � ������ ����������.
   - ��������� ����� ��� �������� ����� ������� � ������ � ������� �� ��.

## ER-���������

![����� ���� ������ ������� Filmorate](/assets/images/Filmorate_final.png)

<details><summary><strong><span style="font-size: 20px;">��������� � �������� ��</span></strong></summary>
   
### Film
- ���������� � �������

### Rating
- ���������� � ��������� �������

### Film_Genre
- �������������� ������� ��� ������� � �� ������

### Genre
- ���������� � ������ �������

### User
- ���������� � ������������� ����������

### Film_User
- �������������� ������� ��� ������� � �������������, ��������� �����

### Friends
- �������������� ������� ��� ������������� � �� ������ (������ �������������)

### Statuses
- ���������� � ������� ������� "������" ����� ��������������

### Directors
- ���������� � ���������

### Film_Directors
- �������������� ������� ��� ������ � ��� ��������

### Feeds
- ���������� � ��������� �������� �� ��������� 

### Reviews
- ���������� �� ������ � ������

### Reviews_likes
- �������������� ������� ��� ������ � ������/���������
</details>

<details><summary><strong><span style="font-size: 20px;">������� SQL-�������� ��� ������ Film</span></strong></summary>
   
### 1. �������� ����� 
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

### 2. �������� �����
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

### 3. ����� ����� �� id
#### findFilm(Long filmId)
```sql
SELECT * 
FROM films 
WHERE id = {filmId};
```

### 4. �������� ������ ���� �������
#### Collection<Film> findAll()
```sql
SELECT * 
FROM films;
```

### 5. ������� �����
#### delete(Long filmId)
```sql
DELETE FROM films 
WHERE id = {filmId};
```

### 6. ��������� ����
#### addLike(Film film, User user)
```sql
INSERT INTO likes(film_id, user_id) 
VALUES ({film.getId()}, {user.getId()});
```

### 7. ������� ����
#### deleteLike(Film film, User user)
```sql
DELETE FROM likes 
WHERE film_id = {film.getId()} AND user_id = {user.getId()};
```

### 8. ���������� ���������� ������
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

### 9. ����� ������� �� ��������
#### findDirectorFilms(Long directorId)
```sql
SELECT f.* FROM films_directors AS fd 
LEFT JOIN films AS f ON fd.film_id = f.id
WHERE fd.director_id = {directorId};
```

### 10. �������� ���������� ������ � ������
#### getLikes(Long filmId)
```sql
SELECT user_id FROM likes
WHERE film_id = {filmId};
```
</details>

<details><summary><strong><span style="font-size: 20px;">������� SQL-�������� ��� ������ User</span></strong></summary>
   
### 1. �������� ������������
#### create(User user)
```sql
INSERT INTO users(email, login, name, birthday)
VALUES ({user.getEmail()},
        {user.getLogin()},
        {user.getName()},
        {user.getBirthday()});
```

### 2. �������� ������������
#### update(User newUser)
```sql
UPDATE users
SET email = {newUser.getEmail()},
    login = {newUser.getLogin()},
    name = {newUser.getName()},
    birthday = {newUser.getBirthday()}
WHERE id = newUser.getId();
```

### 3. ����� ������������ �� id
#### findUser(Long userId)
```sql
SELECT *
FROM users WHERE id = {userId};
```

### 4. �������� ������ �������������
#### getUsers()
```sql
SELECT u.id, u.email, u.login, u.name, u.birthday, GROUP_CONCAT(f.friend_id) AS friends
FROM users u  
LEFT JOIN friends f ON u.id = f.user_id
LEFT JOIN statuses AS s ON f.status_id = s.id AND s.name = '�������������'
GROUP BY u.id, u.email, u.login, u.name, u.birthday;
```

### 5. ������� ������������
#### delete(Long id)
```sql
DELETE FROM users
WHERE id = {id};
```

### 6. �������� ������������ � ������
#### addFriend(Long userId, Long friendId)
```sql
INSERT INTO friends(user_id, friend_id, status_id)
VALUES ({user_id}, {friend_id}, {status_id});
```

### 7. ������� ������������ �� ������
#### deleteFriend(Long userId, Long friendId)
```sql
DELETE FROM friends
WHERE user_id = {userId} AND friend_id = {friendId};
```
</details>

<details><summary><strong><span style="font-size: 20px;">������� SQL-�������� ��� ������ Genre</span></strong></summary>

### 1. �������� ����
#### create(Genre genre)
```sql
INSERT INTO genres(name)
VALUES ({genre.getName()});
```

### 2. ����� ���� �� id
#### findGenre(Long genreId)
```sql
SELECT *
FROM genres
WHERE id = genreId;
```

### 3. �������� ������ ���� ������
#### findAll()
```sql
SELECT *
FROM genres;
```
</details>

<details><summary><strong><span style="font-size: 20px;">������� SQL-�������� ��� ������ Mpa</span></strong></summary>

### 1. �������� �������
#### create(Mpa mpa)
```sql
INSERT INTO ratings(name, description)
VALUES ({mpa.getName()},
        {mpa.getDescription()});
```

### 2. ����� ������� �� id
#### findMpa(Long mpaId)
```sql
SELECT * FROM ratings
WHERE id = {mpaId};
```

### 3. �������� ������ ���� ���������
#### findAll()
```sql
SELECT *
FROM ratings;
```
</details>

<details><summary><strong><span style="font-size: 20px;">������� SQL-�������� ��� ������ Review</span></strong></summary>

### 1. �������� �����
#### create(Review review)
```sql
INSERT INTO reviews(user_id, film_id, content, isPositive, useful)
VALUES ({review.getUserId()},
        {review.getFilmId()},
        {review.getContent()},
        {review.getIsPositive()},
        {review.getUseful()});
```

### 2. �������� ������ ������� � ������
#### reviewsByFilmId(Long filmId, Integer count)
```sql
SELECT *
FROM reviews 
WHERE film_id = {filmId}
LIMIT {count};
```

### 3. ��������� ���� ������
#### increaseUseful(Long reviewId)
```sql
UPDATE reviews
SET useful = useful + 1
WHERE id = {reviewId};
```

### 4. ��������� ������� ������
#### decreaseUseful(Long reviewId)
```sql
UPDATE reviews
SET useful = useful - 1
WHERE id = {reviewId};
```
</details>

<details><summary><strong><span style="font-size: 20px;">������� SQL-�������� ��� ������ Director</span></strong></summary>

### 1. �������� ��������
#### create(Director director)
```sql
INSERT INTO directors(name)
VALUES ({director.getName});
```

### 2. �������� ������ ���������
#### findAll()
```sql
SELECT *
FROM directors;
```

### 3. ����� �������� �� id
#### findDirector(Long directorId)
```sql
SELECT * FROM directors
WHERE id = {directorId};
```

### 4. ������� ��������
#### delete(Long directorId)
```sql
DELETE FROM directors
WHERE id = {directorId};
```
</details>

<details><summary><strong><span style="font-size: 20px;">������� SQL-�������� ��� ������ Feed</span></strong></summary>

### 1. �������� �������
#### addEvent(Feed feed)
```sql
INSERT INTO events (user_id, timestamp, event_type, operation, entity_id)
VALUES ({feed.getUserId()},
                    {feed.getTimestamp()},
                    {feed.getEventType().name()},
                    {feed.getOperation().name()},
                    {feed.getEntityId()});
```

### 2. �������� ������� ������� ������������ �� id
#### getEventsByUserId(Long userId)
```sql
SELECT * FROM events
WHERE user_id = {userId}
ORDER BY timestamp ASC;
```
</details>