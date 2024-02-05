INSERT INTO users (name, password, created_at, updated_at)
VALUES ('User', '123', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

INSERT INTO users (name, password, created_at, updated_at)
VALUES ('Admin', '123', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);


INSERT
INTO
  books
  (  name, author_name, price, in_stock, stock_level, created_at, updated_at, category, number_of_days_for_borrow)
VALUES
  (  'book for dummies', 'dummy author', 30, 1, 1, NOW(), NOW(), 'dummy', 1);

INSERT
INTO
  books
  (  name, author_name, price, in_stock, stock_level, created_at, updated_at, category, number_of_days_for_borrow)
VALUES
  (  'the book of wisdom', 'wisdom author', 100, 1, 1, NOW(), NOW(), 'wisdom', 1);

INSERT
INTO
  books
  (  name, author_name, price, in_stock, stock_level, created_at, updated_at, category, number_of_days_for_borrow)
VALUES
  (  'the book of wisdom part 2', 'wisdom author', 200, 1, 1, NOW(), NOW(), 'wisdom', 2);

 INSERT
  INTO
    books
    (  name, author_name, price, in_stock, stock_level, created_at, updated_at, category, number_of_days_for_borrow)
  VALUES
    (  'dummies for all', 'dummy author', 20, 0, 1, NOW(), NOW(), 'dummy', 2);