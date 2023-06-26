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

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

class LoginServletTest {

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
    void testInvalidData() throws Exception {
        LoginServlet servlet = new LoginServlet();
        servlet.db = mock(DAO.class);

        String login = "poprawny1";
        String password = "Bledne1haslo";
        when(request.getParameter("L_login")).thenReturn(login);
        when(request.getParameter("L_password")).thenReturn(password);

        when(servlet.db.login(login, password)).thenThrow(new Exception("Niepoprawne dane logowania"));

        RequestDispatcher rd = mock(RequestDispatcher.class);
        when(request.getRequestDispatcher(eq("/reg-log.jsp"))).thenReturn(rd);

        servlet.doPost(request,response);

        verify(request, times(1)).setAttribute(eq("L_error"), any(String.class));
        verify(request, times(1)).getRequestDispatcher(eq("/reg-log.jsp"));

        verify(request, never()).getSession();
        verify(session, never()).setAttribute(eq("user"), any(User.class));
        verify(response, never()).sendRedirect(eq("/"));
    }

    @Test
    void testValidData() throws Exception {
        LoginServlet servlet = new LoginServlet();
        servlet.db = mock(DAO.class);

        String login = "poprawny1";
        String password = "Mocne1haslo";
        when(request.getParameter("L_login")).thenReturn(login);
        when(request.getParameter("L_password")).thenReturn(password);

        when(servlet.db.login(login, password)).thenReturn(mock(User.class));

        RequestDispatcher rd = mock(RequestDispatcher.class);
        when(request.getRequestDispatcher(eq("/reg-log.jsp"))).thenReturn(rd);

        servlet.doPost(request,response);

        verify(request, never()).setAttribute(eq("L_error"), anyCollection());
        verify(request, never()).getRequestDispatcher(eq("/reg-log.jsp"));

        verify(request, times(1)).getSession();
        verify(session, times(1)).setAttribute(eq("user"), any(User.class));
        verify(response, times(1)).sendRedirect(eq("/"));
    }
}