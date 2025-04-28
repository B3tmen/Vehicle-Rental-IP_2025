package org.unibl.etf.carrentalbackend.model.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.unibl.etf.carrentalbackend.deserializers.IsoToLocalDateTimeDeserializer;
import org.unibl.etf.carrentalbackend.util.Constants;

import java.time.LocalDateTime;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class ElectricCarDTO extends RentalVehicleDTO {
    private String carId;
    @JsonDeserialize(using = IsoToLocalDateTimeDeserializer.class)
    @JsonFormat(pattern = Constants.DATE_TIME_FORMAT)
    private LocalDateTime purchaseDate;
    private String description;
}
