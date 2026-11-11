package com.mycompany.main_proyecto;

public class Main_proyecto {

    public static void main(String[] args) {
        Docente docente1 = new Docente("12345678", "Willy", "Tapia", "willy@utp.edu.pe", "DOC-001");
        Curso curso1 = new Curso("POO-01", "Programación Orientada a Objetos", docente1);
        
        System.out.println("Curso: " + curso1.getNombreCurso());
        System.out.println("Docente: " + curso1.getDocente().getNombres());
    }
}
