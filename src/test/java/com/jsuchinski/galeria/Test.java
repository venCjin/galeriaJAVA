package com.jsuchinski.galeria;

import com.jsuchinski.galeria.db.DAO;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.assertEquals;

class Test {
    @Mock
    private DAO db;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @org.junit.jupiter.api.Test
    void testGetContent() throws Exception {
//        Mockito.when(db.doSth()).thenReturn(false);
//        assertEquals(db.doSth(), false);
    }
}