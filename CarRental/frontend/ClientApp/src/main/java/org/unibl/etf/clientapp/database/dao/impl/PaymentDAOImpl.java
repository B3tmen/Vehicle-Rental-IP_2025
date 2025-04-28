package org.unibl.etf.clientapp.database.dao.impl;

import org.unibl.etf.clientapp.database.ConnectionPool;
import org.unibl.etf.clientapp.database.DBUtil;
import org.unibl.etf.clientapp.database.dao.interfaces.ClientDAO;
import org.unibl.etf.clientapp.database.dao.interfaces.PaymentDAO;
import org.unibl.etf.clientapp.factory.impl.MySqlDAOFactoryImpl;
import org.unibl.etf.clientapp.factory.interfaces.DAOFactory;
import org.unibl.etf.clientapp.model.dto.Client;
import org.unibl.etf.clientapp.model.dto.Location;
import org.unibl.etf.clientapp.model.dto.Payment;
import org.unibl.etf.clientapp.util.CustomLogger;

import java.sql.*;

public class PaymentDAOImpl implements PaymentDAO {
    private final ConnectionPool connectionPool = ConnectionPool.getInstance();
    private final CustomLogger logger = CustomLogger.getInstance(PaymentDAOImpl.class);
    private final ClientDAO clientDAO;
    private static final String INSERT_PAYMENT_QUERY = "INSERT INTO payment (token, type, expiry_date, card_holder_first_name, card_holder_last_name, card_last_4_digits, fk_client_id) " +
            "VALUES (?, ?, ?, ?, ?, ?, ?)";
    private static final String GET_BY_CLIENT_ID_QUERY = "SELECT * FROM payment WHERE fk_client_id = ?";
    private static final String GET_BY_TOKEN_QUERY = "SELECT * FROM payment WHERE token = ?";
    private static final String GET_BY_ID_QUERY = "SELECT * FROM payment WHERE payment_id = ?";

    public PaymentDAOImpl(){
        DAOFactory daoFactory = new MySqlDAOFactoryImpl();
        clientDAO = daoFactory.getClientDAO();
    }


    @Override
    public Payment insert(Payment payment) throws SQLException {
        Payment paymentInserted = null;

        Connection conn = null;
        PreparedStatement pstmt = null;
        try {
            conn = connectionPool.checkOut();
            pstmt = conn.prepareStatement(INSERT_PAYMENT_QUERY, Statement.RETURN_GENERATED_KEYS);

            pstmt.setString(1, payment.getToken());
            pstmt.setString(2, payment.getType());
            pstmt.setDate(3, payment.getExpiryDate());
            pstmt.setString(4, payment.getHolderFirstName());
            pstmt.setString(5, payment.getHolderLastName());
            pstmt.setString(6, payment.getLast4Digits());
            pstmt.setInt(7, payment.getClient().getId());

            int inserted = pstmt.executeUpdate();
            if(inserted > 0){
                try (ResultSet rs = pstmt.getGeneratedKeys()) {
                    if (rs.next()) {
                        Integer id = rs.getInt(1);
                        paymentInserted = new Payment(id,
                                payment.getToken(),
                                payment.getType(),
                                payment.getExpiryDate(),
                                payment.getHolderFirstName(),
                                payment.getHolderLastName(),
                                payment.getLast4Digits(),
                                payment.getClient());
                    }
                }
                String myStr = "Registered payment for client ID: %d to database.";
                String result = String.format(myStr, payment.getClient().getId());
                logger.info(result);
            }

        } finally {
            DBUtil.closeEverything(conn, pstmt);
        }

        return paymentInserted;
    }

    @Override
    public Payment getByToken(String token) throws SQLException {
        Payment payment = null;
        Connection conn = null;
        PreparedStatement pstmt = null;

        try{
            conn = connectionPool.checkOut();
            pstmt = conn.prepareStatement(GET_BY_TOKEN_QUERY);
            pstmt.setString(1, token);

            ResultSet rs = pstmt.executeQuery();
            if(rs.next()) {
                payment = getPaymentFromResultSet(rs);
            }
        } finally {
            DBUtil.closeEverything(conn, pstmt);
        }

        return payment;
    }

    @Override
    public Payment getByClient(Client client) throws SQLException {
        Payment payment = null;
        Connection conn = null;
        PreparedStatement pstmt = null;

        try{
            conn = connectionPool.checkOut();
            pstmt = conn.prepareStatement(GET_BY_CLIENT_ID_QUERY);
            pstmt.setInt(1, client.getId());

            ResultSet rs = pstmt.executeQuery();
            if(rs.next()) {
                payment = getPaymentFromResultSet(rs);
            }

        } finally {
            DBUtil.closeEverything(conn, pstmt);
        }

        return payment;
    }

    @Override
    public Payment getById(int id) throws SQLException {
        Payment payment = null;
        Connection conn = null;
        PreparedStatement pstmt = null;

        try{
            conn = connectionPool.checkOut();
            pstmt = conn.prepareStatement(GET_BY_ID_QUERY);
            pstmt.setInt(1, id);

            ResultSet rs = pstmt.executeQuery();
            if(rs.next()) {
                payment = getPaymentFromResultSet(rs);
            }
        } finally {
            DBUtil.closeEverything(conn, pstmt);
        }

        return payment;
    }

    private Payment getPaymentFromResultSet(ResultSet rs) throws SQLException {
        Integer id = rs.getInt("payment_id");
        String token = rs.getString("token");
        String type = rs.getString("type");
        Date expiryDate = rs.getDate("expiry_date");
        String holderFirstName = rs.getString("card_holder_first_name");
        String holderLastName = rs.getString("card_holder_last_name");
        String last4Digits = rs.getString("card_last_4_digits");
        int clientId = rs.getInt("fk_client_id");
        Client client = clientDAO.getById(clientId);

        return new Payment(id, token, type, expiryDate, holderFirstName, holderLastName, last4Digits, client);

    }
}
