package com.jsuchinski.galeria.servlet;

import com.jsuchinski.galeria.db.DAO;
import com.jsuchinski.galeria.db.MariaDB_DAOImlp;
import com.jsuchinski.galeria.model.User;

import java.io.*;
import java.sql.SQLException;
import javax.servlet.ServletException;
import javax.servlet.http.*;
import javax.servlet.annotation.*;

@WebServlet(name = "LogServlet", value = "/login")
public class LoginServlet extends HttpServlet {
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
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        String login = request.getParameter("L_login");
        String password = request.getParameter("L_password");

        try {
            User user = db.login(login, password);
            HttpSession session = request.getSession();
            session.setAttribute("user", user);
            response.sendRedirect("/");
        } catch (Exception e) {
            request.setAttribute("L_error", e.getMessage());
            request.getRequestDispatcher("/reg-log.jsp").forward(request, response);
        }
    }
}