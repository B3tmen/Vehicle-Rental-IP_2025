package org.unibl.etf.clientapp.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;
import java.util.Calendar;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Payment {
    private Integer id;
    private String token;
    private String type;
    private Date expiryDate;
    private String holderFirstName;
    private String holderLastName;
    private String last4Digits;
    private Client client;
}
