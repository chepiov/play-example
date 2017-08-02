# --- Sample dataset

# --- !Ups

INSERT INTO author(id, last_name, first_name, middle_name, when_created, when_updated, version, state)
VALUES ('bdd6741d-71b9-493c-9c31-38af19b9e27c', 'Dodgson', 'Charles', 'Lutwidge', now(), now(), 1, false);

INSERT INTO genre(id, name, when_created, when_updated, version, state)
VALUES ('f022820c-cb84-4981-9184-46b6f9a17de8', 'Fantastic', now(), now(), 1, false);

INSERT INTO genre(id, name, when_created, when_updated, version, state)
VALUES ('f022820c-cb84-4981-9184-46b6f9a17de9', 'Science', now(), now(), 1, false);

INSERT INTO book(id, name, year, edition, author_id, when_created, when_updated, version, state)
VALUES (
  '0ae2a56c-5dd4-47ed-b230-690c11a786be',
  'Alice''s Adventures in Wonderland',
  1865,
  1,
  'bdd6741d-71b9-493c-9c31-38 af19b9e27c',
  now(),
  now(),
  1,
  false
);


INSERT INTO book(id, name, year, edition, author_id, when_created, when_updated, version, state)
VALUES (
  '0ae2a56c-5dd4-47ed-b230-690c11a786bc',
  'How Universe works',
  1865,
  1,
  'bdd6741d-71b9-493c-9c31-38 af19b9e27c',
  now(),
  now(),
  1,
  false
);

INSERT INTO book_genre(book_id, genre_id)
VALUES ('0ae2a56c-5dd4-47ed-b230-690c11a786be', 'f022820c-cb84-4981-9184-46b6f9a17de8');

INSERT INTO book_genre(book_id, genre_id)
VALUES ('0ae2a56c-5dd4-47ed-b230-690c11a786bc', 'f022820c-cb84-4981-9184-46b6f9a17de9');


# --- !Downs

DELETE FROM book_genre;
DELETE FROM book;
DELETE FROM genre;
DELETE FROM author;
