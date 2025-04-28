package org.unibl.etf.clientapp.database.dao.impl;

import org.unibl.etf.clientapp.database.ConnectionPool;
import org.unibl.etf.clientapp.database.DBUtil;
import org.unibl.etf.clientapp.database.dao.interfaces.ElectricScooterDAO;
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

public class ElectricScooterDAOImpl implements ElectricScooterDAO {
    private static final ConnectionPool connectionPool = ConnectionPool.getInstance();
    private static final String GET_ALL_SCOOTERS_QUERY = "SELECT fk_vehicle_id, model, rental_price,\n" +
            "\t   scooter_id, max_speed,\n" +
            "\t   manufacturer_id, name, state, address, phone_number, fax, email,\n" +
            "\t   rental_status_id, status\n" +
            "FROM electric_scooter es\n" +
            "JOIN rental_vehicle rv ON es.fk_vehicle_id = rv.vehicle_id\n" +
            "JOIN manufacturer m ON rv.fk_manufacturer_id = m.manufacturer_id\n" +
            "JOIN rental_status rs ON rv.fk_rental_status_id = rs.rental_status_id";
    private static final String GET_BY_ID_QUERY = "SELECT fk_vehicle_id, model, rental_price,\n" +
            "\t   scooter_id, max_speed,\n" +
            "\t   manufacturer_id, name, state, address, phone_number, fax, email,\n" +
            "       rental_status_id, status\n" +
            "FROM electric_scooter es\n" +
            "JOIN rental_vehicle rv ON es.fk_vehicle_id = rv.vehicle_id\n" +
            "JOIN manufacturer m ON rv.fk_manufacturer_id = m.manufacturer_id\n" +
            "JOIN rental_status rs ON rv.fk_rental_status_id = rs.rental_status_id\n" +
            "WHERE fk_vehicle_id = ?";

    private VehicleImageDAO<RentalVehicle> vehicleImageDAO;

    public ElectricScooterDAOImpl() {
        DAOFactory daoFactory = new MySqlDAOFactoryImpl();
        vehicleImageDAO = daoFactory.getVehicleImageDAO();
    }

    @Override
    public List<ElectricScooter> getAll() throws SQLException {
        List<ElectricScooter> scooters = new ArrayList<>();
        Connection conn = null;
        PreparedStatement pstmt = null;

        try{
            conn = connectionPool.checkOut();
            pstmt = conn.prepareStatement(GET_ALL_SCOOTERS_QUERY);
            ResultSet rs = pstmt.executeQuery();
            while(rs.next()){
                ElectricScooter scooter = getScooterFromResultSet(rs);

                scooters.add(scooter);
            }

        } finally {
            DBUtil.closeEverything(conn, pstmt);
        }

        return scooters;
    }

    @Override
    public ElectricScooter getById(int id) throws SQLException {
        ElectricScooter scooter = null;
        Connection conn = null;
        PreparedStatement pstmt = null;
        try{
            conn = connectionPool.checkOut();
            pstmt = conn.prepareStatement(GET_BY_ID_QUERY);
            pstmt.setInt(1, id);

            ResultSet rs = pstmt.executeQuery();
            if(rs.next()){
                scooter = getScooterFromResultSet(rs);
            }

        } finally {
            DBUtil.closeEverything(conn, pstmt);
        }

        return scooter;
    }

    @Override
    public int update(RentalVehicle vehicle) throws SQLException {
        return 0;
    }

    private ElectricScooter getScooterFromResultSet(ResultSet rs) throws SQLException{
        Manufacturer manufacturer = DBUtil.getManufacturerFromResultSet(rs);
        RentalStatus rentalStatus = DBUtil.getRentalStatusFromResultSet(rs);

        ElectricScooter scooter = ElectricScooter.builder()
                .id(rs.getInt("fk_vehicle_id"))
                .model(rs.getString("model"))
                .rentalPrice(rs.getBigDecimal("rental_price"))
                .image(null)
                .manufacturer(manufacturer)
                .rentalStatus(rentalStatus)
                .type(VehicleType.Scooter)
                .scooterId(rs.getString("scooter_id"))
                .maxSpeed(rs.getInt("max_speed"))
                .build();
        Image image = vehicleImageDAO.getImageByEntity(scooter);
        scooter.setImage(image);

        return scooter;
    }
}
