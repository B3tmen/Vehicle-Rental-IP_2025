package org.unibl.etf.clientapp.database.dao.impl;

import org.unibl.etf.clientapp.database.ConnectionPool;
import org.unibl.etf.clientapp.database.DBUtil;
import org.unibl.etf.clientapp.database.dao.interfaces.ElectricBicycleDAO;
import org.unibl.etf.clientapp.database.dao.interfaces.VehicleImageDAO;
import org.unibl.etf.clientapp.factory.impl.MySqlDAOFactoryImpl;
import org.unibl.etf.clientapp.factory.interfaces.DAOFactory;
import org.unibl.etf.clientapp.model.dto.*;
import org.unibl.etf.clientapp.model.enums.VehicleType;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ElectricBicycleDAOImpl implements ElectricBicycleDAO {
    private static final ConnectionPool connectionPool = ConnectionPool.getInstance();
    private static final String GET_ALL_BICYCLES_QUERY = "SELECT fk_vehicle_id, model, rental_price,\n" +
            "\t   bicycle_id, riding_autonomy,\n" +
            "\t   manufacturer_id, name, state, address, phone_number, fax, email,\n" +
            "\t   rental_status_id, status\n" +
            "FROM electric_bicycle eb\n" +
            "JOIN rental_vehicle rv ON eb.fk_vehicle_id = rv.vehicle_id\n" +
            "JOIN manufacturer m ON rv.fk_manufacturer_id = m.manufacturer_id\n" +
            "JOIN rental_status rs ON rv.fk_rental_status_id = rs.rental_status_id\n";
    private static final String GET_BY_ID_QUERY = "SELECT fk_vehicle_id, model, rental_price,\n" +
            "\t   bicycle_id, riding_autonomy,\n" +
            "\t   manufacturer_id, name, state, address, phone_number, fax, email,\n" +
            "       rental_status_id, status\n" +
            "FROM electric_bicycle eb\n" +
            "JOIN rental_vehicle rv ON eb.fk_vehicle_id = rv.vehicle_id\n" +
            "JOIN manufacturer m ON rv.fk_manufacturer_id = m.manufacturer_id\n" +
            "JOIN rental_status rs ON rv.fk_rental_status_id = rs.rental_status_id\n" +
            "WHERE fk_vehicle_id = ?";

    private VehicleImageDAO<RentalVehicle> vehicleImageDAO;

    public ElectricBicycleDAOImpl() {
        DAOFactory daoFactory = new MySqlDAOFactoryImpl();
        vehicleImageDAO = daoFactory.getVehicleImageDAO();
    }

    @Override
    public List<ElectricBicycle> getAll() throws SQLException {
        List<ElectricBicycle> bicycles = new ArrayList<>();
        Connection conn = null;
        PreparedStatement pstmt = null;

        try{
            conn = connectionPool.checkOut();
            pstmt = conn.prepareStatement(GET_ALL_BICYCLES_QUERY);
            ResultSet rs = pstmt.executeQuery();
            while(rs.next()){
                ElectricBicycle bicycle = getBicycleFromResultSet(rs);

                bicycles.add(bicycle);
            }

        } finally {
            DBUtil.closeEverything(conn, pstmt);
        }

        return bicycles;
    }

    @Override
    public ElectricBicycle getById(int id) throws SQLException {
        ElectricBicycle bicycle = null;
        Connection conn = null;
        PreparedStatement pstmt = null;
        try{
            conn = connectionPool.checkOut();
            pstmt = conn.prepareStatement(GET_BY_ID_QUERY);
            pstmt.setInt(1, id);

            ResultSet rs = pstmt.executeQuery();
            if(rs.next()){
                bicycle = getBicycleFromResultSet(rs);
            }

        } finally {
            DBUtil.closeEverything(conn, pstmt);
        }

        return bicycle;
    }

    @Override
    public int update(RentalVehicle vehicle) throws SQLException {
        return 0;
    }

    private ElectricBicycle getBicycleFromResultSet(ResultSet rs) throws SQLException{
        Manufacturer manufacturer = DBUtil.getManufacturerFromResultSet(rs);
        RentalStatus rentalStatus = DBUtil.getRentalStatusFromResultSet(rs);

        ElectricBicycle bicycle = ElectricBicycle.builder()
                .id(rs.getInt("fk_vehicle_id"))
                .model(rs.getString("model"))
                .rentalPrice(rs.getBigDecimal("rental_price"))
                .image(null)    // TODO: fix image retrieval for bicycles
                .manufacturer(manufacturer)
                .rentalStatus(rentalStatus)
                .type(VehicleType.Bicycle)
                .bicycleId(rs.getString("bicycle_id"))
                .ridingAutonomy(rs.getInt("riding_autonomy"))
                .build();
        Image image = vehicleImageDAO.getImageByEntity(bicycle);
        bicycle.setImage(image);

        return bicycle;
    }
}
