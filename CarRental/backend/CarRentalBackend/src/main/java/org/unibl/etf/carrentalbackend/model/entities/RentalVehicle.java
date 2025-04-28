package org.unibl.etf.carrentalbackend.model.entities;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.ColumnDefault;
import org.unibl.etf.carrentalbackend.model.enums.VehicleType;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Entity
@Table(name = "rental_vehicle")
@Inheritance(strategy = InheritanceType.JOINED)
public class RentalVehicle {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "vehicle_id", nullable = false)
    private Integer id;

    @Column(name = "model", nullable = false, length = 45)
    private String model;

    @Column(name = "price", nullable = false, precision = 8, scale = 2)
    private BigDecimal price;

    @Column(name = "rental_price", precision = 6, scale = 2)
    private BigDecimal rentalPrice;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "fk_manufacturer_id", nullable = false)
    private Manufacturer manufacturer;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fk_promotion_id")
    private Promotion promotion;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fk_announcement_id")
    private Announcement announcement;

    @Lob
    @Column(name = "type_", nullable = false)
    @Enumerated(EnumType.STRING)
    private VehicleType type_;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "fk_rental_status_id", nullable = false)
    private RentalStatus rentalStatus;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fk_image_id")
    private Image image;

    @ColumnDefault("1")
    @Column(name = "is_active", nullable = false)
    private Byte isActive;

}