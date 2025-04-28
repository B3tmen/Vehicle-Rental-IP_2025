package org.unibl.etf.carrentalbackend.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.unibl.etf.carrentalbackend.model.enums.UserType;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {
    private Integer id;
    private String username;
    private String passwordHash;
    private String firstName;
    private String lastName;
    private UserType type;
    private Boolean isActive;

}
