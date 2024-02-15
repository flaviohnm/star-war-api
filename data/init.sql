CREATE DATABASE IF NOT EXISTS `db_star_wars_test`;
CREATE DATABASE IF NOT EXISTS `db_star_wars`;

CREATE USER 'dbuser'@'localhost' IDENTIFIED BY 'local';
GRANT ALL PRIVILEGES ON *.* TO 'dbuser'@'%';