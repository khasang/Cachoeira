package ru.khasang.cachoeira.data;

import org.sqlite.SQLiteConfig;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBHelper {
    private static DBHelper instance;
    private static Connection connection;

    public static DBHelper getInstance() {
        if (instance == null) {
            instance = new DBHelper();
        }
        return instance;
    }

    public Connection getConnection(String path) {
        if (connection == null) {
            try {
                Class.forName("org.sqlite.JDBC");
                String dbUrl = "jdbc:sqlite://" + path;
                SQLiteConfig sqLiteConfig = new SQLiteConfig();
                sqLiteConfig.enforceForeignKeys(true);
                connection = DriverManager.getConnection(dbUrl, sqLiteConfig.toProperties());
            } catch (ClassNotFoundException | SQLException e) {
                e.printStackTrace();
            }

        }
        return connection;
    }

    private DBHelper() {
    }
}
