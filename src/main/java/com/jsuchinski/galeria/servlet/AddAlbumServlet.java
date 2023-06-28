package com.jsuchinski.galeria.servlet;

import com.jsuchinski.galeria.db.DAO;
import com.jsuchinski.galeria.db.MariaDB_DAOImlp;
import com.jsuchinski.galeria.model.User;
import com.jsuchinski.galeria.utils.FileUtils;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URL;
import java.sql.SQLException;
import java.util.Set;

@WebServlet(name = "AddAlbumServlet", value = "/dodaj-album")
public class AddAlbumServlet extends HttpServlet {
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

        request.getRequestDispatcher("/dodaj-album.jsp").forward(request, response);
    }

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        User user = (User) request.getSession().getAttribute("user");
        if(user == null || !user.isActive()) {
            response.sendRedirect("/reg-log.jsp?log_err");
            return;
        }

        String albumName = request.getParameter("nazwa_albumu");
        if(albumName == null || albumName.isBlank()) {
            request.setAttribute("error", "Nie podano nazwy albumu.");
        } else if (albumName.length() > 100) {
            request.setAttribute("error", "Za długa nazwa albumu! Maksymalna długość to 100 znaków.");
        } else {
            int albumID = db.addAlbum(albumName, user);
            if (albumID == -1) {
                request.setAttribute("error", "Błąd podczas tworzenia albumu!\nFolder o nazwie: "+albumName+" nie został stworzony.");
            } else {
                String path = request.getRealPath("/img/"+albumID);
                if(fu.makeDirectory(path)) {
                    request.setAttribute("album_id", albumID);
                    request.setAttribute("success", "Folder o nazwie: "+albumName+" został stworzony!");
                } else {
                    request.setAttribute("error", "Błąd podczas tworzenia albumu!\nFolder o nazwie: "+albumName+" nie został stworzony.");
                }
            }
        }

        request.getRequestDispatcher("/dodaj-album.jsp").forward(request, response);
    }
}
