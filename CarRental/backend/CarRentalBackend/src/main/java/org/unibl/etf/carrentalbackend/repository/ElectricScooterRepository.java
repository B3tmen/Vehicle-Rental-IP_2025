package org.unibl.etf.carrentalbackend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.unibl.etf.carrentalbackend.model.entities.ElectricScooter;

public interface ElectricScooterRepository extends JpaRepository<ElectricScooter, Integer> {
    boolean existsByScooterId(String scooterId);
    ElectricScooter findByScooterId(String scooterId);
}
