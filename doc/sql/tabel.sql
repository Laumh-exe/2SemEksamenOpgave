BEGIN;

DROP TABLE IF EXISTS item_itemlist;
DROP TABLE IF EXISTS items_orders;
DROP TABLE IF EXISTS itemlist;
DROP TABLE IF EXISTS public.order;
DROP TABLE IF EXISTS salesperson;
DROP TABLE IF EXISTS customer;
DROP TABLE IF EXISTS item;

CREATE TABLE IF NOT EXISTS customer
(
    id serial NOT NULL,
    email character varying(50),
    firstName character varying(50),
    lastName character varying(50),
    phoneNumber integer,
    password character varying(50),
    PRIMARY KEY (id)
    );

CREATE TABLE IF NOT EXISTS items_orders
(
    order_id integer NOT NULL,
    item_id integer NOT NULL,
    quantity integer NOT NULL
);

CREATE TABLE IF NOT EXISTS item
(
    id serial NOT NULL,
    unit character varying (70) NOT NULL,
    carport_part character varying(10),
    description character varying(200) NOT NULL,
    length double precision NOT NULL,
    price_pr_unit double precision NOT NULL,
    PRIMARY KEY (id)
    );

CREATE TABLE IF NOT EXISTS public.order
(
    id serial NOT NULL,
    status character varying(30) NOT NULL,
    date date NOT NULL,
    customer_id integer NOT NULL,
    total_price double precision NOT NULL,
    carport_width double precision NOT NULL,
    carport_length double precision NOT NULL,
    shed_width double precision,
    shed_length double precision,
    salesperson_id integer,
    PRIMARY KEY (id)
    );


CREATE TABLE IF NOT EXISTS salesperson
(
    id serial NOT NULL,
    firstname character varying(50) NOT NULL,
    lastname character varying(50) NOT NULL,
    email character varying(50) NOT NULL,
    phonenumber integer NOT NULL,
    password character varying(50) NOT NULL,
    PRIMARY KEY (id)
    );

ALTER TABLE IF EXISTS public.order
    ADD FOREIGN KEY (salesperson_id)
    REFERENCES salesperson (id) MATCH SIMPLE
    ON UPDATE NO ACTION
       ON DELETE NO ACTION
    NOT VALID;


ALTER TABLE IF EXISTS public.order
    ADD FOREIGN KEY (customer_id)
    REFERENCES customer (id) MATCH SIMPLE
    ON UPDATE NO ACTION
       ON DELETE NO ACTION
    NOT VALID;


ALTER TABLE IF EXISTS public.items_orders
    ADD FOREIGN KEY (order_id)
    REFERENCES public.order (id) MATCH SIMPLE
    ON UPDATE NO ACTION
       ON DELETE NO ACTION
    NOT VALID;

ALTER TABLE IF EXISTS public.items_orders
    ADD FOREIGN KEY (item_id)
    REFERENCES item (id) MATCH SIMPLE
    ON UPDATE NO ACTION
       ON DELETE NO ACTION
    NOT VALID;

END;