package org.unibl.etf.carrentalbackend.service.impl;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.unibl.etf.carrentalbackend.exception.CustomVehicleIdConflictException;
import org.unibl.etf.carrentalbackend.interfaces.EntityDTOConverter;
import org.unibl.etf.carrentalbackend.model.dto.ElectricScooterDTO;
import org.unibl.etf.carrentalbackend.model.entities.ElectricScooter;
import org.unibl.etf.carrentalbackend.repository.ElectricScooterRepository;
import org.unibl.etf.carrentalbackend.service.interfaces.ElectricScooterService;
import org.unibl.etf.carrentalbackend.service.interfaces.ImageService;
import org.unibl.etf.carrentalbackend.util.Constants;
import org.unibl.etf.carrentalbackend.util.VehicleHelper;

import java.util.List;

@Service
public class ElectricScooterServiceImpl implements ElectricScooterService, EntityDTOConverter<ElectricScooter, ElectricScooterDTO> {

    private final ElectricScooterRepository repository;
    private final ImageService imageService;
    private final ModelMapper mapper;

    @Autowired
    public ElectricScooterServiceImpl(ElectricScooterRepository repository, ImageService imageService, ModelMapper mapper) {
        this.repository = repository;
        this.imageService = imageService;
        this.mapper = mapper;
    }

    @Override
    public List<ElectricScooterDTO> getAll() {
        return List.of();
    }

    @Override
    public ElectricScooterDTO getById(int id) {
        ElectricScooter scooter = repository.findById(id).orElse(null);
        ElectricScooterDTO dto = convertToDTO(scooter);
        VehicleHelper.setImageUrlForVehicle(dto, imageService, Constants.ImagesRelativePaths.SCOOTERS_PATH);

        return dto;
    }

    @Override
    public ElectricScooterDTO insert(ElectricScooterDTO dto) {
        ElectricScooter entity = repository.save(convertToEntity(dto));
        return convertToDTO(entity);
    }

    @Override
    public ElectricScooterDTO update(ElectricScooterDTO dto) {
        ElectricScooter entity = repository.save(convertToEntity(dto));
        return convertToDTO(entity);
    }

    @Override
    public ElectricScooterDTO insert(ElectricScooterDTO dto, MultipartFile imageFile) {
        if(existsByScooterId(dto.getScooterId()))
            throw new CustomVehicleIdConflictException("Scooter with given scooter ID already exists.");

        if(imageFile != null)
            VehicleHelper.addImage(dto, imageService, Constants.ImagesRelativePaths.SCOOTERS_PATH, imageFile);

        return insert(dto);
    }

    @Override
    public ElectricScooterDTO update(ElectricScooterDTO dto, MultipartFile imageFile) {
        ElectricScooter byScooterId = repository.findByScooterId(dto.getScooterId());
        if(byScooterId != null && !byScooterId.getId().equals(dto.getId()))
            throw new CustomVehicleIdConflictException("Scooter with given scooter ID already exists.");

        if(imageFile != null)
            VehicleHelper.updateOldImage(dto, imageService, Constants.ImagesRelativePaths.SCOOTERS_PATH, imageFile);

        ElectricScooterDTO scooterDTO = update(dto);
        VehicleHelper.setImageUrlForVehicle(scooterDTO, imageService, Constants.ImagesRelativePaths.SCOOTERS_PATH);

        return scooterDTO;
    }

    @Override
    public boolean delete(Integer id) {
        return false;
    }

    @Override
    public boolean existsByScooterId(String scooterId) {
        return repository.existsByScooterId(scooterId);
    }

    @Override
    public ElectricScooterDTO convertToDTO(ElectricScooter entity) {
        return mapper.map(entity, ElectricScooterDTO.class);
    }

    @Override
    public ElectricScooter convertToEntity(ElectricScooterDTO dto) {
        return mapper.map(dto, ElectricScooter.class);
    }
}
