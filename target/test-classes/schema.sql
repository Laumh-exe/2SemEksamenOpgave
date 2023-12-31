BEGIN;

DROP TABLE IF EXISTS item_itemlist;
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

CREATE TABLE IF NOT EXISTS itemlist
(
    id serial NOT NULL,
    order_id integer NOT NULL,
    description character varying(70) NOT NULL,
    quantity integer NOT NULL,
    PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS item
(
    id serial NOT NULL,
    price_pr_unit double precision NOT NULL,
    length double precision NOT NULL,
    unit character varying(50) NOT NULL,
    description character varying(60) NOT NULL,
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
    salesperson_id integer NOT NULL,
    PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS item_itemlist
(
    item_id integer NOT NULL,
    itemlist_id integer NOT NULL
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

ALTER TABLE IF EXISTS itemlist
    ADD CONSTRAINT itemlist_order_id_fkey FOREIGN KEY (order_id)
    REFERENCES public.order (id) MATCH SIMPLE
    ON UPDATE NO ACTION
    ON DELETE NO ACTION
    NOT VALID;


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


ALTER TABLE IF EXISTS item_itemlist
    ADD FOREIGN KEY (item_id)
    REFERENCES item (id) MATCH SIMPLE
    ON UPDATE NO ACTION
    ON DELETE NO ACTION
    NOT VALID;


ALTER TABLE IF EXISTS item_itemlist
    ADD FOREIGN KEY (itemlist_id)
    REFERENCES itemlist (id) MATCH SIMPLE
    ON UPDATE NO ACTION
    ON DELETE NO ACTION
    NOT VALID;

END;