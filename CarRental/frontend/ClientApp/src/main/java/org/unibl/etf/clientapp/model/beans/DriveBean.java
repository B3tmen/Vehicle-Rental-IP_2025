package org.unibl.etf.clientapp.model.beans;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DriveBean implements Serializable {
    private Integer duration;
    private BigDecimal totalPrice;
}
