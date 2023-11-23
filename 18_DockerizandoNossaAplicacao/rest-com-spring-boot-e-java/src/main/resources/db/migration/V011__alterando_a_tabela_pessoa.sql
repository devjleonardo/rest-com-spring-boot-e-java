ALTER TABLE `pessoa`
	ADD COLUMN `habilitada` BIT(1) NOT NULL DEFAULT b'1' AFTER `genero`;