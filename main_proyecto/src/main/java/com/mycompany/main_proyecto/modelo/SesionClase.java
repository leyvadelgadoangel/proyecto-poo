package com.mycompany.main_proyecto.modelo;

import java.io.Serializable;
import java.time.LocalDate;

public class SesionClase implements Serializable {
    private static final long serialVersionUID = 1L;
    private String idSesion;
    private Curso curso;
    private LocalDate fecha;

    public SesionClase(String idSesion, Curso curso, LocalDate fecha) {
        this.idSesion = idSesion;
        this.curso = curso;
        this.fecha = fecha;
    }

    public String getIdSesion() { return idSesion; }
    public Curso getCurso() { return curso; }
    public LocalDate getFecha() { return fecha; }

    @Override
    public String toString() {
        return String.format("Sesión [%s] | Curso: %s | Fecha: %s",
                idSesion, curso.getNombreCurso(), fecha);
    }
}
