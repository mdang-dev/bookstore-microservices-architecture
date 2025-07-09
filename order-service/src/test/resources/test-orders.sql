-- Clear existing data and reset sequences (PostgreSQL syntax)
TRUNCATE TABLE order_items CASCADE;
TRUNCATE TABLE orders CASCADE;
ALTER SEQUENCE order_id_seq RESTART WITH 100;
ALTER SEQUENCE order_item_id_seq RESTART WITH 100;
-- Insert test orders
INSERT INTO orders (
        id,
        order_number,
        username,
        customer_name,
        customer_email,
        customer_phone,
        delivery_address_line1,
        delivery_address_line2,
        delivery_city,
        delivery_state,
        delivery_zip_code,
        delivery_country,
        status,
        comments
    )
VALUES (
        1,
        'order-123',
        'wok',
        'Minh Dan',
        'minh.dan@example.com',
        '0999999999',
        '456 Le Loi',
        'Floor 3',
        'Ho Chi Minh City',
        'SG',
        '700000',
        'Vietnamese',
        'NEW',
        NULL
    ),
    (
        2,
        'order-456',
        'wok',
        'Nguyen Huy',
        'huy.nguyen@example.com',
        '0888888888',
        '789 Tran Hung Dao',
        '',
        'Hanoi',
        'HN',
        '100000',
        'Vietnamese',
        'NEW',
        NULL
    );
-- Insert test order items
INSERT INTO order_items (order_id, code, name, price, quantity)
VALUES (1, 'P200', 'Clean Code', 50.0, 1),
    (1, 'P201', 'Effective Java', 60.0, 1),
    (
        2,
        'P202',
        'Java Concurrency in Practice',
        70.0,
        2
    );