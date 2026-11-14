package com.mycompany.main_proyecto.modelo;

import java.io.Serializable;

public abstract class Persona implements Serializable {
    private static final long serialVersionUID = 1L;
    
    protected String dni;
    protected String nombres;
    protected String apellidos;
    protected String correo;

    public Persona(String dni, String nombres, String apellidos, String correo) {
        this.dni = dni;
        this.nombres = nombres;
        this.apellidos = apellidos;
        this.correo = correo;
    }

    public abstract String obtenerDetalleRol();

    public void actualizarDatos(String nombres, String apellidos, String correo) {
        this.nombres = nombres;
        this.apellidos = apellidos;
        this.correo = correo;
    }

    public String getDni() { return dni; }
    public void setDni(String dni) { this.dni = dni; }
    public String getNombres() { return nombres; }
    public void setNombres(String nombres) { this.nombres = nombres; }
    public String getApellidos() { return apellidos; }
    public void setApellidos(String apellidos) { this.apellidos = apellidos; }
    public String getCorreo() { return correo; }
    public void setCorreo(String correo) { this.correo = correo; }

    public String getNombreCompleto() {
        return nombres + " " + apellidos;
    }

    @Override
    public String toString() {
        return String.format("DNI: %s | Nombre: %s | Correo: %s", dni, getNombreCompleto(), correo);
    }
}
