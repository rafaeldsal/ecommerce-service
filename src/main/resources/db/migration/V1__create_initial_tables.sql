CREATE TABLE IF NOT EXISTS `tbl_user` (
    `user_id` INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    `name` VARCHAR(255) NOT NULL,
    `email` VARCHAR(255) NOT NULL,
    `cpf` CHAR(11) NOT NULL,
    `phone_number` VARCHAR(255) NOT NULL,
    `dt_birth` DATE NOT NULL,
    `role` VARCHAR(255) NOT NULL,
    `dt_created` DATE NOT NULL,
    `dt_updated` DATE NOT NULL
);

CREATE TABLE IF NOT EXISTS `tbl_order` (
    `order_id` INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    `dt_order` DATE NOT NULL,
    `status` VARCHAR(255) NULL,
    `dt_updated` DATE NOT NULL,
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
    `imgUrl` VARCHAR(255) NULL,
    `dt_created` DATE NOT NULL,
    `dt_updated` DATE NOT NULL,
    `category_id` INT NOT NULL
);

CREATE TABLE IF NOT EXISTS `tbl_category` (
    `category_id` INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    `name` VARCHAR(255) NOT NULL,
    `description` VARCHAR(255) NULL
);

CREATE TABLE IF NOT EXISTS `tbl_payment` (
    `payment_id` INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    `dt_payment` DATE NOT NULL,
    `payment_method` VARCHAR(90) NOT NULL,
    `amount` DECIMAL(15,2) NOT NULL
);

CREATE TABLE IF NOT EXISTS `tbl_order_history` (
    `history_id` INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    `order_id` INT NOT NULL,
    `status` VARCHAR(255) NOT NULL,
    `dt_event` DATE NOT NULL,
    `dt_created` DATE NOT NULL,
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

INSERT INTO `tbl_category` (`name`, `description`) VALUES
('Brincos', 'Peças delicadas para compor o visual'),
('Colares', 'Colares sofisticados e modernos'),
('Anéis', 'Anéis de prata com designs exclusivos'),
('Braceletes', 'Braceletes elegantes para todas as ocasiões');
