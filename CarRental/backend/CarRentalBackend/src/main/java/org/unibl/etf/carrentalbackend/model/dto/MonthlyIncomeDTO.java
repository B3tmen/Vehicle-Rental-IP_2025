package org.unibl.etf.carrentalbackend.model.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.unibl.etf.carrentalbackend.util.Constants.DATE_TIME_FORMAT;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MonthlyIncomeDTO {
    @JsonFormat(pattern = DATE_TIME_FORMAT)
    private LocalDateTime date;
    private BigDecimal income;
}
