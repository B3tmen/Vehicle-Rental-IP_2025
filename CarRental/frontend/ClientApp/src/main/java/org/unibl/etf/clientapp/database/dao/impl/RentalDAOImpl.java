package org.unibl.etf.clientapp.database.dao.impl;

import org.unibl.etf.clientapp.database.ConnectionPool;
import org.unibl.etf.clientapp.database.DBUtil;
import org.unibl.etf.clientapp.database.dao.interfaces.*;
import org.unibl.etf.clientapp.factory.impl.MySqlDAOFactoryImpl;
import org.unibl.etf.clientapp.factory.interfaces.DAOFactory;
import org.unibl.etf.clientapp.model.dto.*;
import org.unibl.etf.clientapp.model.enums.RentalStatusEnum;
import org.unibl.etf.clientapp.model.enums.VehicleType;
import org.unibl.etf.clientapp.util.CustomLogger;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class RentalDAOImpl implements RentalDAO {
    private final CustomLogger logger = CustomLogger.getInstance(RentalDAOImpl.class);
    private static final ConnectionPool connectionPool = ConnectionPool.getInstance();
    private static final String INSERT_RENTAL_QUERY = "INSERT INTO rental (fk_vehicle_id, fk_client_id, rental_date_time, duration, fk_pickup_location_id, fk_dropoff_location_id, fk_payment_id) " +
            "VALUES (?, ?, ?, ?, ?, ?, ?)";

    private final RentalVehicleDAO<RentalVehicle> rentalVehicleDAO;

    public RentalDAOImpl() {
        this.rentalVehicleDAO = new RentalVehicleDAOImpl();
    }

    @Override
    public Rental insert(Rental rental) throws SQLException {
        Rental inserted = null;
        int insertedRows = 0;
        Connection conn = null;
        PreparedStatement pstmt = null;

        try{
            conn = connectionPool.checkOut();
            conn.setAutoCommit(false);
            pstmt = conn.prepareStatement(INSERT_RENTAL_QUERY, Statement.RETURN_GENERATED_KEYS);
            pstmt.setInt(1, rental.getVehicle().getId());
            pstmt.setInt(2, rental.getClient().getId());
            pstmt.setTimestamp(3, Timestamp.valueOf(rental.getRentalDateTime()));
            pstmt.setInt(4, rental.getDuration());
            pstmt.setInt(5, rental.getPickupLocation().getId());
            pstmt.setInt(6, rental.getDropoffLocation().getId());
            pstmt.setInt(7, rental.getPayment().getId());

            insertedRows = pstmt.executeUpdate();
            if(insertedRows > 0) {
                RentalVehicle vehicle = rental.getVehicle();
                vehicle.getRentalStatus().setId(RentalStatusEnum.Rented.ordinal() + 1);     // TODO: fetch from database?
                vehicle.getRentalStatus().setStatus(RentalStatusEnum.Rented.toString());

                int vehicleUpdated = rentalVehicleDAO.update(vehicle);
                if(vehicleUpdated > 0) {
                    try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                        if (generatedKeys.next()) {
                            conn.commit();
                            int id = generatedKeys.getInt(1);
                            inserted = Rental.builder()
                                    .id(id)
                                    .vehicle(vehicle)
                                    .client(rental.getClient())
                                    .rentalDateTime(rental.getRentalDateTime())
                                    .duration(rental.getDuration())
                                    .pickupLocation(rental.getPickupLocation())
                                    .dropoffLocation(rental.getDropoffLocation())
                                    .payment(rental.getPayment())
                                    .build();

                            String myStr = "Rented vehicle (%s) %s for client ID=%d. Registered to database.";
                            String result = String.format(myStr, rental.getVehicle().getRuntimeVehicleType(), rental.getVehicle().getModel(), rental.getClient().getId());
                            logger.info(result);
                        }
                    }

                }
            }
            else{
                conn.rollback();
                String myStr = "Vehicle ID:%d could not be rented and registered to the database.";
                String result = String.format(myStr, rental.getVehicle().getId());
                logger.warn(result);
            }
        } catch (Exception e){
            conn.rollback();
            e.printStackTrace();
            logger.error(e.getMessage());
        }

        finally {
            DBUtil.closeEverything(conn, pstmt);
        }

        return inserted;
    }

}
