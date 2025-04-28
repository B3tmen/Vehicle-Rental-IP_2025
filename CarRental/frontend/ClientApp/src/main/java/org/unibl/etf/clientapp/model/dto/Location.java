package org.unibl.etf.clientapp.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Location {
    private Integer id;
    private BigDecimal latitude;
    private BigDecimal longitude;
}
