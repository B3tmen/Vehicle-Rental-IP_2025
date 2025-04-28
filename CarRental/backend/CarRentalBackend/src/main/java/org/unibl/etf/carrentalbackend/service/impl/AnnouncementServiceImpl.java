package org.unibl.etf.carrentalbackend.service.impl;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.unibl.etf.carrentalbackend.interfaces.EntityDTOConverter;
import org.unibl.etf.carrentalbackend.model.dto.AnnouncementDTO;
import org.unibl.etf.carrentalbackend.model.entities.Announcement;
import org.unibl.etf.carrentalbackend.repository.AnnouncementRepository;
import org.unibl.etf.carrentalbackend.service.interfaces.AnnouncementService;

import java.util.List;

@Service
public class AnnouncementServiceImpl implements AnnouncementService, EntityDTOConverter<Announcement, AnnouncementDTO> {

    private final AnnouncementRepository repository;
    private final ModelMapper mapper;

    @Autowired
    public AnnouncementServiceImpl(AnnouncementRepository repository, ModelMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }


    @Override
    public List<AnnouncementDTO> getAll() {
        return repository.findAll()
                .stream()
                .map(this::convertToDTO)
                .toList();
    }

    @Override
    public AnnouncementDTO getById(int id) {
        return null;
    }

    @Override
    public AnnouncementDTO insert(AnnouncementDTO dto) {
        Announcement entity = repository.save(convertToEntity(dto));
        return convertToDTO(entity);
    }

    @Override
    public AnnouncementDTO update(AnnouncementDTO dto) {
        return null;
    }

    @Override
    public boolean delete(Integer id) {
        return false;
    }

    @Override
    public AnnouncementDTO convertToDTO(Announcement entity) {
        return mapper.map(entity, AnnouncementDTO.class);
    }

    @Override
    public Announcement convertToEntity(AnnouncementDTO dto) {
        return mapper.map(dto, Announcement.class);
    }
}
