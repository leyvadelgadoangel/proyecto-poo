package com.mycompany.main_proyecto.modelo;

import java.io.Serializable;
import java.time.LocalDate;

public class Matricula implements Serializable {
    private static final long serialVersionUID = 1L;
    private Estudiante estudiante;
    private Curso curso;
    private LocalDate fechaMatricula;

    public Matricula(Estudiante estudiante, Curso curso, LocalDate fechaMatricula) {
        this.estudiante = estudiante;
        this.curso = curso;
        this.fechaMatricula = fechaMatricula;
    }

    public Estudiante getEstudiante() { return estudiante; }
    public Curso getCurso() { return curso; }
    public LocalDate getFechaMatricula() { return fechaMatricula; }

    @Override
    public String toString() {
        return String.format("Curso: %s (%s) | Estudiante: %s (%s) | Fecha: %s",
                curso.getNombreCurso(), curso.getCodigoCurso(),
                estudiante.getNombreCompleto(), estudiante.getCodigoUniversitario(),
                fechaMatricula);
    }
}
