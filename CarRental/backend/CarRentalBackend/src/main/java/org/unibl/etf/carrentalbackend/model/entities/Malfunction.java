package org.unibl.etf.carrentalbackend.model.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "malfunction")
public class Malfunction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "malfunction_id", nullable = false)
    private Integer id;

    @Lob
    @Column(name = "reason", nullable = false, columnDefinition = "TEXT")
    private String reason;

    @Column(name = "time_of_malfunction", nullable = false)
    private LocalDateTime timeOfMalfunction;

    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;

}