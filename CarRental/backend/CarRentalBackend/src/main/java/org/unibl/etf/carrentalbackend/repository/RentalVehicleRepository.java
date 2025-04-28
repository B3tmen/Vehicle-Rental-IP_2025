package org.unibl.etf.carrentalbackend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.unibl.etf.carrentalbackend.model.entities.RentalVehicle;

public interface RentalVehicleRepository extends JpaRepository<RentalVehicle, Integer> {
}
