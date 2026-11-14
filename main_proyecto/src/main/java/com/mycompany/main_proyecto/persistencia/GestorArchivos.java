package com.mycompany.main_proyecto.persistencia;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class GestorArchivos<T extends Serializable> {
    private final String rutaArchivo;

    public GestorArchivos(String nombreArchivo) {
        File carpeta = new File("datos");
        if (!carpeta.exists()) {
            carpeta.mkdirs();
        }
        this.rutaArchivo = "datos/" + nombreArchivo;
    }

    public void guardarLista(List<T> lista) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(rutaArchivo))) {
            oos.writeObject(lista);
        } catch (IOException e) {
            System.err.println("Error al guardar datos en " + rutaArchivo + ": " + e.getMessage());
        }
    }

    @SuppressWarnings("unchecked")
    public List<T> cargarLista() {
        File archivo = new File(rutaArchivo);
        if (!archivo.exists()) {
            return new ArrayList<>();
        }
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(archivo))) {
            return (List<T>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            return new ArrayList<>();
        }
    }
}