package com.jsuchinski.galeria.servlet;

import com.google.common.primitives.Ints;
import com.jsuchinski.galeria.MySqlDB;
import com.jsuchinski.galeria.model.GalleryTileData;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@WebServlet(name = "GaleriaServlet", value = "")
public class GaleriaServlet extends HttpServlet {
    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        try (Connection con = MySqlDB.getConnection()) {
            /// ILOŚĆ ALBUMÓW
            int albumsCount = 0;
            String selectSql = "SELECT COUNT(*) AS liczba_wierszy FROM ( SELECT a.id FROM albumy AS a JOIN zadjecia AS z ON a.id = z.id_albumu WHERE z.zaakceptowane GROUP BY a.id HAVING SUM(z.zaakceptowane) > 0) AS podzapytanie";
            try (PreparedStatement pstmt = con.prepareStatement(selectSql)) {
                try (ResultSet rs = pstmt.executeQuery()) {
                    boolean hasData = rs.next();
                    if (hasData) albumsCount = rs.getInt("liczba_wierszy");

                    //! DEBUG
                    //albumsCount = 0;

                    if (!hasData || albumsCount<=0) {
                        String msg = "W galerii nie ma jeszcze żadnych zdjęć. Bądź pierwszym który je doda!";
                        request.setAttribute("msg", msg);
                        //request.getRequestDispatcher -> in finally
                        System.out.println("GaleriaServlet - brak obrazków");
                        return;
                    }
                }
            }

            /// STRONNICOWANIE
            int pom = 20;
            int pageCount = (int) Math.ceil((double)albumsCount / pom);
            int[] pages = new int [pageCount];
            for (int i = 0; i < pages.length; i++) {
                pages[i] = i+1;
            }
            int page = Optional.ofNullable(request.getParameter("page")).map(Ints::tryParse).orElse(1);
            int sqlpage = pom*(page-1);

            /// SORTOWANIE
            String sortParam = Optional.ofNullable(request.getParameter("sort")).orElse("sort-t");
            String sort = "a.tytul";
            if(sortParam.equals("sort-n")) {
                sort = "u.login";
            } else if (sortParam.equals("sort-d")) {
                sort = "a.data";
            }

            /// MINIATURY
            System.out.println("GaleriaServlet - Miniatury");
            selectSql = "SELECT a.id AS id_a, z.id AS id_z, a.tytul AS tytul, a.data AS data, u.login AS login FROM albumy AS a JOIN uzytkownicy AS u ON a.id_uzytkownika=u.id JOIN zadjecia AS z ON a.id=z.id_albumu WHERE z.zaakceptowane GROUP BY a.id ORDER BY "+sort+" limit ?,?";
            try (PreparedStatement pstmt = con.prepareStatement(selectSql)) {
                pstmt.setInt(1, sqlpage);
                pstmt.setInt(2, pom);
                try (ResultSet rs = pstmt.executeQuery()) {

                    //! DEBUG
                    //albumsCount = 0;

                    List<GalleryTileData> items = new ArrayList<>();

                    while (rs.next()) {
                        //Date d = rs.getDate("data");
                        GalleryTileData data = new GalleryTileData(
                            rs.getInt("id_a"),
                            rs.getInt("id_z"),
                            rs.getString("tytul"),
                            rs.getString("login"),
                            rs.getString("data")
                        );
                        items.add(data);
                    }

                    if (!items.isEmpty()) {
                        request.setAttribute("sortParam", sortParam);
                        request.setAttribute("page", page);
                        request.setAttribute("page_items", pages);
                        request.setAttribute("gallery_items", items);
                        //request.getRequestDispatcher -> in finally
                        System.out.println("GaleriaServlet - Miniatury znaleziono");
                    }
                }
            }


        } catch (SQLException e) {
            request.setAttribute("error", "Błąd bazy danych");
            System.out.println("GaleriaServlet - SQLException");
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            request.setAttribute("error", "Błąd serwera");
            System.out.println("GaleriaServlet - ClassNotFoundException");
            throw new RuntimeException(e);
        } finally {
            System.out.println("GaleriaServlet - finally");
            request.getRequestDispatcher("/galeria.jsp").forward(request, response);
        }
    }
}