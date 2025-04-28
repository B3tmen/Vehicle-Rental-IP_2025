package org.unibl.etf.carrentalbackend.service.impl;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.unibl.etf.carrentalbackend.exception.EmailConflictException;
import org.unibl.etf.carrentalbackend.exception.PhoneNumberConflictException;
import org.unibl.etf.carrentalbackend.interfaces.EntityDTOConverter;
import org.unibl.etf.carrentalbackend.model.dto.ManufacturerDTO;
import org.unibl.etf.carrentalbackend.model.entities.Manufacturer;
import org.unibl.etf.carrentalbackend.repository.ManufacturerRepository;
import org.unibl.etf.carrentalbackend.service.interfaces.ManufacturerService;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional
public class ManufacturerServiceImpl implements ManufacturerService, EntityDTOConverter<Manufacturer, ManufacturerDTO> {

    private final ManufacturerRepository repository;
    private final ModelMapper modelMapper;

    @Autowired
    public ManufacturerServiceImpl(ManufacturerRepository repository, ModelMapper modelMapper) {
        this.repository = repository;
        this.modelMapper = modelMapper;
    }

    @Override
    public List<ManufacturerDTO> getAll() {
        return repository.findAll().stream()
                .filter(m -> m.getDeletedAt() == null)
                .map(this::convertToDTO)
                .toList();
    }

    @Override
    public ManufacturerDTO getById(int id) {
        Manufacturer manufacturer = repository.findById(id).orElse(null);
        return convertToDTO(manufacturer);
    }

    @Override
    public ManufacturerDTO insert(ManufacturerDTO manufacturerDTO) {
        return persistAndCheckForConflicts(manufacturerDTO, false);
    }

    @Override
    public ManufacturerDTO update(ManufacturerDTO manufacturerDTO) {
        return persistAndCheckForConflicts(manufacturerDTO, true);
    }

    private ManufacturerDTO persistAndCheckForConflicts(@Validated ManufacturerDTO manufacturerDTO, boolean isUpdating) {
        ManufacturerDTO manufacturerByPhoneNumber = getByPhoneNumber(manufacturerDTO.getPhoneNumber());
        ManufacturerDTO manufacturerByEmail = getByEmail(manufacturerDTO.getEmail());
        if(!isUpdating){
            if(manufacturerByPhoneNumber != null && !manufacturerByPhoneNumber.getId().equals(manufacturerDTO.getId()) ) {
                throw new PhoneNumberConflictException("This phone number '" + manufacturerDTO.getPhoneNumber() +  "' already exists for another manufacturer");
            }
            if(manufacturerByEmail != null && !manufacturerByEmail.getId().equals(manufacturerDTO.getId()) ) {
                throw new EmailConflictException("This email '" + manufacturerDTO.getEmail() + "' already exists for another manufacturer");
            }
        }

        Manufacturer manufacturer = repository.save(convertToEntity(manufacturerDTO));
        return convertToDTO(manufacturer);
    }

    @Override
    public boolean delete(Integer id){
        boolean exists = repository.existsById(id);
        if(!exists){
            return false;
        }

        ManufacturerDTO manufacturer = getById(id);
        manufacturer.setDeletedAt(LocalDateTime.now());
        ManufacturerDTO updated = update(manufacturer);

        return updated != null;
    }

    @Override
    public ManufacturerDTO getByPhoneNumber(String phoneNumber) {
        Manufacturer manufacturer = repository.findByPhoneNumber(phoneNumber);
        if(manufacturer == null)
            return null;

        return convertToDTO(manufacturer);
    }

    @Override
    public ManufacturerDTO getByEmail(String email) {
        Manufacturer manufacturer = repository.findByEmail(email);
        if(manufacturer == null)
            return null;

        return convertToDTO(manufacturer);
    }

    @Override
    public ManufacturerDTO convertToDTO(Manufacturer entity) {
        return modelMapper.map(entity, ManufacturerDTO.class);
    }

    @Override
    public Manufacturer convertToEntity(ManufacturerDTO dto) {
        return modelMapper.map(dto, Manufacturer.class);
    }


}
