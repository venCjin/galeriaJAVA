package com.jsuchinski.galeria.servlet;

import com.jsuchinski.galeria.MySqlDB;
import com.jsuchinski.galeria.model.User;

import java.io.*;
import java.sql.*;
import javax.servlet.ServletException;
import javax.servlet.http.*;
import javax.servlet.annotation.*;

@WebServlet(name = "LogServlet", value = "/login")
public class LoginServlet extends HttpServlet {
    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        String login = request.getParameter("L_login");
        String password = request.getParameter("L_password");

        try {
            User user = MySqlDB.login(login, password);
            HttpSession session = request.getSession();
            session.setAttribute("user", user);
            response.sendRedirect("/");
        } catch (Exception e) {
            request.setAttribute("L_error", e.getMessage());
            request.getRequestDispatcher("/reg-log.jsp").forward(request, response);
        }
    }
}