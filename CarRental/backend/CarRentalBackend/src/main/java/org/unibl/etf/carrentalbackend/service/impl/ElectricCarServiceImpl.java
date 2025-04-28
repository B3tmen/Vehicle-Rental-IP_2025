package org.unibl.etf.carrentalbackend.service.impl;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.unibl.etf.carrentalbackend.exception.CustomVehicleIdConflictException;
import org.unibl.etf.carrentalbackend.interfaces.EntityDTOConverter;
import org.unibl.etf.carrentalbackend.model.dto.ElectricCarDTO;
import org.unibl.etf.carrentalbackend.model.entities.ElectricCar;
import org.unibl.etf.carrentalbackend.repository.ElectricCarRepository;
import org.unibl.etf.carrentalbackend.service.interfaces.ElectricCarService;
import org.unibl.etf.carrentalbackend.service.interfaces.ImageService;
import org.unibl.etf.carrentalbackend.util.Constants;
import org.unibl.etf.carrentalbackend.util.VehicleHelper;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ElectricCarServiceImpl implements ElectricCarService, EntityDTOConverter<ElectricCar, ElectricCarDTO> {

    private final ElectricCarRepository repository;
    private final ImageService imageService;
    private final ModelMapper mapper;

    @Autowired
    public ElectricCarServiceImpl(ElectricCarRepository repository, ImageService imageService, ModelMapper mapper) {
        this.repository = repository;
        this.imageService = imageService;
        this.mapper = mapper;
    }


    @Override
    public List<ElectricCarDTO> getAll() {
        return repository.findAll()
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public ElectricCarDTO getById(int id) {
        ElectricCar car = repository.findById(id).orElse(null);
        ElectricCarDTO dto = convertToDTO(car);
        VehicleHelper.setImageUrlForVehicle(dto, imageService, Constants.ImagesRelativePaths.CARS_PATH);

        return dto;
    }

    @Override
    public ElectricCarDTO insert(ElectricCarDTO dto) {
        if(dto.getImage() != null && dto.getImage().getName().isEmpty()) {
            dto.setImage(null);
        }

        ElectricCar car = repository.save(convertToEntity(dto));
        return convertToDTO(car);
    }

    @Override
    public ElectricCarDTO update(ElectricCarDTO dto) {


        ElectricCar car = repository.save(convertToEntity(dto));
        return convertToDTO(car);
    }

    @Override
    public ElectricCarDTO insert(ElectricCarDTO dto, MultipartFile imageFile) {
        if(repository.existsByCarId(dto.getCarId()))
            throw new CustomVehicleIdConflictException();

        if(imageFile != null)
            VehicleHelper.addImage(dto, imageService, Constants.ImagesRelativePaths.CARS_PATH, imageFile);

        return insert(dto);
    }

    @Override
    public ElectricCarDTO update(ElectricCarDTO dto, MultipartFile imageFile) {
        ElectricCar byCarId = repository.findByCarId(dto.getCarId());
        if(byCarId != null && !byCarId.getId().equals(dto.getId()))
            throw new CustomVehicleIdConflictException("Car with given car ID already exists.");

        if(imageFile != null)
            VehicleHelper.updateOldImage(dto, imageService, Constants.ImagesRelativePaths.CARS_PATH, imageFile);

        ElectricCarDTO carDTO = update(dto);
        VehicleHelper.setImageUrlForVehicle(carDTO, imageService, Constants.ImagesRelativePaths.CARS_PATH);

        return carDTO;
    }

    @Override
    public boolean delete(Integer id) {
        boolean exists = repository.existsById(id);
        if(exists){
            repository.deleteById(id);
            return !repository.existsById(id);
        }

        return false;
    }

    @Override
    public boolean existsByCarId(String carId) {
        return repository.existsByCarId(carId);
    }

    @Override
    public ElectricCarDTO convertToDTO(ElectricCar entity) {
        return mapper.map(entity, ElectricCarDTO.class);
    }

    @Override
    public ElectricCar convertToEntity(ElectricCarDTO dto) {
        return mapper.map(dto, ElectricCar.class);
    }


}
