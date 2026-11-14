package com.mycompany.main_proyecto.servicio;

import com.mycompany.main_proyecto.excepciones.SistemaAsistenciaException;
import com.mycompany.main_proyecto.modelo.*;
import com.mycompany.main_proyecto.persistencia.*;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class SistemaGestionAcademica {
    private List<Estudiante> estudiantes;
    private List<Docente> docentes;
    private List<Curso> cursos;
    private List<Matricula> matriculas;
    private List<SesionClase> sesiones;
    private List<Asistencia> asistencias;

    // DAOs de SQL Server (Patrón DAO + Singleton JDBC)
    private EstudianteDAO estudianteDAO;
    private DocenteDAO docenteDAO;
    private CursoDAO cursoDAO;
    private MatriculaDAO matriculaDAO;
    private SesionClaseDAO sesionDAO;
    private AsistenciaDAO asistenciaDAO;
    private boolean modoSQLServer = false;

    // Respaldo local binario (.dat) en caso no haya conexión activa a SQL Server
    private final GestorArchivos<Estudiante> repoEstudiantes = new GestorArchivos<>("estudiantes.dat");
    private final GestorArchivos<Docente> repoDocentes = new GestorArchivos<>("docentes.dat");
    private final GestorArchivos<Curso> repoCursos = new GestorArchivos<>("cursos.dat");
    private final GestorArchivos<Matricula> repoMatriculas = new GestorArchivos<>("matriculas.dat");
    private final GestorArchivos<SesionClase> repoSesiones = new GestorArchivos<>("sesiones.dat");
    private final GestorArchivos<Asistencia> repoAsistencias = new GestorArchivos<>("asistencias.dat");

    public SistemaGestionAcademica() {
        try {
            estudianteDAO = new EstudianteDAO();
            docenteDAO = new DocenteDAO();
            cursoDAO = new CursoDAO();
            matriculaDAO = new MatriculaDAO();
            sesionDAO = new SesionClaseDAO();
            asistenciaDAO = new AsistenciaDAO();
            modoSQLServer = true;
            System.out.println("[BD] Conectado exitosamente a SQL Server (BDAsistenciaAcademica).");
        } catch (SQLException e) {
            modoSQLServer = false;
            System.out.println("[AVISO] SQL Server no disponible (" + e.getMessage() + "). Usando respaldo local .dat.");
        }

        cargarTodo();
        if (estudiantes.isEmpty() && docentes.isEmpty()) {
            cargarDatosPrueba();
        }
    }

    private void cargarTodo() {
        if (modoSQLServer) {
            try {
                estudiantes = estudianteDAO.listar();
                docentes = docenteDAO.listar();
                cursos = cursoDAO.listar();
                matriculas = matriculaDAO.listar(estudiantes, cursos);
                sesiones = sesionDAO.listar(cursos);
                asistencias = asistenciaDAO.listar(sesiones, estudiantes);
                return;
            } catch (SQLException e) {
                System.err.println("Error al leer desde SQL Server: " + e.getMessage());
            }
        }
        estudiantes = repoEstudiantes.cargarLista();
        docentes = repoDocentes.cargarLista();
        cursos = repoCursos.cargarLista();
        matriculas = repoMatriculas.cargarLista();
        sesiones = repoSesiones.cargarLista();
        asistencias = repoAsistencias.cargarLista();
    }

    public void guardarRespaldoLocal() {
        repoEstudiantes.guardarLista(estudiantes);
        repoDocentes.guardarLista(docentes);
        repoCursos.guardarLista(cursos);
        repoMatriculas.guardarLista(matriculas);
        repoSesiones.guardarLista(sesiones);
        repoAsistencias.guardarLista(asistencias);
    }

    // A) Gestión de estudiantes
    public void registrarEstudiante(Estudiante est) throws SistemaAsistenciaException {
        boolean existe = estudiantes.stream()
                .anyMatch(e -> e.getCodigoUniversitario().equalsIgnoreCase(est.getCodigoUniversitario()));
        if (existe) throw new SistemaAsistenciaException("Ya existe un estudiante con el código " + est.getCodigoUniversitario());

        if (modoSQLServer) {
            try {
                estudianteDAO.crear(est);
            } catch (SQLException e) {
                throw new SistemaAsistenciaException("Error SQL al registrar estudiante: " + e.getMessage());
            }
        }
        estudiantes.add(est);
        guardarRespaldoLocal();
    }

    public Optional<Estudiante> buscarEstudiante(String codigo) {
        return estudiantes.stream()
                .filter(e -> e.getCodigoUniversitario().equalsIgnoreCase(codigo))
                .findFirst();
    }

    public void modificarEstudiante(String codigo, String nombres, String apellidos, String correo) throws SistemaAsistenciaException {
        Estudiante est = buscarEstudiante(codigo)
                .orElseThrow(() -> new SistemaAsistenciaException("Estudiante no encontrado."));
        est.actualizarDatos(nombres, apellidos, correo);

        if (modoSQLServer) {
            try {
                estudianteDAO.actualizar(est);
            } catch (SQLException e) {
                throw new SistemaAsistenciaException("Error SQL al actualizar estudiante: " + e.getMessage());
            }
        }
        guardarRespaldoLocal();
    }

    public void eliminarEstudiante(String codigo) throws SistemaAsistenciaException {
        boolean existe = estudiantes.stream().anyMatch(e -> e.getCodigoUniversitario().equalsIgnoreCase(codigo));
        if (!existe) throw new SistemaAsistenciaException("No se encontró el estudiante a eliminar.");

        if (modoSQLServer) {
            try {
                estudianteDAO.eliminar(codigo);
            } catch (SQLException e) {
                throw new SistemaAsistenciaException("Error SQL al eliminar estudiante: " + e.getMessage());
            }
        }
        estudiantes.removeIf(e -> e.getCodigoUniversitario().equalsIgnoreCase(codigo));
        guardarRespaldoLocal();
    }

    public List<Estudiante> listarEstudiantes() { return estudiantes; }

    // B) Gestión de docentes
    public void registrarDocente(Docente doc) throws SistemaAsistenciaException {
        boolean existe = docentes.stream()
                .anyMatch(d -> d.getCodigoDocente().equalsIgnoreCase(doc.getCodigoDocente()));
        if (existe) throw new SistemaAsistenciaException("Ya existe un docente con el código " + doc.getCodigoDocente());

        if (modoSQLServer) {
            try {
                docenteDAO.crear(doc);
            } catch (SQLException e) {
                throw new SistemaAsistenciaException("Error SQL al registrar docente: " + e.getMessage());
            }
        }
        docentes.add(doc);
        guardarRespaldoLocal();
    }

    public Optional<Docente> buscarDocente(String codigo) {
        return docentes.stream()
                .filter(d -> d.getCodigoDocente().equalsIgnoreCase(codigo))
                .findFirst();
    }

    public void modificarDocente(String codigo, String nombres, String apellidos, String correo) throws SistemaAsistenciaException {
        Docente doc = buscarDocente(codigo)
                .orElseThrow(() -> new SistemaAsistenciaException("Docente no encontrado."));
        doc.actualizarDatos(nombres, apellidos, correo);

        if (modoSQLServer) {
            try {
                docenteDAO.actualizar(doc);
            } catch (SQLException e) {
                throw new SistemaAsistenciaException("Error SQL al actualizar docente: " + e.getMessage());
            }
        }
        guardarRespaldoLocal();
    }

    public void eliminarDocente(String codigo) throws SistemaAsistenciaException {
        boolean existe = docentes.stream().anyMatch(d -> d.getCodigoDocente().equalsIgnoreCase(codigo));
        if (!existe) throw new SistemaAsistenciaException("No se encontró el docente a eliminar.");

        if (modoSQLServer) {
            try {
                docenteDAO.eliminar(codigo);
            } catch (SQLException e) {
                throw new SistemaAsistenciaException("Error SQL al eliminar docente: " + e.getMessage());
            }
        }
        docentes.removeIf(d -> d.getCodigoDocente().equalsIgnoreCase(codigo));
        guardarRespaldoLocal();
    }

    public List<Docente> listarDocentes() { return docentes; }

    // C) Gestión de cursos
    public void registrarCurso(String codigoCurso, String nombreCurso, String codigoDocente) throws SistemaAsistenciaException {
        boolean existe = cursos.stream().anyMatch(c -> c.getCodigoCurso().equalsIgnoreCase(codigoCurso));
        if (existe) throw new SistemaAsistenciaException("El código de curso ya existe.");
        Docente docente = buscarDocente(codigoDocente)
                .orElseThrow(() -> new SistemaAsistenciaException("El docente asignado no existe."));

        Curso nuevoCurso = new Curso(codigoCurso, nombreCurso, docente);
        if (modoSQLServer) {
            try {
                cursoDAO.crear(nuevoCurso);
            } catch (SQLException e) {
                throw new SistemaAsistenciaException("Error SQL al registrar curso: " + e.getMessage());
            }
        }
        cursos.add(nuevoCurso);
        guardarRespaldoLocal();
    }

    public Optional<Curso> buscarCurso(String codigo) {
        return cursos.stream().filter(c -> c.getCodigoCurso().equalsIgnoreCase(codigo)).findFirst();
    }

    public List<Curso> listarCursos() { return cursos; }

    // D) Matrícula
    public void matricularEstudiante(String codEstudiante, String codCurso) throws SistemaAsistenciaException {
        Estudiante est = buscarEstudiante(codEstudiante)
                .orElseThrow(() -> new SistemaAsistenciaException("Estudiante no encontrado."));
        Curso cur = buscarCurso(codCurso)
                .orElseThrow(() -> new SistemaAsistenciaException("Curso no encontrado."));

        boolean yaMatriculado = matriculas.stream()
                .anyMatch(m -> m.getEstudiante().getCodigoUniversitario().equalsIgnoreCase(codEstudiante)
                        && m.getCurso().getCodigoCurso().equalsIgnoreCase(codCurso));
        if (yaMatriculado) throw new SistemaAsistenciaException("El estudiante ya está matriculado en este curso.");

        Matricula nuevaMat = new Matricula(est, cur, LocalDate.now());
        if (modoSQLServer) {
            try {
                matriculaDAO.crear(nuevaMat);
            } catch (SQLException e) {
                throw new SistemaAsistenciaException("Error SQL al matricular estudiante: " + e.getMessage());
            }
        }
        matriculas.add(nuevaMat);
        guardarRespaldoLocal();
    }

    public List<Estudiante> obtenerEstudiantesPorCurso(String codCurso) {
        return matriculas.stream()
                .filter(m -> m.getCurso().getCodigoCurso().equalsIgnoreCase(codCurso))
                .map(Matricula::getEstudiante)
                .collect(Collectors.toList());
    }

    // E) Registro de sesiones
    public SesionClase crearSesion(String idSesion, String codCurso, LocalDate fecha) throws SistemaAsistenciaException {
        boolean existe = sesiones.stream().anyMatch(s -> s.getIdSesion().equalsIgnoreCase(idSesion));
        if (existe) throw new SistemaAsistenciaException("Ya existe una sesión con ID " + idSesion);
        Curso cur = buscarCurso(codCurso)
                .orElseThrow(() -> new SistemaAsistenciaException("Curso no encontrado."));

        SesionClase sesion = new SesionClase(idSesion, cur, fecha);
        if (modoSQLServer) {
            try {
                sesionDAO.crear(sesion);
            } catch (SQLException e) {
                throw new SistemaAsistenciaException("Error SQL al crear sesión: " + e.getMessage());
            }
        }
        sesiones.add(sesion);
        guardarRespaldoLocal();
        return sesion;
    }

    public Optional<SesionClase> buscarSesion(String idSesion) {
        return sesiones.stream().filter(s -> s.getIdSesion().equalsIgnoreCase(idSesion)).findFirst();
    }

    public List<SesionClase> listarSesiones() { return sesiones; }

    // F y G) Registro y validación de unicidad de asistencia
    public void registrarAsistencia(String idSesion, String codEstudiante, EstadoAsistencia estado) throws SistemaAsistenciaException {
        SesionClase sesion = buscarSesion(idSesion)
                .orElseThrow(() -> new SistemaAsistenciaException("Sesión no encontrada."));
        Estudiante est = buscarEstudiante(codEstudiante)
                .orElseThrow(() -> new SistemaAsistenciaException("Estudiante no encontrado."));

        boolean matriculado = matriculas.stream()
                .anyMatch(m -> m.getEstudiante().getCodigoUniversitario().equalsIgnoreCase(codEstudiante)
                        && m.getCurso().getCodigoCurso().equalsIgnoreCase(sesion.getCurso().getCodigoCurso()));
        if (!matriculado) {
            throw new SistemaAsistenciaException("El estudiante no está matriculado en el curso de esta sesión.");
        }

        boolean duplicado = asistencias.stream()
                .anyMatch(a -> a.getSesion().getIdSesion().equalsIgnoreCase(idSesion)
                        && a.getEstudiante().getCodigoUniversitario().equalsIgnoreCase(codEstudiante));
        if (duplicado) {
            throw new SistemaAsistenciaException("DUPLICADO: Ya existe asistencia registrada para este alumno en la sesión " + idSesion);
        }

        String idAsis = "ASIS-" + (asistencias.size() + 1);
        Asistencia nuevaAsis = new Asistencia(idAsis, sesion, est, estado);

        if (modoSQLServer) {
            try {
                asistenciaDAO.registrar(nuevaAsis);
            } catch (SQLException e) {
                throw new SistemaAsistenciaException("Error SQL al registrar asistencia: " + e.getMessage());
            }
        }
        asistencias.add(nuevaAsis);
        guardarRespaldoLocal();
    }

    // H) Modificación de asistencia
    public void modificarAsistencia(String idSesion, String codEstudiante, EstadoAsistencia nuevoEstado) throws SistemaAsistenciaException {
        Asistencia registro = asistencias.stream()
                .filter(a -> a.getSesion().getIdSesion().equalsIgnoreCase(idSesion)
                        && a.getEstudiante().getCodigoUniversitario().equalsIgnoreCase(codEstudiante))
                .findFirst()
                .orElseThrow(() -> new SistemaAsistenciaException("No se encontró registro de asistencia para modificar."));

        if (modoSQLServer) {
            try {
                asistenciaDAO.modificar(idSesion, codEstudiante, nuevoEstado);
            } catch (SQLException e) {
                throw new SistemaAsistenciaException("Error SQL al modificar asistencia: " + e.getMessage());
            }
        }
        registro.setEstado(nuevoEstado);
        guardarRespaldoLocal();
    }

    // I) Reportes y estadísticas con Streams y Lambdas
    public void generarReportePorEstudiante(String codEstudiante, LocalDate inicio, LocalDate fin) {
        List<Asistencia> filtradas = asistencias.stream()
                .filter(a -> a.getEstudiante().getCodigoUniversitario().equalsIgnoreCase(codEstudiante))
                .filter(a -> !a.getSesion().getFecha().isBefore(inicio) && !a.getSesion().getFecha().isAfter(fin))
                .collect(Collectors.toList());

        imprimirEstadisticas("ESTUDIANTE: " + codEstudiante, filtradas);
    }

    public void generarReportePorCurso(String codCurso, LocalDate inicio, LocalDate fin) {
        List<Asistencia> filtradas = asistencias.stream()
                .filter(a -> a.getSesion().getCurso().getCodigoCurso().equalsIgnoreCase(codCurso))
                .filter(a -> !a.getSesion().getFecha().isBefore(inicio) && !a.getSesion().getFecha().isAfter(fin))
                .collect(Collectors.toList());

        imprimirEstadisticas("CURSO: " + codCurso, filtradas);
    }

    private void imprimirEstadisticas(String titulo, List<Asistencia> lista) {
        System.out.println("\n--- REPORTE DE ASISTENCIA (" + titulo + ") ---");
        if (lista.isEmpty()) {
            System.out.println("No se encontraron registros en el rango de fechas indicado.");
            return;
        }
        lista.forEach(System.out::println);

        long total = lista.size();
        long asistio = lista.stream().filter(a -> a.getEstado() == EstadoAsistencia.ASISTIO).count();
        long tardanza = lista.stream().filter(a -> a.getEstado() == EstadoAsistencia.TARDANZA).count();
        long falta = lista.stream().filter(a -> a.getEstado() == EstadoAsistencia.FALTA).count();

        System.out.printf("\nRESUMEN ESTADÍSTICO (Total registros: %d):\n", total);
        System.out.printf(" -> Asistencias : %d (%.1f%%)\n", asistio, (asistio * 100.0 / total));
        System.out.printf(" -> Tardanzas   : %d (%.1f%%)\n", tardanza, (tardanza * 100.0 / total));
        System.out.printf(" -> Faltas      : %d (%.1f%%)\n", falta, (falta * 100.0 / total));
    }

    private void cargarDatosPrueba() {
        try {
            registrarDocente(new Docente("DOC01", "40123456", "Willy Alexander", "Tapia Ore", "wtapia@juliovermi.edu.pe"));
            registrarEstudiante(new Estudiante("U25257295", "71234567", "Alexis Jeampierre", "Chunga Panta", "u25257295@juliovermi.edu.pe"));
            registrarEstudiante(new Estudiante("U25235660", "72345678", "Carlos Andre", "Ayala Flores", "u25235660@juliovermi.edu.pe"));
            registrarCurso("SI34", "Programación Orientada a Objetos", "DOC01");
            matricularEstudiante("U25257295", "SI34");
            matricularEstudiante("U25235660", "SI34");
        } catch (SistemaAsistenciaException ignored) {}
    }
}
