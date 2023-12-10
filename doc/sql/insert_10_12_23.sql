BEGIN;

ALTER SEQUENCE customer_id_seq RESTART WITH 1;
ALTER SEQUENCE items_for_orders_id_seq RESTART WITH 1;
ALTER SEQUENCE item_id_seq RESTART WITH 1;
ALTER SEQUENCE order_id_seq RESTART WITH 1;
ALTER SEQUENCE salesperson_id_seq RESTART WITH 1;

INSERT INTO customer(email,firstname,lastname,phonenumber,password) VALUES
    ('customer@email.dk', 'customer', 'customer', 00000000, 'customer');

INSERT INTO salesperson(email,firstname,lastname,phonenumber,password) VALUES
    ('salesperson@email.dk', 'salesperson', 'salesperson', 00000000, 'salesperson');

INSERT INTO item(price_pr_unit,length,unit,description) VALUES
                                                            (100, 10, 'Pakke', 'nail'),
                                                            (100, 5, 'Stk', 'plank'),
                                                            (100, 100, 'Stk', 'Stolpe'),
                                                            (100, 360, 'Stk', '25x200 mm. trykimp. Bræt| understernbrædder til for & bag ende'),
                                                            (100, 540, 'Stk', '25x200 mm. trykimp. Bræt| understernbrædder til siderne'),
                                                            (75, 300, 'Stk', '97x97 mm. trykimp. stolpe');

INSERT INTO public.order(status, date, customer_id, salesperson_id, total_price, carport_width, carport_length, shed_width, shed_length) VALUES
    ('READY_FOR_REVIEW','2023-12-20',1,1,11500, 10, 10, -1, -1);

INSERT INTO items_for_orders(order_id,item_id,quantity) VALUES
                                                            (1, 1, 100),
                                                            (1, 2, 4),
                                                            (1, 3, 10),
                                                            (1, 4, 4);


END;