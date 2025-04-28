package org.unibl.etf.carrentalbackend.model.entities;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "rental_status")
public class RentalStatus {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "rental_status_id", nullable = false)
    private Integer id;

    @ColumnDefault("'Free'")
    @Column(name = "status", nullable = false, length = 20)
    private String status;

}