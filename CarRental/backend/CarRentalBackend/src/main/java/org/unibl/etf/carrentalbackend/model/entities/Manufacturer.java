package org.unibl.etf.carrentalbackend.model.entities;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "manufacturer")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Manufacturer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "manufacturer_id", nullable = false)
    private Integer id;

    @Column(name = "name", nullable = false, length = 45)
    private String name;

    @Column(name = "state", nullable = false, length = 45)
    private String state;

    @Column(name = "address", nullable = false, length = 45)
    private String address;

    @Column(name = "phone_number", nullable = false, length = 45)
    private String phoneNumber;

    @Column(name = "fax", nullable = false, length = 45)
    private String fax;

    @Column(name = "email", nullable = false, length = 45)
    private String email;

    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;

}