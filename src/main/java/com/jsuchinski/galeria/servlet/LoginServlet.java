package com.jsuchinski.galeria.servlet;

import com.jsuchinski.galeria.MySqlDB;
import com.jsuchinski.galeria.model.User;

import java.io.*;
import java.sql.*;
import javax.servlet.ServletException;
import javax.servlet.http.*;
import javax.servlet.annotation.*;

@WebServlet(name = "LogServlet", value = "/login")
public class LoginServlet extends HttpServlet {
    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        String login = request.getParameter("L_login");
        String passwd = request.getParameter("L_password");

        try {
            User user = MySqlDB.login(login, passwd);
            HttpSession session = request.getSession();
            session.setAttribute("user", user);
            response.sendRedirect("/");
        } catch (Exception e) {
            request.setAttribute("L_error", e.getMessage());
            request.getRequestDispatcher("/reg-log.jsp").forward(request, response);
        }

        /*try (Connection con = MySqlDB.getConnection()) {
            String selectSql = "SELECT * FROM galeria.uzytkownicy WHERE login=? AND haslo=?";
            try (PreparedStatement pstmt = con.prepareStatement(selectSql)) {
                pstmt.setString(1, login);
                pstmt.setString(2, passwd);
                try (ResultSet rs = pstmt.executeQuery()) {
                    if (rs.next()) {
                        User user = User.fromResultSet(rs);
                        if(user.isActive()) {
                            HttpSession session = request.getSession();
                            session.setAttribute("user", user);
                            response.sendRedirect("/");
                        } else {
                            request.setAttribute("L_error", "Konto podanego użytkownika zostało zablokowane");
                            request.getRequestDispatcher("/reg-log.jsp").forward(request, response);
                        }
                    } else {
                        request.setAttribute("L_error", "Niepoprawne dane");
                        request.getRequestDispatcher("/reg-log.jsp").forward(request, response);
                    }
                }
            }
        } catch (SQLException e) {
            request.setAttribute("L_error", "Błąd bazy danych");
            request.getRequestDispatcher("/reg-log.jsp").forward(request, response);
        } catch (ClassNotFoundException e) {
            request.setAttribute("L_error", "Błąd serwera");
            request.getRequestDispatcher("/reg-log.jsp").forward(request, response);
        }*/
    }
}