package org.unibl.etf.carrentalbackend.model.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.unibl.etf.carrentalbackend.deserializers.IsoToLocalDateTimeDeserializer;

import java.time.LocalDateTime;

import static org.unibl.etf.carrentalbackend.util.Constants.DATE_TIME_FORMAT;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MalfunctionDTO {
    private Integer id;
    private String reason;
    @JsonDeserialize(using = IsoToLocalDateTimeDeserializer.class)
    @JsonFormat(pattern = DATE_TIME_FORMAT) // Match MySQL format
    private LocalDateTime timeOfMalfunction;
    @JsonFormat(pattern = DATE_TIME_FORMAT) // Match MySQL format
    private LocalDateTime deletedAt;
}
