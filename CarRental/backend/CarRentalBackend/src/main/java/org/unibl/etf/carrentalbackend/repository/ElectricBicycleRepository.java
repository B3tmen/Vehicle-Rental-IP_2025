package org.unibl.etf.carrentalbackend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.unibl.etf.carrentalbackend.model.entities.ElectricBicycle;

public interface ElectricBicycleRepository extends JpaRepository<ElectricBicycle, Integer> {
    boolean existsByBicycleId(String bicycleId);
    ElectricBicycle findByBicycleId(String bicycleId);
}
