package com.mycompany.main_proyecto.modelo;

public class Docente extends Persona {
    private static final long serialVersionUID = 1L;
    private String codigoDocente;

    public Docente(String codigoDocente, String dni, String nombres, String apellidos, String correo) {
        super(dni, nombres, apellidos, correo);
        this.codigoDocente = codigoDocente;
    }

    @Override
    public String obtenerDetalleRol() {
        return "DOCENTE [Código: " + codigoDocente + "]";
    }

    public String getCodigoDocente() { return codigoDocente; }
    public void setCodigoDocente(String codigoDocente) { this.codigoDocente = codigoDocente; }

    @Override
    public String toString() {
        return obtenerDetalleRol() + " - " + super.toString();
    }
}