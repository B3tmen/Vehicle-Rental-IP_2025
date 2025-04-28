package org.unibl.etf.carrentalbackend.service.impl;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.unibl.etf.carrentalbackend.exception.ResourceNotFoundException;
import org.unibl.etf.carrentalbackend.interfaces.EntityDTOConverter;
import org.unibl.etf.carrentalbackend.model.dto.ImageDTO;
import org.unibl.etf.carrentalbackend.model.entities.Image;
import org.unibl.etf.carrentalbackend.repository.ImageRepository;
import org.unibl.etf.carrentalbackend.service.interfaces.ImageService;
import org.unibl.etf.carrentalbackend.util.UUIDHelper;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

@PropertySource("classpath:custom.properties")
@Service
public class ImageServiceImpl implements ImageService, EntityDTOConverter<Image, ImageDTO> {
    @Value("${resources.static.images.path}")
    private String resourcesPath;
    @Value("${images.base.url}")
    private String imagesBaseUrl;
    private final ImageRepository imageRepository;
    private final ModelMapper mapper;

    @Autowired
    public ImageServiceImpl(ImageRepository imageRepository, ModelMapper mapper) {
        this.imageRepository = imageRepository;
        this.mapper = mapper;
    }


    @Override
    public ImageDTO getById(int id) {
        Image image = imageRepository.findById(id).orElse(null);
        return convertToDTO(image);
    }

    @Override
    public ImageDTO updateImage(ImageDTO oldImageDTO, String relativePath, MultipartFile file) throws IOException {
        saveToFileSystem(relativePath, oldImageDTO.getName(), file);

        ImageDTO updatedImage = new ImageDTO(oldImageDTO.getId(), oldImageDTO.getName(), oldImageDTO.getType(), null, null);
        setDataAndUrl(updatedImage, relativePath, convertToEntity(oldImageDTO));
        imageRepository.save(convertToEntity(updatedImage));

        return updatedImage;
    }

    @Override
    public ImageDTO uploadImage(String relativePath, MultipartFile file) throws IOException {
        String extension = getExtensionFromFile(file);
        String fileName = UUIDHelper.getRandomUUID() + "." + extension;
        saveToFileSystem(relativePath, fileName, file);

        Image image = Image.builder()
                .name(fileName)
                .type(file.getContentType())
                .build();

        Image inserted = imageRepository.save(image);
        return convertToDTO(inserted);
    }

    @Override
    public ImageDTO downloadImage(String relativePath, Integer id) throws IOException {
        Image image = imageRepository.findById(id).orElseThrow(ResourceNotFoundException::new);
        String url = getResourceUrl(relativePath, image);
        String path = getPath(relativePath, image);

        // Read the image data from the file system
        Path filePath = Paths.get(path);
        byte[] data = null;
        try{
//            if(!Files.exists(filePath)){
//                throw new ResourceNotFoundException();
//            }

            data = Files.readAllBytes(filePath);
        } catch (IOException e) {
            e.printStackTrace();
            throw new ResourceNotFoundException();
        }

        ImageDTO dto = convertToDTO(image);
        dto.setData(data);
        dto.setUrl(url);

        return dto;
    }

    @Override
    public ImageDTO convertToDTO(Image entity) {
        return mapper.map(entity, ImageDTO.class);
    }

    @Override
    public Image convertToEntity(ImageDTO dto) {
        return mapper.map(dto, Image.class);
    }

    private String getExtensionFromFile(MultipartFile file){
        String fileName = file.getOriginalFilename();
        // Handle cases where browsers provide full paths
        String filename = fileName.substring(fileName.lastIndexOf('/') + 1)
                .substring(fileName.lastIndexOf('\\') + 1);

        int dotIndex = filename.lastIndexOf('.');
        if (dotIndex > 0 && dotIndex < filename.length() - 1) {
            return filename.substring(dotIndex + 1).toLowerCase();
        }

        return "unknown";
    }

    private String getPath(String relativePath, Image image) {
        return resourcesPath + relativePath + (!relativePath.endsWith("/") ? "/" : "") + image.getName();
    }

    private String getResourceUrl(String relativePath, Image image) {
        return imagesBaseUrl + relativePath + (!relativePath.endsWith("/") ? "/" : "") + image.getName();
    }

    private void saveToFileSystem(String relativePath, String fileName, MultipartFile file) throws IOException {
        Path filePath = Paths.get(resourcesPath + relativePath + fileName);
        Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
    }

    private void setDataAndUrl(ImageDTO dto, String relativePath, Image image) throws IOException {
        String url = getResourceUrl(relativePath, image);
        Path filePath = Paths.get(getPath(relativePath, image));
        byte[] data = Files.readAllBytes(filePath);

        dto.setData(data);
        dto.setUrl(url);
    }
}
