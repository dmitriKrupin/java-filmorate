//Если вам нужны некоторые данные в базе, их инициализация обычно описывается в файле data.sql
MERGE INTO MPA (MPA_ID, RATING_NAME)
    VALUES (1, 'G'),
           (2, 'PG'),
           (3, 'PG13'),
           (4, 'R'),
           (5, 'NC17');

MERGE INTO GENRE (GENRE_ID, GENRE)
    VALUES (1, 'COMEDY'),
           (2, 'DRAMA'),
           (3, 'CARTOON'),
           (4, 'THRILLER'),
           (5, 'DOCUMENTARY'),
           (6, 'ACTION');