package org.unibl.etf.carrentalbackend.service.impl;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.unibl.etf.carrentalbackend.exception.PersonalCardNumberConflictException;
import org.unibl.etf.carrentalbackend.exception.PhoneNumberConflictException;
import org.unibl.etf.carrentalbackend.exception.UsernameConflictException;
import org.unibl.etf.carrentalbackend.interfaces.EntityDTOConverter;
import org.unibl.etf.carrentalbackend.model.dto.ClientDTO;
import org.unibl.etf.carrentalbackend.model.dto.ImageDTO;
import org.unibl.etf.carrentalbackend.model.entities.Client;
import org.unibl.etf.carrentalbackend.repository.ClientRepository;
import org.unibl.etf.carrentalbackend.service.interfaces.ClientService;
import org.unibl.etf.carrentalbackend.service.interfaces.ImageService;
import org.unibl.etf.carrentalbackend.util.Constants;
import org.unibl.etf.carrentalbackend.util.TextHasher;

import java.io.IOException;
import java.util.List;

@Service
public class ClientServiceImpl implements ClientService, EntityDTOConverter<Client, ClientDTO> {
    private final ClientRepository repository;
    private final ImageService imageService;
    private final ModelMapper mapper;

    @Autowired
    public ClientServiceImpl(ClientRepository repository, ImageService imageService, ModelMapper mapper) {
        this.repository = repository;
        this.imageService = imageService;
        this.mapper = mapper;
    }


    @Override
    public List<ClientDTO> getAll() {
        return repository.findAll()
                .stream()
                .filter(c -> c.getIsActive().equals((byte) 1))
                .map(client -> {
                    ClientDTO dto = convertToDTO(client);
                    setImageUrlForClient(dto);
                    return dto;
                })
                .toList();
    }

    @Override
    public ClientDTO getByUsername(String username) {
        Client client = repository.findByUsername(username);
        if(client == null)
            return null;

        return convertToDTO(client);
    }

    @Override
    public ClientDTO getById(int id) {
        Client client = repository.findById(id).orElse(null);
        ClientDTO clientDTO = convertToDTO(client);
        setImageUrlForClient(clientDTO);

        return clientDTO;
    }

    @Override
    public ClientDTO insert(ClientDTO dto) {
        boolean existsByUsername = getByUsername(dto.getUsername()) != null;
        if(existsByUsername)
            throw new UsernameConflictException("A user with the username '" + dto.getUsername() + "' already exists.");

        if(repository.findByPhoneNumber(dto.getPhoneNumber()) != null)
            throw new PhoneNumberConflictException("Phone number '" + dto.getPhoneNumber() + "' already exists.");

        if(repository.findByPersonalCardNumber(dto.getPersonalCardNumber()) != null)
            throw new PersonalCardNumberConflictException("Personal ID '" + dto.getPersonalCardNumber() + "' belongs to another person!");

        String hash = TextHasher.getBCryptHash(dto.getPasswordHash());
        dto.setPasswordHash(hash);

        Client client = repository.save(convertToEntity(dto));
        return convertToDTO(client);
    }

    @Override
    public ClientDTO insert(ClientDTO dto, MultipartFile imageFile) {
        if(imageFile != null)
            addImage(dto, imageFile);

        return insert(dto);
    }

    @Override
    public ClientDTO update(ClientDTO clientDTO, MultipartFile imageFile) {
        // We can't update the image, if there is no image
        if(clientDTO.getAvatarImage() != null){
            if(clientDTO.getAvatarImage().getId() == null){     // If no ID is present of the image, insert it as new image
                addImage(clientDTO, imageFile);
            }
            else{       // If ID is present of the image, update the old image
                ImageDTO oldImageDTO = imageService.getById(clientDTO.getAvatarImage().getId());
                if(oldImageDTO != null){
                    ImageDTO updatedImage;
                    try {
                        updatedImage = imageService.updateImage(oldImageDTO, Constants.ImagesRelativePaths.CLIENTS_PATH, imageFile);
                        clientDTO.setAvatarImage(updatedImage);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        else{
            addImage(clientDTO, imageFile);
        }

        ClientDTO dto = update(clientDTO);
        setImageUrlForClient(dto);

        return dto;
    }

    @Override
    public ClientDTO update(ClientDTO dto) {
        ClientDTO dtoCheck = getById(dto.getId());
        if(dtoCheck != null && !dtoCheck.getPasswordHash().equals(dto.getPasswordHash())){      // If password wasn't changed, don't hash it again
            String hash = TextHasher.getBCryptHash(dto.getPasswordHash());
            dto.setPasswordHash(hash);
        }
        ClientDTO clientByUsername = getByUsername(dto.getUsername());
        if(clientByUsername != null && !clientByUsername.getId().equals(dto.getId()))
            throw new UsernameConflictException("A user with the username '" + dto.getUsername() + "' already exists.");

        Client clientByPhone = repository.findByPhoneNumber(dto.getPhoneNumber());
        if(clientByPhone != null && !clientByPhone.getId().equals(dto.getId()))
            throw new PhoneNumberConflictException("Phone number '" + dto.getPhoneNumber() + "' already exists.");

        Client clientByPersonalCardNumber = repository.findByPersonalCardNumber(dto.getPersonalCardNumber());
        if(clientByPersonalCardNumber != null && !clientByPersonalCardNumber.getId().equals(dto.getId()))
            throw new PersonalCardNumberConflictException("Personal ID '" + dto.getPersonalCardNumber() + "' belongs to another person!");

        Client client = repository.save(convertToEntity(dto));
        return convertToDTO(client);
    }

    @Override
    public boolean delete(Integer id) {
        boolean exists = repository.existsById(id);
        if(!exists){
            return false;
        }

        ClientDTO client = getById(id);
        client.setIsActive(false);
        ClientDTO updated = update(client);

        return updated != null;
    }

    @Override
    public ClientDTO convertToDTO(Client entity) {
        return mapper.map(entity, ClientDTO.class);
    }

    @Override
    public Client convertToEntity(ClientDTO dto) {
        return mapper.map(dto, Client.class);
    }

    private void addImage(ClientDTO dto, MultipartFile imageFile) {
        ImageDTO imageDTO;
        try {
            imageDTO = imageService.uploadImage(Constants.ImagesRelativePaths.CLIENTS_PATH, imageFile);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        dto.setAvatarImage(imageDTO);
    }

    private void setImageUrlForClient(ClientDTO clientDTO) {
        if(clientDTO.getAvatarImage() != null){
            try {
                ImageDTO imgDto = imageService.downloadImage(Constants.ImagesRelativePaths.CLIENTS_PATH, clientDTO.getAvatarImage().getId());
                clientDTO.getAvatarImage().setUrl(imgDto.getUrl());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
