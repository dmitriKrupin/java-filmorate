DELETE FROM USERS;
ALTER TABLE USERS ALTER COLUMN USER_ID RESTART WITH 1;

DELETE FROM FILMS;
ALTER TABLE FILMS ALTER COLUMN FILM_ID RESTART WITH 1;

DELETE FROM FRIENDS;

DELETE FROM LIKES;

DELETE FROM GENRE_LIST;