package com.jsuchinski.galeria.servlet;

import com.jsuchinski.galeria.db.DAO;
import com.jsuchinski.galeria.model.FotoTileData;
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
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

class AlbumServletTest {

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
    void doGet() throws Exception {
        int idAlbumu = 1;
        int page = 1;
        when(request.getParameter("id_albumu")).thenReturn(String.valueOf(idAlbumu));
        when(request.getParameter("page")).thenReturn(String.valueOf(page));
        List<FotoTileData> items = new ArrayList<>();
        items.add(
            new FotoTileData(idAlbumu, 1, "desc","author", "date")
        );

        AlbumServlet servlet = new AlbumServlet();
        servlet.db = mock(DAO.class);
        when(servlet.db.getFotoCountForAlbum(idAlbumu)).thenReturn(30);
        when(servlet.db.getFotosDataForAlbum(eq(idAlbumu),eq(0),eq(20))).thenReturn(items);

        RequestDispatcher rd = mock(RequestDispatcher.class);
        when(request.getRequestDispatcher(eq("/album.jsp"))).thenReturn(rd);

        servlet.doGet(request,response);

        verify(request, times(1)).setAttribute(eq("page"), eq(page));
        verify(request, times(1)).setAttribute(eq("page_items"), any());
        verify(request, times(1)).setAttribute(eq("items"), eq(items));

        verify(request, times(1)).getRequestDispatcher(eq("/album.jsp"));
    }

    @Test
    void invalidIdAlbum() throws Exception {
        int idAlbumu = -1;
        when(request.getParameter("id_albumu")).thenReturn(String.valueOf(idAlbumu));
        when(request.getParameter("page")).thenReturn("1");
        //List<FotoTileData> items = new ArrayList<>();

        AlbumServlet servlet = new AlbumServlet();
        servlet.db = mock(DAO.class);
        //when(servlet.db.getFotoCountForAlbum(idAlbumu)).thenReturn(30);
        //when(servlet.db.getFotosDataForAlbum(eq(idAlbumu),eq(0),anyInt())).thenReturn(items);

        RequestDispatcher rd = mock(RequestDispatcher.class);
        when(request.getRequestDispatcher(eq("/album.jsp"))).thenReturn(rd);

        servlet.doGet(request,response);

        verify(request, times(1)).setAttribute(eq("msg"), eq("W albumie nie ma żadnych zdjęć!"));
        verify(request, times(1)).getRequestDispatcher(eq("/album.jsp"));
    }

    @Test
    void photoCountZero() throws Exception {
        int idAlbumu = 1;
        when(request.getParameter("id_albumu")).thenReturn(String.valueOf(idAlbumu));
        when(request.getParameter("page")).thenReturn("1");
        //List<FotoTileData> items = new ArrayList<>();

        AlbumServlet servlet = new AlbumServlet();
        servlet.db = mock(DAO.class);
        when(servlet.db.getFotoCountForAlbum(idAlbumu)).thenReturn(0);
        //when(servlet.db.getFotosDataForAlbum(eq(idAlbumu),eq(0),anyInt())).thenReturn(items);

        RequestDispatcher rd = mock(RequestDispatcher.class);
        when(request.getRequestDispatcher(eq("/album.jsp"))).thenReturn(rd);

        servlet.doGet(request,response);

        verify(request, times(1)).setAttribute(eq("msg"), eq("W albumie nie ma żadnych zdjęć!"));
        verify(request, times(1)).getRequestDispatcher(eq("/album.jsp"));
    }

    @Test
    void photoItemListIsEmpty() throws Exception {
        int idAlbumu = 1;
        when(request.getParameter("id_albumu")).thenReturn(String.valueOf(idAlbumu));
        when(request.getParameter("page")).thenReturn("1");
        List<FotoTileData> items = new ArrayList<>();

        AlbumServlet servlet = new AlbumServlet();
        servlet.db = mock(DAO.class);
        when(servlet.db.getFotoCountForAlbum(idAlbumu)).thenReturn(30);
        when(servlet.db.getFotosDataForAlbum(eq(idAlbumu),eq(0),anyInt())).thenReturn(items);

        RequestDispatcher rd = mock(RequestDispatcher.class);
        when(request.getRequestDispatcher(eq("/album.jsp"))).thenReturn(rd);

        servlet.doGet(request,response);

        verify(request, times(1)).setAttribute(eq("msg"), eq("W albumie nie ma żadnych zdjęć!"));
        verify(request, times(1)).getRequestDispatcher(eq("/album.jsp"));
    }

    @Test
    void photoItemListThrowException() throws Exception {
        int idAlbumu = 1;
        when(request.getParameter("id_albumu")).thenReturn(String.valueOf(idAlbumu));
        when(request.getParameter("page")).thenReturn("1");
        List<FotoTileData> items = new ArrayList<>();
        Exception e = new Exception("error msg");

        AlbumServlet servlet = new AlbumServlet();
        servlet.db = mock(DAO.class);
        when(servlet.db.getFotoCountForAlbum(idAlbumu)).thenReturn(30);
        when(servlet.db.getFotosDataForAlbum(eq(idAlbumu),eq(0),anyInt())).thenThrow(e);

        RequestDispatcher rd = mock(RequestDispatcher.class);
        when(request.getRequestDispatcher(eq("/album.jsp"))).thenReturn(rd);

        servlet.doGet(request,response);

        verify(request, times(1)).setAttribute(eq("msg"), eq(e.getMessage()));
        verify(request, times(1)).getRequestDispatcher(eq("/album.jsp"));
    }
}