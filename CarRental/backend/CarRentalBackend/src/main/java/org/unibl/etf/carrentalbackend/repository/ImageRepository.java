package org.unibl.etf.carrentalbackend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.unibl.etf.carrentalbackend.model.entities.Image;

public interface ImageRepository extends JpaRepository<Image, Integer> {
}
