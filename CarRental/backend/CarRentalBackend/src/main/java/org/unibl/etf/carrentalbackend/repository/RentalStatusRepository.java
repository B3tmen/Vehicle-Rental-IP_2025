package org.unibl.etf.carrentalbackend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.unibl.etf.carrentalbackend.model.entities.RentalStatus;

public interface RentalStatusRepository extends JpaRepository<RentalStatus, Integer> {
}
