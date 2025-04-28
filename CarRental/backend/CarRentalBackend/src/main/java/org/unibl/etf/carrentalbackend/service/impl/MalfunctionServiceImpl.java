package org.unibl.etf.carrentalbackend.service.impl;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.unibl.etf.carrentalbackend.interfaces.EntityDTOConverter;
import org.unibl.etf.carrentalbackend.model.dto.MalfunctionDTO;
import org.unibl.etf.carrentalbackend.model.entities.Malfunction;
import org.unibl.etf.carrentalbackend.repository.MalfunctionRepository;
import org.unibl.etf.carrentalbackend.service.interfaces.MalfunctionService;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class MalfunctionServiceImpl implements MalfunctionService, EntityDTOConverter<Malfunction, MalfunctionDTO> {
    private final MalfunctionRepository repository;
    private final ModelMapper mapper;


    @Autowired
    public MalfunctionServiceImpl(MalfunctionRepository repository, ModelMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }


    @Override
    public List<MalfunctionDTO> getAll() {
        return repository.findAll().stream()
                .filter(m -> m.getDeletedAt() == null)
                .map(this::convertToDTO)
                .toList();
    }

    @Override
    public MalfunctionDTO getById(int id) {
        Malfunction malfunction = repository.findById(id).orElse(null);
        if(malfunction != null && malfunction.getDeletedAt() != null) return null;

        return convertToDTO(malfunction);
    }

    @Override
    public MalfunctionDTO insert(MalfunctionDTO dto) {
        return null;
    }

    @Override
    public MalfunctionDTO update(MalfunctionDTO dto) {
        Malfunction malfunction = repository.save(convertToEntity(dto));
        return convertToDTO(malfunction);
    }

    @Override
    public boolean delete(Integer id) {
        MalfunctionDTO dto = getById(id);
        if (dto == null) {
            return false;
        }
        dto.setDeletedAt(LocalDateTime.now());
        update(dto);
        return true;
    }

    @Override
    public MalfunctionDTO convertToDTO(Malfunction entity) {
        return mapper.map(entity, MalfunctionDTO.class);
    }

    @Override
    public Malfunction convertToEntity(MalfunctionDTO dto) {
        return mapper.map(dto, Malfunction.class);
    }
}
