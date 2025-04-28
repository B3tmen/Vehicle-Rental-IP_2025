package org.unibl.etf.carrentalbackend.service.impl;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.unibl.etf.carrentalbackend.interfaces.EntityDTOConverter;
import org.unibl.etf.carrentalbackend.model.dto.MalfunctionDTO;
import org.unibl.etf.carrentalbackend.model.dto.RentalVehicleDTO;
import org.unibl.etf.carrentalbackend.model.dto.VehicleMalfunctionDTO;
import org.unibl.etf.carrentalbackend.model.entities.Malfunction;
import org.unibl.etf.carrentalbackend.model.entities.RentalVehicle;
import org.unibl.etf.carrentalbackend.model.entities.VehicleMalfunction;
import org.unibl.etf.carrentalbackend.model.entities.VehicleMalfunctionId;
import org.unibl.etf.carrentalbackend.repository.MalfunctionRepository;
import org.unibl.etf.carrentalbackend.repository.VehicleMalfunctionRepository;
import org.unibl.etf.carrentalbackend.service.interfaces.RentalVehicleService;
import org.unibl.etf.carrentalbackend.service.interfaces.VehicleMalfunctionService;

import java.util.List;

@Service
public class VehicleMalfunctionServiceImpl implements VehicleMalfunctionService, EntityDTOConverter<VehicleMalfunction, VehicleMalfunctionDTO> {
    private final VehicleMalfunctionRepository vehicleMalfunctionRepository;
    private final MalfunctionRepository malfunctionRepository;
    private final RentalVehicleService rentalVehicleService;
    private final ModelMapper mapper;

    @Autowired
    public VehicleMalfunctionServiceImpl(MalfunctionRepository malfunctionRepository, VehicleMalfunctionRepository vehicleMalfunctionRepository, RentalVehicleService rentalVehicleService, ModelMapper mapper) {
        this.malfunctionRepository = malfunctionRepository;
        this.vehicleMalfunctionRepository = vehicleMalfunctionRepository;
        this.rentalVehicleService = rentalVehicleService;

        this.mapper = mapper;
    }

    @Override
    public List<VehicleMalfunctionDTO> getAll() {
        return vehicleMalfunctionRepository.findAll()
                .stream()
                .filter(vm -> vm.getVehicle().getIsActive() == 1 && vm.getMalfunction().getDeletedAt() == null)
                .map(this::convertToDTO)
                .toList();
    }

    @Override
    public VehicleMalfunctionDTO getById(int id) {
        return null;
    }

    @Override
    public VehicleMalfunctionDTO insert(VehicleMalfunctionDTO dto) {
        RentalVehicleDTO vehicleDTO = dto.getVehicle();
        MalfunctionDTO malfunctionDTO = dto.getMalfunction();
        dto.setVehicleMalfunctionId(new VehicleMalfunctionId(vehicleDTO.getId(), malfunctionDTO.getId()));
        if(malfunctionDTO.getId() == null){
            Malfunction malfunction = malfunctionRepository.save(mapper.map(malfunctionDTO, Malfunction.class));
            dto.setMalfunction(mapper.map(malfunction, MalfunctionDTO.class));
        }

        VehicleMalfunction vehicleMalfunction = vehicleMalfunctionRepository.save(convertToEntity(dto));
        return convertToDTO(vehicleMalfunction);
    }

    @Override
    public VehicleMalfunctionDTO update(VehicleMalfunctionDTO dto) {
        return null;
    }

    @Override
    public boolean delete(Integer id) {
        return false;
    }

    @Override
    public List<MalfunctionDTO> getMalfunctionsByVehicle(int vehicleId) {
        if(rentalVehicleService.getById(vehicleId) == null){
            return null;
        }
        RentalVehicleDTO dto = rentalVehicleService.getById(vehicleId);
        RentalVehicle entity = rentalVehicleService.convertToEntity(dto);

        return vehicleMalfunctionRepository.findAll().stream()
                .filter(vm -> vm.getVehicle().equals(entity) && vm.getMalfunction().getDeletedAt() == null)
                .map(vm ->
                    MalfunctionDTO.builder()
                        .id(vm.getMalfunction().getId())
                        .timeOfMalfunction(vm.getMalfunction().getTimeOfMalfunction())
                        .reason(vm.getMalfunction().getReason())
                        .build()
                )
                .toList();
    }

    @Override
    public VehicleMalfunctionDTO convertToDTO(VehicleMalfunction entity) {
        return mapper.map(entity, VehicleMalfunctionDTO.class);
    }

    @Override
    public VehicleMalfunction convertToEntity(VehicleMalfunctionDTO dto) {
        return mapper.map(dto, VehicleMalfunction.class);
    }


}
