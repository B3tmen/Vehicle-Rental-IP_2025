package org.unibl.etf.clientapp.model.dto.requests;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PaymentCardRequest {
    private Integer rentDuration;
    private String cardType;
    private String cardNumber;
    private String expiryMonth;
    private String expiryYear;
    private String cvv;
    private String holderFirstName;
    private String holderLastName;
}
