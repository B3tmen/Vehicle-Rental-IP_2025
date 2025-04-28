package org.unibl.etf.clientapp.database;

import org.unibl.etf.clientapp.util.ConfigReader;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class ConnectionPool {
    private static ConfigReader configReader = ConfigReader.getInstance();

    private int preconnectCount;
    private int maxIdleConnections;
    private int maxConnections;

    private int connectCount;
    private List<Connection> usedConnections;
    private List<Connection> freeConnections;

    private static ConnectionPool instance;

    public static ConnectionPool getInstance() {
        if (instance == null)
            instance = new ConnectionPool();

        return instance;
    }

    private ConnectionPool() {
        readConnectionPoolPropertyConfiguration();

        try {
            freeConnections = new ArrayList<>();
            usedConnections = new ArrayList<>();

            for (int i = 0; i < preconnectCount; i++) {
                Connection conn = DatabaseConnection.getInstance().getConnection();
                freeConnections.add(conn);
            }
            connectCount = preconnectCount;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void readConnectionPoolPropertyConfiguration() {
        preconnectCount = 0;
        maxIdleConnections = 10;
        maxConnections = 10;

        try {
            preconnectCount = configReader.getPreconnectCount();
            maxIdleConnections = configReader.getMaxIdleConnections();
            maxConnections = configReader.getMaxConnections();
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
    }

    public synchronized Connection checkOut() throws SQLException {
        Connection conn = null;
        if (!freeConnections.isEmpty()) {
            conn = freeConnections.remove(0);
            usedConnections.add(conn);
        }
        else {
            if (connectCount < maxConnections) {
                conn = DatabaseConnection.getInstance().getConnection();
                usedConnections.add(conn);
                connectCount++;
            }
            else {
                try {
                    wait();
                    conn = freeConnections.remove(0);
                    usedConnections.add(conn);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
        return conn;
    }

    public synchronized void checkIn(Connection conn) {
        if (conn == null)
            return;

        if (usedConnections.remove(conn)) {
            freeConnections.add(conn);
            while (freeConnections.size() > maxIdleConnections) {
                int lastOne = freeConnections.size() - 1;
                Connection c = freeConnections.remove(lastOne);
                try {
                    c.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            notify();
        }
    }

    public synchronized void closeStatement(Statement stmt){
        try{
            stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
