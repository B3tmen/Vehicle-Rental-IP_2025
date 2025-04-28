package org.unibl.etf.clientapp.database.dao.impl;

import org.unibl.etf.clientapp.database.ConnectionPool;
import org.unibl.etf.clientapp.database.DBUtil;
import org.unibl.etf.clientapp.database.dao.interfaces.LocationDAO;
import org.unibl.etf.clientapp.model.dto.Location;
import org.unibl.etf.clientapp.util.CustomLogger;

import java.math.BigDecimal;
import java.sql.*;

public class LocationDAOImpl implements LocationDAO {
    private final CustomLogger logger = CustomLogger.getInstance(LocationDAOImpl.class);
    private static final ConnectionPool connectionPool = ConnectionPool.getInstance();
    private static final String INSERT_LOCATION_QUERY = "INSERT INTO location (latitude, longitude) VALUES (?, ?)";
    private static final String UPDATE_LOCATION_QUERY = "UPDATE location SET latitude=? AND longitude=? WHERE location_id=?";
    private static final String GET_BY_ID_QUERY = "SELECT * FROM location WHERE location_id=?";
    private static final String GET_BY_LATITUDE_LONGITUDE_QUERY = "SELECT * FROM location WHERE latitude=? AND longitude=?";

    @Override
    public Location insert(Location location) throws SQLException {
        Location insertedLocation = null;
        int inserted = 0;
        Connection conn = null;
        PreparedStatement pstmt = null;

        try{
            conn = connectionPool.checkOut();
            pstmt = conn.prepareStatement(INSERT_LOCATION_QUERY, Statement.RETURN_GENERATED_KEYS);
            pstmt.setBigDecimal(1, location.getLatitude());
            pstmt.setBigDecimal(2, location.getLongitude());

            inserted = pstmt.executeUpdate();
            if(inserted > 0){
                try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        int id = generatedKeys.getInt(1);
                        insertedLocation = new Location(id, location.getLatitude(), location.getLongitude());
                    }
                }

                String myStr = "Inserted location [%s, %s] into database.";
                String result = String.format(myStr, location.getLatitude().toString(), location.getLongitude().toString());
                logger.info(result);
            }
        } finally {
            DBUtil.closeEverything(conn, pstmt);
        }

        return insertedLocation;
    }

    @Override
    public int update(Location location) throws SQLException {
        int updated = 0;
        Connection conn = null;
        PreparedStatement pstmt = null;

        try{
            conn = connectionPool.checkOut();
            pstmt = conn.prepareStatement(UPDATE_LOCATION_QUERY);
            pstmt.setBigDecimal(1, location.getLatitude());
            pstmt.setBigDecimal(2, location.getLongitude());
            pstmt.setInt(3, location.getId());

            updated = pstmt.executeUpdate();
        } finally {
            DBUtil.closeEverything(conn, pstmt);
        }

        return updated;
    }

    @Override
    public Location getById(int id) throws SQLException {
        Location location = null;
        Connection conn = null;
        PreparedStatement pstmt = null;

        try{
            conn = connectionPool.checkOut();
            pstmt = conn.prepareStatement(GET_BY_ID_QUERY);
            pstmt.setInt(1, id);

            ResultSet rs = pstmt.executeQuery();
            if(rs.next()){
                BigDecimal lat = rs.getBigDecimal("latitude");
                BigDecimal lng = rs.getBigDecimal("longitude");

                location = new Location(id, lat, lng);
            }
        } finally {
            DBUtil.closeEverything(conn, pstmt);
        }

        return location;
    }

    @Override
    public Location getByLatitudeLongitude(BigDecimal latitude, BigDecimal longitude) throws SQLException {
        Location location = null;
        Connection conn = null;
        PreparedStatement pstmt = null;

        try{
            conn = connectionPool.checkOut();
            pstmt = conn.prepareStatement(GET_BY_LATITUDE_LONGITUDE_QUERY);
            pstmt.setBigDecimal(1, latitude);
            pstmt.setBigDecimal(2, longitude);

            ResultSet rs = pstmt.executeQuery();
            if(rs.next()){
                int id = rs.getInt("location_id");
                BigDecimal lat = rs.getBigDecimal("latitude");
                BigDecimal lng = rs.getBigDecimal("longitude");

                location = new Location(id, lat, lng);
            }
        } finally {
            DBUtil.closeEverything(conn, pstmt);
        }

        return location;
    }
}
