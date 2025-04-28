package org.unibl.etf.carrentalbackend.service.impl;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.unibl.etf.carrentalbackend.interfaces.EntityDTOConverter;
import org.unibl.etf.carrentalbackend.model.dto.MapVehicleDTO;
import org.unibl.etf.carrentalbackend.model.dto.RentalDTO;
import org.unibl.etf.carrentalbackend.model.entities.Rental;
import org.unibl.etf.carrentalbackend.repository.RentalRepository;
import org.unibl.etf.carrentalbackend.service.interfaces.RentalService;
import org.unibl.etf.carrentalbackend.service.interfaces.RentalVehicleService;

import java.util.ArrayList;
import java.util.List;

@Service
public class RentalServiceImpl implements RentalService, EntityDTOConverter<Rental, RentalDTO> {
    private final RentalRepository repository;
    private final ModelMapper mapper;

    @Autowired
    public RentalServiceImpl(RentalRepository repository, RentalVehicleService rentalVehicleService, ModelMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Override
    public List<RentalDTO> getAll() {
        return repository.findAll()
                .stream()
                .map(this::convertToDTO)
                .toList();
    }

    @Override
    public List<MapVehicleDTO> getVehiclesOnMap() {
        List<RentalDTO> rentals = this.getAll();
        List<MapVehicleDTO> mapVehicles = new ArrayList<>();
        rentals.forEach(rentalDTO -> mapVehicles.add(new MapVehicleDTO(rentalDTO.getVehicle(), rentalDTO.getDropoffLocation())));

        return mapVehicles;
    }

    @Override
    public List<RentalDTO> getRentalsByVehicle(int vehicleId) {

        List<RentalDTO> rentals = this.getAll();
        return rentals.stream()
                .filter(rental -> rental.getVehicle().getId() == vehicleId)
                .toList();
    }

    @Override
    public RentalDTO convertToDTO(Rental entity) {
        return mapper.map(entity, RentalDTO.class);
    }

    @Override
    public Rental convertToEntity(RentalDTO dto) {
        return mapper.map(dto, Rental.class);
    }
}
