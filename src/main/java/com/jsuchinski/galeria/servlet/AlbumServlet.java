package com.jsuchinski.galeria.servlet;

import com.google.common.primitives.Ints;
import com.jsuchinski.galeria.db.DAO;
import com.jsuchinski.galeria.db.MariaDB_DAOImlp;
import com.jsuchinski.galeria.model.FotoTileData;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@WebServlet(name = "AlbumServlet", value = "/album")
public class AlbumServlet extends HttpServlet {
    DAO db;
    @Override
    public void init() throws ServletException {
        super.init();
        db = new MariaDB_DAOImlp();
        try {
            db.initConnection();
        } catch (SQLException | ClassNotFoundException e) {
            throw new ServletException(e);
        }
    }
    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        try {
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
            int photoCount = db.getFotoCountForAlbum(idAlbumu);
            //! DEBUG
            //photoCount = 0;
            if (photoCount<=0) {
                System.out.println("AlbumServlet - brak obrazków");
                String msg = "W albumie nie ma żadnych zdjęć!";
                request.setAttribute("msg", msg);
                //request.getRequestDispatcher -> in finally
                return;
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
            List<FotoTileData> items = db.getFotosDataForAlbum(idAlbumu,sqlpage,pom);
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

        } catch (Exception e) {
            request.setAttribute("error", e.getMessage());
            System.out.println("AlbumServlet - SQLException");
            throw new RuntimeException(e);
        } finally {
            System.out.println("AlbumServlet - finally");
            request.getRequestDispatcher("/album.jsp").forward(request, response);
        }
    }
}