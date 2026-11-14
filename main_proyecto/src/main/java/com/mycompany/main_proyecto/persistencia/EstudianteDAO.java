package com.mycompany.main_proyecto.persistencia;

import com.mycompany.main_proyecto.modelo.Estudiante;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class EstudianteDAO {
    private final Connection conexion;

    public EstudianteDAO() throws SQLException {
        this.conexion = ConexionSQLServer.getInstancia().getConexion();
    }

    public void crear(Estudiante est) throws SQLException {
        String sql = "{call usp_CrearEstudiante(?, ?, ?, ?, ?)}";
        try (CallableStatement stmt = conexion.prepareCall(sql)) {
            stmt.setString(1, est.getCodigoUniversitario());
            stmt.setString(2, est.getDni());
            stmt.setString(3, est.getNombres());
            stmt.setString(4, est.getApellidos());
            stmt.setString(5, est.getCorreo());
            stmt.execute();
        }
    }

    public List<Estudiante> listar() throws SQLException {
        List<Estudiante> lista = new ArrayList<>();
        String sql = "{call usp_ListarEstudiantes()}";
        try (CallableStatement stmt = conexion.prepareCall(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                lista.add(new Estudiante(
                    rs.getString("codigo_universitario"),
                    rs.getString("dni"),
                    rs.getString("nombres"),
                    rs.getString("apellidos"),
                    rs.getString("correo")
                ));
            }
        }
        return lista;
    }

    public void actualizar(Estudiante est) throws SQLException {
        String sql = "{call usp_ActualizarEstudiante(?, ?, ?, ?)}";
        try (CallableStatement stmt = conexion.prepareCall(sql)) {
            stmt.setString(1, est.getCodigoUniversitario());
            stmt.setString(2, est.getNombres());
            stmt.setString(3, est.getApellidos());
            stmt.setString(4, est.getCorreo());
            stmt.execute();
        }
    }

    public void eliminar(String codigo) throws SQLException {
        String sql = "{call usp_EliminarEstudiante(?)}";
        try (CallableStatement stmt = conexion.prepareCall(sql)) {
            stmt.setString(1, codigo);
            stmt.execute();
        }
    }
}
