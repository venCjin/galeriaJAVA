package com.jsuchinski.galeria.servlet;

import com.google.common.primitives.Ints;
import com.jsuchinski.galeria.db.DAO;
import com.jsuchinski.galeria.db.MariaDB_DAOImlp;
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
            /// ILOŚĆ ALBUMÓW
            int albumsCount = db.getAlbumCountForGallery();
            //! DEBUG
            //albumsCount = 0;
            if (albumsCount<=0) {
                String msg = "W galerii nie ma jeszcze żadnych zdjęć. Bądź pierwszym który je doda!";
                request.setAttribute("msg", msg);
                //request.getRequestDispatcher -> in finally
                System.out.println("GaleriaServlet - brak obrazków");
                return;
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
            List<GalleryTileData> items = db.getAlbumsDataForGallery(sort,sqlpage,pom);
            if (!items.isEmpty()) {
                request.setAttribute("sortParam", sortParam);
                request.setAttribute("page", page);
                request.setAttribute("page_items", pages);
                request.setAttribute("gallery_items", items);
                //request.getRequestDispatcher -> in finally
                System.out.println("GaleriaServlet - Miniatury znaleziono");
            }

        } catch (Exception e) {
            request.setAttribute("error", e.getMessage());
            System.out.println("GaleriaServlet - Exception");
            throw new RuntimeException(e);
        } finally {
            System.out.println("GaleriaServlet - finally");
            request.getRequestDispatcher("/galeria.jsp").forward(request, response);
        }
    }
}