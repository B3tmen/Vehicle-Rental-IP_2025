package org.unibl.etf.clientapp.database;

import lombok.Getter;
import org.unibl.etf.clientapp.util.ConfigReader;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;


@Getter
public class DatabaseConnection {
    private static final ConfigReader configReader = ConfigReader.getInstance();

    private Connection connection;
    private static DatabaseConnection instance;

    private DatabaseConnection(){
        String url = configReader.getDatabaseURL();
        String username = configReader.getDatabaseUsername();
        String password = configReader.getDatabasePassword();

        registerDriver();

        try {
            connection = DriverManager.getConnection(url, username, password);

            if(connection != null){
                System.out.println("Database connection established.");
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

    }

    private void registerDriver(){
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            System.out.println("JDBC driver registered.");
        }
        catch(ClassNotFoundException ex) {
            System.out.println("Error: unable to load driver class!");
            System.exit(1);
        }
    }

    public static DatabaseConnection getInstance() {
        if (instance == null) {
            instance = new DatabaseConnection();
        }
        return instance;
    }

}
