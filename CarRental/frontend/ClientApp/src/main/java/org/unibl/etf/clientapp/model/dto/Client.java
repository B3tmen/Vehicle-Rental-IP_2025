package org.unibl.etf.clientapp.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.unibl.etf.clientapp.model.enums.CitizenType;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Client {
    private Integer id;
    private String username;
    private String passwordHash;
    private String firstName;
    private String lastName;
    private String personalCardNumber;
    private String email;
    private String phoneNumber;
    private Boolean isActive;
    private CitizenType citizenType;
    private Integer driversLicence;

    private Image avatarImage;
}
