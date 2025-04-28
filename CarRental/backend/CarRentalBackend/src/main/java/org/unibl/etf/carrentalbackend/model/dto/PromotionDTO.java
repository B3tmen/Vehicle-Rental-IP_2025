package org.unibl.etf.carrentalbackend.model.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;

import static org.unibl.etf.carrentalbackend.util.Constants.DATE_FORMAT;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PromotionDTO {
    private Integer id;
    private String title;
    private String description;
    @JsonFormat(pattern = DATE_FORMAT)
    private Date duration;
}
