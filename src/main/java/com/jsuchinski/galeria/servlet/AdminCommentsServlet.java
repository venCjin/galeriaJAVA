package com.jsuchinski.galeria.servlet;

import com.jsuchinski.galeria.model.User;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(name = "AdminCommentsServlet", value = "/admin-comments")
public class AdminCommentsServlet extends HttpServlet {
    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        User user = (User) request.getSession().getAttribute("user");
        if(user == null || !(user.isAdmin() || user.isMod()) || !user.isActive()) {
            response.sendRedirect("/");
            return;
        }

        request.getRequestDispatcher("/admin/comments.jsp").forward(request, response);
    }
}
