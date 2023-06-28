package com.jsuchinski.galeria.servlet;

import com.jsuchinski.galeria.db.DAO;
import com.jsuchinski.galeria.model.FotoTileData;
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
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

class FotoServletTest {

    HttpServletRequest request;
    HttpServletResponse response;
    HttpSession session;
    FotoServlet servlet;

    @BeforeEach
    void setUp() {
        request = mock(HttpServletRequest.class);
        response = mock(HttpServletResponse.class);
        session = mock(HttpSession.class);
        when(request.getSession()).thenReturn(session);
        servlet = new FotoServlet();
        servlet.db = mock(DAO.class);
        RequestDispatcher rd = mock(RequestDispatcher.class);
        when(request.getRequestDispatcher(eq("/foto.jsp"))).thenReturn(rd);
    }

    @Test
    void doGet() throws ServletException, IOException {
        int idZ = 1;
        when(request.getParameter("id_zdjecia")).thenReturn(String.valueOf(idZ));
        when(session.getAttribute("user")).thenReturn(mock(User.class));

        when(servlet.db.canRateFoto(any(User.class), eq(idZ))).thenReturn(true);
        when(servlet.db.getFotoRating(eq(idZ))).thenReturn(new Object[]{8.0, 1});
        when(servlet.db.getFotoData(eq(idZ))).thenReturn(mock(FotoTileData.class));

        servlet.doGet(request,response);

        verify(request, times(1)).setAttribute(eq("foto"),any(FotoTileData.class));

        verify(request, times(1)).getRequestDispatcher(eq("/foto.jsp"));
    }

    @Test
    void doGetFotoNotFound() throws ServletException, IOException {
        int idZ = 1;
        when(request.getParameter("id_zdjecia")).thenReturn(String.valueOf(idZ));
        when(session.getAttribute("user")).thenReturn(mock(User.class));

        when(servlet.db.canRateFoto(any(User.class), eq(idZ))).thenReturn(true);
        when(servlet.db.getFotoRating(eq(idZ))).thenReturn(new Object[]{8.0, 1});
        when(servlet.db.getFotoData(eq(idZ))).thenReturn(null);

        servlet.doGet(request,response);

        verify(request, times(1)).setAttribute(eq("msg"),eq("Zdjęcie nie istnieje!"));

        verify(request, times(1)).getRequestDispatcher(eq("/foto.jsp"));
    }

    @Test
    void doGetBadIdZ() throws ServletException, IOException {
        int idZ = -1;
        when(request.getParameter("id_zdjecia")).thenReturn(String.valueOf(idZ));

        servlet.doGet(request,response);

        verify(request, times(1)).setAttribute(eq("msg"), eq("Nie ma takiego zdjęcia!"));

        verify(session, never()).getAttribute(eq("user"));
        verify(servlet.db, never()).canRateFoto(any(User.class), eq(idZ));
        verify(servlet.db, never()).getFotoRating(eq(idZ));
        verify(servlet.db, never()).getFotoData(eq(idZ));

        verify(request, times(1)).getRequestDispatcher(eq("/foto.jsp"));
    }

    @Test
    void doPost() throws ServletException, IOException {
        int idZ = 1;
        int rat = 8;
        when(request.getParameter("ocena")).thenReturn(String.valueOf(rat));
        when(request.getParameter("id_zdjecia")).thenReturn(String.valueOf(idZ));
        when(session.getAttribute("user")).thenReturn(mock(User.class));

        when(servlet.db.canRateFoto(any(User.class), eq(idZ))).thenReturn(false);
        when(servlet.db.getFotoRating(eq(idZ))).thenReturn(new Object[]{8.0, 1});
        when(servlet.db.getFotoData(eq(idZ))).thenReturn(mock(FotoTileData.class));

        servlet.doPost(request,response);

        verify(servlet.db, times(1)).addFotoRating(eq(1),any(User.class),eq(8));

        verify(request, times(1)).getRequestDispatcher(eq("/foto.jsp"));
    }

    @Test
    void doPostBadRating() throws ServletException, IOException {
        int idZ = 1;
        int rat = 12;
        when(request.getParameter("ocena")).thenReturn(String.valueOf(rat));
        when(request.getParameter("id_zdjecia")).thenReturn(String.valueOf(idZ));

        servlet.doPost(request,response);

        verify(request, times(1)).setAttribute(eq("alert"), eq("Ejże! Nie majstruj przy ocenie. Zakres ocen to 1-10."));

        verify(servlet.db, never()).addFotoRating(eq(idZ),any(User.class),eq(rat));

        verify(request, times(1)).getRequestDispatcher(eq("/foto.jsp"));
    }

    @Test
    void doPostBadIdZ() throws ServletException, IOException {
        int idZ = -1;
        int rat = 8;
        when(request.getParameter("ocena")).thenReturn(String.valueOf(rat));
        when(request.getParameter("id_zdjecia")).thenReturn(String.valueOf(idZ));

        servlet.doPost(request,response);

        verify(request, times(1)).setAttribute(eq("msg"), eq("Nie ma takiego zdjęcia!"));

        verify(servlet.db, never()).addFotoRating(eq(idZ),any(User.class),eq(rat));

        verify(request, times(1)).getRequestDispatcher(eq("/foto.jsp"));
    }
}