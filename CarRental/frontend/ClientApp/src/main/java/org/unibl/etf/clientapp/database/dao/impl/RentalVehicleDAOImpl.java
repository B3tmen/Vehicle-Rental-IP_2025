package org.unibl.etf.clientapp.database.dao.impl;

import org.unibl.etf.clientapp.database.ConnectionPool;
import org.unibl.etf.clientapp.database.DBUtil;
import org.unibl.etf.clientapp.database.dao.interfaces.RentalVehicleDAO;
import org.unibl.etf.clientapp.model.dto.*;
import org.unibl.etf.clientapp.model.enums.VehicleType;
import org.unibl.etf.clientapp.util.CustomLogger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class RentalVehicleDAOImpl implements RentalVehicleDAO<RentalVehicle> {
    private final CustomLogger logger = CustomLogger.getInstance(RentalVehicleDAOImpl.class);
    private final ConnectionPool connectionPool = ConnectionPool.getInstance();
    private static final String GET_BY_ID_QUERY = "SELECT vehicle_id, model, rental_price, type_, \n" +
            "\t   manufacturer_id, name, state, address, phone_number, fax, email,\n" +
            "\t   rental_status_id, status\n" +
            "FROM rental_vehicle rv\n" +
            "JOIN manufacturer m ON rv.fk_manufacturer_id = m.manufacturer_id\n" +
            "JOIN rental_status rs ON rv.fk_rental_status_id = rs.rental_status_id\n" +
            "WHERE vehicle_id = ?";
    // TODO: delete unnecessary columns?
    private static final String UPDATE_VEHICLE_QUERY = "UPDATE rental_vehicle SET model=?, rental_price=?, fk_manufacturer_id=?, fk_rental_status_id=? WHERE vehicle_id = ?";

    @Override
    public List<RentalVehicle> getAll() throws SQLException {
        return List.of();
    }

    @Override
    public RentalVehicle getById(int id) throws SQLException {
        RentalVehicle vehicle = null;
        Connection conn = null;
        PreparedStatement pstmt = null;

        try{
            conn = connectionPool.checkOut();
            pstmt = conn.prepareStatement(GET_BY_ID_QUERY);
            pstmt.setInt(1, id);

            ResultSet rs = pstmt.executeQuery();
            if(rs.next()){
                vehicle = getVehicleFromResultSet(rs);
            }

        } finally {
            DBUtil.closeEverything(conn, pstmt);
        }

        return vehicle;
    }

    @Override
    public int update(RentalVehicle vehicle) throws SQLException {
        int updated = 0;
        Connection conn = null;
        PreparedStatement pstmt = null;

        try{
            conn = connectionPool.checkOut();
            conn.setAutoCommit(false);
            pstmt = conn.prepareStatement(UPDATE_VEHICLE_QUERY);
            pstmt.setString(1, vehicle.getModel());
            pstmt.setBigDecimal(2, vehicle.getRentalPrice());
            pstmt.setInt(3, vehicle.getManufacturer().getId());
            pstmt.setInt(4, vehicle.getRentalStatus().getId());
            pstmt.setInt(5, vehicle.getId());

            updated = pstmt.executeUpdate();
            if(updated > 0) {
                conn.commit();
                logger.info("RentalVehicle updated, ID: " + vehicle.getId());
            }
            else{
                conn.rollback();
            }

        }
        finally {
            DBUtil.closeEverything(conn, pstmt);
        }

        return updated;
    }

    private RentalVehicle getVehicleFromResultSet(ResultSet rs) throws SQLException {
        Manufacturer manufacturer = DBUtil.getManufacturerFromResultSet(rs);
        RentalStatus rentalStatus = DBUtil.getRentalStatusFromResultSet(rs);

        RentalVehicle vehicle = ElectricCar.builder()
                .id(rs.getInt("vehicle_id"))
                .model(rs.getString("model"))
                .rentalPrice(rs.getBigDecimal("rental_price"))
                .image(null)
                .manufacturer(manufacturer)
                .rentalStatus(rentalStatus)
                .type(getVehicleType(rs.getString("type_")))
                .build();

        return vehicle;
    }

    private VehicleType getVehicleType(String type) {
        if(type.equals("Car")){
            return VehicleType.Car;
        }
        else if(type.equals("Bicycle")) {
            return VehicleType.Bicycle;
        }
        else{
            return VehicleType.Scooter;
        }
    }
}
