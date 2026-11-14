package com.mycompany.main_proyecto;

import com.mycompany.main_proyecto.excepciones.SistemaAsistenciaException;
import com.mycompany.main_proyecto.modelo.*;
import com.mycompany.main_proyecto.servicio.SistemaGestionAcademica;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Scanner;

public class Main_proyecto {
    private static final Scanner sc = new Scanner(System.in);
    private static final SistemaGestionAcademica sistema = new SistemaGestionAcademica();

    public static void main(String[] args) {
        int opcion = -1;
        do {
            System.out.println("\n=========================================================");
            System.out.println("   UNIVERSIDAD JULIO VERMI - CONTROL DE ASISTENCIA");
            System.out.println("=========================================================");
            System.out.println("1. Gestión de Estudiantes (Registrar / Listar / Editar / Eliminar)");
            System.out.println("2. Gestión de Docentes (Registrar / Listar / Editar / Eliminar)");
            System.out.println("3. Gestión de Cursos (Registrar / Listar)");
            System.out.println("4. Matricular Estudiante en Curso");
            System.out.println("5. Registrar Sesión de Clase y Tomar Asistencia");
            System.out.println("6. Modificar Registro de Asistencia");
            System.out.println("7. Generar Reportes y Estadísticas");
            System.out.println("0. Salir");
            System.out.print("Seleccione una opción: ");

            try {
                opcion = Integer.parseInt(sc.nextLine().trim());
                switch (opcion) {
                    case 1 -> menuEstudiantes();
                    case 2 -> menuDocentes();
                    case 3 -> menuCursos();
                    case 4 -> menuMatricula();
                    case 5 -> menuSesionYAsistencia();
                    case 6 -> menuModificarAsistencia();
                    case 7 -> menuReportes();
                    case 0 -> System.out.println("Guardando datos y cerrando sistema...");
                    default -> System.out.println("Opción no válida.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Error: Ingrese un número válido.");
            } catch (SistemaAsistenciaException e) {
                System.out.println("AVISO DEL SISTEMA: " + e.getMessage());
            } catch (DateTimeParseException e) {
                System.out.println("Error de fecha: Use el formato AAAA-MM-DD (ej. 2026-11-13).");
            }
        } while (opcion != 0);
    }

    private static void menuEstudiantes() throws SistemaAsistenciaException {
        System.out.println("\n--- GESTIÓN DE ESTUDIANTES ---");
        System.out.println("1. Registrar | 2. Listar | 3. Modificar | 4. Eliminar");
        System.out.print("Opción: ");
        int op = Integer.parseInt(sc.nextLine());
        switch (op) {
            case 1 -> {
                System.out.print("Código Universitario: "); String cod = sc.nextLine();
                System.out.print("DNI: "); String dni = sc.nextLine();
                System.out.print("Nombres: "); String nom = sc.nextLine();
                System.out.print("Apellidos: "); String ape = sc.nextLine();
                System.out.print("Correo: "); String cor = sc.nextLine();
                sistema.registrarEstudiante(new Estudiante(cod, dni, nom, ape, cor));
                System.out.println("Estudiante registrado con éxito.");
            }
            case 2 -> sistema.listarEstudiantes().forEach(System.out::println);
            case 3 -> {
                System.out.print("Código del estudiante a modificar: "); String cod = sc.nextLine();
                System.out.print("Nuevos nombres: "); String nom = sc.nextLine();
                System.out.print("Nuevos apellidos: "); String ape = sc.nextLine();
                System.out.print("Nuevo correo: "); String cor = sc.nextLine();
                sistema.modificarEstudiante(cod, nom, ape, cor);
                System.out.println("Datos actualizados.");
            }
            case 4 -> {
                System.out.print("Código del estudiante a eliminar: "); String cod = sc.nextLine();
                sistema.eliminarEstudiante(cod);
                System.out.println("Estudiante eliminado.");
            }
        }
    }

    private static void menuDocentes() throws SistemaAsistenciaException {
        System.out.println("\n--- GESTIÓN DE DOCENTES ---");
        System.out.println("1. Registrar | 2. Listar | 3. Modificar | 4. Eliminar");
        System.out.print("Opción: ");
        int op = Integer.parseInt(sc.nextLine());
        switch (op) {
            case 1 -> {
                System.out.print("Código Docente: "); String cod = sc.nextLine();
                System.out.print("DNI: "); String dni = sc.nextLine();
                System.out.print("Nombres: "); String nom = sc.nextLine();
                System.out.print("Apellidos: "); String ape = sc.nextLine();
                System.out.print("Correo: "); String cor = sc.nextLine();
                sistema.registrarDocente(new Docente(cod, dni, nom, ape, cor));
                System.out.println("Docente registrado con éxito.");
            }
            case 2 -> sistema.listarDocentes().forEach(System.out::println);
            case 3 -> {
                System.out.print("Código del docente a modificar: "); String cod = sc.nextLine();
                System.out.print("Nuevos nombres: "); String nom = sc.nextLine();
                System.out.print("Nuevos apellidos: "); String ape = sc.nextLine();
                System.out.print("Nuevo correo: "); String cor = sc.nextLine();
                sistema.modificarDocente(cod, nom, ape, cor);
                System.out.println("Datos actualizados.");
            }
            case 4 -> {
                System.out.print("Código del docente a eliminar: "); String cod = sc.nextLine();
                sistema.eliminarDocente(cod);
                System.out.println("Docente eliminado.");
            }
        }
    }

    private static void menuCursos() throws SistemaAsistenciaException {
        System.out.println("\n--- GESTIÓN DE CURSOS ---");
        System.out.println("1. Registrar Curso | 2. Listar Cursos");
        System.out.print("Opción: ");
        int op = Integer.parseInt(sc.nextLine());
        if (op == 1) {
            System.out.print("Código de Curso: "); String cod = sc.nextLine();
            System.out.print("Nombre de Curso: "); String nom = sc.nextLine();
            System.out.print("Código Docente responsable: "); String doc = sc.nextLine();
            sistema.registrarCurso(cod, nom, doc);
            System.out.println("Curso registrado.");
        } else {
            sistema.listarCursos().forEach(System.out::println);
        }
    }

    private static void menuMatricula() throws SistemaAsistenciaException {
        System.out.println("\n--- MATRÍCULA DE ESTUDIANTE ---");
        System.out.print("Código Universitario del Estudiante: "); String est = sc.nextLine();
        System.out.print("Código del Curso: "); String cur = sc.nextLine();
        sistema.matricularEstudiante(est, cur);
        System.out.println("Matrícula realizada correctamente.");
    }

    private static void menuSesionYAsistencia() throws SistemaAsistenciaException {
        System.out.println("\n--- REGISTRO DE SESIÓN Y ASISTENCIA ---");
        System.out.print("ID de Nueva Sesión (ej. SES-01): "); String idSes = sc.nextLine();
        System.out.print("Código del Curso: "); String codCur = sc.nextLine();
        System.out.print("Fecha (AAAA-MM-DD, Enter para hoy): "); String fStr = sc.nextLine();
        LocalDate fecha = fStr.isBlank() ? LocalDate.now() : LocalDate.parse(fStr);

        SesionClase sesion = sistema.crearSesion(idSes, codCur, fecha);
        System.out.println("Sesión creada: " + sesion);

        List<Estudiante> matriculados = sistema.obtenerEstudiantesPorCurso(codCur);
        if (matriculados.isEmpty()) {
            System.out.println("No hay estudiantes matriculados en este curso.");
            return;
        }

        for (Estudiante est : matriculados) {
            System.out.printf("Alumno: %s [%s] -> Estado (1=ASISTIO, 2=TARDANZA, 3=FALTA): ",
                    est.getNombreCompleto(), est.getCodigoUniversitario());
            int estOp = Integer.parseInt(sc.nextLine());
            EstadoAsistencia estado = switch (estOp) {
                case 1 -> EstadoAsistencia.ASISTIO;
                case 2 -> EstadoAsistencia.TARDANZA;
                default -> EstadoAsistencia.FALTA;
            };
            sistema.registrarAsistencia(idSes, est.getCodigoUniversitario(), estado);
        }
        System.out.println("Asistencia de la sesión guardada.");
    }

    private static void menuModificarAsistencia() throws SistemaAsistenciaException {
        System.out.println("\n--- MODIFICAR ASISTENCIA ---");
        System.out.print("ID de Sesión: "); String idSes = sc.nextLine();
        System.out.print("Código del Estudiante: "); String codEst = sc.nextLine();
        System.out.print("Nuevo Estado (1=ASISTIO, 2=TARDANZA, 3=FALTA): ");
        int estOp = Integer.parseInt(sc.nextLine());
        EstadoAsistencia estado = switch (estOp) {
            case 1 -> EstadoAsistencia.ASISTIO;
            case 2 -> EstadoAsistencia.TARDANZA;
            default -> EstadoAsistencia.FALTA;
        };
        sistema.modificarAsistencia(idSes, codEst, estado);
        System.out.println("Asistencia actualizada sin duplicados.");
    }

    private static void menuReportes() {
        System.out.println("\n--- REPORTES Y ESTADÍSTICAS ---");
        System.out.println("1. Reporte por Estudiante | 2. Reporte por Curso");
        System.out.print("Opción: ");
        int op = Integer.parseInt(sc.nextLine());
        System.out.print("Fecha Inicio (AAAA-MM-DD): "); LocalDate ini = LocalDate.parse(sc.nextLine());
        System.out.print("Fecha Fin (AAAA-MM-DD): "); LocalDate fin = LocalDate.parse(sc.nextLine());

        if (op == 1) {
            System.out.print("Código Universitario: "); String cod = sc.nextLine();
            sistema.generarReportePorEstudiante(cod, ini, fin);
        } else {
            System.out.print("Código del Curso: "); String cod = sc.nextLine();
            sistema.generarReportePorCurso(cod, ini, fin);
        }
    }
}
