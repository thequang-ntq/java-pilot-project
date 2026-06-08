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
-- user table: user login information

CREATE TABLE IF NOT EXISTS `users` (
	`user_id` INT NOT NULL AUTO_INCREMENT,
	`username` VARCHAR(50) NOT NULL,
	`password` VARCHAR(255) NOT NULL,
    `role` ENUM('ADMIN') NOT NULL DEFAULT 'ADMIN',
    CONSTRAINT pk_users PRIMARY KEY(`user_id`),
    CONSTRAINT uq_users_username UNIQUE(`username`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
-- brands table: brands information
CREATE TABLE IF NOT EXISTS `brands` (
	`brand_id` INT NOT NULL AUTO_INCREMENT,
    `brand_name` VARCHAR(50) NOT NULL,
    `logo` VARCHAR(255) DEFAULT NULL,
    `description` VARCHAR(255) DEFAULT NULL,
    CONSTRAINT pk_brands PRIMARY KEY(`brand_id`),
    CONSTRAINT uq_brands_brand_name UNIQUE(`brand_name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- products table: products information
CREATE TABLE IF NOT EXISTS `products` (
	`product_id` INT NOT NULL AUTO_INCREMENT,
    `product_name` VARCHAR(50) NOT NULL,
    `quantity` INT NOT NULL,
	`price` DECIMAL(10,2) NOT NULL,
    `brand_id` INT NOT NULL,
    `sale_date` DATE NOT NULL DEFAULT (CURDATE()),
    `image` VARCHAR(255) DEFAULT NULL,
    `description` VARCHAR(255) DEFAULT NULL,
    CONSTRAINT pk_products PRIMARY KEY(`product_id`),
    CONSTRAINT fk_products_brands FOREIGN KEY(`brand_id`) REFERENCES `brands`(`brand_id`),
    CONSTRAINT uq_products_product_name UNIQUE(`product_name`),
    CONSTRAINT chk_products_quantity_value CHECK(`quantity` >= 0),
    CONSTRAINT chk_products_price_value CHECK(`price` >= 0)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- refresh token table: refresh token information for new JWT access token
CREATE TABLE IF NOT EXISTS `refresh_tokens` (
	`token_id` INT NOT NULL AUTO_INCREMENT,
	`user_id` INT NOT NULL,
    `token` VARCHAR(50) NOT NULL,
    `expired_at` DATETIME NOT NULL,
    CONSTRAINT pk_refresh_tokens PRIMARY KEY(`token_id`),
    CONSTRAINT fk_refresh_tokens_users FOREIGN KEY(`user_id`) REFERENCES `users`(`user_id`),
    CONSTRAINT uq_refresh_tokens_token UNIQUE(`token`)
);

-- =============================================
-- INDEX
-- =============================================
-- Index for users table
-- search by username for login, register
CREATE INDEX idx_users_username ON `users`(`username`);

-- Index for brands table
-- search by brand_name
CREATE INDEX idx_brands_brand_name ON `brands`(`brand_name`);

-- Index for products table
-- search by product_name
CREATE INDEX idx_products_product_name ON `products`(`product_name`);
-- foreign key with brands table
CREATE INDEX idx_products_brand_id ON `products`(`brand_id`);
-- foreign key for price range (From / To)
CREATE INDEX idx_products_price ON `products`(`price`);

-- Index for refresh_tokens table
-- foreign key with users table
CREATE INDEX idx_refresh_tokens_user_id ON `refresh_tokens`(`user_id`);
-- get token
CREATE INDEX idx_refresh_tokens_token ON `refresh_tokens`(`token`);

-- =============================================
-- INSERT data
-- =============================================
/*
INSERT INTO `users` (`username`, `password`, `role`)
VALUES	('admin1', '$2a$10$cnhfrc7aBdwoVSHSmWiuLuV1c46nyVnqbm7xvfxhxESUSQaSdRi7K', 'ADMIN');

INSERT INTO `brands` (`brand_name`, `logo`, `description`)
VALUES 	('Apple', 'images/brand/20221124-1506-94dbck.png', 'Apple Inc,  California'),
		('Samsung', 'images/brand/20221124-1505-m6nrn9.webp', 'Samsung Inc,  Korea'),
        ('Oppo', 'images/brand/20221124-1505-m0iss6.png', 'Oppo Inc,  China'),
        ('LG', 'images/brand/20221124-1505-vgj6rp.png', 'LG Inc,  Japan'),
        ('Xiaomi', 'images/brand/20221124-1504-ep3pdt.png', 'Xiaomi Inc,  China'),
        ('Sony', 'images/brand/20221124-1504-nak19v.png', 'Sony Inc,  Japan'),
        ('Huawei', 'images/brand/20221124-1503-36khjb.png', 'Huawei made in China'), 
        ('Vivo', 'images/brand/20221124-1503-9dc89b.png', 'Vivo Inc,  China'),
        ('HTC', 'images/brand/20221124-1502-d0h72d.png', 'HTC Inc,  California'),
        ('Asus', 'images/brand/20221124-1501-73qa6b.svg', 'Asus Inc,  China'),
        ('Realme', 'images/brand/20221124-1501-12e3hf.png', 'Realme Inc,  China');
        
INSERT INTO `brands` (`brand_name`, `logo`, `description`)
VALUES	('Lenovo', NULL, 'Lenovo Inc, China'),
		('Acer', NULL, 'Acer Inc, Taiwan'),
		('MSI', NULL, 'MSI Inc, Taiwan'),
		('Razer', NULL, 'Razer Gaming Brand'),
		('Alienware', NULL, 'Dell Gaming Brand'),
		('HP', NULL, 'HP Inc, USA'),
		('Canon', NULL, 'Canon Inc, Japan'),
		('Nokia', NULL, 'Nokia Mobile, Finland'),
		('Motorola', NULL, 'Motorola Mobility'),
		('OnePlus', NULL, 'OnePlus Technology'),
		('Google Pixel', NULL, 'Google Smartphone'),
		('Nothing', NULL, 'Nothing Phone Brand'),
		('Toshiba', NULL, 'Toshiba Electronics'),
		('Gigabyte', NULL, 'Gigabyte Technology'),
		('Intel', NULL, 'Intel Corporation'),
		('AMD', NULL, 'Advanced Micro Devices'),
		('Nvidia', NULL, 'Nvidia Graphics'),
		('Corsair', NULL, 'Corsair Gaming'),
		('HyperX', NULL, 'HyperX Accessories'),
		('Logitech', NULL, 'Logitech Switzerland'),
		('JBL', NULL, 'JBL Audio'),
		('Bose', NULL, 'Bose Audio USA'),
		('Anker', NULL, 'Anker Electronics'),
		('Belkin', NULL, 'Belkin Accessories'),
		('Philips', NULL, 'Philips Electronics'),
		('Panasonic', NULL, 'Panasonic Japan'),
		('Sharp', NULL, 'Sharp Corporation'),
		('BlackBerry', NULL, 'BlackBerry Canada'),
		('Meizu', NULL, 'Meizu Technology'),
		('ZTE', NULL, 'ZTE Corporation'),
		('Honor', NULL, 'Honor Smartphone'),
		('Redmi', NULL, 'Redmi by Xiaomi'),
		('Infinix', NULL, 'Infinix Mobile'),
		('Tecno', NULL, 'Tecno Mobile'),
		('Micromax', NULL, 'Micromax India'),
		('Coolpad', NULL, 'Coolpad Group'),
		('Alcatel', NULL, 'Alcatel Mobile'),
		('BenQ', NULL, 'BenQ Corporation'),
		('ViewSonic', NULL, 'ViewSonic Displays'),
		('Kingston', NULL, 'Kingston Technology');
        
INSERT INTO `products` (`product_name`, `quantity`, `price`, `brand_id`, `sale_date`, `image`, `description`)
VALUES 	('Iphone XS Max', 100, 26990000, 1, '2019-10-12', 'images/product/IPhone XS Max 128GB.jpg', 'Made in USA'),
		('Iphone X', 100, 21090000, 1, '2019-10-09', 'images/product/ipx.jpg', 'Apple\'s aim with the iPhone X was to create an iPhone.'),
        ('Iphone 8 Plus', 100, 17980000, 1, '2019-10-09', 'images/product/IPhone 8 Plus 64GB.jpg', 'The iPhone 8 includes a 4.7-inch display.'),
        ('Iphone 7 Plus', 100, 16500000, 1,'2019-10-10', 'images/product/ip7.jpg', 'The iPhone 7 measures in at 138.3mm tall.'),
        ('Samsung Galaxy Note 10 Plus', 100, 22390000, 2, '2019-10-08', 'images/product/ss note 10+.jpg', 'It runs on the Samsung Exynos 9 Octa 9825 Chipset.'),
        ('Samsung Galaxy S10', 100, 21500000, 2, '2019-10-08', 'images/product/Samsung Galaxy S10 128GB.jpg', 'The Galaxy S10 isn’t all that small, of course.'),
        ('Samsung Galaxy S10 Plus', 100, 21990000, 2, '2019-10-08', 'images/product/Samsung Galaxy S10+ 2 128GB.jpg', 'The Galaxy S10+ is Samsung latest flagship for 2019.'),
        ('Samsung Galaxy A70', 100, 7990000, 2, '2019-10-08', 'images/product/Samsung Galaxy A70 64GB.jpg', 'It is powered by 2GHz octa-core Qualcomm Snapdragon 675.'),
        ('Samsung Galaxy Note 9', 100, 20490000, 2, '2019-10-08', 'images/product/ss note 9.jpg', 'Samsung Note version'),
        ('IPhone 11 Pro Max', 100, 42990000, 1, '2019-10-08', 'images/product/iphone-11-pro-max-512gb-gold.jpg', 'New IPhone'),
        ('Iphone 11', 80, 21990000, 1, '2019-10-08', 'images/product/iphone-11-128gb-purple.jpg', 'New version'),
        ('Iphone 6S Plus', 100, 8990000, 1, '2019-10-12', 'images/product/IPhone 6 32GB.jpg', 'Made in USA'),
        ('Xiaomi Note 7', 100, 4500000, 6, '2019-10-08', 'images/product/xiaominote7.jpg', 'description'),
        ('Huawei P30 Pro', 120, 20690000, 9, '2019-10-08', 'images/product/huawei-p30-pro.jpg', 'Huawei made in China'),
        ('Huawei P30', 100, 15290000, 9, '2019-10-08', 'images/product/huawei-p30-blue-600x600.jpg', 'Huawei made in China'),
        ('Oppo Reno 10X', 70, 19990000, 3, '2019-10-08', 'images/product/oppo-reno-10x-zoom-edition-black.jpg', 'Oppo made in China'),
        ('Oppo A9', 100, 7890000, 3, '2019-10-08', 'images/product/oppo-a9-2020-green-1-600x600.jpg', 'Oppo China'),
        ('Oppo A7', 50, 7000000, 3, '2019-10-08', 'images/product/oppo-r17-pro-14-600x600.jpg', 'Oppo China');
        
INSERT INTO `products` (`product_name`, `quantity`, `price`, `brand_id`, `sale_date`, `image`, `description`)
VALUES	('Lenovo ThinkPad X1 Carbon', 40, 35990000, 28, '2026-05-19', NULL, 'Business laptop'),
		('Lenovo Legion 5', 55, 28990000, 28, '2026-05-19', NULL, 'Gaming laptop'),
		('Acer Aspire 7', 70, 18990000, 29, '2026-05-19', NULL, 'Mid-range laptop'),
		('Acer Nitro 5', 65, 24990000, 29, '2026-05-19', NULL, 'Gaming laptop'),
		('MSI Katana GF66', 35, 27990000, 30, '2026-05-19', NULL, 'MSI gaming series'),
		('MSI Modern 14', 50, 17990000, 30, '2026-05-19', NULL, 'Portable ultrabook'),
		('Razer Blade 15', 20, 55990000, 31, '2026-05-19', NULL, 'Premium gaming laptop'),
		('Alienware M16', 18, 62990000, 32, '2026-05-19', NULL, 'High-end gaming laptop'),
		('HP Pavilion 15', 80, 20990000, 33, '2026-05-19', NULL, 'Office laptop'),
		('HP Victus 16', 42, 31990000, 33, '2026-05-19', NULL, 'Gaming performance'),
		('Canon EOS M50', 15, 16490000, 34, '2026-05-19', NULL, 'Mirrorless camera'),
		('Canon EOS R10', 12, 28990000, 34, '2026-05-19', NULL, 'Professional camera'),
		('Nokia G22', 90, 4990000, 35, '2026-05-19', NULL, 'Budget smartphone'),
		('Nokia X30', 45, 9990000, 35, '2026-05-19', NULL, 'Mid-range Nokia phone'),
		('Motorola Edge 40', 33, 12990000, 36, '2026-05-19', NULL, 'Android smartphone'),
		('Motorola Moto G84', 50, 7990000, 36, '2026-05-19', NULL, 'Affordable phone'),
		('OnePlus 12', 38, 18990000, 37, '2026-05-19', NULL, 'Flagship OnePlus'),
		('OnePlus Nord CE4', 60, 8990000, 37, '2026-05-19', NULL, 'Mid-range OnePlus'),
		('Google Pixel 8', 28, 20990000, 38, '2026-05-19', NULL, 'Google flagship'),
		('Google Pixel 8a', 46, 12990000, 38, '2026-05-19', NULL, 'Affordable Pixel'),
		('Nothing Phone 2', 30, 15990000, 39, '2026-05-19', NULL, 'Transparent design'),
		('Nothing Phone 2a', 55, 9990000, 39, '2026-05-19', NULL, 'Mid-range Nothing'),
		('Toshiba Dynabook X40', 20, 23990000, 40, '2026-05-19', NULL, 'Business notebook'),
		('Gigabyte G5', 25, 25990000, 41, '2026-05-19', NULL, 'Gaming notebook'),
		('Intel Core i5 14600K', 100, 7990000, 42, '2026-05-19', NULL, 'Desktop processor'),
		('Intel Core i7 14700K', 75, 12990000, 42, '2026-05-19', NULL, 'High-end processor'),
		('AMD Ryzen 5 7600X', 90, 6990000, 43, '2026-05-19', NULL, 'AMD desktop CPU'),
		('AMD Ryzen 7 7800X3D', 55, 11990000, 43, '2026-05-19', NULL, 'Gaming processor'),
		('Nvidia RTX 4060', 40, 10990000, 44, '2026-05-19', NULL, 'Graphics card'),
		('Nvidia RTX 4070 Ti', 25, 23990000, 44, '2026-05-19', NULL, 'High-end GPU'),
		('Corsair K70 RGB', 120, 3490000, 45, '2026-05-19', NULL, 'Mechanical keyboard'),
		('Corsair HS80', 75, 2990000, 45, '2026-05-19', NULL, 'Gaming headset'),
		('HyperX Cloud II', 88, 1990000, 46, '2026-05-19', NULL, 'Popular gaming headset'),
		('HyperX Alloy Origins', 60, 2590000, 46, '2026-05-19', NULL, 'Mechanical keyboard'),
		('Logitech G Pro X', 95, 3290000, 47, '2026-05-19', NULL, 'Gaming mouse'),
		('Logitech MX Master 3S', 45, 2490000, 47, '2026-05-19', NULL, 'Productivity mouse'),
		('JBL Charge 5', 65, 3790000, 48, '2026-05-19', NULL, 'Portable speaker'),
		('JBL Tune 770NC', 58, 2490000, 48, '2026-05-19', NULL, 'Wireless headphones'),
		('Bose QuietComfort Ultra', 18, 9990000, 49, '2026-05-19', NULL, 'Noise cancelling'),
		('Bose SoundLink Flex', 34, 4290000, 49, '2026-05-19', NULL, 'Bluetooth speaker');

-- SELECT to check;
/*
SELECT * FROM `users`;
SELECT * FROM `brands`;
SELECT * FROM `products`;
SELECT * FROM `refresh_tokens`;
SELECT * FROM `products` WHERE product_name LIKE '%Iphone%';
*/

/*
DROP TABLE `products`;
DROP TABLE `brands`;
DROP TABLE `refresh_tokens`;
DROP TABLE `users`;
*/