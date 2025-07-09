CREATE SEQUENCE order_id_seq START WITH 1 INCREMENT BY 50;
CREATE SEQUENCE order_item_id_seq START WITH 1 INCREMENT BY 50;

CREATE TABLE orders
(
    id bigint default nextval('order_id_seq') not null,
    order_number text not null unique,
    username text not null,
    customer_name text not null,
    customer_email text not null,
    customer_phone text not null,
    delivery_address_line1 text not null,
    delivery_address_line2 text,
    delivery_city text not null,
    delivery_state text not null,
    delivery_zip_code text not null,
    delivery_country text not null,
    status text not null,
    comments text,
    create_at timestamp,
    update_at timestamp,
    primary key(id)
);

CREATE table order_items
(
    id bigint default nextval('order_item_id_seq') not null,
    code text not null,
    name text not null,
    price numeric not null,
    quantity integer not null,
    primary key(id),
    order_id bigint not null references orders(id)
);