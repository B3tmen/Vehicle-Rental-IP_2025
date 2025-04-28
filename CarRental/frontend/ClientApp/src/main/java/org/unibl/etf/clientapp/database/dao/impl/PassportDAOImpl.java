package org.unibl.etf.clientapp.database.dao.impl;

import org.unibl.etf.clientapp.database.ConnectionPool;
import org.unibl.etf.clientapp.database.DBUtil;
import org.unibl.etf.clientapp.database.DatabaseConnection;
import org.unibl.etf.clientapp.database.dao.interfaces.PassportDAO;
import org.unibl.etf.clientapp.model.dto.Passport;
import org.unibl.etf.clientapp.util.CustomLogger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class PassportDAOImpl implements PassportDAO {
    private final CustomLogger logger = CustomLogger.getInstance(PassportDAOImpl.class);
    private static final ConnectionPool connectionPool = ConnectionPool.getInstance();
    private static final String INSERT_PASSPORT_QUERY = "INSERT INTO passport (passport_number, country, valid_from, valid_to, fk_client_id) " +
            "VALUES (?, ?, ?, ?, ?)";


    @Override
    public int insert(Passport passport, int clientId) throws SQLException {
        int inserted = 0;
        Connection conn = null;
        PreparedStatement pstmt = null;

        try{
            conn = connectionPool.checkOut();
            pstmt = conn.prepareStatement(INSERT_PASSPORT_QUERY);
            pstmt.setString(1, passport.getPassportNumber());
            pstmt.setString(2, passport.getCountry());
            pstmt.setDate(3, passport.getValidFrom());
            pstmt.setDate(4, passport.getValidTo());
            pstmt.setInt(5, clientId);

            inserted = pstmt.executeUpdate();
            if(inserted > 0){
                String myStr = "Inserted passport into database for client ID: %d.";
                String result = String.format(myStr, clientId);
                logger.info(result);
            }
        } finally {
            DBUtil.closeEverything(conn, pstmt);
        }

        return inserted;
    }
}
