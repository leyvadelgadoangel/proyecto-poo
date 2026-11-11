package com.mycompany.main_proyecto;

public class Docente extends Persona {
    private String codigoDocente;
    
    //Constructor
    public Docente (String dni, String nombres, String apellidos, String correo, String codigoDocente){
        super(dni,nombres,apellidos,correo);
        this.codigoDocente = codigoDocente;
    }
    
    //GETTER Y SETTER
    public String getCodigoDocente() {
        return codigoDocente;
    }

    public void setCodigoDocente(String codigoDocente) {
        this.codigoDocente = codigoDocente;
    }
    
    //Método registrarAsistencia - pendiente
    public void registrarAsistencia() {
    System.out.println("El docente " + nombres + " registra asistencia.");
    }
}