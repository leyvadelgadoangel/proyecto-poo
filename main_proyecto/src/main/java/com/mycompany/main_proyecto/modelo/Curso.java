package com.mycompany.main_proyecto.modelo;

import java.io.Serializable;

public class Curso implements Serializable {
    private static final long serialVersionUID = 1L;
    private String codigoCurso;
    private String nombreCurso;
    private Docente docente;

    public Curso(String codigoCurso, String nombreCurso, Docente docente) {
        this.codigoCurso = codigoCurso;
        this.nombreCurso = nombreCurso;
        this.docente = docente;
    }

    public String getCodigoCurso() { return codigoCurso; }
    public void setCodigoCurso(String codigoCurso) { this.codigoCurso = codigoCurso; }
    public String getNombreCurso() { return nombreCurso; }
    public void setNombreCurso(String nombreCurso) { this.nombreCurso = nombreCurso; }
    public Docente getDocente() { return docente; }
    public void setDocente(Docente docente) { this.docente = docente; }

    @Override
    public String toString() {
        String docStr = (docente != null) ? docente.getNombreCompleto() : "Sin asignar";
        return String.format("[%s] %s | Docente: %s", codigoCurso, nombreCurso, docStr);
    }
}
