package com.tindersoccer.user_service;

import com.tindersoccer.user_service.model.Usuario;
import com.tindersoccer.user_service.repository.UsuarioRepository;
import com.tindersoccer.user_service.service.UsuarioService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

public class UsuarioServiceTest {

    private UsuarioRepository usuarioRepository;
    private UsuarioService usuarioService;

    @BeforeEach
    public void setUp() {
        usuarioRepository = Mockito.mock(UsuarioRepository.class);
        usuarioService = new UsuarioService(usuarioRepository);
    }

    @Test
    public void testSaveUsuario() {
        Usuario usuario = new Usuario();
        usuario.setNombre("Julio");
        usuario.setCorreo("julio@mail.com");
        usuario.setRol("ADMIN");

        when(usuarioRepository.save(usuario)).thenReturn(usuario);

        Usuario resultado = usuarioService.saveUsuario(usuario);

        assertNotNull(resultado);
        assertEquals("Julio", resultado.getNombre());
        assertEquals("julio@mail.com", resultado.getCorreo());
        assertEquals("ADMIN", resultado.getRol());
        verify(usuarioRepository, times(1)).save(usuario);
    }
}