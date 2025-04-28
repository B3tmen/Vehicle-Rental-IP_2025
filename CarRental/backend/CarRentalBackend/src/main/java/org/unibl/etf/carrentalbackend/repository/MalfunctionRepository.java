package org.unibl.etf.carrentalbackend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.unibl.etf.carrentalbackend.model.entities.Malfunction;

public interface MalfunctionRepository extends JpaRepository<Malfunction, Integer> {
}
