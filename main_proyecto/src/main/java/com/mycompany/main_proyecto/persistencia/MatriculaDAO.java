package com.mycompany.main_proyecto.persistencia;

import com.mycompany.main_proyecto.modelo.Curso;
import com.mycompany.main_proyecto.modelo.Estudiante;
import com.mycompany.main_proyecto.modelo.Matricula;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class MatriculaDAO {
    private final Connection conexion;

    public MatriculaDAO() throws SQLException {
        this.conexion = ConexionSQLServer.getInstancia().getConexion();
    }

    public void crear(Matricula mat) throws SQLException {
        String sql = "{call usp_CrearMatricula(?, ?, ?)}";
        try (CallableStatement stmt = conexion.prepareCall(sql)) {
            stmt.setString(1, mat.getEstudiante().getCodigoUniversitario());
            stmt.setString(2, mat.getCurso().getCodigoCurso());
            stmt.setDate(3, Date.valueOf(mat.getFechaMatricula()));
            stmt.execute();
        }
    }

    public List<Matricula> listar(List<Estudiante> estudiantes, List<Curso> cursos) throws SQLException {
        List<Matricula> lista = new ArrayList<>();
        String sql = "{call usp_ListarMatriculas()}";
        try (CallableStatement stmt = conexion.prepareCall(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                String codEst = rs.getString("codigo_universitario");
                String codCur = rs.getString("codigo_curso");
                LocalDate fecha = rs.getDate("fecha_matricula").toLocalDate();

                Estudiante est = estudiantes.stream()
                        .filter(e -> e.getCodigoUniversitario().equalsIgnoreCase(codEst))
                        .findFirst().orElse(null);
                Curso cur = cursos.stream()
                        .filter(c -> c.getCodigoCurso().equalsIgnoreCase(codCur))
                        .findFirst().orElse(null);

                if (est != null && cur != null) {
                    lista.add(new Matricula(est, cur, fecha));
                }
            }
        }
        return lista;
    }
}
