package com.tindersoccer.user_service.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import org.springframework.hateoas.RepresentationModel; // AÑADIR ESTA LÍNEA



@Entity
@Table(name = "usuarios")
// AÑADIR LA CLÁUSULA EXTENDS
public class Usuario extends RepresentationModel<Usuario> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nombre;
    private String correo;
    private String rol;

    // AÑADIR CONSTRUCTOR CON PARÁMETROS PARA HACERLO MÁS FÁCIL
    public Usuario(String nombre, String correo, String rol) {
        this.nombre = nombre;
        this.correo = correo;
        this.rol = rol;
    }

    public Usuario() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getRol() {
        return rol;
    }

    public void setRol(String rol) {
        this.rol = rol;
    }
}