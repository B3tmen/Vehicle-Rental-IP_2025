package org.unibl.etf.clientapp.database.dao.impl;

import org.unibl.etf.clientapp.database.ConnectionPool;
import org.unibl.etf.clientapp.database.DBUtil;
import org.unibl.etf.clientapp.database.dao.interfaces.AvatarImageDAO;
import org.unibl.etf.clientapp.model.dto.Client;
import org.unibl.etf.clientapp.model.dto.Image;
import org.unibl.etf.clientapp.model.dto.Invoice;
import org.unibl.etf.clientapp.util.ConfigReader;
import org.unibl.etf.clientapp.util.CustomLogger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class AvatarImageDAOImpl implements AvatarImageDAO {
    private final CustomLogger logger = CustomLogger.getInstance(AvatarImageDAOImpl.class);
    private static final ConfigReader configReader = ConfigReader.getInstance();
    private static final ConnectionPool connectionPool = ConnectionPool.getInstance();
    private static final String INSERT_IMAGE_QUERY = "INSERT INTO image (name, type) VALUES (?, ?)";
    private static final String UPDATE_IMAGE_QUERY = "UPDATE image SET name = ?, type = ? WHERE image_id = ?";
    private static final String UPDATE_IMAGE_FOR_ENTITY_QUERY = "UPDATE client SET fk_avatar_image_id = ? WHERE fk_user_id = ?";
    private static final String GET_CLIENT_BY_ID_QUERY = "SELECT * from client WHERE fk_user_id = ?";
    private static final String GET_IMAGE_BY_ID_QUERY = "SELECT * from image WHERE image_id = ?";

    @Override
    public boolean insertImage(Client entity, Image image) throws SQLException {
        boolean inserted = false;
        Connection conn = null;
        PreparedStatement pstmt = null;

        try{
            conn = connectionPool.checkOut();
            conn.setAutoCommit(false);
            pstmt = conn.prepareStatement(INSERT_IMAGE_QUERY, PreparedStatement.RETURN_GENERATED_KEYS);
            pstmt.setString(1, image.getName());
            pstmt.setString(2, image.getType());
            int insertedCount = pstmt.executeUpdate();
            if(insertedCount > 0){
                try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        int imageId = generatedKeys.getInt(1);
                        image.setId(imageId);

                        boolean insertedForEntity = updateImageIdForEntity(entity, image);
                        if(insertedForEntity){
                            conn.commit();
                            inserted = true;
                            logger.info("Inserted image to database.");
                        }
                        else{
                            conn.rollback();
                        }
                    }
                    else{
                        conn.rollback();
                    }
                }
            }
            else{
                conn.rollback();
            }

        } finally {
            DBUtil.closeEverything(conn, pstmt);
        }

        return inserted;
    }

    @Override
    public boolean updateImage(Client entity, Image image) throws SQLException {
        boolean updated = false;
        Connection conn = null;
        PreparedStatement pstmt = null;

        try{
            conn = connectionPool.checkOut();
            pstmt = conn.prepareStatement(UPDATE_IMAGE_QUERY);
            pstmt.setString(1, image.getName());
            pstmt.setString(2, image.getType());
            pstmt.setInt(3, entity.getAvatarImage().getId());

            int updatedCount = pstmt.executeUpdate();
            if(updatedCount > 0){
                updated = true;
            }
        } finally {
            DBUtil.closeEverything(conn, pstmt);
        }

        return updated;
    }

    @Override
    public Image getImageByEntity(Client entity) throws SQLException {
        int imageId = getImageIdByEntity(entity);
        Image image = null;
        Connection conn = null;
        PreparedStatement pstmt = null;

        try{
            conn = connectionPool.checkOut();
            pstmt = conn.prepareStatement(GET_IMAGE_BY_ID_QUERY);
            pstmt.setInt(1, imageId);
            ResultSet rs = pstmt.executeQuery();
            if(rs.next()){
                String name = rs.getString("name");
                String type = rs.getString("type");
                String url = getImageURL(name);

                image = new Image(imageId, name, type, url);
            }

        } finally {
            DBUtil.closeEverything(conn, pstmt);
        }

        return image;
    }

    private boolean updateImageIdForEntity(Client entity, Image image) throws SQLException {
        boolean updated = false;
        Connection conn = null;
        PreparedStatement pstmt = null;

        try{
            conn = connectionPool.checkOut();
            pstmt = conn.prepareStatement(UPDATE_IMAGE_FOR_ENTITY_QUERY);
            pstmt.setInt(1, image.getId());
            pstmt.setInt(2, entity.getId());
            int updatedCount = pstmt.executeUpdate();
            if(updatedCount > 0){
                updated = true;
            }

        } finally {
            DBUtil.closeEverything(conn, pstmt);
        }

        return updated;
    }

    private int getImageIdByEntity(Client client) throws SQLException {
        int imageId = 0;
        Connection conn = null;
        PreparedStatement pstmt = null;

        try{
            conn = connectionPool.checkOut();
            pstmt = conn.prepareStatement(GET_CLIENT_BY_ID_QUERY);
            pstmt.setInt(1, client.getId());
            ResultSet rs = pstmt.executeQuery();
            if(rs.next()){
                imageId = rs.getInt("fk_avatar_image_id");
            }

        } finally {
            DBUtil.closeEverything(conn, pstmt);
        }

        return imageId;
    }

    private String getImageURL(String name) {
        return configReader.getClientImagesSpringURL() + name;
    }
}
