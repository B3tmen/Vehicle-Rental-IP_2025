-- MySQL Workbench Forward Engineering

SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION';

-- -----------------------------------------------------
-- Schema db_car-rental
-- -----------------------------------------------------

-- -----------------------------------------------------
-- Schema db_car-rental
-- -----------------------------------------------------
CREATE SCHEMA IF NOT EXISTS `db_car-rental` DEFAULT CHARACTER SET utf8 ;
USE `db_car-rental` ;

-- -----------------------------------------------------
-- Table `db_car-rental`.`manufacturer`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `db_car-rental`.`manufacturer` (
  `manufacturer_id` INT NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(45) NOT NULL,
  `state` VARCHAR(45) NOT NULL,
  `address` VARCHAR(45) NOT NULL,
  `phone_number` VARCHAR(45) CHARACTER SET 'ascii' NOT NULL,
  `fax` VARCHAR(45) NOT NULL,
  `email` VARCHAR(45) NOT NULL,
  `deleted_at` DATETIME NULL DEFAULT NULL,
  PRIMARY KEY (`manufacturer_id`),
  UNIQUE INDEX `name_UNIQUE` (`name` ASC) VISIBLE,
  UNIQUE INDEX `phone_number_UNIQUE` (`phone_number` ASC) VISIBLE,
  UNIQUE INDEX `fax_UNIQUE` (`fax` ASC) VISIBLE,
  UNIQUE INDEX `email_UNIQUE` (`email` ASC) VISIBLE)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `db_car-rental`.`promotion`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `db_car-rental`.`promotion` (
  `promotion_id` INT NOT NULL AUTO_INCREMENT,
  `title` VARCHAR(45) NOT NULL,
  `description` TEXT(1000) NOT NULL,
  `duration` DATE NOT NULL,
  PRIMARY KEY (`promotion_id`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `db_car-rental`.`announcement`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `db_car-rental`.`announcement` (
  `announcement_id` INT NOT NULL AUTO_INCREMENT,
  `title` VARCHAR(45) NOT NULL,
  `content` TEXT(2000) NOT NULL,
  PRIMARY KEY (`announcement_id`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `db_car-rental`.`rental_status`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `db_car-rental`.`rental_status` (
  `rental_status_id` INT NOT NULL AUTO_INCREMENT,
  `status` VARCHAR(20) NOT NULL DEFAULT 'Free',
  PRIMARY KEY (`rental_status_id`),
  UNIQUE INDEX `status_UNIQUE` (`status` ASC) VISIBLE)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `db_car-rental`.`image`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `db_car-rental`.`image` (
  `image_id` INT NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(150) NOT NULL,
  `type` VARCHAR(20) NOT NULL,
  PRIMARY KEY (`image_id`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `db_car-rental`.`rental_vehicle`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `db_car-rental`.`rental_vehicle` (
  `vehicle_id` INT NOT NULL AUTO_INCREMENT,
  `model` VARCHAR(45) NOT NULL,
  `price` DECIMAL(8,2) NOT NULL,
  `rental_price` DECIMAL(6,2) NULL,
  `type_` ENUM('Car', 'Bicycle', 'Scooter') NOT NULL,
  `is_active` TINYINT NOT NULL DEFAULT 1,
  `fk_manufacturer_id` INT NOT NULL,
  `fk_image_id` INT NULL,
  `fk_rental_status_id` INT NOT NULL,
  `fk_promotion_id` INT NULL,
  `fk_announcement_id` INT NULL,
  PRIMARY KEY (`vehicle_id`),
  INDEX `fk_rental_vehicle_manufacturer1_idx` (`fk_manufacturer_id` ASC) VISIBLE,
  INDEX `fk_rental_vehicle_promotion1_idx` (`fk_promotion_id` ASC) VISIBLE,
  INDEX `fk_rental_vehicle_announcment1_idx` (`fk_announcement_id` ASC) VISIBLE,
  INDEX `fk_rental_vehicle_rental_status1_idx` (`fk_rental_status_id` ASC) VISIBLE,
  INDEX `fk_rental_vehicle_image1_idx` (`fk_image_id` ASC) VISIBLE,
  CONSTRAINT `fk_rental_vehicle_manufacturer1`
    FOREIGN KEY (`fk_manufacturer_id`)
    REFERENCES `db_car-rental`.`manufacturer` (`manufacturer_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_rental_vehicle_promotion1`
    FOREIGN KEY (`fk_promotion_id`)
    REFERENCES `db_car-rental`.`promotion` (`promotion_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_rental_vehicle_announcment1`
    FOREIGN KEY (`fk_announcement_id`)
    REFERENCES `db_car-rental`.`announcement` (`announcement_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_rental_vehicle_rental_status1`
    FOREIGN KEY (`fk_rental_status_id`)
    REFERENCES `db_car-rental`.`rental_status` (`rental_status_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_rental_vehicle_image1`
    FOREIGN KEY (`fk_image_id`)
    REFERENCES `db_car-rental`.`image` (`image_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `db_car-rental`.`electric_car`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `db_car-rental`.`electric_car` (
  `fk_vehicle_id` INT NOT NULL,
  `car_id` VARCHAR(50) NOT NULL,
  `purchase_date` DATETIME NOT NULL,
  `description` VARCHAR(45) NOT NULL,
  PRIMARY KEY (`fk_vehicle_id`),
  UNIQUE INDEX `unique_id_UNIQUE` (`car_id` ASC) VISIBLE,
  INDEX `fk_electric_car_rental_vehicle1_idx` (`fk_vehicle_id` ASC) VISIBLE,
  CONSTRAINT `fk_electric_car_rental_vehicle1`
    FOREIGN KEY (`fk_vehicle_id`)
    REFERENCES `db_car-rental`.`rental_vehicle` (`vehicle_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `db_car-rental`.`electric_bicycle`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `db_car-rental`.`electric_bicycle` (
  `fk_vehicle_id` INT NOT NULL,
  `bicycle_id` VARCHAR(50) NOT NULL,
  `riding_autonomy` INT NOT NULL,
  UNIQUE INDEX `bicycle_id_UNIQUE` (`bicycle_id` ASC) VISIBLE,
  PRIMARY KEY (`fk_vehicle_id`),
  CONSTRAINT `fk_electric_bicycle_rental_vehicle1`
    FOREIGN KEY (`fk_vehicle_id`)
    REFERENCES `db_car-rental`.`rental_vehicle` (`vehicle_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `db_car-rental`.`electric_scooter`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `db_car-rental`.`electric_scooter` (
  `fk_vehicle_id` INT NOT NULL,
  `scooter_id` VARCHAR(50) NOT NULL,
  `max_speed` INT NOT NULL,
  UNIQUE INDEX `scooter_id_UNIQUE` (`scooter_id` ASC) VISIBLE,
  PRIMARY KEY (`fk_vehicle_id`),
  CONSTRAINT `fk_electric_scooter_rental_vehicle1`
    FOREIGN KEY (`fk_vehicle_id`)
    REFERENCES `db_car-rental`.`rental_vehicle` (`vehicle_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `db_car-rental`.`malfunction`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `db_car-rental`.`malfunction` (
  `malfunction_id` INT NOT NULL AUTO_INCREMENT,
  `reason` TEXT(1000) NOT NULL,
  `time_of_malfunction` DATETIME NOT NULL,
  `deleted_at` DATETIME NULL DEFAULT NULL,
  PRIMARY KEY (`malfunction_id`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `db_car-rental`.`user`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `db_car-rental`.`user` (
  `user_id` INT NOT NULL AUTO_INCREMENT,
  `username` VARCHAR(45) NOT NULL,
  `password_hash` VARCHAR(128) NOT NULL,
  `first_name` VARCHAR(45) NOT NULL,
  `last_name` VARCHAR(45) NOT NULL,
  `type_` ENUM('Client', 'Employee') NOT NULL,
  `is_active` TINYINT NOT NULL DEFAULT 1,
  PRIMARY KEY (`user_id`),
  UNIQUE INDEX `username_UNIQUE` (`username` ASC) VISIBLE)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `db_car-rental`.`client`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `db_car-rental`.`client` (
  `fk_user_id` INT NOT NULL,
  `personal_card_number` CHAR(13) NOT NULL,
  `email` VARCHAR(45) NOT NULL,
  `phone_number` VARCHAR(45) NOT NULL,
  `citizen_type` ENUM('Local', 'Foreigner') NOT NULL,
  `drivers_licence` INT NULL,
  `fk_avatar_image_id` INT NULL,
  PRIMARY KEY (`fk_user_id`),
  UNIQUE INDEX `personal_card_id_UNIQUE` (`personal_card_number` ASC) VISIBLE,
  INDEX `fk_client_image1_idx` (`fk_avatar_image_id` ASC) VISIBLE,
  UNIQUE INDEX `phone_number_UNIQUE` (`phone_number` ASC) VISIBLE,
  CONSTRAINT `fk_client_user1`
    FOREIGN KEY (`fk_user_id`)
    REFERENCES `db_car-rental`.`user` (`user_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_client_image1`
    FOREIGN KEY (`fk_avatar_image_id`)
    REFERENCES `db_car-rental`.`image` (`image_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `db_car-rental`.`employee`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `db_car-rental`.`employee` (
  `fk_user_id` INT NOT NULL,
  `role` ENUM('Administrator', 'Operator', 'Manager') NOT NULL,
  PRIMARY KEY (`fk_user_id`),
  CONSTRAINT `fk_employee_user1`
    FOREIGN KEY (`fk_user_id`)
    REFERENCES `db_car-rental`.`user` (`user_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `db_car-rental`.`location`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `db_car-rental`.`location` (
  `location_id` INT NOT NULL AUTO_INCREMENT,
  `latitude` DECIMAL(9,6) NOT NULL,
  `longitude` DECIMAL(9,6) NOT NULL,
  PRIMARY KEY (`location_id`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `db_car-rental`.`payment`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `db_car-rental`.`payment` (
  `payment_id` INT NOT NULL AUTO_INCREMENT,
  `token` VARCHAR(128) NOT NULL,
  `type` ENUM('Visa', 'MasterCard') NOT NULL,
  `expiry_date` DATE NOT NULL,
  `card_holder_first_name` VARCHAR(45) NOT NULL,
  `card_holder_last_name` VARCHAR(45) NOT NULL,
  `card_last_4_digits` CHAR(4) NOT NULL,
  `fk_client_id` INT NOT NULL,
  PRIMARY KEY (`payment_id`),
  UNIQUE INDEX `card_token_UNIQUE` (`token` ASC) VISIBLE,
  INDEX `fk_payment_client1_idx` (`fk_client_id` ASC) VISIBLE,
  CONSTRAINT `fk_payment_client1`
    FOREIGN KEY (`fk_client_id`)
    REFERENCES `db_car-rental`.`client` (`fk_user_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `db_car-rental`.`rental`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `db_car-rental`.`rental` (
  `rental_id` INT NOT NULL AUTO_INCREMENT,
  `fk_vehicle_id` INT NOT NULL,
  `fk_client_id` INT NOT NULL,
  `rental_date_time` DATETIME NOT NULL,
  `duration` INT NOT NULL,
  `fk_pickup_location_id` INT NOT NULL,
  `fk_dropoff_location_id` INT NULL,
  `fk_payment_id` INT NOT NULL,
  PRIMARY KEY (`rental_id`),
  INDEX `fk_rental_location1_idx` (`fk_pickup_location_id` ASC) VISIBLE,
  INDEX `fk_rental_location2_idx` (`fk_dropoff_location_id` ASC) VISIBLE,
  INDEX `fk_rental_rental_vehicle1_idx` (`fk_vehicle_id` ASC) VISIBLE,
  INDEX `fk_rental_client1_idx` (`fk_client_id` ASC) VISIBLE,
  INDEX `fk_rental_payment1_idx` (`fk_payment_id` ASC) VISIBLE,
  CONSTRAINT `fk_rental_location1`
    FOREIGN KEY (`fk_pickup_location_id`)
    REFERENCES `db_car-rental`.`location` (`location_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_rental_location2`
    FOREIGN KEY (`fk_dropoff_location_id`)
    REFERENCES `db_car-rental`.`location` (`location_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_rental_rental_vehicle1`
    FOREIGN KEY (`fk_vehicle_id`)
    REFERENCES `db_car-rental`.`rental_vehicle` (`vehicle_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_rental_client1`
    FOREIGN KEY (`fk_client_id`)
    REFERENCES `db_car-rental`.`client` (`fk_user_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_rental_payment1`
    FOREIGN KEY (`fk_payment_id`)
    REFERENCES `db_car-rental`.`payment` (`payment_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `db_car-rental`.`invoice`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `db_car-rental`.`invoice` (
  `invoice_id` INT NOT NULL AUTO_INCREMENT,
  `pdf_name` VARCHAR(50) NOT NULL,
  `issue_date` DATETIME NOT NULL,
  `grand_total` DECIMAL(9,2) NOT NULL,
  `fk_rental_id` INT NOT NULL,
  PRIMARY KEY (`invoice_id`),
  INDEX `fk_invoice_rental1_idx` (`fk_rental_id` ASC) VISIBLE,
  CONSTRAINT `fk_invoice_rental1`
    FOREIGN KEY (`fk_rental_id`)
    REFERENCES `db_car-rental`.`rental` (`rental_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `db_car-rental`.`passport`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `db_car-rental`.`passport` (
  `passport_id` INT NOT NULL AUTO_INCREMENT,
  `passport_number` VARCHAR(45) NOT NULL,
  `country` VARCHAR(45) NOT NULL,
  `valid_from` DATE NOT NULL,
  `valid_to` DATE NOT NULL,
  `fk_client_id` INT NOT NULL,
  PRIMARY KEY (`passport_id`),
  UNIQUE INDEX `passport_number_UNIQUE` (`passport_number` ASC) VISIBLE,
  INDEX `fk_passport_client1_idx` (`fk_client_id` ASC) VISIBLE,
  CONSTRAINT `fk_passport_client1`
    FOREIGN KEY (`fk_client_id`)
    REFERENCES `db_car-rental`.`client` (`fk_user_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `db_car-rental`.`vehicle_malfunction`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `db_car-rental`.`vehicle_malfunction` (
  `fk_vehicle_id` INT NOT NULL,
  `fk_malfunction_id` INT NOT NULL,
  PRIMARY KEY (`fk_vehicle_id`, `fk_malfunction_id`),
  INDEX `fk_vehicle_malfunction_malfunction1_idx` (`fk_malfunction_id` ASC) VISIBLE,
  CONSTRAINT `fk_vehicle_malfunction_rental_vehicle1`
    FOREIGN KEY (`fk_vehicle_id`)
    REFERENCES `db_car-rental`.`rental_vehicle` (`vehicle_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_vehicle_malfunction_malfunction1`
    FOREIGN KEY (`fk_malfunction_id`)
    REFERENCES `db_car-rental`.`malfunction` (`malfunction_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;
