package com.mycompany.main_proyecto.persistencia;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConexionSQLServer {
    private static ConexionSQLServer instancia;
    private Connection conexion;

    // Ajusta servidor, usuario y contraseña según tu instancia local de SQL Server
    private final String url = "jdbc:sqlserver://localhost:1433;"
            + "databaseName=BDAsistenciaAcademica;encrypt=false;"
            + "user=sa;password=123456;";

    private ConexionSQLServer() throws SQLException {
        try {
            this.conexion = DriverManager.getConnection(url);
        } catch (SQLException e) {
            throw new SQLException("Error al conectar con SQL Server: " + e.getMessage());
        }
    }

    public static ConexionSQLServer getInstancia() throws SQLException {
        if (instancia == null || instancia.getConexion() == null || instancia.getConexion().isClosed()) {
            instancia = new ConexionSQLServer();
        }
        return instancia;
    }

    public Connection getConexion() {
        return conexion;
    }
}
