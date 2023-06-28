package com.jsuchinski.galeria.servlet;

import com.jsuchinski.galeria.db.DAO;
import com.jsuchinski.galeria.model.Role;
import com.jsuchinski.galeria.model.User;
import com.jsuchinski.galeria.utils.FileUtils;
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

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;

class AddAlbumServletTest {

    HttpServletRequest request;
    HttpServletResponse response;
    HttpSession session;
    AddAlbumServlet servlet;

    @BeforeEach
    void setUp() {
        request = mock(HttpServletRequest.class);
        response = mock(HttpServletResponse.class);
        session = mock(HttpSession.class);
        when(request.getSession()).thenReturn(session);
        servlet = new AddAlbumServlet();
        servlet.db = mock(DAO.class);
        servlet.fu = mock(FileUtils.class);
        RequestDispatcher rd = mock(RequestDispatcher.class);
        when(request.getRequestDispatcher(eq("/dodaj-album.jsp"))).thenReturn(rd);
    }

    @Test
    void doGet() throws ServletException, IOException {
        User u = new User(1,"login","pass","email@aa.pl", Date.valueOf(LocalDate.now()), Role.user,true);
        when(session.getAttribute("user")).thenReturn(u);

        servlet.doGet(request,response);

        verify(request,times(1)).getRequestDispatcher(eq("/dodaj-album.jsp"));
        verify(response,never()).sendRedirect(eq("/reg-log.jsp?log_err"));
    }

    @Test
    void doGetBad() throws ServletException, IOException {
        User u = new User(1,"login","pass","email@aa.pl", Date.valueOf(LocalDate.now()), Role.user,false);
        when(session.getAttribute("user")).thenReturn(u);

        servlet.doGet(request,response);

        verify(request,never()).getRequestDispatcher(eq("/dodaj-album.jsp"));
        verify(response,times(1)).sendRedirect(eq("/reg-log.jsp?log_err"));
    }

    @Test
    void doPostNotActiveUser() throws ServletException, IOException {
        User u = new User(1,"login","pass","email@aa.pl", Date.valueOf(LocalDate.now()), Role.user,false);
        when(session.getAttribute("user")).thenReturn(u);

        servlet.doPost(request,response);

        verify(request,never()).getRequestDispatcher(eq("/dodaj-album.jsp"));
        verify(response,times(1)).sendRedirect(eq("/reg-log.jsp?log_err"));
    }

    @Test
    void doPostBlankAlbumName() throws ServletException, IOException {
        User u = new User(1,"login","pass","email@aa.pl", Date.valueOf(LocalDate.now()), Role.user,true);
        when(session.getAttribute("user")).thenReturn(u);

        when(request.getParameter("nazwa_albumu")).thenReturn(" ");

        servlet.doPost(request,response);

        verify(request,times(1)).setAttribute(eq("error"), eq("Nie podano nazwy albumu."));

        verify(request,times(1)).getRequestDispatcher(eq("/dodaj-album.jsp"));
        verify(response,never()).sendRedirect(eq("/reg-log.jsp?log_err"));
    }

    @Test
    void doPostToLongAlbumName() throws ServletException, IOException {
        User u = new User(1,"login","pass","email@aa.pl", Date.valueOf(LocalDate.now()), Role.user,true);
        when(session.getAttribute("user")).thenReturn(u);

        when(request.getParameter("nazwa_albumu")).thenReturn("a".repeat(101));

        servlet.doPost(request,response);

        verify(request,times(1)).setAttribute(eq("error"), eq("Za długa nazwa albumu! Maksymalna długość to 100 znaków."));

        verify(request,times(1)).getRequestDispatcher(eq("/dodaj-album.jsp"));
        verify(response,never()).sendRedirect(eq("/reg-log.jsp?log_err"));
    }

    @Test
    void doPostBadAlbumId() throws ServletException, IOException {
        User u = new User(1,"login","pass","email@aa.pl", Date.valueOf(LocalDate.now()), Role.user,true);
        when(session.getAttribute("user")).thenReturn(u);

        when(request.getParameter("nazwa_albumu")).thenReturn("nazwa");
        when(servlet.db.addAlbum(eq("nazwa"), eq(u))).thenReturn(-1);

        servlet.doPost(request,response);

        verify(request,times(1)).setAttribute(eq("error"), eq("Błąd podczas tworzenia albumu!\nFolder o nazwie: "+"nazwa"+" nie został stworzony."));

        verify(request,times(1)).getRequestDispatcher(eq("/dodaj-album.jsp"));
        verify(response,never()).sendRedirect(eq("/reg-log.jsp?log_err"));
    }

    @Test
    void doPostMakeDirFail() throws ServletException, IOException {
        int albumID = 41;
        String albumName = "nazwa";
        User u = new User(1,"login","pass","email@aa.pl", Date.valueOf(LocalDate.now()), Role.user,true);
        when(session.getAttribute("user")).thenReturn(u);

        when(request.getParameter("nazwa_albumu")).thenReturn(albumName);
        when(servlet.db.addAlbum(eq("nazwa"), eq(u))).thenReturn(albumID);
        when(request.getRealPath(eq("/img/"+albumID))).thenReturn("/path/to/img/"+albumID);
        when(servlet.fu.makeDirectory(eq("/path/to/img/"+albumID))).thenReturn(false);

        servlet.doPost(request,response);

        verify(request,times(1)).setAttribute(eq("error"), eq("Błąd podczas tworzenia albumu!\nFolder o nazwie: "+albumName+" nie został stworzony."));

        verify(request,times(1)).getRequestDispatcher(eq("/dodaj-album.jsp"));
        verify(response,never()).sendRedirect(eq("/reg-log.jsp?log_err"));
    }

    @Test
    void doPost() throws ServletException, IOException {
        int albumID = 41;
        String albumName = "nazwa";
        User u = new User(1,"login","pass","email@aa.pl", Date.valueOf(LocalDate.now()), Role.user,true);
        when(session.getAttribute("user")).thenReturn(u);

        when(request.getParameter("nazwa_albumu")).thenReturn(albumName);
        when(servlet.db.addAlbum(eq("nazwa"), eq(u))).thenReturn(albumID);
        when(request.getRealPath(eq("/img/"+albumID))).thenReturn("/path/to/img/"+albumID);
        when(servlet.fu.makeDirectory(eq("/path/to/img/"+albumID))).thenReturn(true);

        servlet.doPost(request,response);

        verify(request,times(1)).setAttribute(eq("album_id"), eq(albumID));
        verify(request,times(1)).setAttribute(eq("success"), eq("Folder o nazwie: "+albumName+" został stworzony!"));

        verify(request,times(1)).getRequestDispatcher(eq("/dodaj-album.jsp"));
        verify(response,never()).sendRedirect(eq("/reg-log.jsp?log_err"));
    }
}