package com.jsuchinski.galeria.servlet;

import com.google.common.primitives.Ints;
import com.jsuchinski.galeria.db.DAO;
import com.jsuchinski.galeria.db.MariaDB_DAOImlp;
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
import java.util.Optional;

@WebServlet(name = "FotoServlet", value = "/foto")
public class FotoServlet extends HttpServlet {
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
        // WYSWIETLANIE
        int idZdjecia = Optional.ofNullable(request.getParameter("id_zdjecia")).map(Ints::tryParse).orElse(-1);
        if(idZdjecia == -1) {
            System.out.println("FotoServlet - zły id zdjecia");
            String msg = "Nie ma takiego zdjęcia!";
            request.setAttribute("msg", msg);
            request.getRequestDispatcher("/foto.jsp").forward(request, response);
            return;
        }

        // CZY MOŻNA OCENIC
        User user = (User) request.getSession().getAttribute("user");
        request.setAttribute("can_rate", db.canRateFoto(user, idZdjecia));

        // OCENY
        Object[] pair = db.getFotoRating(idZdjecia);
        request.setAttribute("rating", String.format("%.1f", (double)pair[0]));
        request.setAttribute("rating_count", pair[1]);

        // Dane zdjecia
        FotoTileData foto = db.getFotoData(idZdjecia);
        if(foto != null) {
            request.setAttribute("foto", foto);
            System.out.println("FotoServlet - znaleziono zdjecie");
        } else {
            System.out.println("FotoServlet - zdjecia nie znaleziono");
            String msg = "Zdjęcie nie istnieje!";
            request.setAttribute("msg", msg);
        }

        request.getRequestDispatcher("/foto.jsp").forward(request, response);
    }

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        System.out.println("foto post rating");
        int rating = Optional.ofNullable(request.getParameter("ocena")).map(Ints::tryParse).orElse(-1);
        if (rating < 1 || rating > 10) {
            System.out.println("FotoServlet - zła wartość oceny: " + rating);
            request.setAttribute("alert", "Ejże! Nie majstruj przy ocenie. Zakres ocen to 1-10.");
            request.getRequestDispatcher("/foto.jsp").forward(request, response);
            return;
        }

        int idZdjecia = Optional.ofNullable(request.getParameter("id_zdjecia")).map(Ints::tryParse).orElse(-1);
        if (idZdjecia == -1) {
            System.out.println("FotoServlet - zły id zdjecia");
            String msg = "Nie ma takiego zdjęcia!";
            request.setAttribute("msg", msg);
            request.getRequestDispatcher("/foto.jsp").forward(request, response);
            return;
        }

        User user = (User) request.getSession().getAttribute("user");
        boolean success = db.addFotoRating(idZdjecia,user,rating);

        //request.getRequestDispatcher("/foto").forward(request, response);
        doGet(request,response);
    }
}