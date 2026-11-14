package com.mycompany.main_proyecto.persistencia;

import com.mycompany.main_proyecto.modelo.Docente;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DocenteDAO {
    private final Connection conexion;

    public DocenteDAO() throws SQLException {
        this.conexion = ConexionSQLServer.getInstancia().getConexion();
    }

    public void crear(Docente doc) throws SQLException {
        String sql = "{call usp_CrearDocente(?, ?, ?, ?, ?)}";
        try (CallableStatement stmt = conexion.prepareCall(sql)) {
            stmt.setString(1, doc.getCodigoDocente());
            stmt.setString(2, doc.getDni());
            stmt.setString(3, doc.getNombres());
            stmt.setString(4, doc.getApellidos());
            stmt.setString(5, doc.getCorreo());
            stmt.execute();
        }
    }

    public List<Docente> listar() throws SQLException {
        List<Docente> lista = new ArrayList<>();
        String sql = "{call usp_ListarDocentes()}";
        try (CallableStatement stmt = conexion.prepareCall(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                lista.add(new Docente(
                    rs.getString("codigo_docente"),
                    rs.getString("dni"),
                    rs.getString("nombres"),
                    rs.getString("apellidos"),
                    rs.getString("correo")
                ));
            }
        }
        return lista;
    }

    public void actualizar(Docente doc) throws SQLException {
        String sql = "{call usp_ActualizarDocente(?, ?, ?, ?)}";
        try (CallableStatement stmt = conexion.prepareCall(sql)) {
            stmt.setString(1, doc.getCodigoDocente());
            stmt.setString(2, doc.getNombres());
            stmt.setString(3, doc.getApellidos());
            stmt.setString(4, doc.getCorreo());
            stmt.execute();
        }
    }

    public void eliminar(String codigo) throws SQLException {
        String sql = "{call usp_EliminarDocente(?)}";
        try (CallableStatement stmt = conexion.prepareCall(sql)) {
            stmt.setString(1, codigo);
            stmt.execute();
        }
    }
}
