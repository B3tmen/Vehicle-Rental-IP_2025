package org.unibl.etf.clientapp.database;

import org.unibl.etf.clientapp.model.dto.Announcement;
import org.unibl.etf.clientapp.model.dto.Manufacturer;
import org.unibl.etf.clientapp.model.dto.Promotion;
import org.unibl.etf.clientapp.model.dto.RentalStatus;

import java.sql.*;

public class DBUtil {
    private static final ConnectionPool connectionPool = ConnectionPool.getInstance();

    public static void closeEverything(Connection conn, Statement... stmts) {
        if(stmts != null){
            for(Statement s : stmts){
                if(s != null)
                    connectionPool.closeStatement(s);
            }
        }
        if(conn != null){
            connectionPool.checkIn(conn);
        }
    }

    public static Manufacturer getManufacturerFromResultSet(ResultSet rs) throws SQLException {
        return new Manufacturer(
                rs.getInt("manufacturer_id"),
                rs.getString("name"),
                rs.getString("state"),
                rs.getString("address"),
                rs.getString("phone_number"),
                rs.getString("fax"),
                rs.getString("email")
        );
    }

    public static RentalStatus getRentalStatusFromResultSet(ResultSet rs) throws SQLException {
        return new RentalStatus(
                rs.getInt("rental_status_id"),
                rs.getString("status")
        );
    }

}
