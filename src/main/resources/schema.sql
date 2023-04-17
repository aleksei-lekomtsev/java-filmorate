CREATE TABLE IF NOT EXISTS "film" (
  "id" integer generated by default as identity PRIMARY KEY,
  "name" varchar,
  "description" varchar,
  "release_date" timestamp,
  "duration" integer,
  "mpa_id" integer
);

CREATE TABLE IF NOT EXISTS "like" (
  "id" integer generated by default as identity PRIMARY KEY,
  "film_id" integer,
  "user_id" integer
);

CREATE TABLE IF NOT EXISTS "user" (
  "id" integer generated by default as identity PRIMARY KEY,
  "email" varchar,
  "login" varchar,
  "name" varchar,
  "birthday" date
);

CREATE TABLE IF NOT EXISTS "friend" (
  "id" integer generated by default as identity PRIMARY KEY,
  "user_id" integer,
  "friend_id" integer
);

CREATE TABLE IF NOT EXISTS "genre" (
  "id" integer generated by default as identity PRIMARY KEY,
  "name" varchar
);

CREATE TABLE IF NOT EXISTS "film_genre" (
  "film_id" integer,
  "genre_id" integer,
  PRIMARY KEY ("film_id", "genre_id")
);

CREATE TABLE IF NOT EXISTS "mpa" (
  "id" integer generated by default as identity PRIMARY KEY,
  "name" varchar
);

ALTER TABLE "like" ADD FOREIGN KEY ("film_id") REFERENCES "film" ("id");

ALTER TABLE "like" ADD FOREIGN KEY ("user_id") REFERENCES "user" ("id");

ALTER TABLE "film_genre" ADD FOREIGN KEY ("film_id") REFERENCES "film" ("id");

ALTER TABLE "film_genre" ADD FOREIGN KEY ("genre_id") REFERENCES "genre" ("id");

ALTER TABLE "film" ADD FOREIGN KEY ("mpa_id") REFERENCES "mpa" ("id");

ALTER TABLE "friend" ADD FOREIGN KEY ("user_id") REFERENCES "user" ("id");