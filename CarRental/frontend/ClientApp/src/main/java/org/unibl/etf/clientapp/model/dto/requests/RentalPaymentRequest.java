package org.unibl.etf.clientapp.model.dto.requests;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.unibl.etf.clientapp.model.dto.Location;
import org.unibl.etf.clientapp.model.dto.Passport;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RentalPaymentRequest {
    private Location startLocation;
    private Location endLocation;
    private Passport foreignerPassport;
    private PaymentCardRequest paymentData;
}
