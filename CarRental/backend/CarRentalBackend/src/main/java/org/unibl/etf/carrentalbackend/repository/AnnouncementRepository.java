package org.unibl.etf.carrentalbackend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.unibl.etf.carrentalbackend.model.entities.Announcement;

public interface AnnouncementRepository extends JpaRepository<Announcement, Integer> {
}
