package org.unibl.etf.clientapp.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.sql.Timestamp;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Invoice {
    private Integer id;
    private String pdfName;
    private String invoiceURL;
    private Timestamp issueDate;
    private BigDecimal grandTotal;
    private Rental rental;
}
