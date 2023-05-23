-- create database bd;
 use bd;
CREATE TABLE `cart` (
  `user_id` int(11) NOT NULL,
  `product_id` int(11) NOT NULL,
	FOREIGN KEY (`user_id`) REFERENCES `user` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
	FOREIGN KEY (`product_id`) REFERENCES `product` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `favorite` (
  `favorite_id` int(11) NOT NULL,
  `user_id` int(11) DEFAULT NULL,
  `product_id` int(11) DEFAULT NULL,
    FOREIGN KEY (`user_id`) REFERENCES `user` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
 FOREIGN KEY (`product_id`) REFERENCES `product` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


CREATE TABLE `history` (
  `history_id` int(11) NOT NULL,
  `user_id` int(11) DEFAULT NULL,
  `product_id` int(11) DEFAULT NULL,
   FOREIGN KEY (`user_id`) REFERENCES `user` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
   FOREIGN KEY (`product_id`) REFERENCES `product` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `ordering` (
  `ordering_id` int(11) NOT NULL,
  `order_number` int(11) DEFAULT NULL,
  `order_date` date DEFAULT NULL,
  `status` text,
  `name_on_card` text,
  `card_number` text,
  `expiration_date` date DEFAULT NULL,
  `user_id` int(11) DEFAULT NULL,
  `product_id` int(11) DEFAULT NULL,
  FOREIGN KEY (`user_id`) REFERENCES `user` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  FOREIGN KEY (`product_id`) REFERENCES `product` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;



CREATE TABLE `poster` (
  `poster_id` int(11) NOT NULL,
  `image` text NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

INSERT INTO `poster` (`poster_id`, `image`) VALUES
(4, 'storage_poster\\poster.jpg'),
(5, 'storage_poster\\poster1.jpg'),
(6, 'storage_poster\\poster2.jpg');

CREATE TABLE `product` (
  `id` int(11) NOT NULL,
  `product_name` text,
  `price` double(11,2) NOT NULL,
  `quantity` int(11) NOT NULL,
  `supplier` text NOT NULL,
  `image` text NOT NULL,
  `category` text NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


CREATE TABLE `review` (
  `review_id` int(11) NOT NULL,
  `user_id` int(11) DEFAULT NULL ,
  `product_id` int(11) DEFAULT NULL,
  `feedback` varchar(150) DEFAULT NULL,
  `rate` float(11,1) DEFAULT NULL,
  `review_date` date DEFAULT NULL,
  FOREIGN KEY (`user_id`) REFERENCES `user` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  FOREIGN KEY (`product_id`) REFERENCES `product` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;








CREATE TABLE `shipping` (
  `shipping_id` int(11) NOT NULL,
  `address` text,
  `city` text,
  `country` text,
  `zip` text,
  `phone` text,
  `user_id` int(11) DEFAULT NULL,
  `product_id` int(11) DEFAULT NULL,
   FOREIGN KEY (`user_id`) REFERENCES `user` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
   FOREIGN KEY (`product_id`) REFERENCES `product` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;




CREATE TABLE `user` (
  `id` int(11) NOT NULL,
  `name` text NOT NULL,
  `email` text NOT NULL,
  `password` text NOT NULL,
  `gender` text NOT NULL,
  `age` int(11) NOT NULL,
  `image` text NOT NULL,
  `isAdmin` tinyint(1) NOT NULL DEFAULT '0'
) ENGINE=InnoDB DEFAULT CHARSET=utf8;




ALTER TABLE `cart`
  ADD PRIMARY KEY (`user_id`,`product_id`),
  ADD UNIQUE KEY `cart_constraint` (`user_id`,`product_id`),
  ADD KEY `product_id` (`product_id`);

ALTER TABLE `favorite`
  ADD PRIMARY KEY (`favorite_id`),
  ADD UNIQUE KEY `fav_constraint` (`user_id`,`product_id`),
  ADD KEY `product_id` (`product_id`);

ALTER TABLE `history`
  ADD PRIMARY KEY (`history_id`),
  ADD UNIQUE KEY `history_constraint` (`user_id`,`product_id`),
  ADD KEY `product_id` (`product_id`);

ALTER TABLE `ordering`
  ADD PRIMARY KEY (`ordering_id`),
  ADD KEY `user_id` (`user_id`),
  ADD KEY `product_id` (`product_id`);

ALTER TABLE `poster`
  ADD PRIMARY KEY (`poster_id`);


ALTER TABLE `product`
  ADD PRIMARY KEY (`id`);

ALTER TABLE `review`
  ADD PRIMARY KEY (`review_id`),
  ADD UNIQUE KEY `review_constraint` (`user_id`,`product_id`),
  ADD KEY `product_id` (`product_id`);
  
  

ALTER TABLE `shipping`
  ADD PRIMARY KEY (`shipping_id`),
  ADD KEY `user_id` (`user_id`),
  ADD KEY `product_id` (`product_id`);

ALTER TABLE `user`
  ADD PRIMARY KEY (`id`);

ALTER TABLE `favorite`
  MODIFY `favorite_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=112;

ALTER TABLE `history`
  MODIFY `history_id` int(11) NOT NULL AUTO_INCREMENT;

ALTER TABLE `ordering`
  MODIFY `ordering_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=24;

ALTER TABLE `poster`
  MODIFY `poster_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=7;

ALTER TABLE `product`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=61;

ALTER TABLE `review`
  MODIFY `review_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=50;

ALTER TABLE `shipping`
  MODIFY `shipping_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=23;


ALTER TABLE `user`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=26;

-- ALTER TABLE `cart`
  -- ADD CONSTRAINT `cart_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  -- ADD CONSTRAINT `cart_ibfk_2` FOREIGN KEY (`product_id`) REFERENCES `product` (`id`) ON DELETE CASCADE ON UPDATE CASCADE;

-- ALTER TABLE `favorite`
  -- ADD CONSTRAINT `favorite_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  -- ADD CONSTRAINT `favorite_ibfk_2` FOREIGN KEY (`product_id`) REFERENCES `product` (`id`) ON DELETE CASCADE ON UPDATE CASCADE;

-- ALTER TABLE `history`
  -- ADD CONSTRAINT `history_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  -- ADD CONSTRAINT `history_ibfk_2` FOREIGN KEY (`product_id`) REFERENCES `product` (`id`) ON DELETE CASCADE ON UPDATE CASCADE;

-- ALTER TABLE `ordering`
-- ADD CONSTRAINT `ordering_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
-- ADD CONSTRAINT `ordering_ibfk_2` FOREIGN KEY (`product_id`) REFERENCES `product` (`id`) ON DELETE CASCADE ON UPDATE CASCADE;


-- ALTER TABLE `review`
 -- ADD CONSTRAINT `review_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  -- ADD CONSTRAINT `review_ibfk_2` FOREIGN KEY (`product_id`) REFERENCES `product` (`id`) ON DELETE CASCADE ON UPDATE CASCADE;

-- ALTER TABLE `shipping`
 -- ADD CONSTRAINT `shipping_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
 -- ADD CONSTRAINT `shipping_ibfk_2` FOREIGN KEY (`product_id`) REFERENCES `product` (`id`) ON DELETE CASCADE ON UPDATE CASCADE;

INSERT INTO `product` (`id`, `product_name`, `price`, `quantity`, `supplier`, `image`, `category`) VALUES

(19, 'Уровень строительный Total TMT215086M ', 91.92, 300, 'shop.by', 'storage_product\\measuring4.jpg', 'measuring'),
(20, 'Уровень строительный Empire EM81.12M', 40.78, 300, 'shop.by', 'storage_product\\measuring5.jpg', 'measuring'),
(21, 'Уровень строительный ADA Instruments Pro Digit RUMB A00481 ', 82.30, 300, 'shop.by', 'storage_product\\measuring6.jpg', 'measuring'),
(22, 'Рулетка 3м 16 мм обрезинен.корп. блок GIANT 000050821712 ', 3.60, 700, 'shop.by ', 'storage_product\\measuring7.jpg', 'measuring'),
(23, 'Сварочный трансформатор Telwin QUALITY 220 AC/DC 230-400V', 548.20, 700, 'shop.by', 'storage_product\\metalCutting1.jpg', 'metalCutting'),
(24, 'Сварочный инвертор РЕСАНТА САИ-190ПРОФ', 709.00, 700, 'shop.by', 'storage_product\\metalCutting1.jpg', 'metalCutting'),
(25, 'Сварочный инвертор Deko DKWM250A', 160.90, 676, 'shop.by', 'storage_product\\metalCutting2.jpg', 'metalCutting'),
(26, 'Сварочный инвертор Spec MIG/MMA-250Pro', 805.00, 676, 'shop.by', 'storage_product\\metalCutting3.jpg', 'metalCutting'),
(27, 'Сварочный аппарат Ставр САИ-180БТЛ', 238.40, 943, 'shop.by', 'storage_product\\metalCutting4.jpg', 'metalCutting'),
(28, 'Сварочный инвертор Mikkele COMBIMIG-201G', 450.75, 340, 'shop.by', 'storage_product\\metalCutting5.jpg', 'metalCutting'),
(29, 'Инверторный сварочный полуавтомат Aurora PRO SPEEDWAY 175', 1590.00, 600, 'shop.by', 'storage_product\\metalCutting6.jpg', 'metalCutting'),
(30, 'Сварочный инвертор Deko DKWM250A', 160.00, 845, 'shop.by ', 'storage_product\\metalCutting7.jpg', 'metalCutting'),
(31, 'Сварочный инвертор Solaris MULTIMIG-227', 1098.00, 755, 'shop.by', 'storage_product\\metalCutting8.jpg', 'metalCutting'),
(32, 'Подающий механизм для Aurora SKYWAY 350 DUAL PULSE', 3237.00, 70, 'shop.by', 'storage_product\\metalCutting9.jpg', 'woodworking'), 
(33, 'Бензопила Champion 241-16', 299.00, 245, 'shop.by', 'storage_product\\woodworking.jpg', 'woodworking'),
(34, 'Бензопила Shtenli 550', 490.00, 450, 'shop.by', 'storage_product\\woodworking1.jpg', 'woodworking'),
(35, 'Бензопила Champion 251-18', 320.00, 300, 'shop.by', 'storage_product\\woodworking2.jpg', 'woodworking'),
(36, 'Бензопила ECO CSP-235', 199.89, 250, 'shop.by', 'storage_product\\woodworking3.jpg', 'woodworking'),
(37, 'Бензопила Patriot PT 2512', 249.00, 100, 'shop.by', 'storage_product\\woodworking4.jpg', 'woodworking'),
(38, 'Бензопила Oleo-Mac GSH 51', 629.00, 150, 'shop.by', 'storage_product\\woodworking5.jpg', 'woodworking'),
(39, 'Бензопила DGM GS-232', 173.51, 150, 'shop.by', 'storage_product\\woodworking6.jpg', 'woodworking'),
(40, 'Бензопила Oasis GS-5218', 192.00, 660, 'shop.by', 'storage_product\\woodworking7.jpg', 'woodworking'),
(41, 'Бензопила цепная Husqvarna 450 II', 1300.00, 660, 'shop.by', 'storage_product\\woodworking8.jpg', 'woodworking'),
(42, 'Бензопила EFCO MTH 560', 658.50, 30, 'shop.by', 'storage_product\\woodworking9.jpg', 'household'), 
(43, 'Газонокосилка бензиновая Sturm PL5114SV', 767.70, 45, 'shop.by', 'storage_product\\household.jpg', 'household'),
(44, 'Газонокосилка бензиновая Hyundai L5362S', 1397.00, 12, 'shop.by', 'storage_product\\household1.jpg', 'household'),
(45, 'Газонокосилка электрическая Bosch ARM 37', 339.15, 300, 'shop.by', 'storage_product\\household2.jpg', 'household'),
(46, 'Газонокосилка бензиновая Champion LM5347', 1163.10, 23, 'shop.by', 'storage_product\\household3.jpg', 'household'),
(47, 'Газонокосилка электрическая Makita ELM3720', 499.00, 50, 'shop.by', 'storage_product\\household4.jpg', 'household'),
(48, 'Газонокосилка бензиновая ECO LG-435', 518.40, 39, 'shop.by', 'storage_product\\household5.jpg', 'household'),
(49, 'Газонокосилка бензиновая Champion LMH5640', 1219.00, 57, ' shop.by', 'storage_product\\household6.jpg', 'household'),
(50, 'Газонокосилка бензиновая Skiper GW461S', 690.00, 60, 'shop.by', 'storage_product\\household7.jpg', 'household'),
(51, 'Газонокосилка электрическая Makita ELM 3711', 392.60, 34, 'shop.by', 'storage_product\\household8.jpg', 'household'),
(52, 'Лазерная рулетка ADA COSMO 120 Video', 296.42, 300, 'shop.by', 'storage_product\\measuring.jpg', 'measuring'),
(53, 'Лазерный дальномер-рулетка Wortex LR 4004', 119.50, 400, 'shop.by', 'storage_product\\measuring1.jpg', 'measuring'),
(54, 'Лазерный дальномер-рулетка Smart 20', 101.30, 450, 'shop.by ', 'storage_product\\measuring2.jpg', 'measuring'),
(55, 'Лазерный дальномер-рулетка Mileseey DT10', 160.00, 450, 'shop.by ', 'storage_product\\measuring3.jpg', 'measuring');

INSERT INTO `poster` (`poster_id`, `image`) VALUES
(4, 'storage_poster\\poster.jpg'),
(5, 'storage_poster\\poster1.jpg'),
(6, 'storage_poster\\poster2.jpg');

-- drop database bd;


