package com.jsuchinski.galeria.servlet;

import com.jsuchinski.galeria.db.DAO;
import com.jsuchinski.galeria.model.GalleryTileData;
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
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

class GaleriaServletTest {

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
    void testZeroAlbums() throws ServletException, IOException {
        String page = null;
        String sort = null;
        when(request.getParameter("page")).thenReturn(page);
        when(request.getParameter("sort")).thenReturn(sort);

        GaleriaServlet servlet = new GaleriaServlet();
        servlet.db = mock(DAO.class);
        when(servlet.db.getAlbumCountForGallery()).thenReturn(0);

        RequestDispatcher rd = mock(RequestDispatcher.class);
        when(request.getRequestDispatcher(eq("/galeria.jsp"))).thenReturn(rd);

        servlet.doGet(request,response);
        verify(request,times(1)).setAttribute(eq("msg"), anyString());
    }

    @Test
    void test1() throws Exception {
        String page = null;
        String sort = "sort-t";
        when(request.getParameter("page")).thenReturn(page);
        when(request.getParameter("sort")).thenReturn(sort);

        List<GalleryTileData> fakeList = new ArrayList<>();
        fakeList.add(mock(GalleryTileData.class));

        GaleriaServlet servlet = new GaleriaServlet();
        servlet.db = mock(DAO.class);
        when(servlet.db.getAlbumCountForGallery()).thenReturn(1);
        when(servlet.db.getAlbumsDataForGallery("a.tytul",0,20)).thenReturn(fakeList);

        RequestDispatcher rd = mock(RequestDispatcher.class);
        when(request.getRequestDispatcher(eq("/galeria.jsp"))).thenReturn(rd);

        servlet.doGet(request,response);
        verify(request,never()).setAttribute(eq("msg"), anyString());
        verify(request,never()).setAttribute(eq("error"), any());
    }

    @Test
    void test2() throws Exception {
        String page = null;
        String sort = "sort-n";
        when(request.getParameter("page")).thenReturn(page);
        when(request.getParameter("sort")).thenReturn(sort);

        List<GalleryTileData> fakeList = new ArrayList<>();
        for (int i = 0; i < 45; i++) {
            fakeList.add(mock(GalleryTileData.class));
        }

        GaleriaServlet servlet = new GaleriaServlet();
        servlet.db = mock(DAO.class);
        when(servlet.db.getAlbumCountForGallery()).thenReturn(3);
        when(servlet.db.getAlbumsDataForGallery("u.login",0,20)).thenReturn(fakeList);

        RequestDispatcher rd = mock(RequestDispatcher.class);
        when(request.getRequestDispatcher(eq("/galeria.jsp"))).thenReturn(rd);

        servlet.doGet(request,response);
        verify(request,never()).setAttribute(eq("msg"), anyString());
        verify(request,never()).setAttribute(eq("error"), any());
    }

    @Test
    void test3() throws Exception {
        String page = null;
        String sort = "sort-d";
        when(request.getParameter("page")).thenReturn(page);
        when(request.getParameter("sort")).thenReturn(sort);

        List<GalleryTileData> fakeList = new ArrayList<>();
        for (int i = 0; i < 45; i++) {
            fakeList.add(mock(GalleryTileData.class));
        }

        GaleriaServlet servlet = new GaleriaServlet();
        servlet.db = mock(DAO.class);
        when(servlet.db.getAlbumCountForGallery()).thenReturn(3);
        when(servlet.db.getAlbumsDataForGallery("a.data",0,20)).thenReturn(fakeList);

        RequestDispatcher rd = mock(RequestDispatcher.class);
        when(request.getRequestDispatcher(eq("/galeria.jsp"))).thenReturn(rd);

        servlet.doGet(request,response);
        verify(request,never()).setAttribute(eq("msg"), anyString());
        verify(request,never()).setAttribute(eq("error"), any());
    }

    @Test
    void testException() throws Exception {
        String page = null;
        String sort = "sort-d";
        when(request.getParameter("page")).thenReturn(page);
        when(request.getParameter("sort")).thenReturn(sort);

        List<GalleryTileData> fakeList = new ArrayList<>();
        for (int i = 0; i < 45; i++) {
            fakeList.add(mock(GalleryTileData.class));
        }

        GaleriaServlet servlet = new GaleriaServlet();
        servlet.db = mock(DAO.class);
        when(servlet.db.getAlbumCountForGallery()).thenReturn(3);
        when(servlet.db.getAlbumsDataForGallery("a.data",0,20)).thenThrow(new Exception());

        RequestDispatcher rd = mock(RequestDispatcher.class);
        when(request.getRequestDispatcher(eq("/galeria.jsp"))).thenReturn(rd);

        assertThrows(ServletException.class,() -> servlet.doGet(request,response));

        verify(request,never()).setAttribute(eq("msg"), anyString());
        verify(request,times(1)).setAttribute(eq("error"), any());
    }
}