package org.unibl.etf.clientapp.database.dao.impl;

import org.unibl.etf.clientapp.database.ConnectionPool;
import org.unibl.etf.clientapp.database.DBUtil;
import org.unibl.etf.clientapp.database.dao.interfaces.ImageDAO;
import org.unibl.etf.clientapp.database.dao.interfaces.VehicleImageDAO;
import org.unibl.etf.clientapp.model.dto.Image;
import org.unibl.etf.clientapp.model.dto.RentalVehicle;
import org.unibl.etf.clientapp.model.enums.VehicleType;
import org.unibl.etf.clientapp.util.ConfigReader;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class VehicleImageDAOImpl implements VehicleImageDAO<RentalVehicle> {
    private static final ConfigReader configReader = ConfigReader.getInstance();
    private static final ConnectionPool connectionPool = ConnectionPool.getInstance();
    private static final String GET_VEHICLE_BY_ID_QUERY = "SELECT * from rental_vehicle WHERE vehicle_id = ?";
    private static final String GET_IMAGE_BY_ID_QUERY = "SELECT * from image WHERE image_id = ?";


    @Override
    public Image getImageByEntity(RentalVehicle entity) throws SQLException {
        int imageId = getImageIdByEntity(entity);
        Image image = null;
        if(imageId == 0) {
            String defaultUrl = configReader.getVehiclesImageMissingURL();
            image = new Image(0, "", "", defaultUrl);
            return image;
        }

        Connection conn = null;
        PreparedStatement pstmt = null;
        try{
            conn = connectionPool.checkOut();
            pstmt = conn.prepareStatement(GET_IMAGE_BY_ID_QUERY);
            pstmt.setInt(1, imageId);
            ResultSet rs = pstmt.executeQuery();
            if(rs.next()) {
                int id = rs.getInt("image_id");
                String name = rs.getString("name");
                String type = rs.getString("type");
                String url = getImageURL(entity, name);

                image = new Image(id, name, type, url);
            }
        } finally {
            DBUtil.closeEverything(conn, pstmt);
        }

        return image;
    }

    private int getImageIdByEntity(RentalVehicle entity) throws SQLException {
        int imageId = 0;
        Connection conn = null;
        PreparedStatement pstmt = null;

        try{
            conn = connectionPool.checkOut();
            pstmt = conn.prepareStatement(GET_VEHICLE_BY_ID_QUERY);
            pstmt.setInt(1, entity.getId());
            ResultSet rs = pstmt.executeQuery();
            if(rs.next()){
                imageId = rs.getInt("fk_image_id");
            }

        } finally {
            DBUtil.closeEverything(conn, pstmt);
        }

        return imageId;
    }

    private String getImageURL(RentalVehicle entity, String name) {
        String type = entity.getType().equals(VehicleType.Car) ? "cars" : entity.getType().equals(VehicleType.Bicycle) ? "bicycles" : "scooters";

        return configReader.getVehicleImagesSpringURL() + type + "/" + name;
    }
}
