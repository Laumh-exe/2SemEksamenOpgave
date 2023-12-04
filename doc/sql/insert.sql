BEGIN;

ALTER SEQUENCE customer_id_seq RESTART WITH 1;
ALTER SEQUENCE itemlist_id_seq RESTART WITH 1;
ALTER SEQUENCE item_id_seq RESTART WITH 1;
ALTER SEQUENCE order_id_seq RESTART WITH 1;
ALTER SEQUENCE salesperson_id_seq RESTART WITH 1;

INSERT INTO customer(email,firstname,lastname,phonenumber,password) VALUES
    ('customer@email.dk', 'customer', 'customer', 00000000, 'customer');
	
INSERT INTO salesperson(email,firstname,lastname,phonenumber,password) VALUES
    ('salesperson@email.dk', 'salesperson', 'salesperson', 00000000, 'salesperson');

INSERT INTO item(prise_pr_unit,length,unit,description) VALUES
    (100, 10, 'cm', 'nail'),
    (100, 5, 'm', 'plank');

INSERT INTO public.order(status, date, customer_id, salesperson_id, total_price, carport_width, carport_length) VALUES
    ('READY_FOR_REVIEW','2023-12-20',1,1,11500, 10, 10);

INSERT INTO itemlist(order_id,description,quantity) VALUES
    (1, 'box of nails', 100),
    (1, 'some planks', 10),
    (1, 'more nails',5);

INSERT INTO item_itemlist(item_id, itemlist_id) VALUES
    (1,1),
    (2,2),
    (1,3);

END;