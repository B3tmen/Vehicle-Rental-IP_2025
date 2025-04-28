package org.unibl.etf.clientapp.service;

import jakarta.servlet.http.Part;
import org.unibl.etf.clientapp.database.dao.interfaces.AvatarImageDAO;
import org.unibl.etf.clientapp.model.dto.Client;
import org.unibl.etf.clientapp.model.dto.Image;
import org.unibl.etf.clientapp.util.ConfigReader;
import org.unibl.etf.clientapp.util.UUIDHelper;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.sql.SQLException;

public class ImageService {
    private static ConfigReader configReader = ConfigReader.getInstance();
    private AvatarImageDAO dao;

    public ImageService(AvatarImageDAO dao){
        this.dao = dao;
    }

    public Image insertImage(Client client, Part filePart, String clientAppRootPath) {
        if(filePart == null) return null;

        Image image = getImageFromPart(filePart);
        try {
            String uploadPath = clientAppRootPath + configReader.getAvatarImageRelativePath();
//            File uploadDir = new File(uploadPath);
//            if (!uploadDir.exists()) uploadDir.mkdirs();
            boolean inserted = dao.insertImage(client, image);
            if(inserted) {
                saveToFileSystem(null, image, uploadPath, filePart);
            }
        } catch (SQLException | IOException e){
            e.printStackTrace();
        }

        return image;
    }

    public Image updateImage(Client client, Part filePart, String clientAppRootPath) {
        if(filePart == null) return null;

        Image newImage = getImageFromPart(filePart);
        try {
            String uploadPath = clientAppRootPath + configReader.getAvatarImageRelativePath();
            boolean updated = dao.updateImage(client, newImage);
            if(updated) {
                Image oldImage = client.getAvatarImage();
                newImage.setId(oldImage.getId());
                saveToFileSystem(oldImage, newImage, uploadPath, filePart);
            }
        } catch (SQLException | IOException e){
            e.printStackTrace();
        }

        return newImage;
    }

    private String getFullPath(Image image, String uploadPath){
        return uploadPath + image.getName();
    }

    private String getExtension(String originalName){
        String[] split = originalName.split("/");      // in database its saved as image/png, image/jpeg, ...
        return split[1];
    }

    private Image getImageFromPart(Part imagePart) {
        String type = imagePart.getContentType();
        String extension = getExtension(type);
        String name = UUIDHelper.getRandomUUID() + "." + extension;
        return new Image(0, name, type, null);
    }

    private void saveToFileSystem(Image oldImage, Image newImage, String uploadPath, Part filePart) throws IOException {

        if(oldImage == null) {      // If inserting
            File newFile = new File(getFullPath(newImage, uploadPath));
            try (InputStream fileContent = filePart.getInputStream()) {
                Files.copy(fileContent, newFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
                if(Files.exists(newFile.toPath())){
                    String url = configReader.getClientImagesSpringURL() + newImage.getName();
                    newImage.setUrl(url);
                }
            }
        }
        else{       // If updating
            File oldFile = new File(getFullPath(oldImage, uploadPath));
            File newFile = new File(getFullPath(newImage, uploadPath));
            try (InputStream fileContent = filePart.getInputStream()) {
                Files.copy(fileContent, oldFile.toPath(), StandardCopyOption.REPLACE_EXISTING);

                Files.move(oldFile.toPath(), newFile.toPath(), StandardCopyOption.REPLACE_EXISTING);

                if(Files.exists(newFile.toPath())){
                    String url = configReader.getClientImagesSpringURL() + newImage.getName();
                    newImage.setUrl(url);
                }
            }
        }
    }
}
