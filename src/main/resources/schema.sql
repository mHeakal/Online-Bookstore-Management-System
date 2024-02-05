CREATE TABLE users (
  id BIGINT AUTO_INCREMENT NOT NULL,
   name VARCHAR(255) NOT NULL,
   password VARCHAR(255) NOT NULL,
   created_at TIMESTAMP,
   updated_at TIMESTAMP,
   CONSTRAINT pk_users PRIMARY KEY (id)
);

CREATE TABLE books (
  id BIGINT AUTO_INCREMENT NOT NULL,
   name VARCHAR(255) NOT NULL,
   author_name VARCHAR(255) NOT NULL,
   price DECIMAL NOT NULL,
   in_stock INT NOT NULL,
   borrowed_copies_count INT DEFAULT 0 NOT NULL,
   stock_level INT,
   created_at TIMESTAMP,
   updated_at TIMESTAMP,
   category VARCHAR(255) NOT NULL,
   number_of_days_for_borrow INT,
   CONSTRAINT pk_books PRIMARY KEY (id)
);

CREATE TABLE user_book_requests (
  id BIGINT AUTO_INCREMENT NOT NULL,
   status VARCHAR(255) NOT NULL,
   user_id BIGINT,
   book_id BIGINT NOT NULL,
   total_price BIGINT,
   requested_at TIMESTAMP,
   updated_at TIMESTAMP,
   returned_at TIMESTAMP,
   CONSTRAINT pk_user_book_requests PRIMARY KEY (id)
);

ALTER TABLE user_book_requests ADD CONSTRAINT FK_USER_BOOK_REQUESTS_ON_BOOK FOREIGN KEY (book_id) REFERENCES books (id);

ALTER TABLE user_book_requests ADD CONSTRAINT FK_USER_BOOK_REQUESTS_ON_USER FOREIGN KEY (user_id) REFERENCES users (id);

CREATE TABLE user_browsing_history (
  id BIGINT AUTO_INCREMENT NOT NULL,
   user_id BIGINT,
   book_id BIGINT NOT NULL,
   browsing_history BIGINT,
   CONSTRAINT pk_user_browsing_history PRIMARY KEY (id)
);

ALTER TABLE user_browsing_history ADD CONSTRAINT FK_USER_BROWSING_HISTORY_ON_BOOK FOREIGN KEY (book_id) REFERENCES books (id);

ALTER TABLE user_browsing_history ADD CONSTRAINT FK_USER_BROWSING_HISTORY_ON_USER FOREIGN KEY (user_id) REFERENCES users (id);
