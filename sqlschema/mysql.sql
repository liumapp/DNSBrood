CREATE TABLE `brood`.`zones` (
  `id` INT UNSIGNED NOT NULL AUTO_INCREMENT,
  `domain` VARCHAR(125) NULL,
  `value` VARCHAR(255) NULL,
  `type` VARCHAR(45) NULL,
  `createTime` INT UNSIGNED NULL,
  `updateTime` INT UNSIGNED NULL,
  PRIMARY KEY (`id`),
  UNIQUE INDEX `id_UNIQUE` (`id` ASC),
  UNIQUE INDEX `domain_UNIQUE` (`domain` ASC));
