package com.mycompany.main_proyecto.persistencia;

import com.mycompany.main_proyecto.modelo.Curso;
import com.mycompany.main_proyecto.modelo.SesionClase;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class SesionClaseDAO {
    private final Connection conexion;

    public SesionClaseDAO() throws SQLException {
        this.conexion = ConexionSQLServer.getInstancia().getConexion();
    }

    public void crear(SesionClase ses) throws SQLException {
        String sql = "{call usp_CrearSesion(?, ?, ?)}";
        try (CallableStatement stmt = conexion.prepareCall(sql)) {
            stmt.setString(1, ses.getIdSesion());
            stmt.setString(2, ses.getCurso().getCodigoCurso());
            stmt.setDate(3, Date.valueOf(ses.getFecha()));
            stmt.execute();
        }
    }

    public List<SesionClase> listar(List<Curso> cursos) throws SQLException {
        List<SesionClase> lista = new ArrayList<>();
        String sql = "{call usp_ListarSesiones()}";
        try (CallableStatement stmt = conexion.prepareCall(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                String idSes = rs.getString("id_sesion");
                String codCur = rs.getString("codigo_curso");
                LocalDate fecha = rs.getDate("fecha").toLocalDate();

                Curso cur = cursos.stream()
                        .filter(c -> c.getCodigoCurso().equalsIgnoreCase(codCur))
                        .findFirst().orElse(null);

                if (cur != null) {
                    lista.add(new SesionClase(idSes, cur, fecha));
                }
            }
        }
        return lista;
    }
}
