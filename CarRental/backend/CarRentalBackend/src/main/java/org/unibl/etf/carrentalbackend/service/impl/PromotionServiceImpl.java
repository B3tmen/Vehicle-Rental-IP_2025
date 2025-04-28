package org.unibl.etf.carrentalbackend.service.impl;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.unibl.etf.carrentalbackend.interfaces.EntityDTOConverter;
import org.unibl.etf.carrentalbackend.model.dto.PromotionDTO;
import org.unibl.etf.carrentalbackend.model.entities.Promotion;
import org.unibl.etf.carrentalbackend.repository.PromotionRepository;
import org.unibl.etf.carrentalbackend.service.interfaces.PromotionService;

import java.util.List;

@Service
public class PromotionServiceImpl implements PromotionService, EntityDTOConverter<Promotion, PromotionDTO> {

    private final PromotionRepository repository;
    private final ModelMapper mapper;

    @Autowired
    public PromotionServiceImpl(PromotionRepository repository, ModelMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Override
    public List<PromotionDTO> getAll() {
        return repository.findAll()
                .stream()
                .map(this::convertToDTO)
                .toList();
    }

    @Override
    public PromotionDTO getById(int id) {
        return null;
    }

    @Override
    public PromotionDTO insert(PromotionDTO dto) {
        Promotion entity = repository.save(convertToEntity(dto));
        return convertToDTO(entity);
    }

    @Override
    public PromotionDTO update(PromotionDTO dto) {
        return null;
    }

    @Override
    public boolean delete(Integer id) {
        return false;
    }

    @Override
    public PromotionDTO convertToDTO(Promotion entity) {
        return mapper.map(entity, PromotionDTO.class);
    }

    @Override
    public Promotion convertToEntity(PromotionDTO dto) {
        return mapper.map(dto, Promotion.class);
    }
}
