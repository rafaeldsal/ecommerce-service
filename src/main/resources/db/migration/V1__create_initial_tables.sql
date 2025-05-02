CREATE TABLE IF NOT EXISTS `tbl_user` (
    `user_id` VARCHAR(100) NOT NULL PRIMARY KEY,
    `name` VARCHAR(255) NOT NULL,
    `email` VARCHAR(255) NOT NULL,
    `cpf` CHAR(11) NOT NULL,
    `phone_number` VARCHAR(255) NOT NULL,
    `dt_birth` DATE NOT NULL,
    `role` VARCHAR(255) NOT NULL,
    `dt_created` DATETIME NOT NULL,
    `dt_updated` DATETIME NOT NULL
);

CREATE TABLE IF NOT EXISTS `tbl_address` (
    `address_id` VARCHAR(255) NOT NULL PRIMARY KEY,
    `street` VARCHAR(255),
    `number` VARCHAR(100),
    `complement` VARCHAR(255),
    `neighborhood` VARCHAR(255),
    `city` VARCHAR(255),
    `state` VARCHAR(255),
    `postal_code` VARCHAR(20),
    `user_id` VARCHAR(255) NOT NULL UNIQUE
);

CREATE TABLE IF NOT EXISTS `tbl_user_credentials` (
    `user_credentials_id` VARCHAR(100) NOT NULL PRIMARY KEY,
    `username` CHAR(255) NOT NULL,
    `name` CHAR(255) NOT NULL,
    `password` CHAR(255) NOT NULL,
    `role` CHAR(255) NOT NULL
);

CREATE TABLE IF NOT EXISTS `tbl_user_payment_info` (
    user_payment_info_id VARCHAR(255) NOT NULL PRIMARY KEY,
    card_number VARCHAR(255) NOT NULL UNIQUE,
    card_expiration_year VARCHAR(50) NOT NULL,
    card_expiration_month VARCHAR(50) NOT NULL,
    card_security_code VARCHAR(50) NOT NULL,
    price DECIMAL(19,2) NOT NULL,
    installments INT NOT NULL,
    dt_payment DATETIME NOT NULL,
    user_id VARCHAR(255),
    save_payment_method BOOLEAN NOT NULL
);

CREATE TABLE IF NOT EXISTS `tbl_order` (
    `order_id` VARCHAR(100) NOT NULL PRIMARY KEY,
    `dt_order` DATETIME NOT NULL,
    `status` VARCHAR(255) NULL,
    `total_price` DECIMAL(15,2),
    `dt_updated` DATETIME NOT NULL,
    `user_id` VARCHAR(100) NOT NULL,
    `payment_id` VARCHAR(100) NULL
);

CREATE TABLE IF NOT EXISTS `tbl_order_item` (
    `order_item_id` VARCHAR(100) NOT NULL PRIMARY KEY,
    `quantity` INT NOT NULL,
    `price_at_purchase` DECIMAL(15,2) NOT NULL,
    `order_id` VARCHAR(100) NOT NULL,
    `product_id` VARCHAR(100) NOT NULL
);

CREATE TABLE IF NOT EXISTS `tbl_product` (
    `product_id` VARCHAR(100) NOT NULL PRIMARY KEY,
    `name` VARCHAR(255) NOT NULL,
    `description` VARCHAR(255) NOT NULL,
    `price` DECIMAL(15,2) NOT NULL,
    `stock_quantity` INT NOT NULL,
    `img_url` VARCHAR(255) NULL,
    `dt_created` DATETIME NOT NULL,
    `dt_updated` DATETIME NOT NULL,
    `category_id` VARCHAR(100) NOT NULL
);

CREATE TABLE IF NOT EXISTS `tbl_category` (
    `category_id` VARCHAR(100) NOT NULL PRIMARY KEY,
    `name` VARCHAR(255) NOT NULL,
    `description` VARCHAR(255) NULL
);

CREATE TABLE IF NOT EXISTS `tbl_payment` (
    `payment_id` VARCHAR(100) NOT NULL PRIMARY KEY,
    `dt_payment` DATETIME NOT NULL,
    `payment_method` VARCHAR(90) NOT NULL,
    `save_payment_method` BOOLEAN,
    `currency` VARCHAR(20) NOT NULL,
    `amount` DECIMAL(15,2) NOT NULL,
    `order_id` VARCHAR(100) NOT NULL
);

CREATE TABLE IF NOT EXISTS `tbl_order_history` (
    `history_id` VARCHAR(100) NOT NULL PRIMARY KEY,
    `order_id` VARCHAR(100) NOT NULL,
    `status` VARCHAR(255) NOT NULL,
    `dt_event` DATETIME NOT NULL,
    `dt_created_order` DATETIME NOT NULL,
    `dt_updated` DATETIME NOT NULL,
    `user_id` VARCHAR(100) NULL,
    `note` TEXT NULL
);

-- UNIQUE CONSTRAINTS
ALTER TABLE `tbl_user` ADD UNIQUE `users_email_unique`(`email`);
ALTER TABLE `tbl_user` ADD UNIQUE `users_cpf_unique`(`cpf`);
ALTER TABLE `tbl_user` ADD UNIQUE `users_phone_unique`(`phone_number`);

ALTER TABLE `tbl_product` ADD CONSTRAINT `unique_product_category` UNIQUE (`name`, `category_id`);

-- FOREIGN KEYS
ALTER TABLE `tbl_user_payment_info`
    ADD CONSTRAINT `fk_user_payment_info_user` FOREIGN KEY (`user_id`) REFERENCES `tbl_user`(`user_id`);

ALTER TABLE `tbl_order_history`
    ADD CONSTRAINT `fk_order_history_order` FOREIGN KEY (`order_id`) REFERENCES `tbl_order`(`order_id`) ON DELETE CASCADE;

ALTER TABLE `tbl_address`
    ADD CONSTRAINT `fk_address_user` FOREIGN KEY (`user_id`) REFERENCES `tbl_user`(`user_id`) ON DELETE CASCADE;

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


-- Inserts para tbl_category
INSERT INTO `tbl_category` (`category_id`, `name`, `description`) VALUES
('1', 'Brincos', 'Peças delicadas para compor o visual'),
('2', 'Colares', 'Colares sofisticados e modernos'),
('3', 'Anéis', 'Anéis de prata com designs exclusivos'),
('4', 'Braceletes', 'Braceletes elegantes para todas as ocasiões');

---- Inserts para tbl_user
--INSERT INTO `tbl_user` (`user_id`, `name`, `email`, `cpf`, `phone_number`, `dt_birth`, `role`, `dt_created`, `dt_updated`) VALUES ('user-026ee4e9-515c-4938-9ba6-c421b3adfb0b', 'Anthony Gabriel Ramos', 'bcasa-grande@example.org', '30172489504', '+55 (031) 2025 9136', '1975-03-31', 'USER', '2025-04-27 09:41:18', '2025-04-27 09:41:18');
--INSERT INTO `tbl_user` (`user_id`, `name`, `email`, `cpf`, `phone_number`, `dt_birth`, `role`, `dt_created`, `dt_updated`) VALUES ('user-cc14a7c7-7524-451f-87e9-967bb3498ba1', 'Juliana Vargas', 'fernandesmariana@example.net', '69401382778', '41 4117 5200', '2001-03-29', 'USER', '2025-04-27 09:41:18', '2025-04-27 09:41:18');
--INSERT INTO `tbl_user` (`user_id`, `name`, `email`, `cpf`, `phone_number`, `dt_birth`, `role`, `dt_created`, `dt_updated`) VALUES ('user-23d5d7b0-c36f-404a-805e-4039e4294a86', 'Léo Abreu', 'novaisluiz-henrique@example.net', '50829143750', '31 7095-4259', '1980-12-05', 'ADMIN', '2025-04-27 09:41:18', '2025-04-27 09:41:18');
--INSERT INTO `tbl_user` (`user_id`, `name`, `email`, `cpf`, `phone_number`, `dt_birth`, `role`, `dt_created`, `dt_updated`) VALUES ('user-5eec6029-060f-4837-a8e8-016c7901e0eb', 'João Pedro Nunes', 'da-luzmarcos-vinicius@example.net', '73914628537', '71 8163-6396', '1975-12-03', 'ADMIN', '2025-04-27 09:41:18', '2025-04-27 09:41:18');
--INSERT INTO `tbl_user` (`user_id`, `name`, `email`, `cpf`, `phone_number`, `dt_birth`, `role`, `dt_created`, `dt_updated`) VALUES ('user-746f98be-68d5-41d8-85f6-57a226bf6525', 'Cecilia Caldeira', 'ottomontenegro@example.net', '14397506884', '+55 (081) 3552-8401', '2003-06-20', 'USER', '2025-04-27 09:41:18', '2025-04-27 09:41:18');
--INSERT INTO `tbl_user` (`user_id`, `name`, `email`, `cpf`, `phone_number`, `dt_birth`, `role`, `dt_created`, `dt_updated`) VALUES ('user-a620fcbc-fcf1-46cf-91ed-a69d8b1a7cfa', 'Eloah Vargas', 'ovargas@example.com', '53481209797', '+55 21 7851-0609', '1987-05-10', 'ADMIN', '2025-04-27 09:41:18', '2025-04-27 09:41:18');
--INSERT INTO `tbl_user` (`user_id`, `name`, `email`, `cpf`, `phone_number`, `dt_birth`, `role`, `dt_created`, `dt_updated`) VALUES ('user-d6c86695-980e-4fcd-9285-a877975e0467', 'Laís Cavalcanti', 'davi-miguelnovaes@example.com', '56329104743', '0300 446 2146', '2005-01-25', 'USER', '2025-04-27 09:41:18', '2025-04-27 09:41:18');
--INSERT INTO `tbl_user` (`user_id`, `name`, `email`, `cpf`, `phone_number`, `dt_birth`, `role`, `dt_created`, `dt_updated`) VALUES ('user-0e970758-97d2-45ec-a162-c1114feab5ec', 'Eloá Albuquerque', 'manuelamendonca@example.net', '87214065967', '(071) 9441-4689', '1971-11-06', 'USER', '2025-04-27 09:41:18', '2025-04-27 09:41:18');
--INSERT INTO `tbl_user` (`user_id`, `name`, `email`, `cpf`, `phone_number`, `dt_birth`, `role`, `dt_created`, `dt_updated`) VALUES ('user-51e8dc9c-163a-40e8-8e33-06b8c5917794', 'João Gabriel Cunha', 'luiz-felipe66@example.net', '78621530490', '81 5118 9099', '1996-06-23', 'USER', '2025-04-27 09:41:18', '2025-04-27 09:41:18');
--INSERT INTO `tbl_user` (`user_id`, `name`, `email`, `cpf`, `phone_number`, `dt_birth`, `role`, `dt_created`, `dt_updated`) VALUES ('user-6a05d4ef-32f8-407c-b57b-4cea95125b49', 'Stephany Dias', 'lorenzo24@example.com', '18346972040', '(081) 7299-0080', '2002-06-30', 'ADMIN', '2025-04-27 09:41:18', '2025-04-27 09:41:18');
--
---- Inserts para tbl_product
--INSERT INTO `tbl_product` (`product_id`, `name`, `description`, `price`, `img_url`, `dt_created`, `dt_updated`, `category_id`, `stock_quantity`) VALUES ('prod-524a3f5e-ab27-4f04-8ffe-aeaa1fba08d7', 'Sint', 'Omnis unde accusantium iure veniam praesentium minima.', 51.05, 'https://placekitten.com/996/944', '2025-04-27 09:41:18', '2025-04-27 09:41:18', 2, 302);
--INSERT INTO `tbl_product` (`product_id`, `name`, `description`, `price`, `img_url`, `dt_created`, `dt_updated`, `category_id`, `stock_quantity`) VALUES ('prod-9475610a-312e-4785-b141-b5a38796aef5', 'Aliquam', 'Natus dignissimos asperiores debitis maiores.', 339.9, 'https://placekitten.com/101/969', '2025-04-27 09:41:18', '2025-04-27 09:41:18', 1, 411);
--INSERT INTO `tbl_product` (`product_id`, `name`, `description`, `price`, `img_url`, `dt_created`, `dt_updated`, `category_id`, `stock_quantity`) VALUES ('prod-22ccdf08-f168-478d-947b-7c3c037e681f', 'Perspiciatis', 'Sunt et iure consequuntur.', 374.53, 'https://picsum.photos/35/23', '2025-04-27 09:41:18', '2025-04-27 09:41:18', 1, 48);
--INSERT INTO `tbl_product` (`product_id`, `name`, `description`, `price`, `img_url`, `dt_created`, `dt_updated`, `category_id`, `stock_quantity`) VALUES ('prod-2ecc1926-dfc2-4a78-80c1-be0a6edcbd46', 'Consectetur', 'Omnis accusamus omnis nobis.', 277.76, 'https://picsum.photos/624/804', '2025-04-27 09:41:18', '2025-04-27 09:41:18', 4, 255);
--INSERT INTO `tbl_product` (`product_id`, `name`, `description`, `price`, `img_url`, `dt_created`, `dt_updated`, `category_id`, `stock_quantity`) VALUES ('prod-9a85d7d3-9093-456d-b9a0-54e9fd435802', 'Soluta', 'Pariatur veritatis rem in molestiae voluptatum harum commodi.', 68.64, 'https://placekitten.com/290/810', '2025-04-27 09:41:18', '2025-04-27 09:41:18', 1, 988);
--INSERT INTO `tbl_product` (`product_id`, `name`, `description`, `price`, `img_url`, `dt_created`, `dt_updated`, `category_id`, `stock_quantity`) VALUES ('prod-e221fc09-00d6-42af-93e3-f1c930e74f99', 'Quisquam', 'Voluptatum libero magni totam.', 191.55, 'https://picsum.photos/69/319', '2025-04-27 09:41:18', '2025-04-27 09:41:18', 3, 701);
--INSERT INTO `tbl_product` (`product_id`, `name`, `description`, `price`, `img_url`, `dt_created`, `dt_updated`, `category_id`, `stock_quantity`) VALUES ('prod-e69bd4b0-9dc3-4e78-8469-9ae042b3e07f', 'Ab', 'Ipsum ut ab ipsa beatae velit.', 311.44, 'https://dummyimage.com/715x43', '2025-04-27 09:41:18', '2025-04-27 09:41:18', 2, 919);
--INSERT INTO `tbl_product` (`product_id`, `name`, `description`, `price`, `img_url`, `dt_created`, `dt_updated`, `category_id`, `stock_quantity`) VALUES ('prod-0fad669f-179a-4343-b1d7-782d1972b5e1', 'Dolor', 'Tenetur eaque pariatur aliquid.', 235.06, 'https://dummyimage.com/705x235', '2025-04-27 09:41:18', '2025-04-27 09:41:18', 3, 339);
--INSERT INTO `tbl_product` (`product_id`, `name`, `description`, `price`, `img_url`, `dt_created`, `dt_updated`, `category_id`, `stock_quantity`) VALUES ('prod-5edd4c19-11e4-463f-81bd-31a0abf11b15', 'Dignissimos', 'Distinctio veritatis officiis ab voluptatum.', 425.27, 'https://dummyimage.com/1021x517', '2025-04-27 09:41:18', '2025-04-27 09:41:18', 4, 150);
--INSERT INTO `tbl_product` (`product_id`, `name`, `description`, `price`, `img_url`, `dt_created`, `dt_updated`, `category_id`, `stock_quantity`) VALUES ('prod-e2dbfe93-5382-41f8-b400-5eb35b9f7867', 'A', 'Perferendis quia libero ab quos beatae.', 202.39, 'https://placekitten.com/515/945', '2025-04-27 09:41:18', '2025-04-27 09:41:18', 3, 723);
--INSERT INTO `tbl_product` (`product_id`, `name`, `description`, `price`, `img_url`, `dt_created`, `dt_updated`, `category_id`, `stock_quantity`) VALUES ('prod-210caaa4-c2d5-4bd7-8ec7-0da56715a734', 'Quas', 'Debitis voluptatibus dolores ipsam eos nisi vero.', 475.85, 'https://dummyimage.com/99x8', '2025-04-27 09:41:18', '2025-04-27 09:41:18', 2, 26);
--INSERT INTO `tbl_product` (`product_id`, `name`, `description`, `price`, `img_url`, `dt_created`, `dt_updated`, `category_id`, `stock_quantity`) VALUES ('prod-f0c55a95-6f01-4404-9fd5-f70e16cd329c', 'Qui', 'Voluptate itaque est accusamus aperiam perspiciatis laudantium.', 395.42, 'https://dummyimage.com/946x595', '2025-04-27 09:41:18', '2025-04-27 09:41:18', 4, 216);
--INSERT INTO `tbl_product` (`product_id`, `name`, `description`, `price`, `img_url`, `dt_created`, `dt_updated`, `category_id`, `stock_quantity`) VALUES ('prod-dda2c55c-b43c-4bda-b0fb-5cb793d0c9b7', 'Veniam', 'Id natus inventore.', 123.23, 'https://picsum.photos/946/344', '2025-04-27 09:41:18', '2025-04-27 09:41:18', 2, 948);
--INSERT INTO `tbl_product` (`product_id`, `name`, `description`, `price`, `img_url`, `dt_created`, `dt_updated`, `category_id`, `stock_quantity`) VALUES ('prod-89ce95a9-8314-4496-9b88-313328301f1d', 'Eius', 'Soluta eos aliquam veniam.', 194.56, 'https://placekitten.com/964/629', '2025-04-27 09:41:18', '2025-04-27 09:41:18', 4, 924);
--INSERT INTO `tbl_product` (`product_id`, `name`, `description`, `price`, `img_url`, `dt_created`, `dt_updated`, `category_id`, `stock_quantity`) VALUES ('prod-5d85b42a-ec3a-4c33-8760-731be256cd50', 'Ipsa', 'Aut possimus expedita eligendi numquam.', 395.57, 'https://dummyimage.com/506x79', '2025-04-27 09:41:18', '2025-04-27 09:41:18', 3, 647);
--INSERT INTO `tbl_product` (`product_id`, `name`, `description`, `price`, `img_url`, `dt_created`, `dt_updated`, `category_id`, `stock_quantity`) VALUES ('prod-181a74d6-8037-42c4-b57b-97db64035015', 'Nihil', 'Placeat dolor et fuga.', 152.67, 'https://dummyimage.com/720x569', '2025-04-27 09:41:18', '2025-04-27 09:41:18', 3, 684);
--INSERT INTO `tbl_product` (`product_id`, `name`, `description`, `price`, `img_url`, `dt_created`, `dt_updated`, `category_id`, `stock_quantity`) VALUES ('prod-cfd5b790-0725-4825-8166-2c6a72e30302', 'Expedita', 'Temporibus eligendi officia ab quo quisquam dicta.', 320.59, 'https://placekitten.com/382/596', '2025-04-27 09:41:18', '2025-04-27 09:41:18', 1, 363);
--INSERT INTO `tbl_product` (`product_id`, `name`, `description`, `price`, `img_url`, `dt_created`, `dt_updated`, `category_id`, `stock_quantity`) VALUES ('prod-8536d8a3-1ac0-4a7f-a7a8-fa05abcf2d1f', 'Cupiditate', 'Culpa distinctio incidunt commodi fuga dolores dolore.', 469.47, 'https://dummyimage.com/754x935', '2025-04-27 09:41:18', '2025-04-27 09:41:18', 3, 443);
--INSERT INTO `tbl_product` (`product_id`, `name`, `description`, `price`, `img_url`, `dt_created`, `dt_updated`, `category_id`, `stock_quantity`) VALUES ('prod-c1b4f8b6-9b42-406a-aec8-da4af9d46e39', 'Culpa', 'Dolor doloremque accusamus quia inventore cupiditate.', 313.66, 'https://picsum.photos/884/704', '2025-04-27 09:41:18', '2025-04-27 09:41:18', 3, 446);
--INSERT INTO `tbl_product` (`product_id`, `name`, `description`, `price`, `img_url`, `dt_created`, `dt_updated`, `category_id`, `stock_quantity`) VALUES ('prod-d281bf6f-fe59-4976-921b-c7fe8d66876a', 'Vero', 'Deleniti velit sequi eos excepturi dolores.', 264.48, 'https://placekitten.com/802/555', '2025-04-27 09:41:18', '2025-04-27 09:41:18', 1, 241);
