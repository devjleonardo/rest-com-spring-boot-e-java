CREATE TABLE `pessoa` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `primeiro_nome` varchar(80) NOT NULL,
  `ultimo_nome` varchar(80) NOT NULL,
  `endereco` varchar(100) NOT NULL,
  `genero` varchar(9) NOT NULL,
  PRIMARY KEY (`id`)
);