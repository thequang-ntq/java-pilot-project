/*
    pilot_project_db for pilot project
    @author: Quang
    since 2026-04-24
	character set utf8mb4
    collate utf8mb4_0900_ai_ci (accent-insensitive, case-insensitive)
*/

-- =============================================
-- DATABASE
-- =============================================
CREATE DATABASE IF NOT EXISTS `pilot_project_db` DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
USE pilot_project_db;

-- =============================================
-- TABLE
-- =============================================
-- account table: account login information
CREATE TABLE IF NOT EXISTS `account` (
	`account_id` INT NOT NULL AUTO_INCREMENT,
	`account_name` VARCHAR(50) NOT NULL,
	`password` VARCHAR(255) NOT NULL,
    `role` ENUM('ADMIN', 'USER') NOT NULL DEFAULT 'USER',
	`email` VARCHAR(255) DEFAULT NULL,
    CONSTRAINT pk_account PRIMARY KEY(`account_id`),
    CONSTRAINT uq_account_account_name UNIQUE(`account_name`),
    CONSTRAINT uq_account_email UNIQUE(`email`),
    -- before @: char, num, special char + @ + char, num, special char + . + TLD: com, vn, org...
    CONSTRAINT chk_account_email_format
    CHECK (
        `email` IS NULL OR
        `email` REGEXP '^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\.[A-Za-z]{2,}$'
    )
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- brand table: brand information
CREATE TABLE IF NOT EXISTS `brand` (
	`brand_id` INT NOT NULL AUTO_INCREMENT,
    `brand_name` VARCHAR(50) NOT NULL,
    `logo` VARCHAR(255) DEFAULT NULL,
    `description` VARCHAR(255) DEFAULT NULL,
    `is_deleted` BOOLEAN NOT NULL DEFAULT FALSE,
    CONSTRAINT pk_brand PRIMARY KEY(`brand_id`),
    CONSTRAINT uq_brand_brand_name UNIQUE(`brand_name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- product table: product information
CREATE TABLE IF NOT EXISTS `product` (
	`product_id` INT NOT NULL AUTO_INCREMENT,
    `product_name` VARCHAR(50) NOT NULL,
    `quantity` INT NOT NULL,
	`price` DECIMAL(10, 2) NOT NULL,
    `brand_id` INT NOT NULL,
    `sale_date` DATE NOT NULL DEFAULT (CURDATE()),
    `image` VARCHAR(255) DEFAULT NULL,
    `description` VARCHAR(255) DEFAULT NULL,
    `is_deleted` BOOLEAN NOT NULL DEFAULT FALSE,
    CONSTRAINT pk_product PRIMARY KEY(`product_id`),
    CONSTRAINT fk_product_brand FOREIGN KEY(`brand_id`) REFERENCES `brand`(`brand_id`),
    CONSTRAINT uq_product_product_name UNIQUE(`product_name`),
    CONSTRAINT chk_product_quantity_value CHECK(`quantity` >= 0),
    CONSTRAINT chk_product_price_value CHECK(`price` >= 0)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;


-- order table: order of account information
CREATE TABLE IF NOT EXISTS `order` (
	`order_id` INT NOT NULL AUTO_INCREMENT,
    `account_id` INT NOT NULL,
    `order_time` DATETIME DEFAULT NULL,
    `finish_time` DATETIME DEFAULT NULL,
    `status` ENUM('PRE_ORDER', 'NEW', 'IN_PROGRESS', 'COMPLETED', 'FAILED') NOT NULL DEFAULT 'PRE_ORDER',
    CONSTRAINT pk_order PRIMARY KEY(`order_id`),
    CONSTRAINT fk_order_account FOREIGN KEY(`account_id`) REFERENCES `account`(`account_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- order_detail table: detail of order information
CREATE TABLE IF NOT EXISTS `order_detail` (
	`order_id` INT NOT NULL,
    `product_id` INT NOT NULL,
    `quantity` INT NOT NULL,
    `sale_price` DECIMAL(10, 2) NOT NULL,
	CONSTRAINT pk_order_detail PRIMARY KEY(`order_id`, `product_id`),
    CONSTRAINT fk_order_detail_order FOREIGN KEY(`order_id`) REFERENCES `order`(`order_id`),
    CONSTRAINT fk_order_detail_product FOREIGN KEY(`product_id`) REFERENCES `product`(`product_id`),
    CONSTRAINT chk_order_detail_quantity_value CHECK(`quantity` >= 0),
    CONSTRAINT chk_order_detail_sale_price_value CHECK(`sale_price` >= 0)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- =============================================
-- INDEX
-- =============================================
-- Index for account table
-- search by account_name for login, register
CREATE INDEX idx_account_account_name ON `account`(`account_name`);

-- Index for brand table
-- search by brand_name
CREATE INDEX idx_brand_brand_name ON `brand`(`brand_name`);
-- filter by is_deleted, only show not deleted
-- is_deleted has low cardinality (just 2 value: 0/1) -> no need to compiste index (a,b)
CREATE INDEX idx_brand_is_deleted ON `brand`(`is_deleted`);

-- Index for product table
-- search by product_name
CREATE INDEX idx_product_product_name ON `product`(`product_name`);
-- foreign key with brand table
CREATE INDEX idx_product_brand_id ON `product`(`brand_id`);
-- foreign key for price range (From / To)
CREATE INDEX idx_product_price ON `product`(`price`);
-- filter by is_deleted, only show not deleted
CREATE INDEX idx_product_is_deleted ON `product`(`is_deleted`);

-- Index for order table
-- get the order list for account
CREATE INDEX idx_order_account_id ON `order`(`account_id`);
-- filter by status
CREATE INDEX idx_order_status ON `order`(`status`);
-- search by order_time, finish_time
CREATE INDEX idx_order_order_time ON `order`(`order_time`);
CREATE INDEX idx_order_finish_time ON `order`(`finish_time`);

-- =============================================
-- INSERT data
-- =============================================
/*
INSERT INTO `account` (`account_name`, `password`, `role`, `email`)
VALUES	('admin1', '123', 'ADMIN', NULL),
		('admin2', '123', 'ADMIN', NULL),
		('admin3', '123', 'ADMIN', NULL),
        ('user1',  '123', 'USER', NULL),
        ('user2', '123', 'USER', NULL);

INSERT INTO `brand` (`brand_name`, `logo`, `description`, `is_deleted`)
VALUES 	('Apple', 'images/brand/20221124-1506-94dbck.png', 'Apple Inc,  California', FALSE),
		('Samsung', 'images/brand/20221124-1505-m6nrn9.webp', 'Samsung Inc,  Korea', FALSE),
        ('Oppo', 'images/brand/20221124-1505-m0iss6.png', 'Oppo Inc,  China', FALSE),
        ('LG', 'images/brand/20221124-1505-vgj6rp.png', 'LG Inc,  Japan', FALSE),
        ('Xiaomi', 'images/brand/20221124-1504-ep3pdt.png', 'Xiaomi Inc,  China', FALSE),
        ('Sony', 'images/brand/20221124-1504-nak19v.png', 'Sony Inc,  Japan', FALSE),
        ('Huawei', 'images/brand/20221124-1503-36khjb.png', 'Huawei made in China', FALSE), 
        ('Vivo', 'images/brand/20221124-1503-9dc89b.png', 'Vivo Inc,  China', FALSE),
        ('HTC', 'images/brand/20221124-1502-d0h72d.png', 'HTC Inc,  California', FALSE),
        ('Asus', 'images/brand/20221124-1501-73qa6b.svg', 'Asus Inc,  China', FALSE),
        ('Realme', 'images/brand/20221124-1501-12e3hf.png', 'Realme Inc,  China', FALSE);
        
INSERT INTO `product` (`product_name`, `quantity`, `price`, `brand_id`, `sale_date`, `image`, `description`, `is_deleted`)
VALUES 	('Iphone XS Max', 100, 26990000, 1, '2019-10-12', 'IPhone XS Max 128GB.jpg', 'Made in USA', FALSE),
		('Iphone X', 100, 21090000, 1, '2019-10-09', 'ipx.jpg', 'Apple\'s aim with the iPhone X was to create an iPhone.', FALSE),
        ('Iphone 8 Plus', 100, 17980000, 1, '2019-10-09', 'IPhone 8 Plus 64GB.jpg', 'The iPhone 8 includes a 4.7-inch display.', FALSE),
        ('Iphone 7 Plus', 100, 16500000, 1,'2019-10-10', 'ip7.jpg', 'The iPhone 7 measures in at 138.3mm tall.', FALSE),
        ('Samsung Galaxy Note 10 Plus', 100, 22390000, 2, '2019-10-08', 'ss note 10+.jpg', 'It runs on the Samsung Exynos 9 Octa 9825 Chipset.', FALSE),
        ('Samsung Galaxy S10', 100, 21500000, 2, '2019-10-08', 'Samsung Galaxy S10 128GB.jpg', 'The Galaxy S10 isn’t all that small, of course.', FALSE),
        ('Samsung Galaxy S10 Plus', 100, 21990000, 2, '2019-10-08', 'Samsung Galaxy S10+ 2 128GB.jpg', 'The Galaxy S10+ is Samsung latest flagship for 2019.', FALSE),
        ('Samsung Galaxy A70', 100, 7990000, 2, '2019-10-08', 'Samsung Galaxy A70 64GB.jpg', 'It is powered by 2GHz octa-core Qualcomm Snapdragon 675.', FALSE),
        ('Samsung Galaxy Note 9', 100, 20490000, 2, '2019-10-08', 'ss note 9.jpg', 'Samsung Note version', FALSE),
        ('IPhone 11 Pro Max', 100, 42990000, 1, '2019-10-08', 'iphone-11-pro-max-512gb-gold.jpg', 'New IPhone', FALSE),
        ('Iphone 11', 80, 21990000, 1, '2019-10-08', 'iphone-11-128gb-purple.jpg', 'New version', FALSE),
        ('Iphone 6S Plus', 100, 8990000, 1, '2019-10-12', 'IPhone 6 32GB.jpg', 'Made in USA', FALSE),
        ('Xiaomi Note 7', 100, 4500000, 6, '2019-10-08', 'xiaominote7.jpg', 'description', FALSE),
        ('Huawei P30 Pro', 120, 20690000, 9, '2019-10-08', 'huawei-p30-pro.jpg', 'Huawei made in China', FALSE),
        ('Huawei P30', 100, 15290000, 9, '2019-10-08', 'huawei-p30-blue-600x600.jpg', 'Huawei made in China', FALSE),
        ('Oppo Reno 10X', 70, 19990000, 3, '2019-10-08', 'oppo-reno-10x-zoom-edition-black.jpg', 'Oppo made in China', FALSE),
        ('Oppo A9', 100, 7890000, 3, '2019-10-08', 'oppo-a9-2020-green-1-600x600.jpg', 'Oppo China', FALSE),
        ('Oppo A7', 50, 7000000, 3, '2019-10-08', 'oppo-r17-pro-14-600x600.jpg', 'Oppo China', FALSE);
        
INSERT INTO `order` (`account_id`, `order_time`, `finish_time`, `status`)
VALUES	(4, NULL, NULL, 'PRE_ORDER'),
		(4, '2026-01-15 09:00:00', NULL, 'NEW'),
		(4, '2026-02-01 10:15:00', NULL, 'IN_PROGRESS'),
		(5, '2026-02-20 14:00:00', '2026-02-22 16:00:00', 'COMPLETED'),
		(5, '2026-03-05 11:30:00', '2026-03-06 09:00:00', 'FAILED');

INSERT INTO `order_detail` (`order_id`, `product_id`, `quantity`, `sale_price`)
VALUES
    -- order 1 (pre-order, user1): Iphone XS Max + Iphone X
    (1, 1, 2, 26990000.00),
    (1, 2, 1, 21090000.00),

    -- order 2 (new, user1): Iphone 8 Plus + Iphone 7 Plus
    (2, 3, 1, 17980000.00),
    (2, 4, 3, 16500000.00),

    -- order 3 (in progress, user1): Samsung Galaxy Note 10 Plus + Samsung Galaxy S10
    (3, 5, 1, 22390000.00),
    (3, 6, 2, 21500000.00),

    -- order 4 (completed, user2): Samsung Galaxy S10 Plus + Samsung Galaxy A70
    (4, 7, 1, 21990000.00),
    (4, 8, 4, 7990000.00),

    -- order 5 (failed, user2): Samsung Galaxy Note 9 + IPhone 11 Pro Max
    (5, 9,  2, 20490000.00),
    (5, 10, 1, 42990000.00);
*/

-- SELECT to check;
/*
SELECT * FROM `account`;
SELECT * FROM `brand`;
SELECT * FROM `product`;
SELECT * FROM `order`;
SELECT * FROM `order_detail`;
SELECT * FROM `product` WHERE `is_deleted` = FALSE AND product_name LIKE '%Iphone%';
*/

-- DROP TABLE `order_detail`;
-- DROP TABLE `order`;