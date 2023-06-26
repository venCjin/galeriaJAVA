package com.jsuchinski.galeria.servlet;

import com.jsuchinski.galeria.db.DAO;
import com.jsuchinski.galeria.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class RegiesterServletTest {
    HttpServletRequest request;
    HttpServletResponse response;
    HttpSession session;

    @BeforeEach
    void setUp() {
        request = mock(HttpServletRequest.class);
        response = mock(HttpServletResponse.class);
        session = mock(HttpSession.class);
        when(request.getSession()).thenReturn(session);
    }

    @Test
    void testInvalidData() throws ServletException, IOException {
        String login = "log!n";
        String email = "A@.pl";
        when(request.getParameter("login")).thenReturn(login);
        when(request.getParameter("password")).thenReturn("!@#$");
        when(request.getParameter("password2")).thenReturn("");
        when(request.getParameter("email")).thenReturn(email);

        RegiesterServlet servlet = new RegiesterServlet();
        servlet.db = mock(DAO.class);
        when(servlet.db.isLoginOccupied(login)).thenReturn(true);

        RequestDispatcher rd = mock(RequestDispatcher.class);
        when(request.getRequestDispatcher(eq("/reg-log.jsp?login="+login+"&email="+email))).thenReturn(rd);

        servlet.doPost(request,response);

        //List<String> errors = new ArrayList<String>();
        verify(request, times(1)).setAttribute(eq("R_errors"), anyCollection());
        verify(request, times(1)).getRequestDispatcher(eq("/reg-log.jsp?login="+login+"&email="+email));

        verify(request, never()).getSession();
        verify(session, never()).setAttribute(eq("user"), any(User.class));
        verify(response, never()).sendRedirect(eq("/"));
    }

    @Test
    void testValidData() throws Exception {
        String login = "poprawny1";
        String email = "Aaa@pl.pl";
        String passwd = "passA!1a";
        when(request.getParameter("login")).thenReturn(login);
        when(request.getParameter("password")).thenReturn(passwd);
        when(request.getParameter("password2")).thenReturn(passwd);
        when(request.getParameter("email")).thenReturn(email);

        RegiesterServlet servlet = new RegiesterServlet();
        servlet.db = mock(DAO.class);
        when(servlet.db.isLoginOccupied(login)).thenReturn(false);
        when(servlet.db.createUser(login, passwd, email)).thenReturn(true);
        when(servlet.db.login(login, passwd)).thenReturn(mock(User.class));

        RequestDispatcher rd = mock(RequestDispatcher.class);
        when(request.getRequestDispatcher(eq("/reg-log.jsp?login="+login+"&email="+email))).thenReturn(rd);

        servlet.doPost(request,response);

        //List<String> errors = new ArrayList<String>();

        verify(request, never()).setAttribute(eq("R_errors"), anyCollection());
        verify(request, never()).getRequestDispatcher(eq("/reg-log.jsp?login="+login+"&email="+email));

        verify(request, times(1)).getSession();
        verify(session, times(1)).setAttribute(eq("user"), any(User.class));
        verify(response, times(1)).sendRedirect(eq("/"));
    }

    @Test
    void testCreateUserThrow() throws Exception {
        String login = "poprawny1";
        String email = "Aaa@pl.pl";
        String passwd = "passA!1a";
        when(request.getParameter("login")).thenReturn(login);
        when(request.getParameter("password")).thenReturn(passwd);
        when(request.getParameter("password2")).thenReturn(passwd);
        when(request.getParameter("email")).thenReturn(email);

        RegiesterServlet servlet = new RegiesterServlet();
        servlet.db = mock(DAO.class);
        when(servlet.db.isLoginOccupied(login)).thenReturn(false);
        when(servlet.db.createUser(login, passwd, email)).thenThrow(new Exception());
        when(servlet.db.login(login, passwd)).thenReturn(mock(User.class));

        RequestDispatcher rd = mock(RequestDispatcher.class);
        when(request.getRequestDispatcher(eq("/reg-log.jsp?login="+login+"&email="+email))).thenReturn(rd);

        servlet.doPost(request,response);

        //List<String> errors = new ArrayList<String>();

        verify(request, times(1)).setAttribute(eq("R_errors"), anyCollection());
        verify(request, times(1)).getRequestDispatcher(eq("/reg-log.jsp?login="+login+"&email="+email));

        verify(request, never()).getSession();
        verify(session, never()).setAttribute(eq("user"), any(User.class));
        verify(response, never()).sendRedirect(eq("/"));
    }
}