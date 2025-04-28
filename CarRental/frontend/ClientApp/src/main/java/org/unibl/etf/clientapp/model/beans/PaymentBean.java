package org.unibl.etf.clientapp.model.beans;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.unibl.etf.clientapp.model.dto.Payment;
import org.unibl.etf.clientapp.util.Constants;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PaymentBean implements Serializable {
    private Payment payment;
}
