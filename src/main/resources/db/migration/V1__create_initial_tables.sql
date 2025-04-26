CREATE TABLE IF NOT EXISTS `tbl_user` (
    `user_id` INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    `name` VARCHAR(255) NOT NULL,
    `email` VARCHAR(255) NOT NULL,
    `cpf` CHAR(11) NOT NULL,
    `phone_number` VARCHAR(255) NOT NULL,
    `dt_birth` DATE NOT NULL,
    `role` VARCHAR(255) NOT NULL,
    `dt_created` DATETIME NOT NULL,
    `dt_updated` DATETIME NOT NULL
);

CREATE TABLE IF NOT EXISTS `tbl_user_credentials` (
    `user_credentials_id` INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    `username` CHAR(255) NOT NULL,
    `name` CHAR(255) NOT NULL,
    `password` CHAR(255) NOT NULL,
    `role` CHAR(255) NOT NULL
);

CREATE TABLE IF NOT EXISTS `tbl_order` (
    `order_id` INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    `dt_order` DATETIME NOT NULL,
    `status` VARCHAR(255) NULL,
    `total_price` DECIMAL(15,2),
    `dt_updated` DATETIME NOT NULL,
    `user_id` INT NOT NULL,
    `payment_id` INT NULL
);

CREATE TABLE IF NOT EXISTS `tbl_order_item` (
    `order_item_id` INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    `quantity` INT NOT NULL,
    `price_at_purchase` DECIMAL(15,2) NOT NULL,
    `order_id` INT NOT NULL,
    `product_id` INT NOT NULL
);

CREATE TABLE IF NOT EXISTS `tbl_product` (
    `product_id` INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    `name` VARCHAR(255) NOT NULL,
    `description` VARCHAR(255) NOT NULL,
    `price` DECIMAL(15,2) NOT NULL,
    `stock_quantity` INT NOT NULL,
    `img_url` VARCHAR(255) NULL,
    `dt_created` DATETIME NOT NULL,
    `dt_updated` DATETIME NOT NULL,
    `category_id` INT NOT NULL
);

CREATE TABLE IF NOT EXISTS `tbl_category` (
    `category_id` INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    `name` VARCHAR(255) NOT NULL,
    `description` VARCHAR(255) NULL
);

CREATE TABLE IF NOT EXISTS `tbl_payment` (
    `payment_id` INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    `dt_payment` DATETIME NOT NULL,
    `payment_method` VARCHAR(90) NOT NULL,
    `amount` DECIMAL(15,2) NOT NULL,
    `order_id` INT NOT NULL
);

CREATE TABLE IF NOT EXISTS `tbl_order_history` (
    `history_id` INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    `order_id` INT NOT NULL,
    `status` VARCHAR(255) NOT NULL,
    `dt_event` DATETIME NOT NULL,
    `dt_created_order` DATETIME NOT NULL,
    `dt_updated` DATETIME NOT NULL,
    `user_id` INT NULL,
    `note` TEXT NULL
);

-- UNIQUE CONSTRAINTS
ALTER TABLE `tbl_user` ADD UNIQUE `users_email_unique`(`email`);
ALTER TABLE `tbl_user` ADD UNIQUE `users_cpf_unique`(`cpf`);
ALTER TABLE `tbl_user` ADD UNIQUE `users_phone_unique`(`phone_number`);

ALTER TABLE `tbl_product` ADD CONSTRAINT `unique_product_category` UNIQUE (`name`, `category_id`);

-- FOREIGN KEYS
ALTER TABLE `tbl_order_history`
    ADD CONSTRAINT `fk_order_history_order` FOREIGN KEY (`order_id`) REFERENCES `tbl_order`(`order_id`) ON DELETE CASCADE;

ALTER TABLE `tbl_order_history`
    ADD CONSTRAINT `fk_order_history_user` FOREIGN KEY (`user_id`) REFERENCES `tbl_user`(`user_id`);

ALTER TABLE `tbl_order`
    ADD CONSTRAINT `fk_orders_user` FOREIGN KEY (`user_id`) REFERENCES `tbl_user`(`user_id`);

ALTER TABLE `tbl_order`
    ADD CONSTRAINT `fk_orders_payment` FOREIGN KEY (`payment_id`) REFERENCES `tbl_payment`(`payment_id`);

ALTER TABLE `tbl_order_item`
    ADD CONSTRAINT `fk_order_item_order` FOREIGN KEY (`order_id`) REFERENCES `tbl_order`(`order_id`);

ALTER TABLE `tbl_order_item`
    ADD CONSTRAINT `fk_order_item_product` FOREIGN KEY (`product_id`) REFERENCES `tbl_product`(`product_id`);

ALTER TABLE `tbl_product`
    ADD CONSTRAINT `fk_products_category` FOREIGN KEY (`category_id`) REFERENCES `tbl_category`(`category_id`);

ALTER TABLE `tbl_payment`
    ADD CONSTRAINT `fk_payment_order` FOREIGN KEY (`order_id`) REFERENCES tbl_order(`order_id`) ON DELETE CASCADE;


INSERT INTO `tbl_category` (`name`, `description`) VALUES
('Brincos', 'Peças delicadas para compor o visual'),
('Colares', 'Colares sofisticados e modernos'),
('Anéis', 'Anéis de prata com designs exclusivos'),
('Braceletes', 'Braceletes elegantes para todas as ocasiões');


-- Inserts para tbl_user
INSERT INTO `tbl_user` (`name`, `email`, `cpf`, `phone_number`, `dt_birth`, `role`, `dt_created`, `dt_updated`) VALUES ('Sem Nada e Maraisa', '_@mailna.co', '78563942000', '21 2864-6305', '2004-06-21', 'USER', '2025-04-26 08:47:04', '2025-04-26 08:47:04');
INSERT INTO `tbl_user` (`name`, `email`, `cpf`, `phone_number`, `dt_birth`, `role`, `dt_created`, `dt_updated`) VALUES ('Maysa Aparecida', 'maria-flor29@example.net', '67015294885', '31 3528-8232', '1983-10-19', 'ADMIN', '2025-04-26 08:47:04', '2025-04-26 08:47:04');
INSERT INTO `tbl_user` (`name`, `email`, `cpf`, `phone_number`, `dt_birth`, `role`, `dt_created`, `dt_updated`) VALUES ('Oliver Nascimento', 'davi-miguelnascimento@example.net', '87941053675', '+55 31 2278-0722', '1971-12-15', 'ADMIN', '2025-04-26 08:47:04', '2025-04-26 08:47:04');
INSERT INTO `tbl_user` (`name`, `email`, `cpf`, `phone_number`, `dt_birth`, `role`, `dt_created`, `dt_updated`) VALUES ('Valentina Leão', 'silvamanuela@example.com', '82470369169', '+55 71 5130-5449', '1996-08-16', 'USER', '2025-04-26 08:47:04', '2025-04-26 08:47:04');
INSERT INTO `tbl_user` (`name`, `email`, `cpf`, `phone_number`, `dt_birth`, `role`, `dt_created`, `dt_updated`) VALUES ('Luara Monteiro', 'pedro-henriqueda-rosa@example.org', '28346901704', '+55 31 5515 2234', '1981-06-03', 'ADMIN', '2025-04-26 08:47:04', '2025-04-26 08:47:04');
INSERT INTO `tbl_user` (`name`, `email`, `cpf`, `phone_number`, `dt_birth`, `role`, `dt_created`, `dt_updated`) VALUES ('Yago da Rosa', 'ryanfreitas@example.org', '23657914099', '31 6620-7797', '1990-03-17', 'ADMIN', '2025-04-26 08:47:04', '2025-04-26 08:47:04');
INSERT INTO `tbl_user` (`name`, `email`, `cpf`, `phone_number`, `dt_birth`, `role`, `dt_created`, `dt_updated`) VALUES ('Paulo Nascimento', 'ulopes@example.com', '30187426996', '+55 (061) 2374 8920', '1967-08-02', 'USER', '2025-04-26 08:47:04', '2025-04-26 08:47:04');
INSERT INTO `tbl_user` (`name`, `email`, `cpf`, `phone_number`, `dt_birth`, `role`, `dt_created`, `dt_updated`) VALUES ('Rafaela Gonçalves', 'lucca44@example.org', '78035619268', '+55 (031) 9392-9711', '1991-10-12', 'ADMIN', '2025-04-26 08:47:04', '2025-04-26 08:47:04');
INSERT INTO `tbl_user` (`name`, `email`, `cpf`, `phone_number`, `dt_birth`, `role`, `dt_created`, `dt_updated`) VALUES ('Ravy Borges', 'ucarvalho@example.org', '46912703887', '+55 31 1549 9220', '1984-03-07', 'USER', '2025-04-26 08:47:04', '2025-04-26 08:47:04');
INSERT INTO `tbl_user` (`name`, `email`, `cpf`, `phone_number`, `dt_birth`, `role`, `dt_created`, `dt_updated`) VALUES ('Eloá Cassiano', 'lopesmaria-liz@example.net', '02783496500', '31 1193-4411', '1967-11-05', 'ADMIN', '2025-04-26 08:47:04', '2025-04-26 08:47:04');

-- Inserts para tbl_product
INSERT INTO `tbl_product` (`name`, `description`, `price`, `img_url`, `dt_created`, `dt_updated`, `category_id`, `stock_quantity`) VALUES ('Cupiditate', 'Eaque occaecati voluptatum aliquid.', 370.07, 'https://dummyimage.com/822x679', '2025-04-26 08:47:04', '2025-04-26 08:47:04', 4, 330);
INSERT INTO `tbl_product` (`name`, `description`, `price`, `img_url`, `dt_created`, `dt_updated`, `category_id`, `stock_quantity`) VALUES ('Praesentium', 'Aliquam consequuntur aliquid a.', 280.79, 'https://dummyimage.com/476x647', '2025-04-26 08:47:04', '2025-04-26 08:47:04', 2, 614);
INSERT INTO `tbl_product` (`name`, `description`, `price`, `img_url`, `dt_created`, `dt_updated`, `category_id`, `stock_quantity`) VALUES ('Eos', 'Alias odit delectus sapiente quas temporibus.', 126.16, 'https://dummyimage.com/771x714', '2025-04-26 08:47:04', '2025-04-26 08:47:04', 1, 917);
INSERT INTO `tbl_product` (`name`, `description`, `price`, `img_url`, `dt_created`, `dt_updated`, `category_id`, `stock_quantity`) VALUES ('Repellendus', 'Mollitia sint maxime quod.', 88.1, 'https://dummyimage.com/945x511', '2025-04-26 08:47:04', '2025-04-26 08:47:04', 3, 765);
INSERT INTO `tbl_product` (`name`, `description`, `price`, `img_url`, `dt_created`, `dt_updated`, `category_id`, `stock_quantity`) VALUES ('Nobis', 'Ea repudiandae repellat rerum eos aspernatur.', 271.72, 'https://placekitten.com/120/427', '2025-04-26 08:47:04', '2025-04-26 08:47:04', 4, 329);
INSERT INTO `tbl_product` (`name`, `description`, `price`, `img_url`, `dt_created`, `dt_updated`, `category_id`, `stock_quantity`) VALUES ('Voluptate', 'Sint ad in ducimus modi tempora fugiat.', 114.28, 'https://placekitten.com/302/94', '2025-04-26 08:47:04', '2025-04-26 08:47:04', 3, 406);
INSERT INTO `tbl_product` (`name`, `description`, `price`, `img_url`, `dt_created`, `dt_updated`, `category_id`, `stock_quantity`) VALUES ('Distinctio', 'Iste maiores deserunt velit nihil.', 81.57, 'https://placekitten.com/904/474', '2025-04-26 08:47:04', '2025-04-26 08:47:04', 4, 804);
INSERT INTO `tbl_product` (`name`, `description`, `price`, `img_url`, `dt_created`, `dt_updated`, `category_id`, `stock_quantity`) VALUES ('Alias', 'Iusto sunt consequuntur voluptatum voluptatibus at.', 372.8, 'https://dummyimage.com/602x780', '2025-04-26 08:47:04', '2025-04-26 08:47:04', 1, 185);
INSERT INTO `tbl_product` (`name`, `description`, `price`, `img_url`, `dt_created`, `dt_updated`, `category_id`, `stock_quantity`) VALUES ('Saepe', 'Quo pariatur iure doloribus.', 495.55, 'https://picsum.photos/332/214', '2025-04-26 08:47:04', '2025-04-26 08:47:04', 3, 291);
INSERT INTO `tbl_product` (`name`, `description`, `price`, `img_url`, `dt_created`, `dt_updated`, `category_id`, `stock_quantity`) VALUES ('Ex', 'Recusandae odit quos nostrum earum architecto.', 128.94, 'https://placekitten.com/719/904', '2025-04-26 08:47:04', '2025-04-26 08:47:04', 3, 244);
INSERT INTO `tbl_product` (`name`, `description`, `price`, `img_url`, `dt_created`, `dt_updated`, `category_id`, `stock_quantity`) VALUES ('Consequatur', 'Laboriosam repudiandae minima harum dicta sequi.', 179.18, 'https://placekitten.com/674/173', '2025-04-26 08:47:04', '2025-04-26 08:47:04', 2, 610);
INSERT INTO `tbl_product` (`name`, `description`, `price`, `img_url`, `dt_created`, `dt_updated`, `category_id`, `stock_quantity`) VALUES ('Porro', 'Ut dicta porro nulla vitae at ipsam nostrum.', 326.46, 'https://dummyimage.com/956x838', '2025-04-26 08:47:04', '2025-04-26 08:47:04', 2, 89);
INSERT INTO `tbl_product` (`name`, `description`, `price`, `img_url`, `dt_created`, `dt_updated`, `category_id`, `stock_quantity`) VALUES ('Aperiam', 'Molestias quas officia vero quam nulla.', 426.02, 'https://picsum.photos/70/891', '2025-04-26 08:47:04', '2025-04-26 08:47:04', 2, 293);
INSERT INTO `tbl_product` (`name`, `description`, `price`, `img_url`, `dt_created`, `dt_updated`, `category_id`, `stock_quantity`) VALUES ('Dolores', 'Adipisci cumque officia id.', 394.51, 'https://placekitten.com/290/934', '2025-04-26 08:47:04', '2025-04-26 08:47:04', 1, 342);
INSERT INTO `tbl_product` (`name`, `description`, `price`, `img_url`, `dt_created`, `dt_updated`, `category_id`, `stock_quantity`) VALUES ('Facere', 'Ipsa neque dolore quis voluptatum itaque molestiae iusto.', 102.26, 'https://picsum.photos/469/328', '2025-04-26 08:47:04', '2025-04-26 08:47:04', 3, 640);
INSERT INTO `tbl_product` (`name`, `description`, `price`, `img_url`, `dt_created`, `dt_updated`, `category_id`, `stock_quantity`) VALUES ('Deserunt', 'Asperiores distinctio nesciunt corporis natus labore.', 428.57, 'https://dummyimage.com/172x597', '2025-04-26 08:47:04', '2025-04-26 08:47:04', 1, 968);
INSERT INTO `tbl_product` (`name`, `description`, `price`, `img_url`, `dt_created`, `dt_updated`, `category_id`, `stock_quantity`) VALUES ('Iure', 'Ullam cupiditate deserunt libero cumque omnis officia.', 399.3, 'https://dummyimage.com/638x360', '2025-04-26 08:47:04', '2025-04-26 08:47:04', 1, 510);
INSERT INTO `tbl_product` (`name`, `description`, `price`, `img_url`, `dt_created`, `dt_updated`, `category_id`, `stock_quantity`) VALUES ('Fugiat', 'Quod sunt illo perferendis quo minus recusandae.', 206.44, 'https://placekitten.com/125/935', '2025-04-26 08:47:04', '2025-04-26 08:47:04', 4, 140);
INSERT INTO `tbl_product` (`name`, `description`, `price`, `img_url`, `dt_created`, `dt_updated`, `category_id`, `stock_quantity`) VALUES ('Non', 'Iure sit asperiores ratione voluptate dicta nostrum minus.', 378.93, 'https://dummyimage.com/854x473', '2025-04-26 08:47:04', '2025-04-26 08:47:04', 3, 891);
INSERT INTO `tbl_product` (`name`, `description`, `price`, `img_url`, `dt_created`, `dt_updated`, `category_id`, `stock_quantity`) VALUES ('Suscipit', 'Culpa officia sint ratione impedit asperiores.', 131.59, 'https://dummyimage.com/688x1018', '2025-04-26 08:47:04', '2025-04-26 08:47:04', 1, 253);
