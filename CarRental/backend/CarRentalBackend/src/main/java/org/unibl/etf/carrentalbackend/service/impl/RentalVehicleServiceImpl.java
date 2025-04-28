package org.unibl.etf.carrentalbackend.service.impl;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.unibl.etf.carrentalbackend.model.dto.*;
import org.unibl.etf.carrentalbackend.model.entities.ElectricBicycle;
import org.unibl.etf.carrentalbackend.model.entities.ElectricCar;
import org.unibl.etf.carrentalbackend.model.entities.ElectricScooter;
import org.unibl.etf.carrentalbackend.model.entities.RentalVehicle;
import org.unibl.etf.carrentalbackend.model.enums.VehicleType;
import org.unibl.etf.carrentalbackend.repository.RentalVehicleRepository;
import org.unibl.etf.carrentalbackend.service.interfaces.ImageService;
import org.unibl.etf.carrentalbackend.service.interfaces.RentalVehicleService;
import org.unibl.etf.carrentalbackend.util.Constants;
import org.unibl.etf.carrentalbackend.util.CustomLogger;
import org.unibl.etf.carrentalbackend.util.VehicleHelper;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

@Service
@PropertySource("classpath:custom.properties")
public class RentalVehicleServiceImpl implements RentalVehicleService {
    @Value("${images.base.url}")
    private String vehicleImagesBaseUrl;
    private final CustomLogger logger = CustomLogger.getInstance();

    private final RentalVehicleRepository repository;
    private final ImageService imageService;
    private final ModelMapper mapper;

    @Autowired
    public RentalVehicleServiceImpl(RentalVehicleRepository repository, ImageService imageService, ModelMapper mapper) {
        this.repository = repository;
        this.imageService = imageService;
        this.mapper = mapper;
    }


    @Override
    public List<RentalVehicleDTO> getAll() {
        return repository.findAll()
                .stream()
                .filter(vehicle -> vehicle.getIsActive() == 1)
                .map(vehicle -> {
                    RentalVehicleDTO dto = convertToDTO(vehicle);
                    String relativePath = getRelativePath(dto);
                    VehicleHelper.setImageUrlForVehicle(dto, imageService, relativePath);
                    return dto;
                })
                .toList();
    }

    @Override
    public RentalVehicleDTO getById(int id) {
        RentalVehicle vehicle = repository.findById(id).orElse(null);
        if(vehicle == null || vehicle.getIsActive().equals((byte) 0)){
            return null;
        }
        RentalVehicleDTO dto = convertToDTO(vehicle);
        VehicleHelper.setImageUrlForVehicle(dto, imageService, getRelativePath(dto));

        return dto;
    }

    // We assume only the name of the image is sent
    @Override
    public RentalVehicleDTO insert(RentalVehicleDTO dto) {

        String prefixedUrl = vehicleImagesBaseUrl;
        if(dto instanceof ElectricCarDTO) {
            prefixedUrl += Constants.ImagesRelativePaths.CARS_PATH + dto.getImage().getName();
        }
        else if(dto instanceof ElectricBicycleDTO){
            prefixedUrl += Constants.ImagesRelativePaths.BICYCLES_PATH + dto.getImage().getName();
        }
        else{
            prefixedUrl += Constants.ImagesRelativePaths.SCOOTERS_PATH + dto.getImage().getName();
        }
        dto.getImage().setUrl(prefixedUrl);

        RentalVehicle vehicle = repository.save(convertToEntity(dto));
        return convertToDTO(vehicle);
    }

    @Override
    public RentalVehicleDTO update(RentalVehicleDTO dto) {
        RentalVehicle vehicle = repository.save(convertToEntity(dto));
        return convertToDTO(vehicle);
    }

    @Override
    public boolean delete(Integer id) {
        boolean exists = repository.existsById(id);
        if(!exists){
            return false;
        }

        RentalVehicleDTO vehicle = getById(id);
        vehicle.setIsActive(false);
        RentalVehicleDTO updated = update(vehicle);

        return updated != null;
    }

    @Override
    public RentalVehicleDTO convertToDTO(RentalVehicle entity) {
        if (VehicleType.Car.equals(entity.getType_())) {
            ElectricCar car = (ElectricCar) entity;
            return mapper.map(car, ElectricCarDTO.class);
        }
        else if (VehicleType.Bicycle.equals(entity.getType_())) {
            ElectricBicycle bicycle = (ElectricBicycle) entity;
            return mapper.map(bicycle, ElectricBicycleDTO.class);
        }
        else if (VehicleType.Scooter.equals(entity.getType_())) {
            ElectricScooter scooter = (ElectricScooter) entity;
            return mapper.map(scooter, ElectricScooterDTO.class);
        }

        return null;
    }

    @Override
    public RentalVehicle convertToEntity(RentalVehicleDTO dto) {
        if (VehicleType.Car.equals(dto.getType_())) {
            ElectricCarDTO carDTO = (ElectricCarDTO) dto;
            return mapper.map(carDTO, ElectricCar.class);
        }
        else if (VehicleType.Bicycle.equals(dto.getType_())) {
            ElectricBicycleDTO bicycleDTO = (ElectricBicycleDTO) dto;
            return mapper.map(bicycleDTO, ElectricBicycle.class);
        }
        else if (VehicleType.Scooter.equals(dto.getType_())) {
            ElectricScooterDTO scooterDTO = (ElectricScooterDTO) dto;
            return mapper.map(scooterDTO, ElectricScooter.class);
        }

        return null;
    }

    @Override
    public boolean uploadCSV(MultipartFile file) {
        try{
            List<RentalVehicleDTO> vehicles = new ArrayList<>();
            BufferedReader reader = new BufferedReader(new InputStreamReader(file.getInputStream()));
            String line;

            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");

            }
        } catch (IOException e){
            logger.error(e.getMessage());
        }

        return false;
    }

    @Override
    public List<MalfunctionDTO> getAllMalfunctions(int vehicleId) {
        return List.of();
    }

    private String getRelativePath(RentalVehicleDTO dto){
        if(dto instanceof ElectricCarDTO){
            return Constants.ImagesRelativePaths.CARS_PATH;
        }
        else if(dto instanceof ElectricBicycleDTO){
            return Constants.ImagesRelativePaths.BICYCLES_PATH;
        }
        else{
            return Constants.ImagesRelativePaths.SCOOTERS_PATH;
        }
    }
}
