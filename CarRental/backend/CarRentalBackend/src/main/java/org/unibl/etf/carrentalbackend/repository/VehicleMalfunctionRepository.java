package org.unibl.etf.carrentalbackend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.unibl.etf.carrentalbackend.model.entities.Malfunction;
import org.unibl.etf.carrentalbackend.model.entities.VehicleMalfunction;

import java.util.List;

public interface VehicleMalfunctionRepository extends JpaRepository<VehicleMalfunction, Integer> {
    List<VehicleMalfunction> findByVehicleId(Integer vehicleId);
}
