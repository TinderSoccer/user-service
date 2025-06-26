package com.tindersoccer.user_service.service;

import com.tindersoccer.user_service.model.Usuario;
import com.tindersoccer.user_service.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Optional;

@Service
public class UsuarioService {

    private final UsuarioRepository usuarioRepository; // Modificado a `final` para constructor injection
    private final RestTemplate restTemplate;

    // CONSTRUCTOR CORRECTO para la inyección y la prueba
    // ESTE CONSTRUCTOR REEMPLAZA CUALQUIER OTRO CONSTRUCTOR EN LA CLASE
    @Autowired
    public UsuarioService(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
        this.restTemplate = new RestTemplate();
    }

    public List<Usuario> findAllUsuarios() {
        return usuarioRepository.findAll();
    }

    public Optional<Usuario> findUsuarioById(Long id) {
        return usuarioRepository.findById(id);
    }

    public Usuario saveUsuario(Usuario usuario) {
        return usuarioRepository.save(usuario);
    }

    public Usuario updateUsuario(Long id, Usuario usuarioDetails) {
        Usuario usuario = usuarioRepository.findById(id)
                                .orElseThrow(() -> new RuntimeException("Usuario no encontrado con ID: " + id));
        usuario.setNombre(usuarioDetails.getNombre());
        usuario.setCorreo(usuarioDetails.getCorreo());
        usuario.setRol(usuarioDetails.getRol());
        return usuarioRepository.save(usuario);
    }

    public void deleteUsuario(Long id) {
        usuarioRepository.deleteById(id);
    }

    public String getPartidoInfo(Long usuarioId) {
        String url = "http://localhost:8081/api/partidos/usuario/" + usuarioId;
        try {
            ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
            return "Respuesta del microservicio de partidos (simulada): " + response.getBody();
        } catch (Exception e) {
            return "Error al conectar con el microservicio de partidos (asegúrate de que esté corriendo en el puerto 8081). Error: " + e.getMessage();
        }
    }
}