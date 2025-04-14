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
    `amount` DECIMAL(15,2) NOT NULL
);

CREATE TABLE IF NOT EXISTS `tbl_order_history` (
    `history_id` INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    `order_id` INT NOT NULL,
    `status` VARCHAR(255) NOT NULL,
    `dt_event` DATETIME NOT NULL,
    `dt_created` DATETIME NOT NULL,
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

-- Inserts para tbl_user
INSERT INTO `tbl_user` (`name`, `email`, `cpf`, `phone_number`, `dt_birth`, `role`, `dt_created`, `dt_updated`) VALUES ('Eduarda Almeida', 'maria-lizda-paz@example.com', '39741026552', '(011) 6410-7128', '2004-08-09', 'USER', '2025-04-10 14:50:44', '2025-04-10 14:50:44');
INSERT INTO `tbl_user` (`name`, `email`, `cpf`, `phone_number`, `dt_birth`, `role`, `dt_created`, `dt_updated`) VALUES ('Brayan Vasconcelos', 'carolinesilveira@example.net', '52160978485', '+55 (051) 5157-9821', '1974-05-16', 'ADMIN', '2025-04-10 14:50:44', '2025-04-10 14:50:44');
INSERT INTO `tbl_user` (`name`, `email`, `cpf`, `phone_number`, `dt_birth`, `role`, `dt_created`, `dt_updated`) VALUES ('Davi Lucas Cavalcanti', 'lunasales@example.net', '16935874084', '+55 (061) 3499 0792', '1964-10-08', 'ADMIN', '2025-04-10 14:50:44', '2025-04-10 14:50:44');
INSERT INTO `tbl_user` (`name`, `email`, `cpf`, `phone_number`, `dt_birth`, `role`, `dt_created`, `dt_updated`) VALUES ('Nathan Pires', 'sabrina58@example.net', '91574803620', '0800-184-4049', '1998-03-09', 'ADMIN', '2025-04-10 14:50:44', '2025-04-10 14:50:44');
INSERT INTO `tbl_user` (`name`, `email`, `cpf`, `phone_number`, `dt_birth`, `role`, `dt_created`, `dt_updated`) VALUES ('Vitória Duarte', 'helenavasconcelos@example.net', '95271384691', '71 5027 0085', '1994-03-13', 'ADMIN', '2025-04-10 14:50:44', '2025-04-10 14:50:44');
INSERT INTO `tbl_user` (`name`, `email`, `cpf`, `phone_number`, `dt_birth`, `role`, `dt_created`, `dt_updated`) VALUES ('Isis Farias', 'maria-vitoriamacedo@example.com', '54196087310', '+55 (081) 3228 2789', '1976-09-11', 'USER', '2025-04-10 14:50:44', '2025-04-10 14:50:44');
INSERT INTO `tbl_user` (`name`, `email`, `cpf`, `phone_number`, `dt_birth`, `role`, `dt_created`, `dt_updated`) VALUES ('Luiz Henrique Porto', 'helena52@example.net', '35864219070', '61 6801 2359', '1975-09-11', 'ADMIN', '2025-04-10 14:50:44', '2025-04-10 14:50:44');
INSERT INTO `tbl_user` (`name`, `email`, `cpf`, `phone_number`, `dt_birth`, `role`, `dt_created`, `dt_updated`) VALUES ('Augusto Moura', 'fnunes@example.org', '47605319207', '61 5697-3599', '2003-02-15', 'ADMIN', '2025-04-10 14:50:44', '2025-04-10 14:50:44');
INSERT INTO `tbl_user` (`name`, `email`, `cpf`, `phone_number`, `dt_birth`, `role`, `dt_created`, `dt_updated`) VALUES ('Ian Cavalcanti', 'sousagabriel@example.org', '15489327600', '+55 84 4365-7364', '2006-06-05', 'ADMIN', '2025-04-10 14:50:44', '2025-04-10 14:50:44');
INSERT INTO `tbl_user` (`name`, `email`, `cpf`, `phone_number`, `dt_birth`, `role`, `dt_created`, `dt_updated`) VALUES ('Yuri Aparecida', 'allana85@example.com', '42637581026', '(084) 3249-1097', '2003-05-23', 'USER', '2025-04-10 14:50:44', '2025-04-10 14:50:44');
INSERT INTO `tbl_user` (`name`, `email`, `cpf`, `phone_number`, `dt_birth`, `role`, `dt_created`, `dt_updated`) VALUES ('Gustavo Nascimento', 'lopeshenry@example.org', '94750832600', '71 5501 6973', '1964-05-20', 'ADMIN', '2025-04-10 14:50:44', '2025-04-10 14:50:44');
INSERT INTO `tbl_user` (`name`, `email`, `cpf`, `phone_number`, `dt_birth`, `role`, `dt_created`, `dt_updated`) VALUES ('Elisa Cunha', 'esther80@example.org', '67485932128', '+55 21 8185 8974', '1968-10-13', 'ADMIN', '2025-04-10 14:50:44', '2025-04-10 14:50:44');
INSERT INTO `tbl_user` (`name`, `email`, `cpf`, `phone_number`, `dt_birth`, `role`, `dt_created`, `dt_updated`) VALUES ('Milena Pereira', 'gomeshenry-gabriel@example.com', '29503418615', '+55 11 0494 2955', '2004-06-03', 'ADMIN', '2025-04-10 14:50:44', '2025-04-10 14:50:44');
INSERT INTO `tbl_user` (`name`, `email`, `cpf`, `phone_number`, `dt_birth`, `role`, `dt_created`, `dt_updated`) VALUES ('Thomas Correia', 'sabrinafernandes@example.com', '24801736904', '+55 11 7837 4854', '1964-08-08', 'USER', '2025-04-10 14:50:44', '2025-04-10 14:50:44');
INSERT INTO `tbl_user` (`name`, `email`, `cpf`, `phone_number`, `dt_birth`, `role`, `dt_created`, `dt_updated`) VALUES ('Dra. Ana Luiza Martins', 'teixeiramaria-luisa@example.org', '93054217680', '+55 11 5808-6529', '1974-02-06', 'ADMIN', '2025-04-10 14:50:44', '2025-04-10 14:50:44');
INSERT INTO `tbl_user` (`name`, `email`, `cpf`, `phone_number`, `dt_birth`, `role`, `dt_created`, `dt_updated`) VALUES ('Juan Ferreira', 'raquellima@example.net', '45839617237', '+55 (041) 8205-2444', '1984-02-21', 'ADMIN', '2025-04-10 14:50:44', '2025-04-10 14:50:44');
INSERT INTO `tbl_user` (`name`, `email`, `cpf`, `phone_number`, `dt_birth`, `role`, `dt_created`, `dt_updated`) VALUES ('Bianca Brito', 'bernardo71@example.com', '58694071258', '+55 31 3507-7506', '1972-09-15', 'USER', '2025-04-10 14:50:44', '2025-04-10 14:50:44');
INSERT INTO `tbl_user` (`name`, `email`, `cpf`, `phone_number`, `dt_birth`, `role`, `dt_created`, `dt_updated`) VALUES ('Guilherme Ramos', 'renanmendonca@example.org', '40287196313', '+55 11 6843 2187', '1991-03-07', 'ADMIN', '2025-04-10 14:50:44', '2025-04-10 14:50:44');
INSERT INTO `tbl_user` (`name`, `email`, `cpf`, `phone_number`, `dt_birth`, `role`, `dt_created`, `dt_updated`) VALUES ('Bruno Teixeira', 'aylaalmeida@example.org', '81730925677', '(084) 9944 2276', '2007-02-19', 'USER', '2025-04-10 14:50:44', '2025-04-10 14:50:44');
INSERT INTO `tbl_user` (`name`, `email`, `cpf`, `phone_number`, `dt_birth`, `role`, `dt_created`, `dt_updated`) VALUES ('Agatha da Cruz', 'ribeirogael-henrique@example.org', '86950732112', '+55 71 7282-2633', '1984-02-06', 'ADMIN', '2025-04-10 14:50:44', '2025-04-10 14:50:44');
INSERT INTO `tbl_user` (`name`, `email`, `cpf`, `phone_number`, `dt_birth`, `role`, `dt_created`, `dt_updated`) VALUES ('Maria Vitória Souza', 'meloayla@example.net', '40159673216', '+55 61 7850-2047', '2000-05-03', 'USER', '2025-04-10 14:50:44', '2025-04-10 14:50:44');
INSERT INTO `tbl_user` (`name`, `email`, `cpf`, `phone_number`, `dt_birth`, `role`, `dt_created`, `dt_updated`) VALUES ('Dra. Ana Laura Monteiro', 'gustavo75@example.net', '18956234728', '0900-805-7012', '1986-03-18', 'USER', '2025-04-10 14:50:44', '2025-04-10 14:50:44');
INSERT INTO `tbl_user` (`name`, `email`, `cpf`, `phone_number`, `dt_birth`, `role`, `dt_created`, `dt_updated`) VALUES ('João Lucas Vargas', 'erick85@example.net', '47519238628', '0300-016-9121', '1977-12-23', 'ADMIN', '2025-04-10 14:50:44', '2025-04-10 14:50:44');
INSERT INTO `tbl_user` (`name`, `email`, `cpf`, `phone_number`, `dt_birth`, `role`, `dt_created`, `dt_updated`) VALUES ('Zoe Jesus', 'luiz-miguelpacheco@example.org', '14029835651', '+55 51 8460 1427', '1999-12-28', 'ADMIN', '2025-04-10 14:50:44', '2025-04-10 14:50:44');
INSERT INTO `tbl_user` (`name`, `email`, `cpf`, `phone_number`, `dt_birth`, `role`, `dt_created`, `dt_updated`) VALUES ('Srta. Ana Lívia Mendes', 'jose80@example.org', '67028491331', '+55 (084) 4723 6792', '1989-08-03', 'USER', '2025-04-10 14:50:44', '2025-04-10 14:50:44');
INSERT INTO `tbl_user` (`name`, `email`, `cpf`, `phone_number`, `dt_birth`, `role`, `dt_created`, `dt_updated`) VALUES ('Sr. Bruno da Mota', 'eduarda73@example.org', '63509148720', '(061) 4491 2604', '2002-09-07', 'ADMIN', '2025-04-10 14:50:44', '2025-04-10 14:50:44');
INSERT INTO `tbl_user` (`name`, `email`, `cpf`, `phone_number`, `dt_birth`, `role`, `dt_created`, `dt_updated`) VALUES ('Alana Viana', 'camaragustavo@example.net', '89630451700', '+55 (081) 8513-4905', '1983-01-17', 'USER', '2025-04-10 14:50:44', '2025-04-10 14:50:44');
INSERT INTO `tbl_user` (`name`, `email`, `cpf`, `phone_number`, `dt_birth`, `role`, `dt_created`, `dt_updated`) VALUES ('Fernando Sousa', 'esther85@example.org', '62845039115', '84 3955 3852', '1987-08-13', 'ADMIN', '2025-04-10 14:50:44', '2025-04-10 14:50:44');
INSERT INTO `tbl_user` (`name`, `email`, `cpf`, `phone_number`, `dt_birth`, `role`, `dt_created`, `dt_updated`) VALUES ('Julia Duarte', 'nascimentomariah@example.org', '48975361209', '+55 (071) 1937 9225', '1978-11-20', 'ADMIN', '2025-04-10 14:50:44', '2025-04-10 14:50:44');
INSERT INTO `tbl_user` (`name`, `email`, `cpf`, `phone_number`, `dt_birth`, `role`, `dt_created`, `dt_updated`) VALUES ('Vinícius da Mata', 'gustavo-henriquemarques@example.org', '32796058140', '21 0122-7249', '1965-07-12', 'USER', '2025-04-10 14:50:44', '2025-04-10 14:50:44');
INSERT INTO `tbl_user` (`name`, `email`, `cpf`, `phone_number`, `dt_birth`, `role`, `dt_created`, `dt_updated`) VALUES ('Theo Fonseca', 'helena60@example.com', '20716983559', '84 6533 0890', '1990-06-29', 'USER', '2025-04-10 14:50:44', '2025-04-10 14:50:44');
INSERT INTO `tbl_user` (`name`, `email`, `cpf`, `phone_number`, `dt_birth`, `role`, `dt_created`, `dt_updated`) VALUES ('Beatriz Jesus', 'machadoisabela@example.com', '68947301213', '31 9321 8330', '1995-10-31', 'ADMIN', '2025-04-10 14:50:44', '2025-04-10 14:50:44');
INSERT INTO `tbl_user` (`name`, `email`, `cpf`, `phone_number`, `dt_birth`, `role`, `dt_created`, `dt_updated`) VALUES ('Dr. João Lucas Farias', 'ana-juliasouza@example.org', '45687193057', '+55 71 5439-8414', '1967-07-01', 'USER', '2025-04-10 14:50:44', '2025-04-10 14:50:44');
INSERT INTO `tbl_user` (`name`, `email`, `cpf`, `phone_number`, `dt_birth`, `role`, `dt_created`, `dt_updated`) VALUES ('Dr. Thomas Jesus', 'moreiraaugusto@example.org', '06795134261', '81 9235 6836', '1992-01-14', 'USER', '2025-04-10 14:50:44', '2025-04-10 14:50:44');
INSERT INTO `tbl_user` (`name`, `email`, `cpf`, `phone_number`, `dt_birth`, `role`, `dt_created`, `dt_updated`) VALUES ('Vinícius Porto', 'danterezende@example.org', '52798643155', '+55 81 7835-7315', '1980-12-30', 'USER', '2025-04-10 14:50:44', '2025-04-10 14:50:44');
INSERT INTO `tbl_user` (`name`, `email`, `cpf`, `phone_number`, `dt_birth`, `role`, `dt_created`, `dt_updated`) VALUES ('Dra. Aurora Ramos', 'souzaalana@example.com', '23806157480', '(081) 8572 7262', '1982-10-24', 'ADMIN', '2025-04-10 14:50:44', '2025-04-10 14:50:44');
INSERT INTO `tbl_user` (`name`, `email`, `cpf`, `phone_number`, `dt_birth`, `role`, `dt_created`, `dt_updated`) VALUES ('Srta. Mariane Freitas', 'nascimentoluiz-henrique@example.net', '79615032425', '+55 (081) 6434 4314', '1971-12-17', 'USER', '2025-04-10 14:50:44', '2025-04-10 14:50:44');
INSERT INTO `tbl_user` (`name`, `email`, `cpf`, `phone_number`, `dt_birth`, `role`, `dt_created`, `dt_updated`) VALUES ('Davi Lucas Campos', 'fernandoda-rocha@example.net', '54270691352', '(061) 2984 2892', '1977-08-29', 'ADMIN', '2025-04-10 14:50:44', '2025-04-10 14:50:44');
INSERT INTO `tbl_user` (`name`, `email`, `cpf`, `phone_number`, `dt_birth`, `role`, `dt_created`, `dt_updated`) VALUES ('Luiz Gustavo Barros', 'amelo@example.net', '47823901650', '0300-652-7233', '1969-03-24', 'USER', '2025-04-10 14:50:44', '2025-04-10 14:50:44');
INSERT INTO `tbl_user` (`name`, `email`, `cpf`, `phone_number`, `dt_birth`, `role`, `dt_created`, `dt_updated`) VALUES ('Larissa Rodrigues', 'maria-luizapastor@example.net', '94817203579', '+55 31 0633 6063', '1990-01-30', 'ADMIN', '2025-04-10 14:50:44', '2025-04-10 14:50:44');
INSERT INTO `tbl_user` (`name`, `email`, `cpf`, `phone_number`, `dt_birth`, `role`, `dt_created`, `dt_updated`) VALUES ('Isabel Viana', 'nina67@example.net', '28594603738', '+55 (021) 7967 8800', '2004-01-15', 'ADMIN', '2025-04-10 14:50:44', '2025-04-10 14:50:44');
INSERT INTO `tbl_user` (`name`, `email`, `cpf`, `phone_number`, `dt_birth`, `role`, `dt_created`, `dt_updated`) VALUES ('Catarina Fonseca', 'lopeslucas@example.com', '58193740610', '+55 61 4539-3102', '1999-07-20', 'USER', '2025-04-10 14:50:44', '2025-04-10 14:50:44');
INSERT INTO `tbl_user` (`name`, `email`, `cpf`, `phone_number`, `dt_birth`, `role`, `dt_created`, `dt_updated`) VALUES ('Matheus Camargo', 'moraesisaac@example.com', '28740951685', '+55 (081) 6818-5664', '1973-08-16', 'ADMIN', '2025-04-10 14:50:44', '2025-04-10 14:50:44');
INSERT INTO `tbl_user` (`name`, `email`, `cpf`, `phone_number`, `dt_birth`, `role`, `dt_created`, `dt_updated`) VALUES ('Vitória Viana', 'xbrito@example.net', '54139608757', '+55 71 6351-5765', '1995-07-29', 'ADMIN', '2025-04-10 14:50:44', '2025-04-10 14:50:44');
INSERT INTO `tbl_user` (`name`, `email`, `cpf`, `phone_number`, `dt_birth`, `role`, `dt_created`, `dt_updated`) VALUES ('Isabel Cavalcante', 'sophiamendonca@example.net', '30289514606', '+55 71 8758 6119', '2006-05-08', 'ADMIN', '2025-04-10 14:50:44', '2025-04-10 14:50:44');
INSERT INTO `tbl_user` (`name`, `email`, `cpf`, `phone_number`, `dt_birth`, `role`, `dt_created`, `dt_updated`) VALUES ('Paulo da Luz', 'tfogaca@example.com', '49628715364', '(051) 4001 3216', '1974-01-09', 'ADMIN', '2025-04-10 14:50:44', '2025-04-10 14:50:44');
INSERT INTO `tbl_user` (`name`, `email`, `cpf`, `phone_number`, `dt_birth`, `role`, `dt_created`, `dt_updated`) VALUES ('Ana Julia Correia', 'maria-isis79@example.org', '67318052462', '31 3098-9630', '1982-05-24', 'ADMIN', '2025-04-10 14:50:44', '2025-04-10 14:50:44');
INSERT INTO `tbl_user` (`name`, `email`, `cpf`, `phone_number`, `dt_birth`, `role`, `dt_created`, `dt_updated`) VALUES ('Carlos Eduardo da Rosa', 'benicio29@example.org', '54823716973', '31 4533 9083', '2005-03-16', 'USER', '2025-04-10 14:50:44', '2025-04-10 14:50:44');
INSERT INTO `tbl_user` (`name`, `email`, `cpf`, `phone_number`, `dt_birth`, `role`, `dt_created`, `dt_updated`) VALUES ('Gabriela Leão', 'clarasales@example.com', '94581723060', '+55 (084) 3391 1860', '1975-11-03', 'USER', '2025-04-10 14:50:44', '2025-04-10 14:50:44');
INSERT INTO `tbl_user` (`name`, `email`, `cpf`, `phone_number`, `dt_birth`, `role`, `dt_created`, `dt_updated`) VALUES ('Dr. Otávio da Cruz', 'eda-cunha@example.net', '56813409700', '+55 (084) 9684 8508', '2002-11-16', 'ADMIN', '2025-04-10 14:50:44', '2025-04-10 14:50:44');
INSERT INTO `tbl_user` (`name`, `email`, `cpf`, `phone_number`, `dt_birth`, `role`, `dt_created`, `dt_updated`) VALUES ('Dr. Oliver Leão', 'fnascimento@example.org', '57098216402', '31 4212 1568', '1987-11-08', 'ADMIN', '2025-04-10 14:50:44', '2025-04-10 14:50:44');
INSERT INTO `tbl_user` (`name`, `email`, `cpf`, `phone_number`, `dt_birth`, `role`, `dt_created`, `dt_updated`) VALUES ('Luiz Henrique Albuquerque', 'emillyvargas@example.com', '76014398287', '41 8361-4160', '1965-10-10', 'ADMIN', '2025-04-10 14:50:44', '2025-04-10 14:50:44');
INSERT INTO `tbl_user` (`name`, `email`, `cpf`, `phone_number`, `dt_birth`, `role`, `dt_created`, `dt_updated`) VALUES ('Larissa Fogaça', 'wpastor@example.net', '67854210390', '+55 (071) 8972 2242', '2002-08-18', 'ADMIN', '2025-04-10 14:50:44', '2025-04-10 14:50:44');
INSERT INTO `tbl_user` (`name`, `email`, `cpf`, `phone_number`, `dt_birth`, `role`, `dt_created`, `dt_updated`) VALUES ('Laís Casa Grande', 'carvalhofrancisco@example.net', '78503964139', '51 8056 8387', '1976-07-30', 'ADMIN', '2025-04-10 14:50:44', '2025-04-10 14:50:44');
INSERT INTO `tbl_user` (`name`, `email`, `cpf`, `phone_number`, `dt_birth`, `role`, `dt_created`, `dt_updated`) VALUES ('Luiz Miguel Pires', 'raulnascimento@example.org', '64258710326', '+55 84 7133-0756', '1983-05-22', 'ADMIN', '2025-04-10 14:50:44', '2025-04-10 14:50:44');
INSERT INTO `tbl_user` (`name`, `email`, `cpf`, `phone_number`, `dt_birth`, `role`, `dt_created`, `dt_updated`) VALUES ('Dante da Cruz', 'fogacalevi@example.org', '41985637057', '+55 (011) 1350-9312', '1991-09-05', 'ADMIN', '2025-04-10 14:50:44', '2025-04-10 14:50:44');
INSERT INTO `tbl_user` (`name`, `email`, `cpf`, `phone_number`, `dt_birth`, `role`, `dt_created`, `dt_updated`) VALUES ('Laís Rodrigues', 'thiagocavalcante@example.net', '31825470626', '81 9045 3218', '1974-06-10', 'USER', '2025-04-10 14:50:44', '2025-04-10 14:50:44');
INSERT INTO `tbl_user` (`name`, `email`, `cpf`, `phone_number`, `dt_birth`, `role`, `dt_created`, `dt_updated`) VALUES ('Sr. Matheus Leão', 'miguelmendes@example.org', '86315794246', '21 9973-6230', '1994-03-17', 'USER', '2025-04-10 14:50:44', '2025-04-10 14:50:44');
INSERT INTO `tbl_user` (`name`, `email`, `cpf`, `phone_number`, `dt_birth`, `role`, `dt_created`, `dt_updated`) VALUES ('Hadassa da Cruz', 'mirella80@example.net', '42937681069', '0900-928-7906', '1966-08-24', 'ADMIN', '2025-04-10 14:50:44', '2025-04-10 14:50:44');
INSERT INTO `tbl_user` (`name`, `email`, `cpf`, `phone_number`, `dt_birth`, `role`, `dt_created`, `dt_updated`) VALUES ('Dr. Ravi da Cunha', 'mourapedro-lucas@example.org', '58604379193', '11 2552 9018', '1972-11-12', 'USER', '2025-04-10 14:50:44', '2025-04-10 14:50:44');
INSERT INTO `tbl_user` (`name`, `email`, `cpf`, `phone_number`, `dt_birth`, `role`, `dt_created`, `dt_updated`) VALUES ('Otto Costela', 'bmelo@example.com', '67432198519', '21 9434-4047', '1982-11-29', 'USER', '2025-04-10 14:50:44', '2025-04-10 14:50:44');
INSERT INTO `tbl_user` (`name`, `email`, `cpf`, `phone_number`, `dt_birth`, `role`, `dt_created`, `dt_updated`) VALUES ('Murilo Lima', 'apinto@example.org', '94017285694', '(051) 3354-1451', '1984-05-23', 'USER', '2025-04-10 14:50:44', '2025-04-10 14:50:44');
INSERT INTO `tbl_user` (`name`, `email`, `cpf`, `phone_number`, `dt_birth`, `role`, `dt_created`, `dt_updated`) VALUES ('Maysa Vargas', 'limaluiz-fernando@example.org', '21583960759', '61 4146 7807', '1965-07-24', 'USER', '2025-04-10 14:50:44', '2025-04-10 14:50:44');
INSERT INTO `tbl_user` (`name`, `email`, `cpf`, `phone_number`, `dt_birth`, `role`, `dt_created`, `dt_updated`) VALUES ('Beatriz da Mota', 'saraborges@example.com', '19324568728', '0900-618-7205', '1990-06-26', 'USER', '2025-04-10 14:50:44', '2025-04-10 14:50:44');
INSERT INTO `tbl_user` (`name`, `email`, `cpf`, `phone_number`, `dt_birth`, `role`, `dt_created`, `dt_updated`) VALUES ('Dr. Otto Fonseca', 'jesusrael@example.org', '61320845924', '+55 (081) 1219 8285', '1975-01-02', 'USER', '2025-04-10 14:50:44', '2025-04-10 14:50:44');
INSERT INTO `tbl_user` (`name`, `email`, `cpf`, `phone_number`, `dt_birth`, `role`, `dt_created`, `dt_updated`) VALUES ('Yuri Cavalcanti', 'pietropacheco@example.com', '25397861030', '81 5640-7029', '1971-04-23', 'USER', '2025-04-10 14:50:44', '2025-04-10 14:50:44');
INSERT INTO `tbl_user` (`name`, `email`, `cpf`, `phone_number`, `dt_birth`, `role`, `dt_created`, `dt_updated`) VALUES ('Yuri Oliveira', 'pachecobruna@example.com', '29084567374', '61 0963 7661', '1972-09-29', 'ADMIN', '2025-04-10 14:50:44', '2025-04-10 14:50:44');
INSERT INTO `tbl_user` (`name`, `email`, `cpf`, `phone_number`, `dt_birth`, `role`, `dt_created`, `dt_updated`) VALUES ('Gustavo Sá', 'davimendonca@example.org', '87306129503', '81 2552-0760', '1987-12-14', 'ADMIN', '2025-04-10 14:50:44', '2025-04-10 14:50:44');
INSERT INTO `tbl_user` (`name`, `email`, `cpf`, `phone_number`, `dt_birth`, `role`, `dt_created`, `dt_updated`) VALUES ('Olivia Souza', 'limasofia@example.com', '80945732104', '+55 21 7515 9865', '1998-02-01', 'USER', '2025-04-10 14:50:44', '2025-04-10 14:50:44');
INSERT INTO `tbl_user` (`name`, `email`, `cpf`, `phone_number`, `dt_birth`, `role`, `dt_created`, `dt_updated`) VALUES ('Dr. Calebe Farias', 'oda-rosa@example.net', '10268495351', '(071) 9948 6250', '1969-07-27', 'USER', '2025-04-10 14:50:44', '2025-04-10 14:50:44');
INSERT INTO `tbl_user` (`name`, `email`, `cpf`, `phone_number`, `dt_birth`, `role`, `dt_created`, `dt_updated`) VALUES ('Maria Isis Ribeiro', 'leodas-neves@example.org', '92465783092', '(071) 9126-8981', '1977-11-17', 'ADMIN', '2025-04-10 14:50:44', '2025-04-10 14:50:44');
INSERT INTO `tbl_user` (`name`, `email`, `cpf`, `phone_number`, `dt_birth`, `role`, `dt_created`, `dt_updated`) VALUES ('Dra. Hadassa Aparecida', 'mmontenegro@example.com', '13546092716', '51 3736 5065', '1964-07-29', 'ADMIN', '2025-04-10 14:50:44', '2025-04-10 14:50:44');
INSERT INTO `tbl_user` (`name`, `email`, `cpf`, `phone_number`, `dt_birth`, `role`, `dt_created`, `dt_updated`) VALUES ('Luiz Otávio Macedo', 'yago09@example.com', '30641578253', '0500 953 9244', '2004-02-17', 'ADMIN', '2025-04-10 14:50:44', '2025-04-10 14:50:44');
INSERT INTO `tbl_user` (`name`, `email`, `cpf`, `phone_number`, `dt_birth`, `role`, `dt_created`, `dt_updated`) VALUES ('Maya Nascimento', 'joaquimabreu@example.com', '43905812797', '21 1977 1742', '2001-10-22', 'USER', '2025-04-10 14:50:44', '2025-04-10 14:50:44');
INSERT INTO `tbl_user` (`name`, `email`, `cpf`, `phone_number`, `dt_birth`, `role`, `dt_created`, `dt_updated`) VALUES ('Sabrina da Paz', 'emillyleao@example.net', '41635298709', '+55 71 0915-1789', '2004-06-15', 'ADMIN', '2025-04-10 14:50:44', '2025-04-10 14:50:44');
INSERT INTO `tbl_user` (`name`, `email`, `cpf`, `phone_number`, `dt_birth`, `role`, `dt_created`, `dt_updated`) VALUES ('Luísa Mendonça', 'mariafogaca@example.net', '74293108696', '71 7532-9754', '1996-01-30', 'USER', '2025-04-10 14:50:44', '2025-04-10 14:50:44');
INSERT INTO `tbl_user` (`name`, `email`, `cpf`, `phone_number`, `dt_birth`, `role`, `dt_created`, `dt_updated`) VALUES ('Sr. Matteo Camargo', 'xcunha@example.net', '84973526029', '81 0549-0241', '1987-11-03', 'USER', '2025-04-10 14:50:44', '2025-04-10 14:50:44');
INSERT INTO `tbl_user` (`name`, `email`, `cpf`, `phone_number`, `dt_birth`, `role`, `dt_created`, `dt_updated`) VALUES ('Júlia Correia', 'caldeiraravi-lucca@example.com', '53918762068', '+55 71 8667-7584', '1985-08-24', 'ADMIN', '2025-04-10 14:50:44', '2025-04-10 14:50:44');
INSERT INTO `tbl_user` (`name`, `email`, `cpf`, `phone_number`, `dt_birth`, `role`, `dt_created`, `dt_updated`) VALUES ('João Vitor Barbosa', 'stephanymoura@example.org', '14925038605', '+55 84 1691 6636', '1971-02-12', 'ADMIN', '2025-04-10 14:50:44', '2025-04-10 14:50:44');
INSERT INTO `tbl_user` (`name`, `email`, `cpf`, `phone_number`, `dt_birth`, `role`, `dt_created`, `dt_updated`) VALUES ('Cauê Nunes', 'eduardo95@example.com', '93280746140', '+55 (011) 5058-2889', '1966-08-30', 'USER', '2025-04-10 14:50:44', '2025-04-10 14:50:44');
INSERT INTO `tbl_user` (`name`, `email`, `cpf`, `phone_number`, `dt_birth`, `role`, `dt_created`, `dt_updated`) VALUES ('Dr. Joaquim Castro', 'lopesemanuel@example.com', '83415962709', '(061) 2455-2487', '1968-02-10', 'USER', '2025-04-10 14:50:44', '2025-04-10 14:50:44');
INSERT INTO `tbl_user` (`name`, `email`, `cpf`, `phone_number`, `dt_birth`, `role`, `dt_created`, `dt_updated`) VALUES ('Enzo Freitas', 'da-cruzisabel@example.com', '93517860484', '+55 (061) 9916-7424', '1978-05-21', 'ADMIN', '2025-04-10 14:50:44', '2025-04-10 14:50:44');
INSERT INTO `tbl_user` (`name`, `email`, `cpf`, `phone_number`, `dt_birth`, `role`, `dt_created`, `dt_updated`) VALUES ('Marcelo da Mota', 'monteiroluan@example.com', '31425980651', '+55 11 2887-5968', '1967-11-26', 'ADMIN', '2025-04-10 14:50:44', '2025-04-10 14:50:44');
INSERT INTO `tbl_user` (`name`, `email`, `cpf`, `phone_number`, `dt_birth`, `role`, `dt_created`, `dt_updated`) VALUES ('João Miguel Farias', 'renancorreia@example.org', '20941856720', '+55 81 8906-7545', '1982-10-01', 'USER', '2025-04-10 14:50:44', '2025-04-10 14:50:44');
INSERT INTO `tbl_user` (`name`, `email`, `cpf`, `phone_number`, `dt_birth`, `role`, `dt_created`, `dt_updated`) VALUES ('Isabella Fogaça', 'eduardodas-neves@example.com', '58294637092', '41 2488 0699', '1987-12-08', 'ADMIN', '2025-04-10 14:50:44', '2025-04-10 14:50:44');
INSERT INTO `tbl_user` (`name`, `email`, `cpf`, `phone_number`, `dt_birth`, `role`, `dt_created`, `dt_updated`) VALUES ('Ravi Lucca Guerra', 'levialmeida@example.org', '48709613501', '(071) 0345-8919', '1983-05-09', 'USER', '2025-04-10 14:50:44', '2025-04-10 14:50:44');
INSERT INTO `tbl_user` (`name`, `email`, `cpf`, `phone_number`, `dt_birth`, `role`, `dt_created`, `dt_updated`) VALUES ('Maria Pimenta', 'luiz-otaviocamargo@example.net', '61859740294', '+55 84 1631 3112', '1979-05-04', 'USER', '2025-04-10 14:50:44', '2025-04-10 14:50:44');
INSERT INTO `tbl_user` (`name`, `email`, `cpf`, `phone_number`, `dt_birth`, `role`, `dt_created`, `dt_updated`) VALUES ('Helena Cavalcante', 'mouramaria-helena@example.org', '42506178902', '+55 21 1788 1361', '1971-07-26', 'USER', '2025-04-10 14:50:44', '2025-04-10 14:50:44');
INSERT INTO `tbl_user` (`name`, `email`, `cpf`, `phone_number`, `dt_birth`, `role`, `dt_created`, `dt_updated`) VALUES ('Brenda Araújo', 'pbarros@example.net', '70463892178', '(051) 1651 9850', '2007-03-22', 'ADMIN', '2025-04-10 14:50:44', '2025-04-10 14:50:44');
INSERT INTO `tbl_user` (`name`, `email`, `cpf`, `phone_number`, `dt_birth`, `role`, `dt_created`, `dt_updated`) VALUES ('Aurora Guerra', 'pedro-miguelcamara@example.com', '65843172007', '+55 (031) 5944 9604', '1975-12-27', 'ADMIN', '2025-04-10 14:50:44', '2025-04-10 14:50:44');
INSERT INTO `tbl_user` (`name`, `email`, `cpf`, `phone_number`, `dt_birth`, `role`, `dt_created`, `dt_updated`) VALUES ('Danilo Silva', 'marquesravi@example.com', '76510923840', '31 9674 2830', '2004-11-02', 'USER', '2025-04-10 14:50:44', '2025-04-10 14:50:44');
INSERT INTO `tbl_user` (`name`, `email`, `cpf`, `phone_number`, `dt_birth`, `role`, `dt_created`, `dt_updated`) VALUES ('Juan Borges', 'pietracassiano@example.org', '35647918255', '(084) 0383 1830', '1986-04-03', 'ADMIN', '2025-04-10 14:50:44', '2025-04-10 14:50:44');
INSERT INTO `tbl_user` (`name`, `email`, `cpf`, `phone_number`, `dt_birth`, `role`, `dt_created`, `dt_updated`) VALUES ('Laís Pereira', 'olivercunha@example.net', '87065492338', '61 3035 5404', '1989-05-20', 'USER', '2025-04-10 14:50:44', '2025-04-10 14:50:44');
INSERT INTO `tbl_user` (`name`, `email`, `cpf`, `phone_number`, `dt_birth`, `role`, `dt_created`, `dt_updated`) VALUES ('Luísa Cirino', 'bda-rosa@example.com', '49827360574', '(081) 0954-0814', '1965-04-25', 'ADMIN', '2025-04-10 14:50:44', '2025-04-10 14:50:44');
INSERT INTO `tbl_user` (`name`, `email`, `cpf`, `phone_number`, `dt_birth`, `role`, `dt_created`, `dt_updated`) VALUES ('Dra. Ana Luiza Cirino', 'ana-claramachado@example.com', '78019436278', '+55 81 0733-8306', '1966-03-22', 'ADMIN', '2025-04-10 14:50:44', '2025-04-10 14:50:44');
INSERT INTO `tbl_user` (`name`, `email`, `cpf`, `phone_number`, `dt_birth`, `role`, `dt_created`, `dt_updated`) VALUES ('Rhavi Mendes', 'andradebento@example.org', '76029438565', '+55 (081) 2126-3078', '1979-05-17', 'USER', '2025-04-10 14:50:44', '2025-04-10 14:50:44');
INSERT INTO `tbl_user` (`name`, `email`, `cpf`, `phone_number`, `dt_birth`, `role`, `dt_created`, `dt_updated`) VALUES ('Raquel Sousa', 'cavalcantilais@example.com', '32546918060', '+55 81 6868-8235', '1965-05-20', 'ADMIN', '2025-04-10 14:50:44', '2025-04-10 14:50:44');
INSERT INTO `tbl_user` (`name`, `email`, `cpf`, `phone_number`, `dt_birth`, `role`, `dt_created`, `dt_updated`) VALUES ('Caleb Novais', 'sampaiomaria-flor@example.com', '34791280504', '51 9734 6680', '2004-05-20', 'USER', '2025-04-10 14:50:44', '2025-04-10 14:50:44');
INSERT INTO `tbl_user` (`name`, `email`, `cpf`, `phone_number`, `dt_birth`, `role`, `dt_created`, `dt_updated`) VALUES ('Luara Montenegro', 'csiqueira@example.net', '36089215759', '(011) 9747 1121', '1974-05-31', 'USER', '2025-04-10 14:50:44', '2025-04-10 14:50:44');
INSERT INTO `tbl_user` (`name`, `email`, `cpf`, `phone_number`, `dt_birth`, `role`, `dt_created`, `dt_updated`) VALUES ('Sophie Nunes', 'rodrigueseloa@example.org', '32845967047', '0500-429-6701', '1967-12-29', 'ADMIN', '2025-04-10 14:50:44', '2025-04-10 14:50:44');

-- Inserts para tbl_product
INSERT INTO `tbl_product` (`name`, `description`, `price`, `img_url`, `dt_created`, `dt_updated`, `category_id`, `stock_quantity`) VALUES ('Minima', 'Delectus deserunt sunt.', 158.02, 'https://dummyimage.com/148x275', '2025-04-14 19:29:18', '2025-04-14 19:29:18', 2, 636);
INSERT INTO `tbl_product` (`name`, `description`, `price`, `img_url`, `dt_created`, `dt_updated`, `category_id`, `stock_quantity`) VALUES ('Incidunt', 'At dolore blanditiis eum.', 234.32, 'https://picsum.photos/910/643', '2025-04-14 19:29:18', '2025-04-14 19:29:18', 1, 370);
INSERT INTO `tbl_product` (`name`, `description`, `price`, `img_url`, `dt_created`, `dt_updated`, `category_id`, `stock_quantity`) VALUES ('Suscipit', 'Tempora laudantium ea nostrum qui voluptates.', 183.59, 'https://placekitten.com/706/569', '2025-04-14 19:29:18', '2025-04-14 19:29:18', 1, 594);
INSERT INTO `tbl_product` (`name`, `description`, `price`, `img_url`, `dt_created`, `dt_updated`, `category_id`, `stock_quantity`) VALUES ('Enim', 'Sequi facilis fuga adipisci.', 170.21, 'https://picsum.photos/742/361', '2025-04-14 19:29:18', '2025-04-14 19:29:18', 4, 938);
INSERT INTO `tbl_product` (`name`, `description`, `price`, `img_url`, `dt_created`, `dt_updated`, `category_id`, `stock_quantity`) VALUES ('Illo', 'Veniam cum nihil illo cum.', 94.52, 'https://placekitten.com/669/648', '2025-04-14 19:29:18', '2025-04-14 19:29:18', 2, 147);
INSERT INTO `tbl_product` (`name`, `description`, `price`, `img_url`, `dt_created`, `dt_updated`, `category_id`, `stock_quantity`) VALUES ('Rem', 'Voluptates quos tempora voluptatem recusandae dolor in.', 207.34, 'https://picsum.photos/361/774', '2025-04-14 19:29:18', '2025-04-14 19:29:18', 1, 338);
INSERT INTO `tbl_product` (`name`, `description`, `price`, `img_url`, `dt_created`, `dt_updated`, `category_id`, `stock_quantity`) VALUES ('Ea', 'Quam iste minima delectus rem.', 444.13, 'https://placekitten.com/773/827', '2025-04-14 19:29:18', '2025-04-14 19:29:18', 1, 823);
INSERT INTO `tbl_product` (`name`, `description`, `price`, `img_url`, `dt_created`, `dt_updated`, `category_id`, `stock_quantity`) VALUES ('Tempore', 'Distinctio unde repellendus aliquid hic voluptatibus consequuntur.', 281.35, 'https://picsum.photos/297/393', '2025-04-14 19:29:18', '2025-04-14 19:29:18', 2, 468);
INSERT INTO `tbl_product` (`name`, `description`, `price`, `img_url`, `dt_created`, `dt_updated`, `category_id`, `stock_quantity`) VALUES ('Quos', 'Iusto corporis impedit illo ab iure corporis.', 333.2, 'https://dummyimage.com/447x643', '2025-04-14 19:29:18', '2025-04-14 19:29:18', 3, 465);
INSERT INTO `tbl_product` (`name`, `description`, `price`, `img_url`, `dt_created`, `dt_updated`, `category_id`, `stock_quantity`) VALUES ('Earum', 'Voluptates iusto non voluptatibus consequuntur.', 163.19, 'https://dummyimage.com/429x84', '2025-04-14 19:29:18', '2025-04-14 19:29:18', 2, 427);
INSERT INTO `tbl_product` (`name`, `description`, `price`, `img_url`, `dt_created`, `dt_updated`, `category_id`, `stock_quantity`) VALUES ('Natus', 'Eveniet nulla amet velit possimus repudiandae.', 82.36, 'https://dummyimage.com/76x777', '2025-04-14 19:29:18', '2025-04-14 19:29:18', 2, 417);
INSERT INTO `tbl_product` (`name`, `description`, `price`, `img_url`, `dt_created`, `dt_updated`, `category_id`, `stock_quantity`) VALUES ('Aspernatur', 'Assumenda ab dolorem eius voluptas unde suscipit necessitatibus.', 211.6, 'https://dummyimage.com/519x921', '2025-04-14 19:29:18', '2025-04-14 19:29:18', 2, 994);
INSERT INTO `tbl_product` (`name`, `description`, `price`, `img_url`, `dt_created`, `dt_updated`, `category_id`, `stock_quantity`) VALUES ('Quam', 'Ad eveniet pariatur illo numquam ratione aliquid.', 52.33, 'https://placekitten.com/260/315', '2025-04-14 19:29:18', '2025-04-14 19:29:18', 4, 580);
INSERT INTO `tbl_product` (`name`, `description`, `price`, `img_url`, `dt_created`, `dt_updated`, `category_id`, `stock_quantity`) VALUES ('Molestiae', 'Voluptas deserunt eaque quaerat.', 107.39, 'https://dummyimage.com/562x210', '2025-04-14 19:29:18', '2025-04-14 19:29:18', 4, 479);
INSERT INTO `tbl_product` (`name`, `description`, `price`, `img_url`, `dt_created`, `dt_updated`, `category_id`, `stock_quantity`) VALUES ('Porro', 'Autem voluptatum velit nam qui cum.', 429.63, 'https://placekitten.com/909/986', '2025-04-14 19:29:18', '2025-04-14 19:29:18', 2, 795);
INSERT INTO `tbl_product` (`name`, `description`, `price`, `img_url`, `dt_created`, `dt_updated`, `category_id`, `stock_quantity`) VALUES ('Facere', 'Tempore unde ut animi minus at ab.', 267.18, 'https://placekitten.com/37/976', '2025-04-14 19:29:18', '2025-04-14 19:29:18', 3, 677);
INSERT INTO `tbl_product` (`name`, `description`, `price`, `img_url`, `dt_created`, `dt_updated`, `category_id`, `stock_quantity`) VALUES ('Sapiente', 'Tenetur earum saepe dignissimos nostrum.', 395.05, 'https://picsum.photos/230/669', '2025-04-14 19:29:18', '2025-04-14 19:29:18', 4, 401);
INSERT INTO `tbl_product` (`name`, `description`, `price`, `img_url`, `dt_created`, `dt_updated`, `category_id`, `stock_quantity`) VALUES ('Maiores', 'In a esse vitae.', 321.18, 'https://placekitten.com/134/601', '2025-04-14 19:29:18', '2025-04-14 19:29:18', 2, 560);
INSERT INTO `tbl_product` (`name`, `description`, `price`, `img_url`, `dt_created`, `dt_updated`, `category_id`, `stock_quantity`) VALUES ('Officia', 'Maiores ipsam fugiat quos quia.', 106.41, 'https://placekitten.com/696/805', '2025-04-14 19:29:18', '2025-04-14 19:29:18', 4, 42);
INSERT INTO `tbl_product` (`name`, `description`, `price`, `img_url`, `dt_created`, `dt_updated`, `category_id`, `stock_quantity`) VALUES ('Iusto', 'Illo architecto voluptates illo.', 217.97, 'https://picsum.photos/602/165', '2025-04-14 19:29:18', '2025-04-14 19:29:18', 2, 757);
INSERT INTO `tbl_product` (`name`, `description`, `price`, `img_url`, `dt_created`, `dt_updated`, `category_id`, `stock_quantity`) VALUES ('Quas', 'Eaque aliquam hic quaerat esse.', 424.48, 'https://picsum.photos/864/568', '2025-04-14 19:29:18', '2025-04-14 19:29:18', 4, 976);
INSERT INTO `tbl_product` (`name`, `description`, `price`, `img_url`, `dt_created`, `dt_updated`, `category_id`, `stock_quantity`) VALUES ('Et', 'Dolore illum animi voluptates ducimus.', 74.86, 'https://placekitten.com/644/239', '2025-04-14 19:29:18', '2025-04-14 19:29:18', 2, 531);
INSERT INTO `tbl_product` (`name`, `description`, `price`, `img_url`, `dt_created`, `dt_updated`, `category_id`, `stock_quantity`) VALUES ('Quisquam', 'Impedit natus culpa vel rerum illo est.', 90.18, 'https://placekitten.com/650/422', '2025-04-14 19:29:18', '2025-04-14 19:29:18', 1, 677);
INSERT INTO `tbl_product` (`name`, `description`, `price`, `img_url`, `dt_created`, `dt_updated`, `category_id`, `stock_quantity`) VALUES ('Laborum', 'Modi eum debitis sapiente facilis.', 118.59, 'https://picsum.photos/11/632', '2025-04-14 19:29:18', '2025-04-14 19:29:18', 2, 946);
INSERT INTO `tbl_product` (`name`, `description`, `price`, `img_url`, `dt_created`, `dt_updated`, `category_id`, `stock_quantity`) VALUES ('Placeat', 'Nam placeat officiis consequatur.', 57.44, 'https://placekitten.com/689/755', '2025-04-14 19:29:18', '2025-04-14 19:29:18', 4, 372);
INSERT INTO `tbl_product` (`name`, `description`, `price`, `img_url`, `dt_created`, `dt_updated`, `category_id`, `stock_quantity`) VALUES ('Cumque', 'Nesciunt magni cumque.', 92.66, 'https://picsum.photos/575/641', '2025-04-14 19:29:18', '2025-04-14 19:29:18', 3, 197);
INSERT INTO `tbl_product` (`name`, `description`, `price`, `img_url`, `dt_created`, `dt_updated`, `category_id`, `stock_quantity`) VALUES ('Excepturi', 'Consequuntur provident at accusantium enim.', 403.55, 'https://placekitten.com/4/932', '2025-04-14 19:29:18', '2025-04-14 19:29:18', 1, 211);
INSERT INTO `tbl_product` (`name`, `description`, `price`, `img_url`, `dt_created`, `dt_updated`, `category_id`, `stock_quantity`) VALUES ('Alias', 'Cupiditate illo dolor nobis neque reiciendis enim.', 334.49, 'https://dummyimage.com/660x199', '2025-04-14 19:29:18', '2025-04-14 19:29:18', 1, 913);
INSERT INTO `tbl_product` (`name`, `description`, `price`, `img_url`, `dt_created`, `dt_updated`, `category_id`, `stock_quantity`) VALUES ('Officiis', 'Unde recusandae sequi culpa iste est quo.', 488.33, 'https://dummyimage.com/685x329', '2025-04-14 19:29:18', '2025-04-14 19:29:18', 2, 492);
INSERT INTO `tbl_product` (`name`, `description`, `price`, `img_url`, `dt_created`, `dt_updated`, `category_id`, `stock_quantity`) VALUES ('Aliquid', 'Veniam dolorem cum beatae.', 388.85, 'https://dummyimage.com/244x20', '2025-04-14 19:29:18', '2025-04-14 19:29:18', 3, 333);
INSERT INTO `tbl_product` (`name`, `description`, `price`, `img_url`, `dt_created`, `dt_updated`, `category_id`, `stock_quantity`) VALUES ('Voluptate', 'Ullam quibusdam iste veritatis enim ipsam.', 90.46, 'https://dummyimage.com/28x890', '2025-04-14 19:29:18', '2025-04-14 19:29:18', 3, 982);
INSERT INTO `tbl_product` (`name`, `description`, `price`, `img_url`, `dt_created`, `dt_updated`, `category_id`, `stock_quantity`) VALUES ('Quaerat', 'Illo consequatur voluptas eveniet.', 179.64, 'https://dummyimage.com/915x945', '2025-04-14 19:29:18', '2025-04-14 19:29:18', 4, 208);
INSERT INTO `tbl_product` (`name`, `description`, `price`, `img_url`, `dt_created`, `dt_updated`, `category_id`, `stock_quantity`) VALUES ('In', 'At molestiae officia minima ad amet minima.', 63.1, 'https://picsum.photos/831/350', '2025-04-14 19:29:18', '2025-04-14 19:29:18', 2, 546);
INSERT INTO `tbl_product` (`name`, `description`, `price`, `img_url`, `dt_created`, `dt_updated`, `category_id`, `stock_quantity`) VALUES ('Molestias', 'Deleniti illum culpa quidem dignissimos tenetur.', 478.73, 'https://dummyimage.com/857x171', '2025-04-14 19:29:18', '2025-04-14 19:29:18', 1, 812);
INSERT INTO `tbl_product` (`name`, `description`, `price`, `img_url`, `dt_created`, `dt_updated`, `category_id`, `stock_quantity`) VALUES ('Quod', 'Nulla quaerat corporis dolor saepe.', 425.24, 'https://picsum.photos/524/65', '2025-04-14 19:29:18', '2025-04-14 19:29:18', 2, 946);
INSERT INTO `tbl_product` (`name`, `description`, `price`, `img_url`, `dt_created`, `dt_updated`, `category_id`, `stock_quantity`) VALUES ('Neque', 'Deserunt est quaerat.', 448.54, 'https://dummyimage.com/686x539', '2025-04-14 19:29:18', '2025-04-14 19:29:18', 2, 892);
INSERT INTO `tbl_product` (`name`, `description`, `price`, `img_url`, `dt_created`, `dt_updated`, `category_id`, `stock_quantity`) VALUES ('Tempora', 'Suscipit molestiae minima accusantium.', 68.8, 'https://dummyimage.com/853x739', '2025-04-14 19:29:18', '2025-04-14 19:29:18', 3, 205);
INSERT INTO `tbl_product` (`name`, `description`, `price`, `img_url`, `dt_created`, `dt_updated`, `category_id`, `stock_quantity`) VALUES ('Voluptatum', 'Labore porro doloremque quidem blanditiis.', 159.76, 'https://picsum.photos/516/139', '2025-04-14 19:29:18', '2025-04-14 19:29:18', 1, 42);
INSERT INTO `tbl_product` (`name`, `description`, `price`, `img_url`, `dt_created`, `dt_updated`, `category_id`, `stock_quantity`) VALUES ('Pariatur', 'Cumque pariatur maiores.', 439.21, 'https://dummyimage.com/954x936', '2025-04-14 19:29:18', '2025-04-14 19:29:18', 1, 79);
INSERT INTO `tbl_product` (`name`, `description`, `price`, `img_url`, `dt_created`, `dt_updated`, `category_id`, `stock_quantity`) VALUES ('Odit', 'Modi reprehenderit sit aut nostrum laudantium accusamus.', 62.46, 'https://dummyimage.com/593x701', '2025-04-14 19:29:18', '2025-04-14 19:29:18', 3, 774);
INSERT INTO `tbl_product` (`name`, `description`, `price`, `img_url`, `dt_created`, `dt_updated`, `category_id`, `stock_quantity`) VALUES ('Nesciunt', 'Dolorum maiores ratione quae.', 87.1, 'https://picsum.photos/108/115', '2025-04-14 19:29:18', '2025-04-14 19:29:18', 1, 22);
INSERT INTO `tbl_product` (`name`, `description`, `price`, `img_url`, `dt_created`, `dt_updated`, `category_id`, `stock_quantity`) VALUES ('Inventore', 'Dolorem vitae impedit maiores facere dolores.', 339.65, 'https://picsum.photos/762/42', '2025-04-14 19:29:18', '2025-04-14 19:29:18', 2, 502);
INSERT INTO `tbl_product` (`name`, `description`, `price`, `img_url`, `dt_created`, `dt_updated`, `category_id`, `stock_quantity`) VALUES ('Quibusdam', 'Deserunt dolorem eveniet voluptatem eaque molestiae deleniti.', 455.08, 'https://picsum.photos/526/294', '2025-04-14 19:29:18', '2025-04-14 19:29:18', 4, 112);
INSERT INTO `tbl_product` (`name`, `description`, `price`, `img_url`, `dt_created`, `dt_updated`, `category_id`, `stock_quantity`) VALUES ('Dolorum', 'Doloremque recusandae hic impedit possimus.', 153.48, 'https://dummyimage.com/695x431', '2025-04-14 19:29:18', '2025-04-14 19:29:18', 2, 96);
INSERT INTO `tbl_product` (`name`, `description`, `price`, `img_url`, `dt_created`, `dt_updated`, `category_id`, `stock_quantity`) VALUES ('Maxime', 'Modi natus repellat eveniet voluptatem corrupti.', 370.32, 'https://dummyimage.com/16x494', '2025-04-14 19:29:18', '2025-04-14 19:29:18', 4, 155);
INSERT INTO `tbl_product` (`name`, `description`, `price`, `img_url`, `dt_created`, `dt_updated`, `category_id`, `stock_quantity`) VALUES ('Dignissimos', 'Voluptates rerum explicabo qui.', 71.52, 'https://picsum.photos/396/354', '2025-04-14 19:29:18', '2025-04-14 19:29:18', 3, 568);
INSERT INTO `tbl_product` (`name`, `description`, `price`, `img_url`, `dt_created`, `dt_updated`, `category_id`, `stock_quantity`) VALUES ('Laboriosam', 'Minima nesciunt aperiam enim sequi a.', 157.57, 'https://dummyimage.com/664x134', '2025-04-14 19:29:18', '2025-04-14 19:29:18', 1, 647);
INSERT INTO `tbl_product` (`name`, `description`, `price`, `img_url`, `dt_created`, `dt_updated`, `category_id`, `stock_quantity`) VALUES ('Accusantium', 'Rem molestias beatae ipsa quam inventore.', 368.24, 'https://picsum.photos/907/426', '2025-04-14 19:29:18', '2025-04-14 19:29:18', 3, 688);
INSERT INTO `tbl_product` (`name`, `description`, `price`, `img_url`, `dt_created`, `dt_updated`, `category_id`, `stock_quantity`) VALUES ('Assumenda', 'Magnam adipisci porro nihil ratione temporibus praesentium.', 96.0, 'https://dummyimage.com/536x670', '2025-04-14 19:29:18', '2025-04-14 19:29:18', 4, 340);
INSERT INTO `tbl_product` (`name`, `description`, `price`, `img_url`, `dt_created`, `dt_updated`, `category_id`, `stock_quantity`) VALUES ('Iure', 'Tempore numquam atque veritatis earum quod.', 425.57, 'https://placekitten.com/664/406', '2025-04-14 19:29:18', '2025-04-14 19:29:18', 1, 4);
INSERT INTO `tbl_product` (`name`, `description`, `price`, `img_url`, `dt_created`, `dt_updated`, `category_id`, `stock_quantity`) VALUES ('Minus', 'Quo voluptate magni dignissimos alias est libero voluptatem.', 458.85, 'https://dummyimage.com/504x794', '2025-04-14 19:29:18', '2025-04-14 19:29:18', 4, 758);
INSERT INTO `tbl_product` (`name`, `description`, `price`, `img_url`, `dt_created`, `dt_updated`, `category_id`, `stock_quantity`) VALUES ('Illum', 'Doloribus laborum illum eligendi.', 497.96, 'https://placekitten.com/20/329', '2025-04-14 19:29:18', '2025-04-14 19:29:18', 3, 486);
INSERT INTO `tbl_product` (`name`, `description`, `price`, `img_url`, `dt_created`, `dt_updated`, `category_id`, `stock_quantity`) VALUES ('Sunt', 'Porro omnis occaecati totam optio vero adipisci.', 210.76, 'https://placekitten.com/908/163', '2025-04-14 19:29:18', '2025-04-14 19:29:18', 1, 786);
INSERT INTO `tbl_product` (`name`, `description`, `price`, `img_url`, `dt_created`, `dt_updated`, `category_id`, `stock_quantity`) VALUES ('Necessitatibus', 'Excepturi deserunt minima.', 68.81, 'https://placekitten.com/382/352', '2025-04-14 19:29:18', '2025-04-14 19:29:18', 2, 864);
INSERT INTO `tbl_product` (`name`, `description`, `price`, `img_url`, `dt_created`, `dt_updated`, `category_id`, `stock_quantity`) VALUES ('Recusandae', 'Laborum delectus alias necessitatibus.', 56.79, 'https://picsum.photos/537/929', '2025-04-14 19:29:18', '2025-04-14 19:29:18', 2, 434);
INSERT INTO `tbl_product` (`name`, `description`, `price`, `img_url`, `dt_created`, `dt_updated`, `category_id`, `stock_quantity`) VALUES ('Deserunt', 'Recusandae libero dicta labore ea.', 235.56, 'https://picsum.photos/462/201', '2025-04-14 19:29:18', '2025-04-14 19:29:18', 3, 527);
INSERT INTO `tbl_product` (`name`, `description`, `price`, `img_url`, `dt_created`, `dt_updated`, `category_id`, `stock_quantity`) VALUES ('Ullam', 'Iure quo nemo beatae ex vel.', 158.44, 'https://picsum.photos/531/181', '2025-04-14 19:29:18', '2025-04-14 19:29:18', 3, 866);
INSERT INTO `tbl_product` (`name`, `description`, `price`, `img_url`, `dt_created`, `dt_updated`, `category_id`, `stock_quantity`) VALUES ('Perferendis', 'Nulla exercitationem vero veritatis cum.', 314.37, 'https://picsum.photos/1021/147', '2025-04-14 19:29:18', '2025-04-14 19:29:18', 1, 415);
INSERT INTO `tbl_product` (`name`, `description`, `price`, `img_url`, `dt_created`, `dt_updated`, `category_id`, `stock_quantity`) VALUES ('Magnam', 'Maxime excepturi magnam amet.', 230.01, 'https://dummyimage.com/613x154', '2025-04-14 19:29:18', '2025-04-14 19:29:18', 4, 608);
INSERT INTO `tbl_product` (`name`, `description`, `price`, `img_url`, `dt_created`, `dt_updated`, `category_id`, `stock_quantity`) VALUES ('Eveniet', 'Corporis commodi illum esse corrupti.', 421.47, 'https://placekitten.com/720/213', '2025-04-14 19:29:18', '2025-04-14 19:29:18', 4, 121);
INSERT INTO `tbl_product` (`name`, `description`, `price`, `img_url`, `dt_created`, `dt_updated`, `category_id`, `stock_quantity`) VALUES ('Nulla', 'Alias quasi hic tempore sequi fugiat.', 382.22, 'https://placekitten.com/29/855', '2025-04-14 19:29:18', '2025-04-14 19:29:18', 3, 882);
INSERT INTO `tbl_product` (`name`, `description`, `price`, `img_url`, `dt_created`, `dt_updated`, `category_id`, `stock_quantity`) VALUES ('Nobis', 'Molestias assumenda voluptatibus cum.', 156.12, 'https://placekitten.com/485/551', '2025-04-14 19:29:18', '2025-04-14 19:29:18', 4, 108);
INSERT INTO `tbl_product` (`name`, `description`, `price`, `img_url`, `dt_created`, `dt_updated`, `category_id`, `stock_quantity`) VALUES ('Magni', 'Repellat atque laudantium incidunt quos dicta.', 416.16, 'https://dummyimage.com/874x356', '2025-04-14 19:29:18', '2025-04-14 19:29:18', 1, 396);
INSERT INTO `tbl_product` (`name`, `description`, `price`, `img_url`, `dt_created`, `dt_updated`, `category_id`, `stock_quantity`) VALUES ('Ab', 'Repellendus distinctio sit tempora nulla rerum et ut.', 415.43, 'https://dummyimage.com/852x449', '2025-04-14 19:29:18', '2025-04-14 19:29:18', 2, 429);
INSERT INTO `tbl_product` (`name`, `description`, `price`, `img_url`, `dt_created`, `dt_updated`, `category_id`, `stock_quantity`) VALUES ('Ex', 'Numquam facilis asperiores sed reprehenderit tempore ad.', 52.88, 'https://placekitten.com/771/100', '2025-04-14 19:29:18', '2025-04-14 19:29:18', 4, 220);
INSERT INTO `tbl_product` (`name`, `description`, `price`, `img_url`, `dt_created`, `dt_updated`, `category_id`, `stock_quantity`) VALUES ('Debitis', 'Deleniti quasi accusamus iusto dolores debitis reprehenderit.', 361.44, 'https://picsum.photos/657/454', '2025-04-14 19:29:18', '2025-04-14 19:29:18', 3, 480);
INSERT INTO `tbl_product` (`name`, `description`, `price`, `img_url`, `dt_created`, `dt_updated`, `category_id`, `stock_quantity`) VALUES ('Corrupti', 'Repudiandae architecto dolorum labore accusantium delectus.', 492.89, 'https://dummyimage.com/31x147', '2025-04-14 19:29:18', '2025-04-14 19:29:18', 4, 504);
INSERT INTO `tbl_product` (`name`, `description`, `price`, `img_url`, `dt_created`, `dt_updated`, `category_id`, `stock_quantity`) VALUES ('Id', 'Sint reprehenderit quisquam sint esse quo ea.', 214.69, 'https://placekitten.com/605/404', '2025-04-14 19:29:18', '2025-04-14 19:29:18', 3, 670);
INSERT INTO `tbl_product` (`name`, `description`, `price`, `img_url`, `dt_created`, `dt_updated`, `category_id`, `stock_quantity`) VALUES ('A', 'Dolor voluptas fuga.', 374.74, 'https://picsum.photos/357/467', '2025-04-14 19:29:18', '2025-04-14 19:29:18', 4, 608);
INSERT INTO `tbl_product` (`name`, `description`, `price`, `img_url`, `dt_created`, `dt_updated`, `category_id`, `stock_quantity`) VALUES ('Nam', 'Nesciunt porro nesciunt veniam itaque mollitia sint quas.', 401.39, 'https://placekitten.com/461/204', '2025-04-14 19:29:18', '2025-04-14 19:29:18', 3, 595);
INSERT INTO `tbl_product` (`name`, `description`, `price`, `img_url`, `dt_created`, `dt_updated`, `category_id`, `stock_quantity`) VALUES ('Beatae', 'Fuga ducimus similique esse quo commodi expedita sint.', 323.12, 'https://picsum.photos/532/941', '2025-04-14 19:29:18', '2025-04-14 19:29:18', 3, 285);
INSERT INTO `tbl_product` (`name`, `description`, `price`, `img_url`, `dt_created`, `dt_updated`, `category_id`, `stock_quantity`) VALUES ('Distinctio', 'Laudantium omnis dolore ab praesentium ut perferendis reiciendis.', 388.24, 'https://picsum.photos/392/79', '2025-04-14 19:29:18', '2025-04-14 19:29:18', 3, 3);
INSERT INTO `tbl_product` (`name`, `description`, `price`, `img_url`, `dt_created`, `dt_updated`, `category_id`, `stock_quantity`) VALUES ('Dolore', 'Nesciunt sit reprehenderit sit laboriosam.', 422.62, 'https://dummyimage.com/396x37', '2025-04-14 19:29:18', '2025-04-14 19:29:18', 3, 280);
INSERT INTO `tbl_product` (`name`, `description`, `price`, `img_url`, `dt_created`, `dt_updated`, `category_id`, `stock_quantity`) VALUES ('Facilis', 'Autem fugiat beatae sapiente.', 205.57, 'https://dummyimage.com/58x705', '2025-04-14 19:29:18', '2025-04-14 19:29:18', 2, 689);
INSERT INTO `tbl_product` (`name`, `description`, `price`, `img_url`, `dt_created`, `dt_updated`, `category_id`, `stock_quantity`) VALUES ('Reiciendis', 'Quisquam at quas officiis delectus porro repellendus eligendi.', 134.62, 'https://dummyimage.com/917x420', '2025-04-14 19:29:18', '2025-04-14 19:29:18', 2, 885);
INSERT INTO `tbl_product` (`name`, `description`, `price`, `img_url`, `dt_created`, `dt_updated`, `category_id`, `stock_quantity`) VALUES ('Eum', 'Nisi officia eaque esse autem doloremque.', 380.3, 'https://placekitten.com/470/414', '2025-04-14 19:29:18', '2025-04-14 19:29:18', 2, 641);
INSERT INTO `tbl_product` (`name`, `description`, `price`, `img_url`, `dt_created`, `dt_updated`, `category_id`, `stock_quantity`) VALUES ('Amet', 'Alias velit dolores aliquam.', 454.38, 'https://picsum.photos/12/262', '2025-04-14 19:29:18', '2025-04-14 19:29:18', 4, 102);
INSERT INTO `tbl_product` (`name`, `description`, `price`, `img_url`, `dt_created`, `dt_updated`, `category_id`, `stock_quantity`) VALUES ('Possimus', 'Vero tempora esse vel iusto voluptates nihil possimus.', 151.56, 'https://picsum.photos/752/155', '2025-04-14 19:29:18', '2025-04-14 19:29:18', 2, 551);
INSERT INTO `tbl_product` (`name`, `description`, `price`, `img_url`, `dt_created`, `dt_updated`, `category_id`, `stock_quantity`) VALUES ('Ipsum', 'Assumenda distinctio nulla voluptates natus magnam soluta.', 96.04, 'https://placekitten.com/966/858', '2025-04-14 19:29:18', '2025-04-14 19:29:18', 1, 923);
INSERT INTO `tbl_product` (`name`, `description`, `price`, `img_url`, `dt_created`, `dt_updated`, `category_id`, `stock_quantity`) VALUES ('Fugiat', 'Cumque pariatur fugit.', 271.3, 'https://placekitten.com/714/233', '2025-04-14 19:29:18', '2025-04-14 19:29:18', 4, 104);
INSERT INTO `tbl_product` (`name`, `description`, `price`, `img_url`, `dt_created`, `dt_updated`, `category_id`, `stock_quantity`) VALUES ('At', 'Id numquam ad inventore ea laudantium.', 299.57, 'https://placekitten.com/4/672', '2025-04-14 19:29:18', '2025-04-14 19:29:18', 3, 56);
INSERT INTO `tbl_product` (`name`, `description`, `price`, `img_url`, `dt_created`, `dt_updated`, `category_id`, `stock_quantity`) VALUES ('Accusamus', 'Maxime numquam tempore quidem.', 234.23, 'https://placekitten.com/822/888', '2025-04-14 19:29:18', '2025-04-14 19:29:18', 4, 46);
INSERT INTO `tbl_product` (`name`, `description`, `price`, `img_url`, `dt_created`, `dt_updated`, `category_id`, `stock_quantity`) VALUES ('Cupiditate', 'Recusandae beatae quas.', 90.06, 'https://picsum.photos/246/489', '2025-04-14 19:29:18', '2025-04-14 19:29:18', 4, 389);
INSERT INTO `tbl_product` (`name`, `description`, `price`, `img_url`, `dt_created`, `dt_updated`, `category_id`, `stock_quantity`) VALUES ('Modi', 'Tempore numquam non cupiditate suscipit sunt possimus.', 163.89, 'https://picsum.photos/542/81', '2025-04-14 19:29:18', '2025-04-14 19:29:18', 1, 447);
INSERT INTO `tbl_product` (`name`, `description`, `price`, `img_url`, `dt_created`, `dt_updated`, `category_id`, `stock_quantity`) VALUES ('Sed', 'Delectus velit in.', 119.08, 'https://placekitten.com/426/570', '2025-04-14 19:29:18', '2025-04-14 19:29:18', 4, 283);
INSERT INTO `tbl_product` (`name`, `description`, `price`, `img_url`, `dt_created`, `dt_updated`, `category_id`, `stock_quantity`) VALUES ('Veritatis', 'Cupiditate ipsam facilis magni.', 489.11, 'https://picsum.photos/444/673', '2025-04-14 19:29:18', '2025-04-14 19:29:18', 2, 815);
INSERT INTO `tbl_product` (`name`, `description`, `price`, `img_url`, `dt_created`, `dt_updated`, `category_id`, `stock_quantity`) VALUES ('Consectetur', 'Dignissimos recusandae et repellat.', 107.76, 'https://dummyimage.com/134x1023', '2025-04-14 19:29:18', '2025-04-14 19:29:18', 2, 176);
INSERT INTO `tbl_product` (`name`, `description`, `price`, `img_url`, `dt_created`, `dt_updated`, `category_id`, `stock_quantity`) VALUES ('Sequi', 'Necessitatibus reiciendis suscipit.', 205.39, 'https://dummyimage.com/58x805', '2025-04-14 19:29:18', '2025-04-14 19:29:18', 1, 789);
INSERT INTO `tbl_product` (`name`, `description`, `price`, `img_url`, `dt_created`, `dt_updated`, `category_id`, `stock_quantity`) VALUES ('Eos', 'Dignissimos hic qui quos et accusamus.', 349.66, 'https://dummyimage.com/782x369', '2025-04-14 19:29:18', '2025-04-14 19:29:18', 3, 826);
INSERT INTO `tbl_product` (`name`, `description`, `price`, `img_url`, `dt_created`, `dt_updated`, `category_id`, `stock_quantity`) VALUES ('Similique', 'Ad neque laudantium quasi.', 316.68, 'https://dummyimage.com/134x781', '2025-04-14 19:29:18', '2025-04-14 19:29:18', 3, 567);
INSERT INTO `tbl_product` (`name`, `description`, `price`, `img_url`, `dt_created`, `dt_updated`, `category_id`, `stock_quantity`) VALUES ('Dolorem', 'Voluptates molestias cupiditate.', 120.03, 'https://dummyimage.com/357x919', '2025-04-14 19:29:18', '2025-04-14 19:29:18', 3, 84);
INSERT INTO `tbl_product` (`name`, `description`, `price`, `img_url`, `dt_created`, `dt_updated`, `category_id`, `stock_quantity`) VALUES ('Libero', 'Nihil voluptas unde quo distinctio magnam molestiae.', 416.57, 'https://picsum.photos/655/588', '2025-04-14 19:29:18', '2025-04-14 19:29:18', 1, 417);
INSERT INTO `tbl_product` (`name`, `description`, `price`, `img_url`, `dt_created`, `dt_updated`, `category_id`, `stock_quantity`) VALUES ('Culpa', 'Amet iure a maiores cupiditate expedita aperiam.', 263.47, 'https://picsum.photos/1/242', '2025-04-14 19:29:18', '2025-04-14 19:29:18', 3, 582);
INSERT INTO `tbl_product` (`name`, `description`, `price`, `img_url`, `dt_created`, `dt_updated`, `category_id`, `stock_quantity`) VALUES ('Veniam', 'Velit illum nihil quae maiores quidem.', 167.31, 'https://dummyimage.com/560x22', '2025-04-14 19:29:18', '2025-04-14 19:29:18', 1, 702);
INSERT INTO `tbl_product` (`name`, `description`, `price`, `img_url`, `dt_created`, `dt_updated`, `category_id`, `stock_quantity`) VALUES ('Autem', 'Quam voluptatem amet consequuntur quis hic architecto.', 265.5, 'https://picsum.photos/803/638', '2025-04-14 19:29:18', '2025-04-14 19:29:18', 3, 670);
INSERT INTO `tbl_product` (`name`, `description`, `price`, `img_url`, `dt_created`, `dt_updated`, `category_id`, `stock_quantity`) VALUES ('Totam', 'Fuga vero laboriosam ea rerum corporis.', 287.03, 'https://dummyimage.com/98x349', '2025-04-14 19:29:18', '2025-04-14 19:29:18', 1, 766);
INSERT INTO `tbl_product` (`name`, `description`, `price`, `img_url`, `dt_created`, `dt_updated`, `category_id`, `stock_quantity`) VALUES ('Deleniti', 'Quas nobis repellat repudiandae itaque.', 304.04, 'https://placekitten.com/439/465', '2025-04-14 19:29:18', '2025-04-14 19:29:18', 4, 784);
INSERT INTO `tbl_product` (`name`, `description`, `price`, `img_url`, `dt_created`, `dt_updated`, `category_id`, `stock_quantity`) VALUES ('Nisi', 'Quasi hic eveniet vero voluptate porro.', 301.02, 'https://placekitten.com/362/858', '2025-04-14 19:29:18', '2025-04-14 19:29:18', 3, 19);
INSERT INTO `tbl_product` (`name`, `description`, `price`, `img_url`, `dt_created`, `dt_updated`, `category_id`, `stock_quantity`) VALUES ('Aut', 'Vel aliquid voluptatum beatae harum accusantium explicabo.', 161.41, 'https://placekitten.com/40/366', '2025-04-14 19:29:18', '2025-04-14 19:29:18', 2, 609);
INSERT INTO `tbl_product` (`name`, `description`, `price`, `img_url`, `dt_created`, `dt_updated`, `category_id`, `stock_quantity`) VALUES ('Labore', 'Optio aliquam quam veniam pariatur voluptate animi aliquid.', 225.14, 'https://picsum.photos/866/181', '2025-04-14 19:29:18', '2025-04-14 19:29:18', 3, 866);
