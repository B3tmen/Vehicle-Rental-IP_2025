package org.unibl.etf.carrentalbackend.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.unibl.etf.carrentalbackend.model.enums.EmployeeRole;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AuthenticationResponseDTO {
    private UserDTO user;
    private String jwtToken;
    private List<EmployeeRole> roles;
}
