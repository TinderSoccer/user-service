package com.tindersoccer.user_service.controller;

import com.tindersoccer.user_service.model.Usuario;
import com.tindersoccer.user_service.service.UsuarioService;
import com.tindersoccer.user_service.dto.UsuarioRequestDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.stream.Collectors;

// Importa las clases necesarias para HATEOAS
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/api/usuarios")
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    // Endpoint para listar todos los usuarios, ahora con enlaces HATEOAS
    @GetMapping
    public ResponseEntity<List<Usuario>> getAllUsuarios() {
        List<Usuario> usuarios = usuarioService.findAllUsuarios().stream()
                // A cada usuario en la lista le añade un enlace 'self' para obtener sus detalles
                .map(usuario -> usuario.add(linkTo(methodOn(UsuarioController.class).getUsuarioById(usuario.getId())).withSelfRel()))
                .collect(Collectors.toList());
        return new ResponseEntity<>(usuarios, HttpStatus.OK);
    }

    // Endpoint para obtener un usuario por ID, ahora con múltiples enlaces HATEOAS
    @GetMapping("/{id}")
    public ResponseEntity<Usuario> getUsuarioById(@PathVariable Long id) {
        return usuarioService.findUsuarioById(id)
                .map(usuario -> {
                    // Añade un enlace 'self' al recurso actual
                    usuario.add(linkTo(methodOn(UsuarioController.class).getUsuarioById(id)).withSelfRel());
                    // Añade un enlace 'all-usuarios' para volver a la lista completa
                    usuario.add(linkTo(methodOn(UsuarioController.class).getAllUsuarios()).withRel("all-usuarios"));
                    // Añade un enlace 'delete-usuario' para la operación de eliminación
                    usuario.add(linkTo(methodOn(UsuarioController.class).deleteUsuario(id)).withRel("delete-usuario"));
                    // Añade un enlace 'update-usuario' para la operación de actualización
                    usuario.add(linkTo(methodOn(UsuarioController.class).updateUsuario(id, null)).withRel("update-usuario"));
                    return new ResponseEntity<>(usuario, HttpStatus.OK);
                })
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    // MODIFICADO: Ahora recibe UsuarioRequestDTO en lugar de Usuario
    @PostMapping
    public ResponseEntity<Usuario> createUsuario(@RequestBody UsuarioRequestDTO usuarioDTO) {
        // Mapea el DTO a la entidad Usuario
        Usuario usuario = new Usuario();
        usuario.setNombre(usuarioDTO.getNombre());
        usuario.setCorreo(usuarioDTO.getCorreo());
        usuario.setRol(usuarioDTO.getRol());

        Usuario nuevoUsuario = usuarioService.saveUsuario(usuario);
        // Añade un enlace 'self' al nuevo recurso creado para poder acceder a él
        nuevoUsuario.add(linkTo(methodOn(UsuarioController.class).getUsuarioById(nuevoUsuario.getId())).withSelfRel());
        return new ResponseEntity<>(nuevoUsuario, HttpStatus.CREATED);
    }

    // MODIFICADO: Ahora recibe UsuarioRequestDTO en lugar de Usuario
    @PutMapping("/{id}")
    public ResponseEntity<Usuario> updateUsuario(@PathVariable Long id, @RequestBody UsuarioRequestDTO usuarioDTO) {
        try {
            // Mapea el DTO a una entidad temporal para la actualización
            Usuario usuarioDetails = new Usuario();
            usuarioDetails.setNombre(usuarioDTO.getNombre());
            usuarioDetails.setCorreo(usuarioDTO.getCorreo());
            usuarioDetails.setRol(usuarioDTO.getRol());

            Usuario usuarioActualizado = usuarioService.updateUsuario(id, usuarioDetails);
            // Añade un enlace 'self' al recurso actualizado
            usuarioActualizado.add(linkTo(methodOn(UsuarioController.class).getUsuarioById(usuarioActualizado.getId())).withSelfRel());
            return new ResponseEntity<>(usuarioActualizado, HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    // Endpoint para eliminar un usuario (se mantiene sin cambios en la respuesta)
    @DeleteMapping("/{id}")
    public ResponseEntity<HttpStatus> deleteUsuario(@PathVariable Long id) {
        try {
            usuarioService.deleteUsuario(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // AÑADIDO: Nuevo endpoint para la simulación de comunicación RESTful
    @GetMapping("/{id}/partido-info")
    public ResponseEntity<String> getPartidoInfoForUsuario(@PathVariable Long id) {
        String partidoInfo = usuarioService.getPartidoInfo(id);
        return new ResponseEntity<>(partidoInfo, HttpStatus.OK);
    }
}