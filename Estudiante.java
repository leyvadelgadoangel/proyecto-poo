package com.mycompany.main_proyecto;

public class Estudiante extends Persona{
    private String codigoUniversitario;
    
    public Estudiante (String dni, String nombres, String apellidos, String correo, String codigoUniversitario){
        super(dni, nombres, apellidos, correo);
        this.codigoUniversitario = codigoUniversitario;
    }
    
    //GETTER Y SETTER
    public String getCodigoUniversitario() {
        return codigoUniversitario;
    }

    public void setCodigoUniversitario(String codigoUniversitario) {
        this.codigoUniversitario = codigoUniversitario;
    }
    
    //Métodos - pendiente
    public void registrar() {
    System.out.println("Estudiante registrado: " + nombres);
    }
    public void consultar() {
    System.out.println("Consultando datos del estudiante...");
    }
    
}
