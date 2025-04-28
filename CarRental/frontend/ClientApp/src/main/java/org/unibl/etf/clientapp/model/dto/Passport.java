package org.unibl.etf.clientapp.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class Passport {
    private Integer id;
    private String passportNumber;
    private String country;

    private Date validFrom;
    private Date validTo;
}
