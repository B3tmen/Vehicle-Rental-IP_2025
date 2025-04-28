package org.unibl.etf.carrentalbackend.model.entities;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.unibl.etf.carrentalbackend.model.enums.EmployeeRole;

@Data
@Entity
@Table(name = "employee")
@PrimaryKeyJoinColumn(name = "fk_user_id")
public class Employee extends User {

    @Column(name = "role", nullable = false)
    @Enumerated(EnumType.STRING)
    private EmployeeRole role;

}