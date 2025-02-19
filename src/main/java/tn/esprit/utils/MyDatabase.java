package tn.esprit.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class MyDatabase {
    // to establish connection with the db

    private final String URL="jdbc:mysql://localhost:3306/pidev";
    private final String USERNAME="root";
    private final String PASSWORD="";
    private Connection conn;

    private static MyDatabase instance;


    private MyDatabase() {
        try {
            conn = DriverManager.getConnection(URL , USERNAME , PASSWORD);
            System.out.println("Successfully connected to database ");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
    public static MyDatabase getInstance() {
        if (instance == null) {
            instance = new MyDatabase();
        }
        return instance;
    }

    public Connection getConn() {
        return conn;
    }
}
