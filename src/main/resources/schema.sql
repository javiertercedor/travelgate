CREATE TABLE search_result (
   id BIGINT AUTO_INCREMENT PRIMARY KEY,
   search_id TEXT,
   hotel_code VARCHAR(255),
   price INT,
   status VARCHAR(255)
);
