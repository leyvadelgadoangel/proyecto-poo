package com.mycompany.main_proyecto.modelo;

public class Estudiante extends Persona {
    private static final long serialVersionUID = 1L;
    private String codigoUniversitario;

    public Estudiante(String codigoUniversitario, String dni, String nombres, String apellidos, String correo) {
        super(dni, nombres, apellidos, correo);
        this.codigoUniversitario = codigoUniversitario;
    }

    @Override
    public String obtenerDetalleRol() {
        return "ESTUDIANTE [Código: " + codigoUniversitario + "]";
    }

    public String getCodigoUniversitario() { return codigoUniversitario; }
    public void setCodigoUniversitario(String codigoUniversitario) { this.codigoUniversitario = codigoUniversitario; }

    @Override
    public String toString() {
        return obtenerDetalleRol() + " - " + super.toString();
    }
}