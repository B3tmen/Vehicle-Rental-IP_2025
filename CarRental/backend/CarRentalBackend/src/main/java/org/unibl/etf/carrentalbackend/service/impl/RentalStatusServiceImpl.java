package org.unibl.etf.carrentalbackend.service.impl;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.unibl.etf.carrentalbackend.interfaces.EntityDTOConverter;
import org.unibl.etf.carrentalbackend.model.dto.RentalStatusDTO;
import org.unibl.etf.carrentalbackend.model.entities.RentalStatus;
import org.unibl.etf.carrentalbackend.repository.RentalStatusRepository;
import org.unibl.etf.carrentalbackend.service.interfaces.RentalStatusService;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class RentalStatusServiceImpl implements RentalStatusService, EntityDTOConverter<RentalStatus, RentalStatusDTO> {

    private final RentalStatusRepository repository;
    private final ModelMapper mapper;

    @Autowired
    public RentalStatusServiceImpl(RentalStatusRepository repository, ModelMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Override
    public List<RentalStatusDTO> getAll() {
        return repository.findAll().stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    @Override
    public RentalStatusDTO getById(int id) {
        return null;
    }

    @Override
    public RentalStatusDTO insert(RentalStatusDTO dto) {
        return null;
    }

    @Override
    public RentalStatusDTO update(RentalStatusDTO dto) {
        return null;
    }

    @Override
    public boolean delete(Integer id) {
        return false;
    }

    @Override
    public RentalStatusDTO convertToDTO(RentalStatus entity) {
        return mapper.map(entity, RentalStatusDTO.class);
    }

    @Override
    public RentalStatus convertToEntity(RentalStatusDTO dto) {
        return mapper.map(dto, RentalStatus.class);
    }
}
