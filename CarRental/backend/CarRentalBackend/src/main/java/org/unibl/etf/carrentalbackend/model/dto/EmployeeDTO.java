package org.unibl.etf.carrentalbackend.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.unibl.etf.carrentalbackend.model.enums.EmployeeRole;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeDTO extends UserDTO {
    private EmployeeRole role;
}
