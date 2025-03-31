CREATE TABLE IF NOT EXISTS `users` (
    `users_id` INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    `name` VARCHAR(255) NOT NULL,
    `email` VARCHAR(255) NOT NULL,
    `cpf` CHAR(255) NOT NULL,
    `phone_number` VARCHAR(255) NOT NULL,
    `dt_birth` DATE NOT NULL,
    `role` VARCHAR(255) NOT NULL,
    `dt_created` DATE NULL,
    `dt_updated` DATE NULL
);

CREATE TABLE IF NOT EXISTS `orders` (
    `orders_id` INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    `dt_order` DATE NOT NULL,
    `status` VARCHAR(255) NULL,
    `users_id` INT NOT NULL
);

CREATE TABLE IF NOT EXISTS `order_item` (
    `order_item_id` INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    `quantity` INT NOT NULL,
    `price_at_purchase` DECIMAL(15,2) NOT NULL,
    `orders_id` INT NOT NULL,
    `products_id` INT NOT NULL
);

CREATE TABLE IF NOT EXISTS `products` (
    `products_id` INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    `name` VARCHAR(255) NOT NULL,
    `description` VARCHAR(255) NOT NULL,
    `price` DECIMAL(15,2) NOT NULL,
    `imgUrl` VARCHAR(255) NULL,
    `categories_id` INT NOT NULL
);

CREATE TABLE IF NOT EXISTS `categories` (
    `categories_id` INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    `name` VARCHAR(255) NOT NULL,
    `description` VARCHAR(255) NOT NULL
);

CREATE TABLE IF NOT EXISTS `payments` (
    `payments_id` INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    `dt_payment` DATE NOT NULL,
    `payment_method` VARCHAR(90) NOT NULL,
    `amount` DECIMAL(15,2) NOT NULL,
    `orders_id` INT NOT NULL
);

CREATE TABLE IF NOT EXISTS `user_payment_info` (
    `user_payment_info_id` INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    `card_number` CHAR(255) NOT NULL,
    `card_expiration_month` INT NOT NULL,
    `card_expiration_year` INT NOT NULL,
    `card_security_code` CHAR(255) NOT NULL,
    `price` DECIMAL(15,2) NOT NULL,
    `instalments` INT NOT NULL,
    `dt_payment` DATE NOT NULL,
    `users_id` INT
);

CREATE TABLE IF NOT EXISTS `order_history` (
    `history_id` INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    `order_id` INT NOT NULL,
    `status` VARCHAR(255) NOT NULL,
    `dt_event` DATE,
    `users_id` INT NULL,
    `note` TEXT NULL
);

-- UNIQUE CONSTRAINTS
ALTER TABLE `users` ADD UNIQUE `users_email_unique`(`email`);
ALTER TABLE `users` ADD UNIQUE `users_cpf_unique`(`cpf`);
ALTER TABLE `users` ADD UNIQUE `users_phone_unique`(`phone_number`);
ALTER TABLE `user_payment_info` ADD UNIQUE `user_payment_info_card_number_unique`(`card_number`);

-- FOREIGN KEYS
ALTER TABLE `order_history`
    ADD CONSTRAINT `fk_order_history_orders` FOREIGN KEY (`order_id`) REFERENCES `orders`(`orders_id`);

ALTER TABLE `order_history`
    ADD CONSTRAINT `fk_order_history_users` FOREIGN KEY (`users_id`) REFERENCES `users`(`users_id`);

ALTER TABLE `orders`
    ADD CONSTRAINT `fk_orders_users` FOREIGN KEY (`users_id`) REFERENCES `users`(`users_id`);

ALTER TABLE `order_item`
    ADD CONSTRAINT `fk_order_item_orders` FOREIGN KEY (`orders_id`) REFERENCES `orders`(`orders_id`);

ALTER TABLE `order_item`
    ADD CONSTRAINT `fk_order_item_product` FOREIGN KEY (`products_id`) REFERENCES `products`(`products_id`);

ALTER TABLE `products`
    ADD CONSTRAINT `fk_products_categories` FOREIGN KEY (`categories_id`) REFERENCES `categories`(`categories_id`);

ALTER TABLE `payments`
    ADD CONSTRAINT `fk_payments_orders` FOREIGN KEY (`orders_id`) REFERENCES `orders`(`orders_id`);

ALTER TABLE `user_payment_info`
    ADD CONSTRAINT `fk_user_payment_info_users` FOREIGN KEY (`users_id`) REFERENCES `users`(`users_id`);

INSERT INTO `categories` (`name`, `description`) VALUES
('Brincos', 'Peças delicadas para compor o visual'),
('Colares', 'Colares sofisticados e modernos'),
('Anéis', 'Anéis de prata com designs exclusivos'),
('Braceletes', 'Braceletes elegantes para todas as ocasiões');
