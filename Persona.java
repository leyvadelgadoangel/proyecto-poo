package com.mycompany.main_proyecto;

public abstract class Persona {
    
    protected String dni;
    protected String nombres;
    protected String apellidos;
    protected String correo;
    
    //Constuctor
    public Persona (String dni, String nombres, String apellidos, String correo){
        this.dni = dni;
        this.nombres = nombres;
        this.apellidos = apellidos;
        this.correo = correo;
    }
    
    //Método: Actualizar datos
        public void actualizarDatos( String nombres, String apellidos, String correo){
        this.nombres = nombres;
        this.apellidos = apellidos;
        this.correo = correo;
    }
    
    //GETTERS Y SETTERS
    public String getDni() {
        return dni;
    }

    public String getNombres() {
        return nombres;
    }

    public String getApellidos() {
        return apellidos;
    }

    public String getCorreo() {
        return correo;
    }
    
    @Override
    public String toString(){
        return "DNI: "+dni+" | "+nombres+" "+apellidos+" | Correo: "+correo;
    }
}