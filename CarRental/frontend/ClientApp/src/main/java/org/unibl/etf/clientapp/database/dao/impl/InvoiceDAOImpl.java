package org.unibl.etf.clientapp.database.dao.impl;

import org.unibl.etf.clientapp.database.ConnectionPool;
import org.unibl.etf.clientapp.database.DBUtil;
import org.unibl.etf.clientapp.database.dao.interfaces.*;
import org.unibl.etf.clientapp.factory.impl.MySqlDAOFactoryImpl;
import org.unibl.etf.clientapp.factory.interfaces.DAOFactory;
import org.unibl.etf.clientapp.model.dto.*;
import org.unibl.etf.clientapp.model.enums.VehicleType;
import org.unibl.etf.clientapp.util.ConfigReader;

import java.math.BigDecimal;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class InvoiceDAOImpl implements InvoiceDAO {
    private final ConfigReader configReader = ConfigReader.getInstance();
    private final ConnectionPool connectionPool = ConnectionPool.getInstance();
    private static final String INSERT_INVOICE_QUERY = "INSERT INTO invoice (pdf_name, issue_date, grand_total, fk_rental_id) VALUES (?, ?, ?, ?)";
    private static final String GET_ALL_BY_CLIENT_ID_QUERY = "SELECT * FROM invoice i\n" +
                                                             "RIGHT OUTER JOIN rental r ON i.fk_rental_id=r.rental_id\n" +
                                                             "WHERE r.fk_client_id=?;";

    private final DAOFactory daoFactory;
    private final RentalVehicleDAO<RentalVehicle> rentalVehicleDAO;
    private final ClientDAO clientDAO;
    private final LocationDAO locationDAO;
    private final PaymentDAO paymentDAO;

    public InvoiceDAOImpl() {
        this.daoFactory = new MySqlDAOFactoryImpl();
        this.rentalVehicleDAO = new RentalVehicleDAOImpl();
        this.clientDAO = daoFactory.getClientDAO();
        this.locationDAO = daoFactory.getLocationDAO();
        this.paymentDAO = daoFactory.getPaymentDAO();
    }

    @Override
    public Invoice insert(Invoice invoice) throws SQLException {
        Invoice inserted = null;
        Connection conn = null;
        PreparedStatement pstmt = null;

        try {
            conn = connectionPool.checkOut();
            conn.setAutoCommit(false);
            pstmt = conn.prepareStatement(INSERT_INVOICE_QUERY, PreparedStatement.RETURN_GENERATED_KEYS);
            pstmt.setString(1, invoice.getPdfName());
            pstmt.setTimestamp(2, invoice.getIssueDate());
            pstmt.setBigDecimal(3, invoice.getGrandTotal());
            pstmt.setInt(4, invoice.getRental().getId());

            System.out.println("Invoice from DAO: " + invoice);

            int insertedRows = pstmt.executeUpdate();
            if(insertedRows > 0){
                try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        int id = generatedKeys.getInt(1);
                        inserted = new Invoice(id, invoice.getPdfName(), invoice.getInvoiceURL(), invoice.getIssueDate(), invoice.getGrandTotal(), invoice.getRental());

                        System.out.println("Insreted invoice: " + inserted);
                        conn.commit();
                    }
                }
            }
            else{
                conn.rollback();
            }
        }
        finally {
            DBUtil.closeEverything(conn, pstmt);
        }

        return inserted;
    }

    @Override
    public List<Invoice> getAllByClientId(int clientId) throws SQLException {
        List<Invoice> invoices = new ArrayList<>();
        Connection conn = null;
        PreparedStatement pstmt = null;

        try{
            conn = connectionPool.checkOut();
            pstmt = conn.prepareStatement(GET_ALL_BY_CLIENT_ID_QUERY);
            pstmt.setInt(1, clientId);
            ResultSet rs = pstmt.executeQuery();

            while(rs.next()){
                Invoice invoice = getInvoiceFromResultSet(rs);
                invoices.add(invoice);
            }

        } catch (Exception e){
            e.printStackTrace();
        }

        finally {
            DBUtil.closeEverything(conn, pstmt);
        }

        return invoices;
    }

    private Invoice getInvoiceFromResultSet(ResultSet rs) throws SQLException {
        int id = rs.getInt("invoice_id");
        String pdfName = rs.getString("pdf_name");
        String url = configReader.getInvoicesURL() + pdfName;
        Timestamp issueDate = rs.getTimestamp("issue_date");
        BigDecimal grandTotal = rs.getBigDecimal("grand_total");
        Rental rental = getRentalFromResultSet(rs);

        return new Invoice(id, pdfName, url, issueDate, grandTotal, rental);
    }

    private Rental getRentalFromResultSet(ResultSet rs) throws SQLException {
        Integer id = rs.getInt("rental_id");

        int vehicleId = rs.getInt("fk_vehicle_id");
        RentalVehicle vehicleParent = rentalVehicleDAO.getById(vehicleId);
        RentalVehicle concreteVehicle = getConcreteVehicle(vehicleParent);

        int clientId = rs.getInt("fk_client_id");
        Client client = clientDAO.getById(clientId);

        Timestamp timestamp = rs.getTimestamp("rental_date_time");
        LocalDateTime rentalDateTime = timestamp.toLocalDateTime();
        Integer duration = rs.getInt("duration");

        int pickupLocationId = rs.getInt("fk_pickup_location_id");
        Location pickupLocation = locationDAO.getById(pickupLocationId);
        int dropoffLocationId = rs.getInt("fk_dropoff_location_id");
        Location dropoffLocation = locationDAO.getById(dropoffLocationId);

        int paymentId = rs.getInt("fk_payment_id");
        Payment payment = paymentDAO.getById(paymentId);

        return Rental.builder()
                .id(id)
                .vehicle(concreteVehicle)
                .client(client)
                .rentalDateTime(rentalDateTime)
                .duration(duration)
                .pickupLocation(pickupLocation)
                .dropoffLocation(dropoffLocation)
                .payment(payment)
                .build();
    }

    private RentalVehicle getConcreteVehicle(RentalVehicle rentalVehicle) throws SQLException {
        VehicleType type = rentalVehicle.getType();
        RentalVehicleDAO dao = null;
        if(type.equals(VehicleType.Car)) {
            dao = daoFactory.getRentalVehicleDAO(ElectricCar.class);
        }
        else if(type.equals(VehicleType.Bicycle)) {
            dao = daoFactory.getRentalVehicleDAO(ElectricBicycle.class);
        }
        else {
            dao = daoFactory.getRentalVehicleDAO(ElectricScooter.class);
        }

        return dao.getById(rentalVehicle.getId());
    }
}
