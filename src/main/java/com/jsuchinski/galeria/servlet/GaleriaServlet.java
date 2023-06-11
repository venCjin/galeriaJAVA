package com.jsuchinski.galeria.servlet;

import com.jsuchinski.galeria.MySqlDB;
import com.jsuchinski.galeria.model.User;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@WebServlet(name = "GaleriaServlet", value = "")
public class GaleriaServlet extends HttpServlet {
    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        try (Connection con = MySqlDB.getConnection()) {
            String selectSql = "SELECT a.id AS ile FROM albumy AS a JOIN zadjecia AS z ON a.id=z.id_albumu WHERE z.zaakceptowane GROUP BY a.id HAVING SUM(z.zaakceptowane)>0";
            try (PreparedStatement pstmt = con.prepareStatement(selectSql)) {
                try (ResultSet rs = pstmt.executeQuery()) {
                    if (rs.next()) {

                    } else {

                    }
                }
            }
        } catch (SQLException e) {
            //request.setAttribute("L_error", "Błąd bazy danych");
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            //request.setAttribute("L_error", "Błąd serwera");
            throw new RuntimeException(e);
        } finally {
            request.getRequestDispatcher("/galeria.jsp").forward(request, response);
        }
    }
}