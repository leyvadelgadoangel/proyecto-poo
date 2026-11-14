package com.mycompany.main_proyecto.persistencia;

import com.mycompany.main_proyecto.modelo.Asistencia;
import com.mycompany.main_proyecto.modelo.EstadoAsistencia;
import com.mycompany.main_proyecto.modelo.Estudiante;
import com.mycompany.main_proyecto.modelo.SesionClase;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AsistenciaDAO {
    private final Connection conexion;

    public AsistenciaDAO() throws SQLException {
        this.conexion = ConexionSQLServer.getInstancia().getConexion();
    }

    public void registrar(Asistencia asis) throws SQLException {
        String sql = "{call usp_RegistrarAsistencia(?, ?, ?, ?)}";
        try (CallableStatement stmt = conexion.prepareCall(sql)) {
            stmt.setString(1, asis.getIdAsistencia());
            stmt.setString(2, asis.getSesion().getIdSesion());
            stmt.setString(3, asis.getEstudiante().getCodigoUniversitario());
            stmt.setString(4, asis.getEstado().name());
            stmt.execute();
        }
    }

    public void modificar(String idSesion, String codEstudiante, EstadoAsistencia nuevoEstado) throws SQLException {
        String sql = "{call usp_ModificarAsistencia(?, ?, ?)}";
        try (CallableStatement stmt = conexion.prepareCall(sql)) {
            stmt.setString(1, idSesion);
            stmt.setString(2, codEstudiante);
            stmt.setString(3, nuevoEstado.name());
            stmt.execute();
        }
    }

    public List<Asistencia> listar(List<SesionClase> sesiones, List<Estudiante> estudiantes) throws SQLException {
        List<Asistencia> lista = new ArrayList<>();
        String sql = "{call usp_ListarAsistencias()}";
        try (CallableStatement stmt = conexion.prepareCall(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                String idAsis = rs.getString("id_asistencia");
                String idSes = rs.getString("id_sesion");
                String codEst = rs.getString("codigo_universitario");
                EstadoAsistencia estEnum = EstadoAsistencia.valueOf(rs.getString("estado"));

                SesionClase ses = sesiones.stream()
                        .filter(s -> s.getIdSesion().equalsIgnoreCase(idSes))
                        .findFirst().orElse(null);
                Estudiante est = estudiantes.stream()
                        .filter(e -> e.getCodigoUniversitario().equalsIgnoreCase(codEst))
                        .findFirst().orElse(null);

                if (ses != null && est != null) {
                    lista.add(new Asistencia(idAsis, ses, est, estEnum));
                }
            }
        }
        return lista;
    }
}
