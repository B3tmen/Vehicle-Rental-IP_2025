package org.unibl.etf.carrentalbackend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.unibl.etf.carrentalbackend.model.entities.ElectricCar;

public interface ElectricCarRepository extends JpaRepository<ElectricCar, Integer> {
    boolean existsByCarId(String carId);
    ElectricCar findByCarId(String carId);
}
