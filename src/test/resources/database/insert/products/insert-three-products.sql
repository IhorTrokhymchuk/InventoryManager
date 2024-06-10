INSERT INTO products (id, name, uniq_code, wholesale_price, retail_price)
VALUES (1, 'PRODUCT1', 'PRODUCT1', 9.99, 19.99);
INSERT INTO products_categories (product_id, category_id)
VALUES (1, 1);
INSERT INTO products_categories (product_id, category_id)
VALUES (1, 2);

INSERT INTO products (id, name, uniq_code, wholesale_price, retail_price)
VALUES (2, 'PRODUCT2', 'PRODUCT2', 19.99, 29.99);
INSERT INTO products_categories (product_id, category_id)
VALUES (2, 1);

INSERT INTO products (id, name, uniq_code, wholesale_price, retail_price)
VALUES (3, 'PRODUCT3', 'PRODUCT3', 119.99, 219.99);
INSERT INTO products_categories (product_id, category_id)
VALUES (3, 3);
