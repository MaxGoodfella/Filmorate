--DROP TABLE FILM_GENRE, GENRES, FILM_RATING, FILM_FANS, USERS, USER_FRIENDSHIP, FILMS;

create table IF NOT EXISTS FILMS
(
    FILM_ID      INTEGER auto_increment,
    NAME         CHARACTER VARYING      not null,
    DESCRIPTION  CHARACTER VARYING(200) not null,
    RELEASE_DATE DATE                   not null,
    DURATION     INTEGER                not null,
    -- RATING_ID    INTEGER                not null,
    -- POPULARITY   INTEGER,
    constraint FILMS_PK
        primary key (FILM_ID)
        --,
    -- constraint FILMS_FILM_RATING_FK
        -- foreign key (RATING_ID) references FILM_RATING
);

create table IF NOT EXISTS USERS
(
    USER_ID       INTEGER auto_increment,
    NAME          CHARACTER VARYING,
    EMAIL         CHARACTER VARYING UNIQUE not null,
    LOGIN         CHARACTER VARYING UNIQUE not null,
    DATE_OF_BIRTH DATE              not null,
    constraint USERS_PK
        primary key (USER_ID)
);

create table IF NOT EXISTS FILM_FANS
(
    FILM_ID INTEGER not null,
    USER_ID INTEGER not null,
    constraint FILM_FANS_FILMS_FK
        foreign key (FILM_ID) references FILMS ON DELETE CASCADE,
    constraint FILM_FANS_USERS_FK
        foreign key (USER_ID) references USERS ON DELETE CASCADE
);

create table IF NOT EXISTS GENRES
(
    GENRE_ID   INTEGER auto_increment,
    GENRE_NAME CHARACTER VARYING UNIQUE not null,
    constraint GENRE_PK
        primary key (GENRE_ID)
);

create table IF NOT EXISTS FILM_GENRE
(
    FILM_ID  INTEGER not null,
    GENRE_ID INTEGER not null,
    constraint FILM_GENRE_FILMS_FK
        foreign key (FILM_ID) references FILMS ON DELETE CASCADE,
    constraint FILM_GENRE_GENRE_FK
        foreign key (GENRE_ID) references GENRES ON DELETE CASCADE
);

create table IF NOT EXISTS FILM_RATING
(
    RATING_ID   INTEGER auto_increment,
    RATING_NAME CHARACTER VARYING UNIQUE not null,
    constraint FILM_RATING_PK
        primary key (RATING_ID)
);

create table IF NOT EXISTS USER_FRIENDSHIP
(
    USER_ID           INTEGER not null,
    FRIEND_ID         INTEGER not null,
    constraint USER_FRIENDSHIP_USERS_FK
        foreign key (USER_ID) references USERS ON DELETE CASCADE,
    constraint USER_FRIENDSHIP_USERS_FK_1
        foreign key (FRIEND_ID) references USERS ON DELETE CASCADE
);

