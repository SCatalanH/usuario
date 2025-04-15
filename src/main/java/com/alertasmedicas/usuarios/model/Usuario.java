package com.alertasmedicas.usuarios.model;

public class Usuario {

    private String idUsuario;
    private String username;
    private String pass;
    private String nombre;
    private String apellido;

    public Usuario() {}

    public Usuario(String idUsuario, String username, String pass, String nombre, String apellido) {
        this.idUsuario = idUsuario;
        this.username = username;
        this.pass = pass;
        this.nombre = nombre;
        this.apellido = apellido;
    }

    public String getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(String idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }
}
