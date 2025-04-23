create database banking_system;
use banking_system;

CREATE TABLE `transactions` (
  `id` int(11) NOT NULL,
  `user_id` int(11) DEFAULT NULL,
  `type` enum('deposit','withdraw') DEFAULT NULL,
  `amount` double DEFAULT NULL,
  `timestamp` timestamp NOT NULL DEFAULT current_timestamp()
);



CREATE TABLE `users` (
  `id` int(11) NOT NULL,
  `username` varchar(50) DEFAULT NULL,
  `password` varchar(100) DEFAULT NULL,
  `name` varchar(100) DEFAULT NULL,
  `email` varchar(100) DEFAULT NULL,
  `phone` varchar(15) DEFAULT NULL,
  `is_admin` tinyint(1) DEFAULT NULL,
  `account_number` varchar(20) DEFAULT NULL
);
insert into users (id, username, password, name, email, phone, is_admin, account_number) 
values
(NULL, 'admin', 'admin123', 'Admin User', 'admin@example.com', '1234567890', 1, '999999999999');