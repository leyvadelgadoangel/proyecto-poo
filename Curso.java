package com.mycompany.main_proyecto;

public class Curso {
    //Atributos
    private String codigoCurso;
    private String nombreCurso;
    private Docente docente;
        
    //Constructor
    public Curso(String codigoCurso, String nombreCurso, Docente docente){
        this.codigoCurso = codigoCurso;
        this.nombreCurso = nombreCurso;
        this.docente = docente;
    }
    
    //GETTER y SETTER

    public String getCodigoCurso() {
        return codigoCurso;
    }

    public String getNombreCurso() {
        return nombreCurso;
    }

    public Docente getDocente() {
        return docente;
    }

    public void setCodigoCurso(String codigoCurso) {
        this.codigoCurso = codigoCurso;
    }

    public void setNombreCurso(String nombreCurso) {
        this.nombreCurso = nombreCurso;
    }

    public void setDocente(Docente docente) {
        this.docente = docente;
    }
    
    
    //Registrar Curso: - PENDIENTE
    public void registrarCurso(){
        System.out.println("Se registró el curso...");
    }
}