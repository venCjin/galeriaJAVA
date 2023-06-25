package com.jsuchinski.galeria.servlet;

import com.google.common.primitives.Ints;
import com.jsuchinski.galeria.MySqlDB;
import com.jsuchinski.galeria.model.AlbumTileData;
import com.jsuchinski.galeria.model.FotoTileData;
import com.jsuchinski.galeria.model.User;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@WebServlet(name = "FotoServlet", value = "/foto")
public class FotoServlet extends HttpServlet {
    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        try (Connection con = MySqlDB.getConnection()) {
            String selectSql;

            // WYSWIETLANIE
            int idZdjecia = Optional.ofNullable(request.getParameter("id_zdjecia")).map(Ints::tryParse).orElse(-1);
            if(idZdjecia == -1) {
                System.out.println("FotoServlet - zły id zdjecia");
                String msg = "Nie ma takiego zdjęcia!";
                request.setAttribute("msg", msg);
                //request.getRequestDispatcher -> in finally
                return;
            }

            // CZY MOŻNA OCENIC
            User user = (User) request.getSession().getAttribute("user");
            request.setAttribute("can_rate", false);
            if(user != null && user.isActive()) {
                selectSql = "SELECT *" +
                    " FROM zadjecia_oceny" +
                    " WHERE id_uzytkownika=?" +
                    " AND id_zadjecia=?";
                try (PreparedStatement pstmt = con.prepareStatement(selectSql)) {
                    pstmt.setInt(1, user.getId());
                    pstmt.setInt(2, idZdjecia);
                    try (ResultSet rs = pstmt.executeQuery()) {
                        if(!rs.next()) {
                            request.setAttribute("can_rate", true);
                        }
                    }
                }
            }

            // OCENY
            selectSql = "SELECT AVG(ocena) as avg, COUNT(ocena) as count" +
                " FROM zadjecia_oceny" +
                " WHERE id_zadjecia=?";
            try (PreparedStatement pstmt = con.prepareStatement(selectSql)) {
                pstmt.setInt(1, idZdjecia);
                try (ResultSet rs = pstmt.executeQuery()) {
                    if(rs.next()) {
                        request.setAttribute("rating", String.format("%.1f", rs.getDouble("avg")));
                        request.setAttribute("rating_count", rs.getInt("count"));
                    }
                }
            }

            // Dane zdjecia
            selectSql = "SELECT z.tytul, z.id_albumu, z.data, u.login" +
                " FROM zadjecia AS z" +
                " JOIN uzytkownicy AS u ON z.id_uzytkownika=u.id" +
                " WHERE zaakceptowane AND z.id=?";
            try (PreparedStatement pstmt = con.prepareStatement(selectSql)) {
                pstmt.setInt(1, idZdjecia);
                try (ResultSet rs = pstmt.executeQuery()) {

                    if (rs.next()) {
                        //Date d = rs.getDate("data");
                        FotoTileData foto = new FotoTileData(
                            rs.getInt("id_albumu"),
                            idZdjecia,
                            rs.getString("tytul"),
                            rs.getString("login"),
                            rs.getString("data")
                        );
                        request.setAttribute("foto", foto);
                        System.out.println("FotoServlet - znaleziono zdjecie");
                    } else {
                        System.out.println("FotoServlet - zdjecia nie znaleziono");
                        String msg = "Zdjęcie nie istnieje!";
                        request.setAttribute("msg", msg);
                    }
                }
            }
        } catch (SQLException e) {
            request.setAttribute("error", "Błąd bazy danych");
            System.out.println("AlbumServlet - SQLException");
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            request.setAttribute("error", "Błąd serwera");
            System.out.println("AlbumServlet - ClassNotFoundException");
            throw new RuntimeException(e);
        } finally {
            System.out.println("AlbumServlet - finally");
            request.getRequestDispatcher("/foto.jsp").forward(request, response);
        }
    }

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        System.out.println("foto post rating");
        try (Connection con = MySqlDB.getConnection()) {
            //String ocena = request.getParameter("ocena");
            int rating = Optional.ofNullable(request.getParameter("ocena")).map(Ints::tryParse).orElse(-1);
            if (rating < 1 || rating > 10) {
                System.out.println("FotoServlet - zła wartość oceny: " + rating);
                //request.setAttribute("msg", "Ejże! Nie majstruj przy ocenie. Zakres ocen to 1-10.");
                request.setAttribute("alert", "Ejże! Nie majstruj przy ocenie. Zakres ocen to 1-10.");
                return;
            }

            int idZdjecia = Optional.ofNullable(request.getParameter("id_zdjecia")).map(Ints::tryParse).orElse(-1);
            if (idZdjecia == -1) {
                System.out.println("FotoServlet - zły id zdjecia");
                String msg = "Nie ma takiego zdjęcia!";
                request.setAttribute("msg", msg);
                //request.getRequestDispatcher -> in finally
                return;
            }

            User user = (User) request.getSession().getAttribute("user");
            if (user != null && user.isActive()) {
                String insertSql = "INSERT INTO zadjecia_oceny VALUES (?,?,?)";
                try (PreparedStatement pstmt = con.prepareStatement(insertSql)) {
                    pstmt.setInt(1, idZdjecia);
                    pstmt.setInt(2, user.getId());
                    pstmt.setInt(3, rating);
                    boolean created = pstmt.executeUpdate() > 0;
                    //
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }
        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        } finally {
            //request.getRequestDispatcher("/foto").forward(request, response);
            doGet(request,response);
        }
    }
}