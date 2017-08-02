# --- Created by Ebean DDL
# To stop Ebean DDL generation, remove this comment and start using Evolutions

# --- !Ups

create table author (
  id                            uuid not null,
  last_name                     varchar(255) not null,
  first_name                    varchar(255) not null,
  middle_name                   varchar(255) not null,
  when_created                  timestamp not null,
  when_updated                  timestamp not null,
  version                       bigint not null,
  state                         boolean default false not null,
  constraint pk_author primary key (id)
);

create table book (
  id                            uuid not null,
  name                          varchar(255) not null,
  year                          integer not null,
  edition                       integer not null,
  author_id                     uuid not null,
  when_created                  timestamp not null,
  when_updated                  timestamp not null,
  version                       bigint not null,
  state                         boolean default false not null,
  constraint pk_book primary key (id)
);

create table book_genre (
  book_id                       uuid not null,
  genre_id                      uuid not null,
  constraint pk_book_genre primary key (book_id,genre_id)
);

create table genre (
  id                            uuid not null,
  name                          varchar(255),
  when_created                  timestamp not null,
  when_updated                  timestamp not null,
  version                       bigint not null,
  state                         boolean default false not null,
  constraint pk_genre primary key (id)
);

create table genre_book (
  genre_id                      uuid not null,
  book_id                       uuid not null,
  constraint pk_genre_book primary key (genre_id,book_id)
);

alter table book add constraint fk_book_author_id foreign key (author_id) references author (id) on delete restrict on update restrict;
create index ix_book_author_id on book (author_id);

alter table book_genre add constraint fk_book_genre_book foreign key (book_id) references book (id) on delete restrict on update restrict;
create index ix_book_genre_book on book_genre (book_id);

alter table book_genre add constraint fk_book_genre_genre foreign key (genre_id) references genre (id) on delete restrict on update restrict;
create index ix_book_genre_genre on book_genre (genre_id);

alter table genre_book add constraint fk_genre_book_genre foreign key (genre_id) references genre (id) on delete restrict on update restrict;
create index ix_genre_book_genre on genre_book (genre_id);

alter table genre_book add constraint fk_genre_book_book foreign key (book_id) references book (id) on delete restrict on update restrict;
create index ix_genre_book_book on genre_book (book_id);


# --- !Downs

alter table book drop constraint if exists fk_book_author_id;
drop index if exists ix_book_author_id;

alter table book_genre drop constraint if exists fk_book_genre_book;
drop index if exists ix_book_genre_book;

alter table book_genre drop constraint if exists fk_book_genre_genre;
drop index if exists ix_book_genre_genre;

alter table genre_book drop constraint if exists fk_genre_book_genre;
drop index if exists ix_genre_book_genre;

alter table genre_book drop constraint if exists fk_genre_book_book;
drop index if exists ix_genre_book_book;

drop table if exists author;

drop table if exists book;

drop table if exists book_genre;

drop table if exists genre;

drop table if exists genre_book;

