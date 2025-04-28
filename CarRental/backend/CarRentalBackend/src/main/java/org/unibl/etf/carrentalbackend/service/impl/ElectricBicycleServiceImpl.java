package org.unibl.etf.carrentalbackend.service.impl;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.unibl.etf.carrentalbackend.exception.CustomVehicleIdConflictException;
import org.unibl.etf.carrentalbackend.interfaces.EntityDTOConverter;
import org.unibl.etf.carrentalbackend.model.dto.ElectricBicycleDTO;
import org.unibl.etf.carrentalbackend.model.entities.ElectricBicycle;
import org.unibl.etf.carrentalbackend.repository.ElectricBicycleRepository;
import org.unibl.etf.carrentalbackend.service.interfaces.ElectricBicycleService;
import org.unibl.etf.carrentalbackend.service.interfaces.ImageService;
import org.unibl.etf.carrentalbackend.util.Constants;
import org.unibl.etf.carrentalbackend.util.VehicleHelper;

import java.util.List;

@Service
public class ElectricBicycleServiceImpl implements ElectricBicycleService, EntityDTOConverter<ElectricBicycle, ElectricBicycleDTO> {

    private final ElectricBicycleRepository repository;
    private final ImageService imageService;
    private final ModelMapper mapper;

    @Autowired
    public ElectricBicycleServiceImpl(ElectricBicycleRepository repository, ImageService imageService, ModelMapper mapper) {
        this.repository = repository;
        this.imageService = imageService;
        this.mapper = mapper;
    }

    @Override
    public List<ElectricBicycleDTO> getAll() {
        return List.of();
    }

    @Override
    public ElectricBicycleDTO getById(int id) {
        ElectricBicycle bicycle = repository.findById(id).orElse(null);
        ElectricBicycleDTO dto = convertToDTO(bicycle);
        VehicleHelper.setImageUrlForVehicle(dto, imageService, Constants.ImagesRelativePaths.BICYCLES_PATH);

        return dto;
    }

    @Override
    public ElectricBicycleDTO insert(ElectricBicycleDTO dto) {
        ElectricBicycle entity = repository.save(convertToEntity(dto));
        return convertToDTO(entity);
    }

    @Override
    public ElectricBicycleDTO update(ElectricBicycleDTO dto) {
        ElectricBicycle entity = repository.save(convertToEntity(dto));
        return convertToDTO(entity);
    }

    @Override
    public ElectricBicycleDTO insert(ElectricBicycleDTO dto, MultipartFile imageFile) {
        if(repository.existsByBicycleId(dto.getBicycleId()))
            throw new CustomVehicleIdConflictException("Bicycle with given bicycle ID already exists.");

        if(imageFile != null)
            VehicleHelper.addImage(dto, imageService, Constants.ImagesRelativePaths.BICYCLES_PATH, imageFile);

        return insert(dto);
    }

    @Override
    public ElectricBicycleDTO update(ElectricBicycleDTO dto, MultipartFile imageFile) {
        ElectricBicycle byBicycleId = repository.findByBicycleId(dto.getBicycleId());
        if(byBicycleId != null && !byBicycleId.getId().equals(dto.getId()))
            throw new CustomVehicleIdConflictException("Bicycle with given bicycle ID already exists.");

        if(imageFile != null)
            VehicleHelper.updateOldImage(dto, imageService, Constants.ImagesRelativePaths.BICYCLES_PATH, imageFile);

        ElectricBicycleDTO bicycleDTO = update(dto);
        VehicleHelper.setImageUrlForVehicle(bicycleDTO, imageService, Constants.ImagesRelativePaths.BICYCLES_PATH);

        return bicycleDTO;
    }

    @Override
    public boolean delete(Integer id) {
        return false;
    }

    @Override
    public boolean existsByBicycleId(String bicycleId) {
        return repository.existsByBicycleId(bicycleId);
    }

    @Override
    public ElectricBicycleDTO convertToDTO(ElectricBicycle entity) {
        return mapper.map(entity, ElectricBicycleDTO.class);
    }

    @Override
    public ElectricBicycle convertToEntity(ElectricBicycleDTO dto) {
        return mapper.map(dto, ElectricBicycle.class);
    }


}
