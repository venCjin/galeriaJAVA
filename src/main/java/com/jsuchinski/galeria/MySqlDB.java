package com.jsuchinski.galeria;

import com.jsuchinski.galeria.model.User;

import java.sql.*;

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

    public static boolean isLoginOccupied(String login) {
        boolean found = false;
        try (Connection con = getConnection()) {
            String sql = "SELECT COUNT(*) FROM galeria.uzytkownicy WHERE login = ?";
            try (PreparedStatement pstmt = con.prepareStatement(sql)) {
                pstmt.setString(1, login);
                try (ResultSet rs = pstmt.executeQuery()) {
                    rs.next();
                    int count = rs.getInt(1);
                    if (count > 0) {
                        found = true;
                    }
                }
            }
        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        return found;
    }

    public static boolean createUser(String login, String password, String email) throws Exception {
        boolean created = false;
        try (Connection con = getConnection()) {
            String sql = "INSERT INTO uzytkownicy (login, email, haslo, zarejestrowany, uprawnienia, aktywny) VALUES(?, ?, ?, CURRENT_DATE, 'uzytkownik', 1)";
            try (PreparedStatement pstmt = con.prepareStatement(sql)) {
                pstmt.setString(1, login);
                pstmt.setString(2, email);
                pstmt.setString(3, password);

                created = pstmt.executeUpdate() > 0;
            }
        } catch (SQLException | ClassNotFoundException e) {
            throw new Exception("Wystąpił błąd przy rejestrowaniu użytkownika.<br>Opis błędu: "+e.getMessage(), e);
        }
        return created;
    }

    public static User login(String login, String password) throws Exception {
        if (login.isBlank() || password.isBlank()) {
            throw new Exception("Niepoprawne dane logowania");
        }
        try (Connection con = MySqlDB.getConnection()) {
            String selectSql = "SELECT * FROM galeria.uzytkownicy WHERE login=? AND haslo=?";
            try (PreparedStatement pstmt = con.prepareStatement(selectSql)) {
                pstmt.setString(1, login);
                pstmt.setString(2, password);
                try (ResultSet rs = pstmt.executeQuery()) {
                    if (rs.next()) {
                        return User.fromResultSet(rs);
                    } else {
                        throw new Exception("Niepoprawne dane logowania");
                    }
                }
            }
        } catch (SQLException e) {
            throw new Exception("Błąd bazy danych", e);
        } catch (ClassNotFoundException e) {
            throw new Exception("Błąd serwera", e);
        }
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
