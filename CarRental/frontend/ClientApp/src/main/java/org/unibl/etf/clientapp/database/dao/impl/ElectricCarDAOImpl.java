package org.unibl.etf.clientapp.database.dao.impl;

import org.unibl.etf.clientapp.database.ConnectionPool;
import org.unibl.etf.clientapp.database.DBUtil;
import org.unibl.etf.clientapp.database.dao.interfaces.ElectricCarDAO;
import org.unibl.etf.clientapp.database.dao.interfaces.VehicleImageDAO;
import org.unibl.etf.clientapp.factory.impl.MySqlDAOFactoryImpl;
import org.unibl.etf.clientapp.factory.interfaces.DAOFactory;
import org.unibl.etf.clientapp.model.dto.*;
import org.unibl.etf.clientapp.model.enums.VehicleType;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ElectricCarDAOImpl implements ElectricCarDAO {

    private final ConnectionPool connectionPool = ConnectionPool.getInstance();
    private static final String GET_ALL_CARS_QUERY = "SELECT fk_vehicle_id, model, rental_price,\n" +
            "\t   car_id, purchase_date, ec.description AS car_description,\n" +
            "\t   manufacturer_id, name, state, address, phone_number, fax, email,\n" +
            "\t   rental_status_id, status\n" +
            "FROM electric_car ec\n" +
            "JOIN rental_vehicle rv ON ec.fk_vehicle_id = rv.vehicle_id\n" +
            "JOIN manufacturer m ON rv.fk_manufacturer_id = m.manufacturer_id\n" +
            "JOIN rental_status rs ON rv.fk_rental_status_id = rs.rental_status_id";
    private static final String GET_BY_ID_QUERY = "SELECT fk_vehicle_id, model, rental_price,\n" +
            "\t   car_id, purchase_date, ec.description AS car_description,\n" +
            "\t   manufacturer_id, name, state, address, phone_number, fax, email,\n" +
            "       rental_status_id, status\n" +
            "FROM electric_car ec\n" +
            "JOIN rental_vehicle rv ON ec.fk_vehicle_id = rv.vehicle_id\n" +
            "JOIN manufacturer m ON rv.fk_manufacturer_id = m.manufacturer_id\n" +
            "JOIN rental_status rs ON rv.fk_rental_status_id = rs.rental_status_id\n" +
            "WHERE fk_vehicle_id = ?";

    private VehicleImageDAO<RentalVehicle> vehicleImageDAO;

    public ElectricCarDAOImpl() {
        DAOFactory daoFactory = new MySqlDAOFactoryImpl();
        vehicleImageDAO = daoFactory.getVehicleImageDAO();
    }

    @Override
    public List<ElectricCar> getAll() throws SQLException {
        List<ElectricCar> cars = new ArrayList<>();
        Connection conn = null;
        PreparedStatement pstmt = null;

        try{
            conn = connectionPool.checkOut();
            pstmt = conn.prepareStatement(GET_ALL_CARS_QUERY);
            ResultSet rs = pstmt.executeQuery();
            while(rs.next()){
                ElectricCar car = getCarFromResultSet(rs);

                cars.add(car);
            }

        } finally {
            DBUtil.closeEverything(conn, pstmt);
        }

        return cars;
    }

    @Override
    public ElectricCar getById(int id) throws SQLException {
        ElectricCar car = null;
        Connection conn = null;
        PreparedStatement pstmt = null;
        try{
            conn = connectionPool.checkOut();
            pstmt = conn.prepareStatement(GET_BY_ID_QUERY);
            pstmt.setInt(1, id);

            ResultSet rs = pstmt.executeQuery();
            if(rs.next()){
                car = getCarFromResultSet(rs);
            }

        } finally {
            DBUtil.closeEverything(conn, pstmt);
        }

        return car;
    }

    @Override
    public int update(RentalVehicle vehicle) throws SQLException {
        return 0;
    }

    private ElectricCar getCarFromResultSet(ResultSet rs) throws SQLException {
        Manufacturer manufacturer = DBUtil.getManufacturerFromResultSet(rs);
        RentalStatus rentalStatus = DBUtil.getRentalStatusFromResultSet(rs);

        ElectricCar car = ElectricCar.builder()
                .id(rs.getInt("fk_vehicle_id"))
                .model(rs.getString("model"))
                .rentalPrice(rs.getBigDecimal("rental_price"))
                .image(null)
                .manufacturer(manufacturer)
                .rentalStatus(rentalStatus)
                .type(VehicleType.Car)
                .carId(rs.getString("car_id"))
                .description(rs.getString("car_description"))
                .purchaseDate(rs.getDate("purchase_date"))
                .build();

        Image image = vehicleImageDAO.getImageByEntity(car);
        car.setImage(image);

        return car;
    }
}
