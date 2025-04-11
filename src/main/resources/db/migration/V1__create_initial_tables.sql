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
INSERT INTO `tbl_product` (`name`, `description`, `price`, `img_url`, `dt_created`, `dt_updated`, `category_id`) VALUES ('Exercitationem', 'Enim odio hic laudantium.', 175.64, 'https://placekitten.com/922/90', '2025-04-10 14:50:44', '2025-04-10 14:50:44', 4);
INSERT INTO `tbl_product` (`name`, `description`, `price`, `img_url`, `dt_created`, `dt_updated`, `category_id`) VALUES ('Reiciendis', 'Temporibus aliquid harum a.', 263.38, 'https://placekitten.com/482/312', '2025-04-10 14:50:44', '2025-04-10 14:50:44', 1);
INSERT INTO `tbl_product` (`name`, `description`, `price`, `img_url`, `dt_created`, `dt_updated`, `category_id`) VALUES ('Minima', 'Ducimus eum maiores illum enim.', 74.09, 'https://placekitten.com/756/712', '2025-04-10 14:50:44', '2025-04-10 14:50:44', 1);
INSERT INTO `tbl_product` (`name`, `description`, `price`, `img_url`, `dt_created`, `dt_updated`, `category_id`) VALUES ('Aliquid', 'Distinctio maiores eos optio.', 307.82, 'https://picsum.photos/253/627', '2025-04-10 14:50:44', '2025-04-10 14:50:44', 2);
INSERT INTO `tbl_product` (`name`, `description`, `price`, `img_url`, `dt_created`, `dt_updated`, `category_id`) VALUES ('Deleniti', 'Enim architecto voluptates quaerat incidunt nihil.', 465.4, 'https://picsum.photos/2/766', '2025-04-10 14:50:44', '2025-04-10 14:50:44', 2);
INSERT INTO `tbl_product` (`name`, `description`, `price`, `img_url`, `dt_created`, `dt_updated`, `category_id`) VALUES ('Dolor', 'Illum voluptas deleniti harum laudantium blanditiis ea.', 104.22, 'https://placekitten.com/362/966', '2025-04-10 14:50:44', '2025-04-10 14:50:44', 1);
INSERT INTO `tbl_product` (`name`, `description`, `price`, `img_url`, `dt_created`, `dt_updated`, `category_id`) VALUES ('Maiores', 'Animi beatae nam rem voluptas nostrum cumque.', 285.53, 'https://placekitten.com/887/434', '2025-04-10 14:50:44', '2025-04-10 14:50:44', 4);
INSERT INTO `tbl_product` (`name`, `description`, `price`, `img_url`, `dt_created`, `dt_updated`, `category_id`) VALUES ('Voluptate', 'Voluptas libero reprehenderit quia blanditiis illum.', 316.13, 'https://placekitten.com/665/231', '2025-04-10 14:50:44', '2025-04-10 14:50:44', 3);
INSERT INTO `tbl_product` (`name`, `description`, `price`, `img_url`, `dt_created`, `dt_updated`, `category_id`) VALUES ('Saepe', 'Delectus corporis aut accusantium.', 117.99, 'https://placekitten.com/928/457', '2025-04-10 14:50:44', '2025-04-10 14:50:44', 1);
INSERT INTO `tbl_product` (`name`, `description`, `price`, `img_url`, `dt_created`, `dt_updated`, `category_id`) VALUES ('Labore', 'Dolor nobis nemo modi possimus eligendi quos.', 470.15, 'https://picsum.photos/597/873', '2025-04-10 14:50:44', '2025-04-10 14:50:44', 1);
INSERT INTO `tbl_product` (`name`, `description`, `price`, `img_url`, `dt_created`, `dt_updated`, `category_id`) VALUES ('Dolorum', 'Optio exercitationem error exercitationem delectus voluptas dolorum rerum.', 178.82, 'https://picsum.photos/175/403', '2025-04-10 14:50:44', '2025-04-10 14:50:44', 2);
INSERT INTO `tbl_product` (`name`, `description`, `price`, `img_url`, `dt_created`, `dt_updated`, `category_id`) VALUES ('Aspernatur', 'Sint aspernatur quidem deserunt reprehenderit.', 162.11, 'https://picsum.photos/336/556', '2025-04-10 14:50:44', '2025-04-10 14:50:44', 4);
INSERT INTO `tbl_product` (`name`, `description`, `price`, `img_url`, `dt_created`, `dt_updated`, `category_id`) VALUES ('Aperiam', 'Porro molestias optio dolore dolore blanditiis.', 345.16, 'https://picsum.photos/387/208', '2025-04-10 14:50:44', '2025-04-10 14:50:44', 4);
INSERT INTO `tbl_product` (`name`, `description`, `price`, `img_url`, `dt_created`, `dt_updated`, `category_id`) VALUES ('Perferendis', 'Repellendus assumenda ipsam iusto vitae debitis earum.', 214.7, 'https://picsum.photos/628/798', '2025-04-10 14:50:44', '2025-04-10 14:50:44', 1);
INSERT INTO `tbl_product` (`name`, `description`, `price`, `img_url`, `dt_created`, `dt_updated`, `category_id`) VALUES ('Delectus', 'Est vitae neque tempore saepe.', 185.94, 'https://picsum.photos/393/365', '2025-04-10 14:50:44', '2025-04-10 14:50:44', 1);
INSERT INTO `tbl_product` (`name`, `description`, `price`, `img_url`, `dt_created`, `dt_updated`, `category_id`) VALUES ('Possimus', 'Eius similique quis labore error.', 392.83, 'https://dummyimage.com/39x512', '2025-04-10 14:50:44', '2025-04-10 14:50:44', 2);
INSERT INTO `tbl_product` (`name`, `description`, `price`, `img_url`, `dt_created`, `dt_updated`, `category_id`) VALUES ('Impedit', 'Corrupti velit impedit maiores.', 419.48, 'https://picsum.photos/878/36', '2025-04-10 14:50:44', '2025-04-10 14:50:44', 2);
INSERT INTO `tbl_product` (`name`, `description`, `price`, `img_url`, `dt_created`, `dt_updated`, `category_id`) VALUES ('Temporibus', 'Velit velit praesentium amet facilis est modi.', 99.24, 'https://placekitten.com/847/55', '2025-04-10 14:50:44', '2025-04-10 14:50:44', 1);
INSERT INTO `tbl_product` (`name`, `description`, `price`, `img_url`, `dt_created`, `dt_updated`, `category_id`) VALUES ('Est', 'Iure suscipit labore modi quaerat numquam incidunt.', 431.71, 'https://placekitten.com/518/778', '2025-04-10 14:50:44', '2025-04-10 14:50:44', 1);
INSERT INTO `tbl_product` (`name`, `description`, `price`, `img_url`, `dt_created`, `dt_updated`, `category_id`) VALUES ('Repellendus', 'Nihil qui ex commodi dolorum.', 126.47, 'https://placekitten.com/370/278', '2025-04-10 14:50:44', '2025-04-10 14:50:44', 1);
INSERT INTO `tbl_product` (`name`, `description`, `price`, `img_url`, `dt_created`, `dt_updated`, `category_id`) VALUES ('Quisquam', 'Culpa veritatis adipisci officiis quod.', 263.4, 'https://dummyimage.com/740x379', '2025-04-10 14:50:44', '2025-04-10 14:50:44', 4);
INSERT INTO `tbl_product` (`name`, `description`, `price`, `img_url`, `dt_created`, `dt_updated`, `category_id`) VALUES ('Suscipit', 'Deleniti esse fugit hic repudiandae incidunt.', 380.39, 'https://picsum.photos/385/146', '2025-04-10 14:50:44', '2025-04-10 14:50:44', 1);
INSERT INTO `tbl_product` (`name`, `description`, `price`, `img_url`, `dt_created`, `dt_updated`, `category_id`) VALUES ('Qui', 'Tempore quo eum laborum inventore.', 197.43, 'https://picsum.photos/811/963', '2025-04-10 14:50:44', '2025-04-10 14:50:44', 2);
INSERT INTO `tbl_product` (`name`, `description`, `price`, `img_url`, `dt_created`, `dt_updated`, `category_id`) VALUES ('Mollitia', 'Voluptatibus molestias quo nostrum assumenda ducimus.', 154.52, 'https://placekitten.com/457/626', '2025-04-10 14:50:44', '2025-04-10 14:50:44', 4);
INSERT INTO `tbl_product` (`name`, `description`, `price`, `img_url`, `dt_created`, `dt_updated`, `category_id`) VALUES ('Natus', 'Consequuntur ipsa atque.', 280.54, 'https://picsum.photos/369/238', '2025-04-10 14:50:44', '2025-04-10 14:50:44', 3);
INSERT INTO `tbl_product` (`name`, `description`, `price`, `img_url`, `dt_created`, `dt_updated`, `category_id`) VALUES ('Laudantium', 'Error cum sint nisi cum.', 469.92, 'https://picsum.photos/377/992', '2025-04-10 14:50:44', '2025-04-10 14:50:44', 4);
INSERT INTO `tbl_product` (`name`, `description`, `price`, `img_url`, `dt_created`, `dt_updated`, `category_id`) VALUES ('Tenetur', 'Nam consequatur minus dolor esse.', 236.92, 'https://dummyimage.com/693x589', '2025-04-10 14:50:44', '2025-04-10 14:50:44', 3);
INSERT INTO `tbl_product` (`name`, `description`, `price`, `img_url`, `dt_created`, `dt_updated`, `category_id`) VALUES ('Eius', 'Cum illo facere accusantium.', 457.52, 'https://dummyimage.com/384x471', '2025-04-10 14:50:44', '2025-04-10 14:50:44', 1);
INSERT INTO `tbl_product` (`name`, `description`, `price`, `img_url`, `dt_created`, `dt_updated`, `category_id`) VALUES ('Voluptatibus', 'Error velit atque architecto.', 285.11, 'https://placekitten.com/278/284', '2025-04-10 14:50:44', '2025-04-10 14:50:44', 3);
INSERT INTO `tbl_product` (`name`, `description`, `price`, `img_url`, `dt_created`, `dt_updated`, `category_id`) VALUES ('Facilis', 'Saepe soluta quae natus culpa aliquid laboriosam ut.', 213.0, 'https://picsum.photos/426/116', '2025-04-10 14:50:44', '2025-04-10 14:50:44', 1);
INSERT INTO `tbl_product` (`name`, `description`, `price`, `img_url`, `dt_created`, `dt_updated`, `category_id`) VALUES ('Cumque', 'Nemo dignissimos veniam ea quidem aperiam dolore.', 381.48, 'https://picsum.photos/181/130', '2025-04-10 14:50:44', '2025-04-10 14:50:44', 1);
INSERT INTO `tbl_product` (`name`, `description`, `price`, `img_url`, `dt_created`, `dt_updated`, `category_id`) VALUES ('Expedita', 'Ipsum culpa sunt ducimus ad neque quibusdam odit.', 394.83, 'https://picsum.photos/89/239', '2025-04-10 14:50:44', '2025-04-10 14:50:44', 3);
INSERT INTO `tbl_product` (`name`, `description`, `price`, `img_url`, `dt_created`, `dt_updated`, `category_id`) VALUES ('Aliquam', 'Sunt laboriosam ex totam quam ut.', 220.2, 'https://picsum.photos/904/406', '2025-04-10 14:50:44', '2025-04-10 14:50:44', 1);
INSERT INTO `tbl_product` (`name`, `description`, `price`, `img_url`, `dt_created`, `dt_updated`, `category_id`) VALUES ('Dolores', 'Repudiandae quasi fugiat sapiente iure.', 260.99, 'https://dummyimage.com/333x178', '2025-04-10 14:50:44', '2025-04-10 14:50:44', 1);
INSERT INTO `tbl_product` (`name`, `description`, `price`, `img_url`, `dt_created`, `dt_updated`, `category_id`) VALUES ('Deserunt', 'Esse expedita voluptas soluta recusandae amet.', 473.93, 'https://picsum.photos/639/986', '2025-04-10 14:50:44', '2025-04-10 14:50:44', 4);
INSERT INTO `tbl_product` (`name`, `description`, `price`, `img_url`, `dt_created`, `dt_updated`, `category_id`) VALUES ('Molestiae', 'Voluptatibus sit repellat perferendis earum enim.', 226.87, 'https://placekitten.com/995/364', '2025-04-10 14:50:44', '2025-04-10 14:50:44', 3);
INSERT INTO `tbl_product` (`name`, `description`, `price`, `img_url`, `dt_created`, `dt_updated`, `category_id`) VALUES ('Nihil', 'Corporis reprehenderit est eveniet repudiandae.', 430.0, 'https://placekitten.com/309/384', '2025-04-10 14:50:44', '2025-04-10 14:50:44', 2);
INSERT INTO `tbl_product` (`name`, `description`, `price`, `img_url`, `dt_created`, `dt_updated`, `category_id`) VALUES ('Ipsa', 'Placeat minima pariatur nulla labore itaque alias.', 462.46, 'https://dummyimage.com/570x700', '2025-04-10 14:50:44', '2025-04-10 14:50:44', 4);
INSERT INTO `tbl_product` (`name`, `description`, `price`, `img_url`, `dt_created`, `dt_updated`, `category_id`) VALUES ('Quibusdam', 'Minima molestiae voluptatem quidem ab magnam.', 135.29, 'https://placekitten.com/221/974', '2025-04-10 14:50:44', '2025-04-10 14:50:44', 3);
INSERT INTO `tbl_product` (`name`, `description`, `price`, `img_url`, `dt_created`, `dt_updated`, `category_id`) VALUES ('Recusandae', 'Esse ipsa corporis nisi delectus.', 301.22, 'https://picsum.photos/399/615', '2025-04-10 14:50:44', '2025-04-10 14:50:44', 1);
INSERT INTO `tbl_product` (`name`, `description`, `price`, `img_url`, `dt_created`, `dt_updated`, `category_id`) VALUES ('Sequi', 'Quod officia maiores mollitia eos impedit neque.', 460.81, 'https://picsum.photos/172/895', '2025-04-10 14:50:44', '2025-04-10 14:50:44', 4);
INSERT INTO `tbl_product` (`name`, `description`, `price`, `img_url`, `dt_created`, `dt_updated`, `category_id`) VALUES ('Vitae', 'Dignissimos praesentium at numquam nam vitae minus.', 100.35, 'https://placekitten.com/673/930', '2025-04-10 14:50:44', '2025-04-10 14:50:44', 3);
INSERT INTO `tbl_product` (`name`, `description`, `price`, `img_url`, `dt_created`, `dt_updated`, `category_id`) VALUES ('Nam', 'Quasi nesciunt vero omnis corrupti quia.', 343.35, 'https://picsum.photos/665/786', '2025-04-10 14:50:44', '2025-04-10 14:50:44', 3);
INSERT INTO `tbl_product` (`name`, `description`, `price`, `img_url`, `dt_created`, `dt_updated`, `category_id`) VALUES ('Occaecati', 'Non commodi quod reiciendis eaque voluptatibus.', 74.17, 'https://placekitten.com/638/400', '2025-04-10 14:50:44', '2025-04-10 14:50:44', 2);
INSERT INTO `tbl_product` (`name`, `description`, `price`, `img_url`, `dt_created`, `dt_updated`, `category_id`) VALUES ('At', 'Placeat dolores voluptatem deserunt atque.', 152.92, 'https://picsum.photos/202/80', '2025-04-10 14:50:44', '2025-04-10 14:50:44', 4);
INSERT INTO `tbl_product` (`name`, `description`, `price`, `img_url`, `dt_created`, `dt_updated`, `category_id`) VALUES ('Alias', 'Sequi laboriosam incidunt eos quibusdam mollitia laudantium.', 433.85, 'https://picsum.photos/252/68', '2025-04-10 14:50:44', '2025-04-10 14:50:44', 2);
INSERT INTO `tbl_product` (`name`, `description`, `price`, `img_url`, `dt_created`, `dt_updated`, `category_id`) VALUES ('Eveniet', 'Eaque labore aliquam voluptatibus ipsa.', 374.09, 'https://dummyimage.com/909x336', '2025-04-10 14:50:44', '2025-04-10 14:50:44', 3);
INSERT INTO `tbl_product` (`name`, `description`, `price`, `img_url`, `dt_created`, `dt_updated`, `category_id`) VALUES ('Blanditiis', 'Blanditiis iure quibusdam nisi.', 118.69, 'https://placekitten.com/151/690', '2025-04-10 14:50:44', '2025-04-10 14:50:44', 2);
INSERT INTO `tbl_product` (`name`, `description`, `price`, `img_url`, `dt_created`, `dt_updated`, `category_id`) VALUES ('Nobis', 'Blanditiis inventore asperiores corrupti officiis harum corporis.', 125.36, 'https://picsum.photos/305/733', '2025-04-10 14:50:44', '2025-04-10 14:50:44', 3);
INSERT INTO `tbl_product` (`name`, `description`, `price`, `img_url`, `dt_created`, `dt_updated`, `category_id`) VALUES ('Provident', 'Quo facilis earum debitis odio.', 66.66, 'https://dummyimage.com/357x14', '2025-04-10 14:50:44', '2025-04-10 14:50:44', 1);
INSERT INTO `tbl_product` (`name`, `description`, `price`, `img_url`, `dt_created`, `dt_updated`, `category_id`) VALUES ('Eaque', 'Totam quis velit voluptatibus porro corporis.', 259.41, 'https://picsum.photos/38/412', '2025-04-10 14:50:44', '2025-04-10 14:50:44', 4);
INSERT INTO `tbl_product` (`name`, `description`, `price`, `img_url`, `dt_created`, `dt_updated`, `category_id`) VALUES ('Tempora', 'Cumque voluptate voluptate ea.', 485.3, 'https://placekitten.com/204/629', '2025-04-10 14:50:44', '2025-04-10 14:50:44', 2);
INSERT INTO `tbl_product` (`name`, `description`, `price`, `img_url`, `dt_created`, `dt_updated`, `category_id`) VALUES ('Minus', 'Incidunt nulla minima illo eius dolorum rem.', 69.68, 'https://picsum.photos/544/635', '2025-04-10 14:50:44', '2025-04-10 14:50:44', 3);
INSERT INTO `tbl_product` (`name`, `description`, `price`, `img_url`, `dt_created`, `dt_updated`, `category_id`) VALUES ('Similique', 'Maxime neque quaerat nostrum.', 241.38, 'https://picsum.photos/342/383', '2025-04-10 14:50:44', '2025-04-10 14:50:44', 4);
INSERT INTO `tbl_product` (`name`, `description`, `price`, `img_url`, `dt_created`, `dt_updated`, `category_id`) VALUES ('Amet', 'Placeat odit error dolorem.', 90.95, 'https://dummyimage.com/692x594', '2025-04-10 14:50:44', '2025-04-10 14:50:44', 4);
INSERT INTO `tbl_product` (`name`, `description`, `price`, `img_url`, `dt_created`, `dt_updated`, `category_id`) VALUES ('Incidunt', 'Natus exercitationem suscipit possimus.', 198.53, 'https://dummyimage.com/427x746', '2025-04-10 14:50:44', '2025-04-10 14:50:44', 1);
INSERT INTO `tbl_product` (`name`, `description`, `price`, `img_url`, `dt_created`, `dt_updated`, `category_id`) VALUES ('Tempore', 'Eaque molestias sunt cum reiciendis nemo quibusdam recusandae.', 424.28, 'https://placekitten.com/698/670', '2025-04-10 14:50:44', '2025-04-10 14:50:44', 4);
INSERT INTO `tbl_product` (`name`, `description`, `price`, `img_url`, `dt_created`, `dt_updated`, `category_id`) VALUES ('Magni', 'Nulla eaque a cum autem maiores.', 482.62, 'https://picsum.photos/828/602', '2025-04-10 14:50:44', '2025-04-10 14:50:44', 2);
INSERT INTO `tbl_product` (`name`, `description`, `price`, `img_url`, `dt_created`, `dt_updated`, `category_id`) VALUES ('Laborum', 'Nihil quos asperiores quibusdam.', 368.27, 'https://picsum.photos/77/381', '2025-04-10 14:50:44', '2025-04-10 14:50:44', 3);
INSERT INTO `tbl_product` (`name`, `description`, `price`, `img_url`, `dt_created`, `dt_updated`, `category_id`) VALUES ('Veritatis', 'Quis alias ab.', 350.63, 'https://dummyimage.com/414x164', '2025-04-10 14:50:44', '2025-04-10 14:50:44', 3);
INSERT INTO `tbl_product` (`name`, `description`, `price`, `img_url`, `dt_created`, `dt_updated`, `category_id`) VALUES ('Eos', 'Ea nulla eum libero consequatur sint.', 235.08, 'https://placekitten.com/242/905', '2025-04-10 14:50:44', '2025-04-10 14:50:44', 3);
INSERT INTO `tbl_product` (`name`, `description`, `price`, `img_url`, `dt_created`, `dt_updated`, `category_id`) VALUES ('Voluptatem', 'Asperiores quibusdam ea assumenda eum.', 368.1, 'https://dummyimage.com/847x522', '2025-04-10 14:50:44', '2025-04-10 14:50:44', 1);
INSERT INTO `tbl_product` (`name`, `description`, `price`, `img_url`, `dt_created`, `dt_updated`, `category_id`) VALUES ('Sapiente', 'Unde dolorum maiores blanditiis dolore quam quasi ut.', 328.32, 'https://picsum.photos/714/358', '2025-04-10 14:50:44', '2025-04-10 14:50:44', 2);
INSERT INTO `tbl_product` (`name`, `description`, `price`, `img_url`, `dt_created`, `dt_updated`, `category_id`) VALUES ('Enim', 'Quae ipsam ipsum corrupti.', 220.27, 'https://placekitten.com/259/669', '2025-04-10 14:50:44', '2025-04-10 14:50:44', 1);
INSERT INTO `tbl_product` (`name`, `description`, `price`, `img_url`, `dt_created`, `dt_updated`, `category_id`) VALUES ('Ipsam', 'Quasi occaecati molestias.', 333.61, 'https://picsum.photos/463/399', '2025-04-10 14:50:44', '2025-04-10 14:50:44', 3);
INSERT INTO `tbl_product` (`name`, `description`, `price`, `img_url`, `dt_created`, `dt_updated`, `category_id`) VALUES ('Placeat', 'Aliquid quis itaque.', 439.39, 'https://dummyimage.com/459x42', '2025-04-10 14:50:44', '2025-04-10 14:50:44', 1);
INSERT INTO `tbl_product` (`name`, `description`, `price`, `img_url`, `dt_created`, `dt_updated`, `category_id`) VALUES ('Porro', 'Quas sed natus ea blanditiis enim totam mollitia.', 320.37, 'https://picsum.photos/263/73', '2025-04-10 14:50:44', '2025-04-10 14:50:44', 1);
INSERT INTO `tbl_product` (`name`, `description`, `price`, `img_url`, `dt_created`, `dt_updated`, `category_id`) VALUES ('Fugit', 'Ut sit illum reprehenderit debitis sunt perferendis.', 413.93, 'https://placekitten.com/303/200', '2025-04-10 14:50:44', '2025-04-10 14:50:44', 4);
INSERT INTO `tbl_product` (`name`, `description`, `price`, `img_url`, `dt_created`, `dt_updated`, `category_id`) VALUES ('Iure', 'Ipsum fugiat tenetur voluptatibus.', 430.21, 'https://dummyimage.com/634x379', '2025-04-10 14:50:44', '2025-04-10 14:50:44', 3);
INSERT INTO `tbl_product` (`name`, `description`, `price`, `img_url`, `dt_created`, `dt_updated`, `category_id`) VALUES ('Rerum', 'Similique cupiditate velit vero non suscipit ex.', 469.19, 'https://placekitten.com/475/423', '2025-04-10 14:50:44', '2025-04-10 14:50:44', 1);
INSERT INTO `tbl_product` (`name`, `description`, `price`, `img_url`, `dt_created`, `dt_updated`, `category_id`) VALUES ('Aut', 'Laborum repellendus vero consequatur adipisci.', 169.71, 'https://dummyimage.com/168x450', '2025-04-10 14:50:44', '2025-04-10 14:50:44', 4);
INSERT INTO `tbl_product` (`name`, `description`, `price`, `img_url`, `dt_created`, `dt_updated`, `category_id`) VALUES ('Inventore', 'Occaecati possimus molestiae ipsa earum cumque esse.', 180.37, 'https://picsum.photos/552/365', '2025-04-10 14:50:44', '2025-04-10 14:50:44', 1);
INSERT INTO `tbl_product` (`name`, `description`, `price`, `img_url`, `dt_created`, `dt_updated`, `category_id`) VALUES ('Ipsum', 'Neque et culpa consectetur necessitatibus libero esse.', 429.18, 'https://dummyimage.com/516x634', '2025-04-10 14:50:44', '2025-04-10 14:50:44', 3);
INSERT INTO `tbl_product` (`name`, `description`, `price`, `img_url`, `dt_created`, `dt_updated`, `category_id`) VALUES ('Quis', 'Similique beatae corporis dolorum animi.', 402.83, 'https://picsum.photos/497/195', '2025-04-10 14:50:44', '2025-04-10 14:50:44', 1);
INSERT INTO `tbl_product` (`name`, `description`, `price`, `img_url`, `dt_created`, `dt_updated`, `category_id`) VALUES ('Veniam', 'Corrupti placeat velit quidem odit eius facilis.', 105.51, 'https://picsum.photos/297/339', '2025-04-10 14:50:44', '2025-04-10 14:50:44', 1);
INSERT INTO `tbl_product` (`name`, `description`, `price`, `img_url`, `dt_created`, `dt_updated`, `category_id`) VALUES ('Quasi', 'Veniam necessitatibus aliquam quaerat.', 106.36, 'https://dummyimage.com/351x380', '2025-04-10 14:50:44', '2025-04-10 14:50:44', 4);
INSERT INTO `tbl_product` (`name`, `description`, `price`, `img_url`, `dt_created`, `dt_updated`, `category_id`) VALUES ('Modi', 'Atque reprehenderit non dolore animi.', 208.42, 'https://picsum.photos/257/710', '2025-04-10 14:50:44', '2025-04-10 14:50:44', 3);
INSERT INTO `tbl_product` (`name`, `description`, `price`, `img_url`, `dt_created`, `dt_updated`, `category_id`) VALUES ('Nesciunt', 'Beatae suscipit blanditiis cum dolores.', 290.05, 'https://dummyimage.com/747x704', '2025-04-10 14:50:44', '2025-04-10 14:50:44', 3);
INSERT INTO `tbl_product` (`name`, `description`, `price`, `img_url`, `dt_created`, `dt_updated`, `category_id`) VALUES ('Perspiciatis', 'Doloremque impedit sed tenetur autem exercitationem quasi.', 52.93, 'https://picsum.photos/387/291', '2025-04-10 14:50:44', '2025-04-10 14:50:44', 4);
INSERT INTO `tbl_product` (`name`, `description`, `price`, `img_url`, `dt_created`, `dt_updated`, `category_id`) VALUES ('Quos', 'Sequi eius corrupti rerum alias tempora ratione.', 228.41, 'https://picsum.photos/801/4', '2025-04-10 14:50:44', '2025-04-10 14:50:44', 4);
INSERT INTO `tbl_product` (`name`, `description`, `price`, `img_url`, `dt_created`, `dt_updated`, `category_id`) VALUES ('Pariatur', 'Quae praesentium quia tempore.', 371.72, 'https://picsum.photos/178/329', '2025-04-10 14:50:44', '2025-04-10 14:50:44', 2);
INSERT INTO `tbl_product` (`name`, `description`, `price`, `img_url`, `dt_created`, `dt_updated`, `category_id`) VALUES ('Quae', 'Eum dolorem iusto modi rerum harum tempore.', 230.62, 'https://placekitten.com/291/476', '2025-04-10 14:50:44', '2025-04-10 14:50:44', 1);
INSERT INTO `tbl_product` (`name`, `description`, `price`, `img_url`, `dt_created`, `dt_updated`, `category_id`) VALUES ('Omnis', 'Ad assumenda aliquid labore blanditiis quidem.', 344.64, 'https://picsum.photos/990/14', '2025-04-10 14:50:44', '2025-04-10 14:50:44', 3);
INSERT INTO `tbl_product` (`name`, `description`, `price`, `img_url`, `dt_created`, `dt_updated`, `category_id`) VALUES ('Iusto', 'Qui ipsa quas voluptate dolores saepe.', 355.07, 'https://dummyimage.com/537x510', '2025-04-10 14:50:44', '2025-04-10 14:50:44', 1);
INSERT INTO `tbl_product` (`name`, `description`, `price`, `img_url`, `dt_created`, `dt_updated`, `category_id`) VALUES ('Consequuntur', 'Mollitia repudiandae saepe quaerat impedit possimus.', 472.04, 'https://picsum.photos/680/621', '2025-04-10 14:50:44', '2025-04-10 14:50:44', 2);
INSERT INTO `tbl_product` (`name`, `description`, `price`, `img_url`, `dt_created`, `dt_updated`, `category_id`) VALUES ('Quod', 'Tenetur nisi ipsa alias recusandae id exercitationem.', 172.21, 'https://picsum.photos/839/168', '2025-04-10 14:50:44', '2025-04-10 14:50:44', 2);
INSERT INTO `tbl_product` (`name`, `description`, `price`, `img_url`, `dt_created`, `dt_updated`, `category_id`) VALUES ('Voluptatum', 'Aliquid reprehenderit laudantium incidunt temporibus molestiae quae.', 285.38, 'https://dummyimage.com/31x596', '2025-04-10 14:50:44', '2025-04-10 14:50:44', 4);
INSERT INTO `tbl_product` (`name`, `description`, `price`, `img_url`, `dt_created`, `dt_updated`, `category_id`) VALUES ('Vel', 'Corporis modi facere.', 253.46, 'https://picsum.photos/669/624', '2025-04-10 14:50:44', '2025-04-10 14:50:44', 4);
INSERT INTO `tbl_product` (`name`, `description`, `price`, `img_url`, `dt_created`, `dt_updated`, `category_id`) VALUES ('Fuga', 'Reiciendis dignissimos dignissimos quaerat laborum.', 481.09, 'https://placekitten.com/70/188', '2025-04-10 14:50:44', '2025-04-10 14:50:44', 4);
INSERT INTO `tbl_product` (`name`, `description`, `price`, `img_url`, `dt_created`, `dt_updated`, `category_id`) VALUES ('Laboriosam', 'Error ratione iure officia sequi.', 408.41, 'https://picsum.photos/727/935', '2025-04-10 14:50:44', '2025-04-10 14:50:44', 4);
INSERT INTO `tbl_product` (`name`, `description`, `price`, `img_url`, `dt_created`, `dt_updated`, `category_id`) VALUES ('Id', 'Quidem voluptatibus deleniti maiores veniam enim totam est.', 253.67, 'https://dummyimage.com/47x81', '2025-04-10 14:50:44', '2025-04-10 14:50:44', 2);
INSERT INTO `tbl_product` (`name`, `description`, `price`, `img_url`, `dt_created`, `dt_updated`, `category_id`) VALUES ('Ea', 'Ex praesentium quis officiis.', 320.0, 'https://placekitten.com/927/407', '2025-04-10 14:50:44', '2025-04-10 14:50:44', 1);
INSERT INTO `tbl_product` (`name`, `description`, `price`, `img_url`, `dt_created`, `dt_updated`, `category_id`) VALUES ('Cupiditate', 'Harum iste eligendi accusamus laboriosam sed fuga.', 351.83, 'https://picsum.photos/636/577', '2025-04-10 14:50:44', '2025-04-10 14:50:44', 1);
INSERT INTO `tbl_product` (`name`, `description`, `price`, `img_url`, `dt_created`, `dt_updated`, `category_id`) VALUES ('Quas', 'Minima nihil architecto quaerat deserunt a dolorum.', 300.29, 'https://dummyimage.com/758x385', '2025-04-10 14:50:44', '2025-04-10 14:50:44', 1);
INSERT INTO `tbl_product` (`name`, `description`, `price`, `img_url`, `dt_created`, `dt_updated`, `category_id`) VALUES ('Repudiandae', 'Blanditiis labore nobis est impedit non.', 392.13, 'https://picsum.photos/347/375', '2025-04-10 14:50:44', '2025-04-10 14:50:44', 1);
INSERT INTO `tbl_product` (`name`, `description`, `price`, `img_url`, `dt_created`, `dt_updated`, `category_id`) VALUES ('Quaerat', 'At numquam at excepturi.', 338.54, 'https://dummyimage.com/727x679', '2025-04-10 14:50:44', '2025-04-10 14:50:44', 3);
INSERT INTO `tbl_product` (`name`, `description`, `price`, `img_url`, `dt_created`, `dt_updated`, `category_id`) VALUES ('Molestias', 'Molestiae sequi provident earum maxime eveniet.', 496.04, 'https://dummyimage.com/476x956', '2025-04-10 14:50:44', '2025-04-10 14:50:44', 1);
INSERT INTO `tbl_product` (`name`, `description`, `price`, `img_url`, `dt_created`, `dt_updated`, `category_id`) VALUES ('Nemo', 'Ea exercitationem illum nam eaque ab.', 145.44, 'https://dummyimage.com/590x269', '2025-04-10 14:50:44', '2025-04-10 14:50:44', 2);
INSERT INTO `tbl_product` (`name`, `description`, `price`, `img_url`, `dt_created`, `dt_updated`, `category_id`) VALUES ('Quam', 'Ratione quam ullam ea voluptate saepe.', 357.81, 'https://picsum.photos/118/863', '2025-04-10 14:50:44', '2025-04-10 14:50:44', 1);
INSERT INTO `tbl_product` (`name`, `description`, `price`, `img_url`, `dt_created`, `dt_updated`, `category_id`) VALUES ('Earum', 'Dolorum impedit vel doloremque aspernatur.', 248.57, 'https://picsum.photos/978/220', '2025-04-10 14:50:44', '2025-04-10 14:50:44', 1);
