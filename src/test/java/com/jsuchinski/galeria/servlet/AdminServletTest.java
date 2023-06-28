package com.jsuchinski.galeria.servlet;

import com.jsuchinski.galeria.model.Role;
import com.jsuchinski.galeria.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import java.io.IOException;
import java.sql.Date;
import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AdminServletTest {

    HttpServletRequest request;
    HttpServletResponse response;
    HttpSession session;
    AdminServlet servlet;

    @BeforeEach
    void setUp() {
        request = mock(HttpServletRequest.class);
        response = mock(HttpServletResponse.class);
        session = mock(HttpSession.class);
        when(request.getSession()).thenReturn(session);
        servlet = new AdminServlet();
    }

    @Test
    void doGetAdminActive() throws ServletException, IOException {
        User u = new User(1,"login","pass","email@aa.pl", Date.valueOf(LocalDate.now()), Role.admin,true);
        when(session.getAttribute("user")).thenReturn(u);

        RequestDispatcher rd = mock(RequestDispatcher.class);
        when(request.getRequestDispatcher(eq("/admin/index.jsp"))).thenReturn(rd);

        servlet.doGet(request,response);

        verify(request,times(1)).getRequestDispatcher(eq("/admin/index.jsp"));
        verify(response,never()).sendRedirect(eq("/"));
    }

    @Test
    void doGetModActive() throws ServletException, IOException {
        User u = new User(1,"login","pass","email@aa.pl", Date.valueOf(LocalDate.now()), Role.mod,true);
        when(session.getAttribute("user")).thenReturn(u);

        RequestDispatcher rd = mock(RequestDispatcher.class);
        when(request.getRequestDispatcher(eq("/admin/index.jsp"))).thenReturn(rd);

        servlet.doGet(request,response);

        verify(request,times(1)).getRequestDispatcher(eq("/admin/index.jsp"));
        verify(response,never()).sendRedirect(eq("/"));
    }

    @Test
    void doGetAdminNotActive() throws ServletException, IOException {
        User u = new User(1,"login","pass","email@aa.pl", Date.valueOf(LocalDate.now()), Role.admin,false);
        when(session.getAttribute("user")).thenReturn(u);

        servlet.doGet(request,response);

        verify(response,times(1)).sendRedirect(eq("/"));
        verify(request,never()).getRequestDispatcher(eq("/admin/index.jsp"));
    }

    @Test
    void doGetModNotActive() throws ServletException, IOException {
        User u = new User(1,"login","pass","email@aa.pl", Date.valueOf(LocalDate.now()), Role.mod,false);
        when(session.getAttribute("user")).thenReturn(u);

        servlet.doGet(request,response);

        verify(response,times(1)).sendRedirect(eq("/"));
        verify(request,never()).getRequestDispatcher(eq("/admin/index.jsp"));
    }

    @Test
    void doGetUserActive() throws ServletException, IOException {
        User u = new User(1,"login","pass","email@aa.pl", Date.valueOf(LocalDate.now()), Role.user,true);
        when(session.getAttribute("user")).thenReturn(u);

        servlet.doGet(request,response);

        verify(response,times(1)).sendRedirect(eq("/"));
        verify(request,never()).getRequestDispatcher(eq("/admin/index.jsp"));
    }

    @Test
    void doGetUserNotActive() throws ServletException, IOException {
        User u = new User(1,"login","pass","email@aa.pl", Date.valueOf(LocalDate.now()), Role.user,false);
        when(session.getAttribute("user")).thenReturn(u);

        servlet.doGet(request,response);

        verify(response,times(1)).sendRedirect(eq("/"));
        verify(request,never()).getRequestDispatcher(eq("/admin/index.jsp"));
    }
}