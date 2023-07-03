package com.jsuchinski.galeria.servlet;

import com.google.common.primitives.Ints;
import com.jsuchinski.galeria.db.DAO;
import com.jsuchinski.galeria.db.MariaDB_DAOImlp;
import com.jsuchinski.galeria.model.GalleryTileData;
import com.jsuchinski.galeria.model.User;
import com.jsuchinski.galeria.utils.FileUtils;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@WebServlet(name = "AddFotoServlet", value = "/dodaj-foto")
@MultipartConfig(
    fileSizeThreshold = 1024 * 1024 * 1, // 1 MB
    maxFileSize = 1024 * 1024 * 10,      // 10 MB
    maxRequestSize = 1024 * 1024 * 100   // 100 MB
)
public class AddFotoServlet extends HttpServlet {
    DAO db;
    FileUtils fu;
    @Override
    public void init() throws ServletException {
        super.init();
        fu = new FileUtils();
        db = new MariaDB_DAOImlp();
        try {
            db.initConnection();
        } catch (SQLException | ClassNotFoundException e) {
            throw new ServletException(e);
        }
    }
    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        User user = (User) request.getSession().getAttribute("user");
        if(user == null || !user.isActive()) {
            response.sendRedirect("/reg-log.jsp?log_err");
            return;
        }

        try {
            List<GalleryTileData> items = db.getUserAlbums(user);
            request.setAttribute("albums", items);
        } catch (Exception e) {
            request.setAttribute("error", e.getMessage());
        }

        request.getRequestDispatcher("/dodaj-foto.jsp").forward(request, response);
    }

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        User user = (User) request.getSession().getAttribute("user");
        if(user == null || !user.isActive()) {
            response.sendRedirect("/reg-log.jsp?log_err");
            return;
        }

        int idAlbumu = Optional.ofNullable(request.getParameter("id_albumu")).map(Ints::tryParse).orElse(-1);
        if(idAlbumu==-1) {
            request.setAttribute("error", "Zły identyfikator(id) albumu!");
            request.getRequestDispatcher("/dodaj-foto.jsp").forward(request, response);
            return;
        }

        String desc = Optional.ofNullable(request.getParameter("opis")).orElse("");

        int idZdjecia;
        try {
            idZdjecia = db.addFoto(idAlbumu,desc,user);
        } catch (Exception e) {
            request.setAttribute("error", e.getMessage());
            request.getRequestDispatcher("/dodaj-foto.jsp").forward(request, response);
            return;
        }

        if(fu.saveFoto(request.getPart("zdjecie"), request.getRealPath("/img/"+idAlbumu+"/"+idZdjecia))) {
            request.setAttribute("success", "Zdjęcie zostało dodane!");
            request.setAttribute("id_albumu", idAlbumu);
            request.setAttribute("id_foto", idZdjecia);
        }

        //request.getRequestDispatcher("/dodaj-foto.jsp").forward(request, response);
        doGet(request,response);
    }
}
