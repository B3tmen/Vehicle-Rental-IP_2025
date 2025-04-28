package org.unibl.etf.carrentalbackend.model.entities;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.ColumnDefault;
import org.unibl.etf.carrentalbackend.model.enums.UserType;


@Entity
@Table(name = "user")
@Inheritance(strategy = InheritanceType.JOINED)
@Data
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id", nullable = false)
    private Integer id;

    @Column(name = "username", nullable = false, length = 45)
    private String username;

    @Column(name = "password_hash", nullable = false, length = 128)
    private String passwordHash;

    @Column(name = "first_name", nullable = false, length = 45)
    private String firstName;

    @Column(name = "last_name", nullable = false, length = 45)
    private String lastName;

    @Column(name = "type_", nullable = false, length = 31)
    @Enumerated(EnumType.STRING)
    private UserType type;

    @ColumnDefault("1")
    @Column(name = "is_active", nullable = false)
    private Byte isActive;

}