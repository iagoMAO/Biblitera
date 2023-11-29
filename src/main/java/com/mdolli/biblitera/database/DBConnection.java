package com.mdolli.biblitera.database;

import java.sql.*;

public class DBConnection
{
    private static String host = "localhost";
    private static String port = "3306";

    private static String user = "root";
    private static String password = "iago1406@";
    private static String database = "biblitera";

    private static Connection conn = null;
    private static String driverName = "com.mysql.cj.jdbc.Driver";

    private static String url = String.format("jdbc:mysql://%s:%s/%s", host, port, database);

    public static Connection estabilishConnection() throws ClassNotFoundException, SQLException {
        Class.forName(driverName);
        conn = DriverManager.getConnection(url, user, password);
        return conn;
    }

    public static void dropConnection() throws SQLException {
        if(conn != null)
            conn.close();
    }
}
