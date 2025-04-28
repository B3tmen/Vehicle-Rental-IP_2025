package org.unibl.etf.carrentalbackend.service.impl;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.unibl.etf.carrentalbackend.exception.CustomVehicleIdConflictException;
import org.unibl.etf.carrentalbackend.exception.InvalidCsvFormatException;
import org.unibl.etf.carrentalbackend.exception.InvalidDateTimeFormatException;
import org.unibl.etf.carrentalbackend.model.entities.*;
import org.unibl.etf.carrentalbackend.model.enums.VehicleType;
import org.unibl.etf.carrentalbackend.repository.*;
import org.unibl.etf.carrentalbackend.service.interfaces.ElectricBicycleService;
import org.unibl.etf.carrentalbackend.service.interfaces.ElectricCarService;
import org.unibl.etf.carrentalbackend.service.interfaces.ElectricScooterService;
import org.unibl.etf.carrentalbackend.service.interfaces.VehicleCsvService;
import org.unibl.etf.carrentalbackend.util.Constants;
import org.unibl.etf.carrentalbackend.util.CustomLogger;

import java.io.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;

@Service
public class VehicleCsvServiceImpl implements VehicleCsvService {
    private final CustomLogger logger = CustomLogger.getInstance();
    private final RentalVehicleRepository vehicleRepository;
    private final ElectricCarService electricCarService;
    private final ElectricBicycleService electricBicycleService;
    private final ElectricScooterService electricScooterService;
    private final ManufacturerRepository manufacturerRepository;

    @Autowired
    public VehicleCsvServiceImpl(RentalVehicleRepository vehicleRepository, ElectricCarService electricCarService, ElectricBicycleService electricBicycleService, ElectricScooterService electricScooterService, ManufacturerRepository manufacturerRepository) {
        this.vehicleRepository = vehicleRepository;
        this.electricCarService = electricCarService;
        this.electricBicycleService = electricBicycleService;
        this.electricScooterService = electricScooterService;
        this.manufacturerRepository = manufacturerRepository;
    }

    @Override
    public List<RentalVehicle> save(MultipartFile file) {
        List<RentalVehicle> addedVehicles = new ArrayList<>();
        try(BufferedReader br = new BufferedReader(new InputStreamReader(file.getInputStream()));
            CSVReader csvReader = new CSVReader(br)){

            List<RentalVehicle> vehicles = new ArrayList<>();

            String[] headers = csvReader.readNext();
            String[] line;

            while((line = csvReader.readNext()) != null){

                RentalVehicle vehicle = null;
                if(line.length > 0){
                    if(headers[4].equals("manufId")){
                        Manufacturer manufacturerTemp = readManufacturer(line);

                        if(manufacturerTemp == null) throw new InvalidCsvFormatException();

                        if(!manufacturerRepository.existsByAllFields(manufacturerTemp) && manufacturerRepository.findById(manufacturerTemp.getId()).isEmpty()){
                            manufacturerTemp.setId(null);
                            manufacturerRepository.save(manufacturerTemp);
                        }
                        else{
                            String name = manufacturerTemp.getName();
                            Manufacturer manufacturer = manufacturerRepository.findByName(name);

                            switch(line[3]){    // type field
                                case "Car" -> {
                                    vehicle = readElectricCar(line, manufacturer);
                                }
                                case "Bicycle" -> {
                                    vehicle = readElectricBicycle(line, manufacturer);
                                }
                                case "Scooter" -> {
                                    vehicle = readElectricScooter(line, manufacturer);
                                }
                                default -> throw new InvalidCsvFormatException();
                            }
                        }

                    }
                    else{
                        throw new InvalidCsvFormatException();
                    }

                    if(vehicle != null) {
                        vehicle.setIsActive((byte) 1);
                        vehicles.add(vehicle);
                    }
                }

            }

            if(!vehicles.isEmpty()){
                addedVehicles = vehicleRepository.saveAll(vehicles);
                logger.info("Added vehicle(s) from CSV file");
            }
        } catch (IOException | NumberFormatException | CsvValidationException e){
            e.printStackTrace();
        }

        return addedVehicles;
    }

    private RentalVehicle readElectricScooter(String[] line, Manufacturer manufacturer) throws NumberFormatException {
        int index = 12;
        boolean inBounds = index < line.length;
        if(inBounds){
            String scooterId = line[11];

            if(!electricScooterService.existsByScooterId(scooterId)){
                Integer maxSpeed = Integer.parseInt(line[12]);

                return ElectricScooter.builder()
                        .id(null)
                        .model(line[0])
                        .price(new BigDecimal(line[1]))
                        .rentalPrice(new BigDecimal(line[2]))
                        .rentalStatus(new RentalStatus(1,"Free"))
                        .type_(VehicleType.Scooter)
                        .manufacturer(manufacturer)
                        .scooterId(scooterId)
                        .maxSpeed(maxSpeed)
                        .build();
            }
        }
        return null;
    }

    private RentalVehicle readElectricBicycle(String[] line, Manufacturer manufacturer) throws NumberFormatException {
        int index = 12;
        boolean inBounds = index < line.length;
        if(inBounds){
            String bicycleId = line[11];
            if(!electricBicycleService.existsByBicycleId(bicycleId)){
                Integer autonomy = Integer.parseInt(line[12]);

                return ElectricBicycle.builder()
                        .id(null)
                        .model(line[0])
                        .price(new BigDecimal(line[1]))
                        .rentalPrice(new BigDecimal(line[2]))
                        .rentalStatus(new RentalStatus(1,"Free"))
                        .type_(VehicleType.Bicycle)
                        .manufacturer(manufacturer)
                        .bicycleId(bicycleId)
                        .ridingAutonomy(autonomy)
                        .build();
            }
        }
        return null;
    }

    private RentalVehicle readElectricCar(String[] line, Manufacturer manufacturer) throws NumberFormatException {
        int index = 13;
        boolean inBounds = index < line.length;
        if(inBounds){
            String carId = line[11];
            if(electricCarService.existsByCarId(carId)){
                throw new CustomVehicleIdConflictException();
            }

            DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern(Constants.DATE_TIME_FORMAT);
            LocalDateTime purchaseDate = null;
            try{
                purchaseDate = LocalDateTime.parse(line[12], dateFormatter);
            } catch (DateTimeParseException e){
                throw new InvalidDateTimeFormatException();
            }

            String description = line[13];

            return ElectricCar.builder()
                    .id(null)
                    .model(line[0])
                    .price(new BigDecimal(line[1]))
                    .rentalPrice(new BigDecimal(line[2]))
                    .rentalStatus(new RentalStatus(1,"Free"))
                    .type_(VehicleType.Car)
                    .manufacturer(manufacturer)
                    .carId(carId)
                    .purchaseDate(purchaseDate)
                    .description(description)
                    .build();
        }
        return null;
    }

    private Manufacturer readManufacturer(String[] line){
        int index = 10;
        boolean isBounded = index < line.length;
        if(isBounded){
            int manufId = Integer.parseInt(line[4]);
            String name = line[5];
            String state = line[6];
            String address = line[7];
            String phoneNumber = line[8];
            String fax = line[9];
            String email = line[10];

            return new Manufacturer(manufId, name, state, address, phoneNumber, fax, email, null);
        }

        return null;
    }
}
