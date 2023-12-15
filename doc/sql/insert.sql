BEGIN;

ALTER SEQUENCE customer_id_seq RESTART WITH 1;
ALTER SEQUENCE item_id_seq RESTART WITH 1;
ALTER SEQUENCE order_id_seq RESTART WITH 1;
ALTER SEQUENCE salesperson_id_seq RESTART WITH 1;

INSERT INTO customer(email, firstname, lastname, phonenumber, password)
VALUES ('customer@email.dk', 'customer', 'customer', 00000000, 'customer');

INSERT INTO salesperson(email, firstname, lastname, phonenumber, password)
VALUES ('salesperson@email.dk', 'salesperson', 'salesperson', 00000000, 'salesperson');

INSERT INTO item(unit, description, length, price_pr_unit, function)
VALUES
       --SPÆR - BESKRIVELSE BLIVER TIL FUNCTION
       ('Stk', '45x195 mm. spærtræ', 240, 100,'spær'),
       ('Stk', '45x195 mm. spærtræ', 300, 120,'spær'),
       ('Stk', '45x195 mm. spærtræ', 360, 140,'spær'),
       ('Stk', '45x195 mm. spærtræ', 420, 160,'spær'),
       ('Stk', '45x195 mm. spærtræ', 480, 180,'spær'),
       ('Stk', '45x195 mm. spærtræ', 540, 200,'spær'),
       ('Stk', '45x195 mm. spærtræ', 600, 220,'spær'),
       ('Stk', '45x195 mm. spærtræ', 660, 240,'spær'),
       ('Stk', '45x195 mm. spærtræ', 720, 260,'spær'),

        --REM
       ('Stk', '45x195 mm. spærtræ - til rem',240, 100,'rem'),
       ('Stk', '45x195 mm. spærtræ - til rem',300, 120,'rem'),
       ('Stk', '45x195 mm. spærtræ - til rem',360, 140,'rem'),
       ('Stk', '45x195 mm. spærtræ - til rem',420, 160,'rem'),
       ('Stk', '45x195 mm. spærtræ - til rem',480, 180,'rem'),
       ('Stk', '45x195 mm. spærtræ - til rem',540, 200,'rem'),
       ('Stk', '45x195 mm. spærtræ - til rem',600, 220,'rem'),
       ('Stk', '45x195 mm. spærtræ - til rem',660, 240,'rem'),
       ('Stk', '45x195 mm. spærtræ - til rem',720, 260,'rem'),

        --Stolper - kun en kan blive brugt i vores system(ingen mulighed for valg af højde)
       ('Stk', '97x97 mm. trykimp. Stolpe', 300, 340,'stolpe');

INSERT INTO public.order(status, date, customer_id, salesperson_id, total_price, carport_width, carport_length,
                         shed_width, shed_length)
--CARPORT KAN IKKE VÆRE BREDDERE END LÆNGSTE STYKKE SPÆR
VALUES ('READY_FOR_REVIEW', '2023-12-20', 1, 1, 11500, 6, 7.8, -1, -1);

INSERT INTO items_orders(order_id, item_id, quantity)
VALUES (1, 1, 100),
       (1, 2, 4),
       (1, 3, 10),
       (1, 4, 4);


END;