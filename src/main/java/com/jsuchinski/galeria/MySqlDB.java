package com.jsuchinski.galeria;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class MySqlDB {
    public static Connection getConnection() throws SQLException, ClassNotFoundException {
        Class.forName("com.mysql.cj.jdbc.Driver");
        String url = "jdbc:mysql://localhost:3306/galeria";
//        String user = "user1";
//        String pass = "pass";
        String user = "root";
        String pass = "password";
        return DriverManager.getConnection(url, user, pass);
    }

//    public static void main(String[] args) {
//        String url = "jdbc:mysql://localhost:3306/galeria";
//        String user = "user1";
//        String pass = "pass";
//        try (Connection con = getConnection()) {
////        try (Connection con = DriverManager.getConnection(url, user, pass)) {
////            try (Statement stmt = con.createStatement()) {
////                String tableSql = "CREATE TABLE IF NOT EXISTS employees"
////                        + "(emp_id int PRIMARY KEY AUTO_INCREMENT, name varchar(30),"
////                        + "position varchar(30), salary double)";
////                stmt.execute(tableSql);
////            }
//            System.out.println("good");
//        } catch (SQLException | ClassNotFoundException e) {
//            throw new RuntimeException(e);
//        }
//    }
}
