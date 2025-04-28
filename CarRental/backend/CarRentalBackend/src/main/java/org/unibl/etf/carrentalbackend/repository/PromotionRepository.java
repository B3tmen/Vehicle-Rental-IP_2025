package org.unibl.etf.carrentalbackend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.unibl.etf.carrentalbackend.model.entities.Promotion;

public interface PromotionRepository extends JpaRepository<Promotion, Integer> {
}
