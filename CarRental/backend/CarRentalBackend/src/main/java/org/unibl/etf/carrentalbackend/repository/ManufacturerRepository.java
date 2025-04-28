package org.unibl.etf.carrentalbackend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.unibl.etf.carrentalbackend.model.entities.Manufacturer;

public interface ManufacturerRepository extends JpaRepository<Manufacturer, Integer> {

    @Query("SELECT CASE WHEN COUNT(m) > 0 THEN TRUE ELSE FALSE END " +
            "FROM Manufacturer m " +
            "WHERE m.name = :#{#manufacturer.name} " +
            "AND m.state = :#{#manufacturer.state} " +
            "AND m.address = :#{#manufacturer.address} " +
            "AND m.phoneNumber = :#{#manufacturer.phoneNumber} " +
            "AND m.fax = :#{#manufacturer.fax} " +
            "AND m.email = :#{#manufacturer.email}"
    )
    boolean existsByAllFields(@Param("manufacturer") Manufacturer manufacturer);

    Manufacturer findByName(String name);
    Manufacturer findByPhoneNumber(String phoneNumber);
    Manufacturer findByEmail(String email);
}
