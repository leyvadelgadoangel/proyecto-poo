package com.mycompany.main_proyecto.modelo;

import java.io.Serializable;

public class Asistencia implements Serializable {
    private static final long serialVersionUID = 1L;
    private String idAsistencia;
    private SesionClase sesion;
    private Estudiante estudiante;
    private EstadoAsistencia estado;

    public Asistencia(String idAsistencia, SesionClase sesion, Estudiante estudiante, EstadoAsistencia estado) {
        this.idAsistencia = idAsistencia;
        this.sesion = sesion;
        this.estudiante = estudiante;
        this.estado = estado;
    }

    public String getIdAsistencia() { return idAsistencia; }
    public SesionClase getSesion() { return sesion; }
    public Estudiante getEstudiante() { return estudiante; }
    public EstadoAsistencia getEstado() { return estado; }
    public void setEstado(EstadoAsistencia estado) { this.estado = estado; }

    @Override
    public String toString() {
        return String.format("ID: %s | Sesión: %s (%s) | Alumno: %s [%s] | Estado: %s",
                idAsistencia, sesion.getIdSesion(), sesion.getFecha(),
                estudiante.getNombreCompleto(), estudiante.getCodigoUniversitario(), estado);
    }
}