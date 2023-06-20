package com.jsuchinski.galeria.servlet;

import com.google.common.primitives.Ints;
import com.jsuchinski.galeria.MySqlDB;
import com.jsuchinski.galeria.model.AlbumTileData;
import com.jsuchinski.galeria.model.GalleryTileData;

import javax.annotation.Nullable;
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

@WebServlet(name = "AlbumServlet", value = "/album")
public class AlbumServlet extends HttpServlet {
    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        try (Connection con = MySqlDB.getConnection()) {
            int idAlbumu = Optional.ofNullable(request.getParameter("id_albumu")).map(Ints::tryParse).orElse(-1);
            request.setAttribute("id_albumu", idAlbumu);
            if(idAlbumu == -1) {
                System.out.println("AlbumServlet - zły id albumu");
                String msg = "W albumie nie ma żadnych zdjęć!";
                request.setAttribute("msg", msg);
                //request.getRequestDispatcher -> in finally
                return;
            }
            /// ILOŚĆ ZDJĘć
            int photoCount = 0;
            String selectSql =
                    "SELECT COUNT(*) AS liczba_wierszy FROM (" +
                    " SELECT z.id" +
                    " FROM albumy AS a" +
                    " JOIN zadjecia AS z" +
                    " ON a.id=z.id_albumu" +
                    " WHERE z.zaakceptowane AND a.id=?" +
                    " GROUP BY z.id" +
                    ") AS podzapytanie";
            try (PreparedStatement pstmt = con.prepareStatement(selectSql)) {
                pstmt.setInt(1, idAlbumu);
                try (ResultSet rs = pstmt.executeQuery()) {
                    boolean hasData = rs.next();
                    if (hasData) photoCount = rs.getInt("liczba_wierszy");

                    //! DEBUG
                    //photoCount = 0;

                    if (!hasData || photoCount<=0) {
                        System.out.println("AlbumServlet - brak obrazków");
                        String msg = "W albumie nie ma żadnych zdjęć!";
                        request.setAttribute("msg", msg);
                        //request.getRequestDispatcher -> in finally
                        return;
                    }
                }
            }

            /// STRONNICOWANIE
            int pom = 20;
            int pageCount = (int) Math.ceil((double)photoCount / pom);
            int[] pages = new int [pageCount];
            for (int i = 0; i < pages.length; i++) {
                pages[i] = i+1;
            }
            int page = Optional.ofNullable(request.getParameter("page")).map(Ints::tryParse).orElse(1);
            int sqlpage = pom*(page-1);

            /// MINIATURY
            System.out.println("GaleriaServlet - Miniatury");
            selectSql = "SELECT z.id AS id_z, z.tytul AS opis, z.data AS data, u.login AS autor" +
                " FROM albumy AS a" +
                " JOIN zadjecia AS z ON a.id=z.id_albumu" +
                " JOIN uzytkownicy AS u ON z.id_uzytkownika=u.id" +
                " WHERE z.zaakceptowane AND a.id=?" +
                " ORDER BY a.tytul" +
                " limit ?,?";
            try (PreparedStatement pstmt = con.prepareStatement(selectSql)) {
                pstmt.setInt(1, idAlbumu);
                pstmt.setInt(2, sqlpage);
                pstmt.setInt(3, pom);
                try (ResultSet rs = pstmt.executeQuery()) {

                    //! DEBUG
                    //albumsCount = 0;

                    List<AlbumTileData> items = new ArrayList<>();

                    while (rs.next()) {
                        //Date d = rs.getDate("data");
                        AlbumTileData data = new AlbumTileData(
                            rs.getInt("id_z"),
                            rs.getString("opis"),
                            rs.getString("autor"),
                            rs.getString("data")
                        );
                        items.add(data);
                    }

                    if (!items.isEmpty()) {
                        request.setAttribute("page", page);
                        request.setAttribute("page_items", pages);
                        request.setAttribute("items", items);
                        //request.getRequestDispatcher -> in finally
                        System.out.println("AlbumServlet - Miniatury znaleziono");
                    } else {
                        System.out.println("AlbumServlet - Brak obrazkow 2");
                        String msg = "W albumie nie ma żadnych zdjęć!";
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
            request.getRequestDispatcher("/album.jsp").forward(request, response);
        }
    }
}