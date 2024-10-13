CREATE TABLE IF NOT EXISTS films (
  id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
  name varchar NOT NULL,
  description VARCHAR(200),
  releaseDate DATE,
  duration INTEGER,
  rating_id BIGINT
);

CREATE TABLE IF NOT EXISTS users (
  id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
  email VARCHAR NOT NULL,
  login VARCHAR NOT NULL,
  name VARCHAR,
  birthday DATE
);

CREATE TABLE IF NOT EXISTS genres (
  id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
  name VARCHAR NOT NULL
);

CREATE TABLE IF NOT EXISTS ratings (
  id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
  name VARCHAR NOT NULL,
  description VARCHAR
);

CREATE TABLE IF NOT EXISTS statuses (
  id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
  name VARCHAR NOT NULL
);

CREATE TABLE IF NOT EXISTS films_genres (
  film_id BIGINT NOT NULL,
  genre_id BIGINT NOT NULL,
  PRIMARY KEY (film_id, genre_id)
);

CREATE TABLE IF NOT EXISTS likes (
  film_id BIGINT NOT NULL,
  user_id BIGINT NOT NULL,
  PRIMARY KEY (film_id, user_id)
);

CREATE TABLE IF NOT EXISTS friends (
  user_id BIGINT NOT NULL,
  friend_id BIGINT NOT NULL,
  status_id BIGINT NOT NULL,
  PRIMARY KEY (user_id, friend_id)
);

CREATE TABLE IF NOT EXISTS feeds (
  event_id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
  user_id BIGINT NOT NULL,
  timestamp BIGINT NOT NULL,
  event_type VARCHAR NOT NULL,
  operation VARCHAR NOT NULL,
  entity_id BIGINT NOT NULL
);

CREATE TABLE IF NOT EXISTS directors (
  id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
  name VARCHAR NOT NULL
);

CREATE TABLE IF NOT EXISTS films_directors (
  film_id BIGINT NOT NULL,
  director_id BIGINT NOT NULL,
  PRIMARY KEY (film_id, director_id)
);

CREATE TABLE IF NOT EXISTS reviews (
  id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
  user_id BIGINT NOT NULL,
  film_id BIGINT NOT NULL,
  content VARCHAR,
  isPositive BOOLEAN,
  useful INTEGER
);

CREATE TABLE IF NOT EXISTS reviews_likes (
  review_id BIGINT NOT NULL,
  user_id BIGINT NOT NULL,
  isLike BOOLEAN,
  PRIMARY KEY (review_id, user_id)
);

ALTER TABLE films ADD FOREIGN KEY (rating_id) REFERENCES ratings (id);

ALTER TABLE films_genres ADD FOREIGN KEY (film_id) REFERENCES films (id) ON DELETE CASCADE;

ALTER TABLE films_genres ADD FOREIGN KEY (genre_id) REFERENCES genres (id);

ALTER TABLE likes ADD FOREIGN KEY (film_id) REFERENCES films (id) ON DELETE CASCADE;

ALTER TABLE likes ADD FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE;

ALTER TABLE friends ADD FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE;

ALTER TABLE friends ADD FOREIGN KEY (friend_id) REFERENCES users (id) ON DELETE CASCADE;

ALTER TABLE friends ADD FOREIGN KEY (status_id) REFERENCES statuses (id);

ALTER TABLE feeds ADD FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE;

ALTER TABLE films_directors ADD FOREIGN KEY (film_id) REFERENCES films (id) ON DELETE CASCADE;

ALTER TABLE films_directors ADD FOREIGN KEY (director_id) REFERENCES directors (id);

ALTER TABLE reviews ADD FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE;

ALTER TABLE reviews ADD FOREIGN KEY (film_id) REFERENCES films (id) ON DELETE CASCADE;

ALTER TABLE reviews_likes ADD FOREIGN KEY (review_id) REFERENCES reviews (id) ON DELETE CASCADE;

ALTER TABLE reviews_likes ADD FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE;