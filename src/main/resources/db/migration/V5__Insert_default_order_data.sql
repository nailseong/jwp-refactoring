INSERT INTO orders (id, order_table_id, order_status, ordered_time)
    VALUES (1, 1, 'COOKING', '2022-11-03T00:00:00.000000');
INSERT INTO order_line_item (seq, order_id, menu_id, quantity)
    VALUES (1, 1, 4, 2);
