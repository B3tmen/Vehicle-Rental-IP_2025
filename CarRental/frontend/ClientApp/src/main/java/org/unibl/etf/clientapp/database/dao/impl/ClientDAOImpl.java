package org.unibl.etf.clientapp.database.dao.impl;

import org.unibl.etf.clientapp.database.ConnectionPool;
import org.unibl.etf.clientapp.database.DBUtil;
import org.unibl.etf.clientapp.database.dao.interfaces.AvatarImageDAO;
import org.unibl.etf.clientapp.database.dao.interfaces.ClientDAO;
import org.unibl.etf.clientapp.factory.impl.MySqlDAOFactoryImpl;
import org.unibl.etf.clientapp.factory.interfaces.DAOFactory;
import org.unibl.etf.clientapp.model.dto.requests.AuthenticationRequest;
import org.unibl.etf.clientapp.model.dto.Client;
import org.unibl.etf.clientapp.model.dto.Image;
import org.unibl.etf.clientapp.model.enums.CitizenType;
import org.unibl.etf.clientapp.model.enums.UserType;
import org.unibl.etf.clientapp.util.CustomLogger;
import org.unibl.etf.clientapp.util.HelperClass;

import java.sql.*;

public class ClientDAOImpl implements ClientDAO {
    private final CustomLogger logger = CustomLogger.getInstance(ClientDAOImpl.class);
    private static final ConnectionPool connectionPool = ConnectionPool.getInstance();
    private static final String LOGIN_QUERY = "SELECT * FROM user u " +
                                              "INNER JOIN client c ON u.user_id=c.fk_user_id " +
                                              "WHERE u.username=? AND u.type_='Client'";
    private static final String CHANGE_PASSWORD_QUERY = "UPDATE user u " +
                                                        "SET password_hash=? " +
                                                        "WHERE u.user_id=? AND u.type_='Client'";
    private static final String GET_BY_ID_QUERY = "SELECT * FROM user u " +
                                                     "INNER JOIN client c ON u.user_id=c.fk_user_id " +
                                                     "WHERE u.user_id=?";
    private static final String DEACTIVATE_CLIENT_QUERY = "UPDATE user u SET u.is_active=0 WHERE u.user_id=?";
    private static final String INSERT_USER_QUERY = "INSERT INTO user (username, password_hash, first_name, last_name, type_) VALUES (?, ?, ?, ?, ?)";
    private static final String INSERT_CLIENT_QUERY = "INSERT INTO client (fk_user_id, personal_card_number, email, phone_number, fk_avatar_image_id, citizen_type, drivers_licence) VALUES (?, ?, ?, ?, ?, ?, ?)";

    private final AvatarImageDAO imageDAO;

    public ClientDAOImpl() {
        DAOFactory daoFactory = new MySqlDAOFactoryImpl();
        imageDAO = daoFactory.getAvatarImageDAO();
    }

    @Override
    public Client login(AuthenticationRequest authenticationRequest) throws SQLException {
        String username = authenticationRequest.getUsername();
        String password = authenticationRequest.getPassword();

        Connection conn = null;
        PreparedStatement pstmt = null;
        Client client = null;
        try{
            conn = connectionPool.checkOut();
            pstmt = conn.prepareStatement(LOGIN_QUERY);
            pstmt.setString(1, username);

            ResultSet rs = pstmt.executeQuery();
            if(rs.next()){
                String passwordHash = rs.getString("password_hash");    // Get the stored password hash from the database (BCrypt adds salt automatically, so we have to
                                                                                    // verify it using the verifyer() function)
                boolean verified = HelperClass.verifyPassword(password, passwordHash);
                if(verified){
                    client = getClientFromResultSet(rs);
                }
            }

        } finally {
            DBUtil.closeEverything(conn, pstmt);
        }

        return client;
    }

    @Override
    public Client register(Client client) throws SQLException {
        String hash = HelperClass.getBCryptHash(client.getPasswordHash());
        client.setPasswordHash(hash);

        Client newClient = null;
        Connection conn = null;
        PreparedStatement userStmt = null;
        PreparedStatement clientStmt = null;
        ResultSet generatedKeys = null;
        try{
            conn = connectionPool.checkOut();
            conn.setAutoCommit(false);  // Start transaction

            userStmt = conn.prepareStatement(INSERT_USER_QUERY, Statement.RETURN_GENERATED_KEYS);
            generatedKeys = insertUser(client, userStmt); // // Insert into the user table, return the ID FK

            if(generatedKeys.next()){
                int userId = generatedKeys.getInt(1);
                client.setId(userId);
                clientStmt = conn.prepareStatement(INSERT_CLIENT_QUERY);
                newClient = insertClient(client, clientStmt);   // Insert into the client table

                if(newClient != null){
                    conn.commit();
                }
                else{
                    conn.rollback();
                    throw new SQLException("Creating client failed. Rolling back transaction");
                }
            }
            else{
                conn.rollback();
                throw new SQLException("Creating client failed, no ID obtained.");
            }

        } finally {
            DBUtil.closeEverything(conn, userStmt, clientStmt);
            if(generatedKeys != null)
                generatedKeys.close();
        }

        return newClient;
    }

    @Override
    public Client changePassword(Client client, String newPasswordHash) throws SQLException {
        Connection conn = null;
        PreparedStatement updateStmt = null;
        PreparedStatement selectStmt = null;
        Client updatedClient = null;

        try{
            conn = connectionPool.checkOut();
            conn.setAutoCommit(false);  // Start transaction

            updateStmt = conn.prepareStatement(CHANGE_PASSWORD_QUERY);
            updateStmt.setString(1, newPasswordHash);
            updateStmt.setInt(2, client.getId());

            int updated = updateStmt.executeUpdate();
            if(updated > 0){
                conn.commit();
                DBUtil.closeEverything(conn, updateStmt);

                updatedClient = getById(client.getId());
            }
            else{
                conn.rollback();
                throw new SQLException("Changing password for client failed. Rolling back transaction");
            }
        } finally {
            DBUtil.closeEverything(conn, updateStmt, selectStmt);
        }

        return updatedClient;
    }

    @Override
    public Client getById(int id) throws SQLException {
        Client client = null;
        Connection conn = null;
        PreparedStatement pstmt = null;

        try{
            conn = connectionPool.checkOut();
            pstmt = conn.prepareStatement(GET_BY_ID_QUERY);
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();
            if(rs.next()){
                client = getClientFromResultSet(rs);
            }
        } finally {
            DBUtil.closeEverything(conn, pstmt);
        }

        return client;
    }

    @Override
    public int deactivate(int id) throws SQLException {
        int deactivated = 0;
        Connection conn = null;
        PreparedStatement pstmt = null;

        try{
            conn = connectionPool.checkOut();
            pstmt = conn.prepareStatement(DEACTIVATE_CLIENT_QUERY);
            pstmt.setInt(1, id);

            deactivated = pstmt.executeUpdate();
        } finally {
            DBUtil.closeEverything(conn, pstmt);
        }

        return deactivated;
    }

    private CitizenType getCitizenType(String type){
        if (type.equals("Foreigner")) {
            return CitizenType.Foreigner;
        }
        return CitizenType.Local;
    }

    private ResultSet insertUser(Client client, PreparedStatement userStmt) throws SQLException {
        userStmt.setString(1, client.getUsername());
        userStmt.setString(2, client.getPasswordHash());
        userStmt.setString(3, client.getFirstName());
        userStmt.setString(4, client.getLastName());
        userStmt.setString(5, UserType.Client.toString());

        int userRows = userStmt.executeUpdate();
        if (userRows == 0) {
            throw new SQLException("Creating user failed, no rows affected.");
        }
        return userStmt.getGeneratedKeys();
    }

    private Client insertClient(Client client, PreparedStatement clientStmt) throws SQLException {
        clientStmt.setInt(1, client.getId());
        clientStmt.setString(2, client.getPersonalCardNumber());
        clientStmt.setString(3, client.getEmail());
        clientStmt.setString(4, client.getPhoneNumber());
        if (client.getAvatarImage() != null) {
            clientStmt.setInt(5, client.getAvatarImage().getId());
        } else {
            clientStmt.setNull(5, Types.INTEGER);
        }
        clientStmt.setString(6, client.getCitizenType().name());
        clientStmt.setNull(7, Types.INTEGER);

        int inserted = clientStmt.executeUpdate();
        if(inserted > 0){
            Integer id = client.getId();
            String username = client.getUsername();
            String passwordHash = client.getPasswordHash();
            String firstName = client.getFirstName();
            String lastName = client.getLastName();
            String personalCardNumber = client.getPersonalCardNumber();
            String email = client.getEmail();
            String phoneNumber = client.getPhoneNumber();
            boolean isActive = client.getIsActive();
            CitizenType citizenType = client.getCitizenType();
            Integer driversLicence = client.getDriversLicence();
            Image image = client.getAvatarImage();

            return new Client(id, username, passwordHash, firstName, lastName, personalCardNumber, email, phoneNumber, isActive, citizenType, driversLicence, image);
        }

        return null;
    }

    private Client getClientFromResultSet(ResultSet rs) throws SQLException {
        int id = rs.getInt("user_id");
        String username = rs.getString("username");
        String passwordHash = rs.getString("password_hash");
        String firstName = rs.getString("first_name");
        String lastName = rs.getString("last_name");
        String personalCardNumber = rs.getString("personal_card_number");
        String email = rs.getString("email");
        boolean isActive = rs.getBoolean("is_active");
        int driversLicence = rs.getInt("drivers_licence");
        String phoneNumber = rs.getString("phone_number");
        String citizenType = rs.getString("citizen_type");

        Client client = new Client(id, username, passwordHash, firstName, lastName,
                personalCardNumber, email, phoneNumber, isActive, getCitizenType(citizenType), driversLicence, null);
        Image avatarImage = imageDAO.getImageByEntity(client);
        client.setAvatarImage(avatarImage);

        return client;
    }
}
