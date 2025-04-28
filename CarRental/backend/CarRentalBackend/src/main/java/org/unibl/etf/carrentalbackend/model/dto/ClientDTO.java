package org.unibl.etf.carrentalbackend.model.dto;

import lombok.*;
import org.unibl.etf.carrentalbackend.model.enums.CitizenType;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class ClientDTO extends UserDTO {
    private String personalCardNumber;
    private String email;
    private String phoneNumber;
    private CitizenType citizenType;
    private Integer driversLicence;

    private ImageDTO avatarImage;
}
