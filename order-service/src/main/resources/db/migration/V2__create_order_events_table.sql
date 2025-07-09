CREATE SEQUENCE order_event_id_seq START WITH 1 INCREMENT BY 50;

CREATE TABLE order_events
(
    id bigint default nextval('order_event_id_seq') not null,
    order_number text not null references orders(order_number),
    event_id text not null unique,
    event_type text not null,
    payload text not null,
    created_at timestamp,
    updated_at timestamp,
    primary key(id)
);