--new

create table if not exists MPA
(
    MPA_ID      INTEGER auto_increment,
    RATING_NAME CHARACTER VARYING(10),
    constraint MPA_PK
        primary key (MPA_ID)
);

create unique index if not exists MPA_RATING_NAME_UINDEX
    on MPA (RATING_NAME);

create table if not exists GENRE
(
    GENRE_ID INTEGER auto_increment,
    GENRE    CHARACTER VARYING,
    constraint GENRE_PK
        primary key (GENRE_ID)
);

create unique index if not exists GENRE_GENRE_UINDEX
    on GENRE (GENRE);

--
create table if not exists FILMS
(
    FILM_ID      INTEGER auto_increment,
    NAME         CHARACTER VARYING not null,
    DESCRIPTION  CHARACTER VARYING(200),
    RELEASE_DATE CHARACTER VARYING,
    DURATION     INTEGER,
    MPA_ID       INTEGER           not null,
    constraint FILMS_MPA_MPA_ID_FK
        foreign key (MPA_ID) references MPA
);

create unique index if not exists FILMS_FILM_ID_UINDEX
    on FILMS (FILM_ID);

alter table FILMS
    add constraint if not exists FILMS_PK
        primary key (FILM_ID);

create table if not exists GENRE_LIST
(
    GENRE_ID INTEGER not null,
    FILM_ID  INTEGER not null,
    constraint GENRE_LIST_FILMS_FILM_ID_FK
        foreign key (FILM_ID) references FILMS,
    constraint GENRE_LIST_GENRE_GENRE_ID_FK
        foreign key (GENRE_ID) references GENRE
);

create table if not exists USERS
(
    USER_ID  INTEGER auto_increment,
    EMAIL    CHARACTER VARYING not null,
    LOGIN    CHARACTER VARYING not null,
    NAME     CHARACTER VARYING,
    BIRTHDAY DATE,
    constraint USERS_PK
        primary key (USER_ID)
);

create unique index if not exists USERS_USER_ID_UINDEX
    on USERS (USER_ID);

create table if not exists LIKES_LIST
(
    FILM_ID INTEGER not null,
    USER_ID INTEGER not null,
    constraint LIKES_LIST_FILMS_FILM_ID_FK
        foreign key (FILM_ID) references FILMS,
    constraint LIKES_LIST_USERS_USER_ID_FK
        foreign key (USER_ID) references USERS
);

create unique index if not exists LIKES_LIST_FILM_ID_USER_ID_UINDEX
    on LIKES_LIST (FILM_ID, USER_ID);

create table if not exists FRIENDS_ID_LIST
(
    USER_ID                   INTEGER not null,
    FRIENDS_ID                INTEGER not null,
    STATUS_APPLICATION_FRIEND BOOLEAN not null,
    constraint FRIENDS_ID_LIST_USERS_USER_ID_FK
        foreign key (USER_ID) references USERS,
    constraint FRIENDS_ID_LIST_USERS_USER_ID_FK_2
        foreign key (FRIENDS_ID) references USERS
);

create unique index if not exists FRIENDS_ID_LIST_USER_ID_FRIENDS_ID_STATUS_APPLICATION_FRIEND_UINDEX
    on FRIENDS_ID_LIST (USER_ID, FRIENDS_ID, STATUS_APPLICATION_FRIEND);