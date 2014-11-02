DROP DATABASE  IF EXISTS hokan_ng_dev;

CREATE DATABASE `hokan_ng_dev` CHARACTER SET utf8 COLLATE utf8_general_ci;
GRANT ALL ON `hokan_ng_dev`.* TO `hokan_ng`@localhost IDENTIFIED BY 'hokan_ng';

FLUSH PRIVILEGES;
