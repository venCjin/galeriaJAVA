package com.jsuchinski.galeria.servlet;

import com.google.common.primitives.Ints;
import com.jsuchinski.galeria.db.DAO;
import com.jsuchinski.galeria.db.MariaDB_DAOImlp;
import com.jsuchinski.galeria.model.FotoTileData;
import com.jsuchinski.galeria.model.User;
import com.jsuchinski.galeria.utils.FileUtils;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@WebServlet(name = "AdminUsersServlet", value = "/admin-foto")
public class AdminFotoServlet extends HttpServlet {
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
        if(user == null || !(user.isAdmin() || user.isMod()) || !user.isActive()) {
            response.sendRedirect("/");
            return;
        }

        String zType = request.getParameter("z_type");
        if(zType!=null) {
            String sqlParam = " ";
//            if (zType.equals("all")) {
//            } else
            if (zType.equals("to_acpt")) {
                sqlParam = " WHERE zaakceptowane=0 ";
            }
            List<FotoTileData> items = db.adminGetFotosWithParam(sqlParam);
            request.setAttribute("items", items);
            request.setAttribute("z_type", zType);
        }

        request.getRequestDispatcher("/admin/foto.jsp").forward(request, response);
    }

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        User user = (User) request.getSession().getAttribute("user");
        if(user == null || !(user.isAdmin() || user.isMod()) || !user.isActive()) {
            response.sendRedirect("/");
            return;
        }

        int idZdjecia = Optional.ofNullable(request.getParameter("id_zdjecia")).map(Ints::tryParse).orElse(-1);
        if(idZdjecia == -1) {
            request.setAttribute("error", "Nie wybrano zdjęcia lub zły identyfikator(id) zdjęcia!");
            request.getRequestDispatcher("/admin/foto.jsp").forward(request, response);
            return;
        }
//        String a = request.getParameter("akcpt_z");
//        String b = request.getParameter("usn_z");
        boolean accept = Optional.ofNullable(request.getParameter("akcpt_z")).isPresent();
        boolean delete = Optional.ofNullable(request.getParameter("usn_z")).map(s -> true).orElse(false);

        if(accept) {
            try {
                if (db.acceptFoto(idZdjecia)) {
                    request.setAttribute("success", "Zdjęcia zostało zaakceptowane");
                } else {
                    request.setAttribute("error", "Zaakceptowanie zdjęcia nie powiodła się. Spróbuj ponownie.");
                }
            } catch (Exception e) {
                request.setAttribute("error", e.getMessage());
            }
        } else if(delete) {
            int idAlbumu = Optional.ofNullable(request.getParameter("id_albumu")).map(Ints::tryParse).orElse(-1);
            if(idAlbumu == -1) {
                request.setAttribute("error", "Usunięcie zdjęcia nie powiodło się. Spróbuj ponownie.");
                request.getRequestDispatcher("/admin/foto.jsp").forward(request, response);
                return;
            }
            try {
                if (db.deleteFoto(idZdjecia)) {
                    request.setAttribute("success", "Zdjęcie zostało usunięte");
                } else {
                    request.setAttribute("error", "Usunięcie zdjęcia nie powiodło się. Spróbuj ponownie.");
                }
                fu.deleteFoto(request.getRealPath("/img/"+idAlbumu+"/"+idZdjecia));
            } catch (Exception e) {
                request.setAttribute("error", e.getMessage());
            }
        }

        String zType = request.getParameter("z_type");

        doGet(request,response);
    }
}
