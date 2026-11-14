package com.mycompany.main_proyecto.persistencia;

import com.mycompany.main_proyecto.modelo.Curso;
import com.mycompany.main_proyecto.modelo.Docente;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CursoDAO {
    private final Connection conexion;

    public CursoDAO() throws SQLException {
        this.conexion = ConexionSQLServer.getInstancia().getConexion();
    }

    public void crear(Curso cur) throws SQLException {
        String sql = "{call usp_CrearCurso(?, ?, ?)}";
        try (CallableStatement stmt = conexion.prepareCall(sql)) {
            stmt.setString(1, cur.getCodigoCurso());
            stmt.setString(2, cur.getNombreCurso());
            stmt.setString(3, cur.getDocente().getCodigoDocente());
            stmt.execute();
        }
    }

    public List<Curso> listar() throws SQLException {
        List<Curso> lista = new ArrayList<>();
        String sql = "{call usp_ListarCursos()}";
        try (CallableStatement stmt = conexion.prepareCall(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                Docente doc = new Docente(
                    rs.getString("codigo_docente"),
                    rs.getString("dni"),
                    rs.getString("nombres"),
                    rs.getString("apellidos"),
                    rs.getString("correo")
                );
                lista.add(new Curso(
                    rs.getString("codigo_curso"),
                    rs.getString("nombre_curso"),
                    doc
                ));
            }
        }
        return lista;
    }
}
