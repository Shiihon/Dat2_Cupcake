-- This script was generated by the ERD tool in pgAdmin 4.
-- Please log an issue at https://github.com/pgadmin-org/pgadmin4/issues/new/choose if you find any bugs, including reproduction steps.

BEGIN;


CREATE TABLE IF NOT EXISTS public.users
(
    user_id
    serial
    NOT
    NULL,
    user_email
    character
    varying
(
    50
) NOT NULL,
    user_password character varying
(
    50
) NOT NULL,
    user_role character varying
(
    30
) NOT NULL DEFAULT 'customer',
    user_balance integer NOT NULL DEFAULT 500,
    PRIMARY KEY
(
    user_id
)
    );

CREATE TABLE IF NOT EXISTS public.cupcake_bottoms
(
    cupcake_bottom_id
    serial
    NOT
    NULL,
    cupcake_bottom_name
    character
    varying
(
    30
) NOT NULL,
    cupcake_bottom_price integer NOT NULL,
    PRIMARY KEY
(
    cupcake_bottom_id
)
    );

CREATE TABLE IF NOT EXISTS public.cupcake_tops
(
    cupcake_top_id
    serial
    NOT
    NULL,
    cupcake_top_name
    character
    varying
(
    30
) NOT NULL,
    cupcake_top_price integer NOT NULL,
    PRIMARY KEY
(
    cupcake_top_id
)
    );

CREATE TABLE IF NOT EXISTS public.orders
(
    order_id
    serial
    NOT
    NULL,
    user_id
    integer
    NOT
    NULL,
    order_status
    character
    varying
(
    30
) NOT NULL,
    order_timestamp timestamp without time zone NOT NULL,
    PRIMARY KEY
(
    order_id
)
    );

CREATE TABLE IF NOT EXISTS public.order_items
(
    order_item_id
    serial
    NOT
    NULL,
    order_id
    integer
    NOT
    NULL,
    cupcake_bottom_id
    integer
    NOT
    NULL,
    cupcake_top_id
    integer
    NOT
    NULL,
    order_item_quantity
    integer
    NOT
    NULL,
    PRIMARY
    KEY
(
    order_item_id
)
    );

ALTER TABLE IF EXISTS public.orders
    ADD FOREIGN KEY (user_id)
    REFERENCES public.users (user_id) MATCH SIMPLE
    ON
UPDATE NO ACTION
ON
DELETE
NO ACTION
    NOT VALID;


ALTER TABLE IF EXISTS public.order_items
    ADD FOREIGN KEY (order_id)
    REFERENCES public.orders (order_id) MATCH SIMPLE
    ON
UPDATE NO ACTION
ON
DELETE
NO ACTION
    NOT VALID;


ALTER TABLE IF EXISTS public.order_items
    ADD FOREIGN KEY (cupcake_bottom_id)
    REFERENCES public.cupcake_bottoms (cupcake_bottom_id) MATCH SIMPLE
    ON
UPDATE NO ACTION
ON
DELETE
NO ACTION
    NOT VALID;


ALTER TABLE IF EXISTS public.order_items
    ADD FOREIGN KEY (cupcake_top_id)
    REFERENCES public.cupcake_tops (cupcake_top_id) MATCH SIMPLE
    ON
UPDATE NO ACTION
ON
DELETE
NO ACTION
    NOT VALID;


INSERT INTO public.users (user_email, user_password, user_role, user_balance)
VALUES ('jon@cphbusiness.dk', '1234', 'customer', 500),
       ('signe@cphbusiness.dk', '1234', 'admin', 0),
       ('jesper@cphbusiness.dk', '1234', 'customer', 500);


INSERT INTO public.cupcake_bottoms (cupcake_bottom_name, cupcake_bottom_price)
VALUES ('Chocolate', 5),
       ('Vanilla', 5),
       ('Nutmeg', 5),
       ('Pistachio', 6),
       ('Almond', 7);


INSERT INTO public.cupcake_tops (cupcake_top_name, cupcake_top_price)
VALUES ('Chocolate', 5),
       ('Blueberry', 5),
       ('Raspberry', 5),
       ('Crispy', 6),
       ('Strawberry', 6),
       ('Rum/Raisin', 7),
       ('Orange', 8),
       ('Lemon', 8),
       ('Blue cheese', 9);


INSERT INTO public.orders (user_id, order_status, order_timestamp)
VALUES (1, 'Complete', '2024-03-18 10:34:29'),
       (1, 'In Progress', '2024-03-28 18:47:11'),
       (1, 'In Progress', '2024-04-01 15:52:49'),
       (3, 'Complete', '2024-04-21 12:47:21'),
       (3, 'In Progress', '2024-03-27 21:22:59');


INSERT INTO public.order_items (order_id, cupcake_bottom_id, cupcake_top_id, order_item_quantity)
VALUES (1, 1, 7, 6),
       (1, 4, 5, 2),

       (2, 2, 7, 4),
       (2, 1, 7, 8),
       (2, 3, 7, 2),

       (3, 2, 2, 3),

       (4, 1, 6, 2),

       (5, 4, 3, 4),
       (5, 4, 9, 1);


END;