package org.unibl.etf.carrentalbackend.model.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDateTime;

import static org.unibl.etf.carrentalbackend.util.Constants.DATE_TIME_FORMAT;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class ManufacturerDTO {
    private Integer id;
    private String name;
    private String state;
    private String address;
    private String phoneNumber;
    private String fax;
    private String email;
    @JsonFormat(pattern = DATE_TIME_FORMAT) // Match MySQL format
    private LocalDateTime deletedAt;
}
