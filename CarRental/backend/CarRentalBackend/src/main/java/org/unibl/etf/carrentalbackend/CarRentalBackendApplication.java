package org.unibl.etf.carrentalbackend;

import org.modelmapper.ModelMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.unibl.etf.carrentalbackend.service.impl.RentalVehicleServiceImpl;
import org.unibl.etf.carrentalbackend.service.interfaces.RentalVehicleService;
import org.unibl.etf.carrentalbackend.util.UUIDHelper;

@SpringBootApplication
public class CarRentalBackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(CarRentalBackendApplication.class, args);


    }


}
