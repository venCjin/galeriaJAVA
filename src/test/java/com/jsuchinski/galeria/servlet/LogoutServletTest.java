package com.jsuchinski.galeria.servlet;

import com.jsuchinski.galeria.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class LogoutServletTest {

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
    void doGet() throws IOException {
        //when(session.getAttribute("user")).thenReturn(new User(1,"l","p","e",new Date(121211), Role.user,true));

        new LogoutServlet().doGet(request, response);

        verify(request, atLeast(1)).getSession();
        verify(session).invalidate();
        verify(session,never()).setAttribute(eq("user"), any(User.class));

        assertNull(session.getAttribute("user"));
    }
}