-- SCRIPT SQL SERVER - UNIVERSIDAD JULIO VERMI (CONTROL DE ASISTENCIA)

IF NOT EXISTS (SELECT name FROM sys.databases WHERE name = 'BDAsistenciaAcademica')
BEGIN
    CREATE DATABASE BDAsistenciaAcademica;
END
GO

USE BDAsistenciaAcademica;
GO

-- 1. TABLAS RELACIONALES (3FN)
IF OBJECT_ID('dbo.Asistencia', 'U') IS NOT NULL DROP TABLE dbo.Asistencia;
IF OBJECT_ID('dbo.SesionClase', 'U') IS NOT NULL DROP TABLE dbo.SesionClase;
IF OBJECT_ID('dbo.Matricula', 'U') IS NOT NULL DROP TABLE dbo.Matricula;
IF OBJECT_ID('dbo.Curso', 'U') IS NOT NULL DROP TABLE dbo.Curso;
IF OBJECT_ID('dbo.Docente', 'U') IS NOT NULL DROP TABLE dbo.Docente;
IF OBJECT_ID('dbo.Estudiante', 'U') IS NOT NULL DROP TABLE dbo.Estudiante;
GO

CREATE TABLE dbo.Estudiante (
    codigo_universitario VARCHAR(12) PRIMARY KEY,
    dni CHAR(8) NOT NULL UNIQUE,
    nombres NVARCHAR(60) NOT NULL,
    apellidos NVARCHAR(60) NOT NULL,
    correo NVARCHAR(100) NOT NULL,
    activo BIT NOT NULL DEFAULT 1
);

CREATE TABLE dbo.Docente (
    codigo_docente VARCHAR(12) PRIMARY KEY,
    dni CHAR(8) NOT NULL UNIQUE,
    nombres NVARCHAR(60) NOT NULL,
    apellidos NVARCHAR(60) NOT NULL,
    correo NVARCHAR(100) NOT NULL
);

CREATE TABLE dbo.Curso (
    codigo_curso VARCHAR(10) PRIMARY KEY,
    nombre_curso NVARCHAR(100) NOT NULL,
    codigo_docente VARCHAR(12) NOT NULL,
    CONSTRAINT FK_Curso_Docente FOREIGN KEY (codigo_docente) REFERENCES dbo.Docente(codigo_docente)
);

CREATE TABLE dbo.Matricula (
    id_matricula INT PRIMARY KEY IDENTITY(1,1),
    codigo_universitario VARCHAR(12) NOT NULL,
    codigo_curso VARCHAR(10) NOT NULL,
    fecha_matricula DATE NOT NULL DEFAULT CAST(GETDATE() AS DATE),
    CONSTRAINT FK_Mat_Est FOREIGN KEY (codigo_universitario) REFERENCES dbo.Estudiante(codigo_universitario),
    CONSTRAINT FK_Mat_Cur FOREIGN KEY (codigo_curso) REFERENCES dbo.Curso(codigo_curso),
    CONSTRAINT UQ_Matricula_Unica UNIQUE (codigo_universitario, codigo_curso)
);

CREATE TABLE dbo.SesionClase (
    id_sesion VARCHAR(20) PRIMARY KEY,
    codigo_curso VARCHAR(10) NOT NULL,
    fecha DATE NOT NULL,
    CONSTRAINT FK_Ses_Cur FOREIGN KEY (codigo_curso) REFERENCES dbo.Curso(codigo_curso)
);

CREATE TABLE dbo.Asistencia (
    id_asistencia VARCHAR(20) PRIMARY KEY,
    id_sesion VARCHAR(20) NOT NULL,
    codigo_universitario VARCHAR(12) NOT NULL,
    estado VARCHAR(12) NOT NULL CHECK (estado IN ('ASISTIO', 'TARDANZA', 'FALTA')),
    CONSTRAINT FK_Asi_Ses FOREIGN KEY (id_sesion) REFERENCES dbo.SesionClase(id_sesion),
    CONSTRAINT FK_Asi_Est FOREIGN KEY (codigo_universitario) REFERENCES dbo.Estudiante(codigo_universitario),
    CONSTRAINT UQ_Asistencia_Unica UNIQUE (id_sesion, codigo_universitario)
);
GO

-- 2. PROCEDIMIENTOS ALMACENADOS (INVOCADOS DESDE LOS DAO CON CallableStatement)

-- ESTUDIANTES
CREATE PROCEDURE usp_CrearEstudiante
    @codigo_universitario VARCHAR(12),
    @dni CHAR(8),
    @nombres NVARCHAR(60),
    @apellidos NVARCHAR(60),
    @correo NVARCHAR(100)
AS
BEGIN
    INSERT INTO dbo.Estudiante (codigo_universitario, dni, nombres, apellidos, correo, activo)
    VALUES (@codigo_universitario, @dni, @nombres, @apellidos, @correo, 1);
END
GO

CREATE PROCEDURE usp_ListarEstudiantes
AS
BEGIN
    SELECT codigo_universitario, dni, nombres, apellidos, correo
    FROM dbo.Estudiante
    WHERE activo = 1;
END
GO

CREATE PROCEDURE usp_ActualizarEstudiante
    @codigo_universitario VARCHAR(12),
    @nombres NVARCHAR(60),
    @apellidos NVARCHAR(60),
    @correo NVARCHAR(100)
AS
BEGIN
    UPDATE dbo.Estudiante
    SET nombres = @nombres, apellidos = @apellidos, correo = @correo
    WHERE codigo_universitario = @codigo_universitario;
END
GO

CREATE PROCEDURE usp_EliminarEstudiante
    @codigo_universitario VARCHAR(12)
AS
BEGIN
    DELETE FROM dbo.Estudiante WHERE codigo_universitario = @codigo_universitario;
END
GO

-- DOCENTES
CREATE PROCEDURE usp_CrearDocente
    @codigo_docente VARCHAR(12),
    @dni CHAR(8),
    @nombres NVARCHAR(60),
    @apellidos NVARCHAR(60),
    @correo NVARCHAR(100)
AS
BEGIN
    INSERT INTO dbo.Docente (codigo_docente, dni, nombres, apellidos, correo)
    VALUES (@codigo_docente, @dni, @nombres, @apellidos, @correo);
END
GO

CREATE PROCEDURE usp_ListarDocentes
AS
BEGIN
    SELECT codigo_docente, dni, nombres, apellidos, correo FROM dbo.Docente;
END
GO

CREATE PROCEDURE usp_ActualizarDocente
    @codigo_docente VARCHAR(12),
    @nombres NVARCHAR(60),
    @apellidos NVARCHAR(60),
    @correo NVARCHAR(100)
AS
BEGIN
    UPDATE dbo.Docente
    SET nombres = @nombres, apellidos = @apellidos, correo = @correo
    WHERE codigo_docente = @codigo_docente;
END
GO

CREATE PROCEDURE usp_EliminarDocente
    @codigo_docente VARCHAR(12)
AS
BEGIN
    DELETE FROM dbo.Docente WHERE codigo_docente = @codigo_docente;
END
GO

-- CURSOS
CREATE PROCEDURE usp_CrearCurso
    @codigo_curso VARCHAR(10),
    @nombre_curso NVARCHAR(100),
    @codigo_docente VARCHAR(12)
AS
BEGIN
    INSERT INTO dbo.Curso (codigo_curso, nombre_curso, codigo_docente)
    VALUES (@codigo_curso, @nombre_curso, @codigo_docente);
END
GO

CREATE PROCEDURE usp_ListarCursos
AS
BEGIN
    SELECT c.codigo_curso, c.nombre_curso, c.codigo_docente,
           d.dni, d.nombres, d.apellidos, d.correo
    FROM dbo.Curso c
    INNER JOIN dbo.Docente d ON c.codigo_docente = d.codigo_docente;
END
GO

-- MATRICULAS
CREATE PROCEDURE usp_CrearMatricula
    @codigo_universitario VARCHAR(12),
    @codigo_curso VARCHAR(10),
    @fecha_matricula DATE
AS
BEGIN
    INSERT INTO dbo.Matricula (codigo_universitario, codigo_curso, fecha_matricula)
    VALUES (@codigo_universitario, @codigo_curso, @fecha_matricula);
END
GO

CREATE PROCEDURE usp_ListarMatriculas
AS
BEGIN
    SELECT codigo_universitario, codigo_curso, fecha_matricula FROM dbo.Matricula;
END
GO

-- SESIONES
CREATE PROCEDURE usp_CrearSesion
    @id_sesion VARCHAR(20),
    @codigo_curso VARCHAR(10),
    @fecha DATE
AS
BEGIN
    INSERT INTO dbo.SesionClase (id_sesion, codigo_curso, fecha)
    VALUES (@id_sesion, @codigo_curso, @fecha);
END
GO

CREATE PROCEDURE usp_ListarSesiones
AS
BEGIN
    SELECT id_sesion, codigo_curso, fecha FROM dbo.SesionClase;
END
GO

-- ASISTENCIAS
CREATE PROCEDURE usp_RegistrarAsistencia
    @id_asistencia VARCHAR(20),
    @id_sesion VARCHAR(20),
    @codigo_universitario VARCHAR(12),
    @estado VARCHAR(12)
AS
BEGIN
    INSERT INTO dbo.Asistencia (id_asistencia, id_sesion, codigo_universitario, estado)
    VALUES (@id_asistencia, @id_sesion, @codigo_universitario, @estado);
END
GO

CREATE PROCEDURE usp_ModificarAsistencia
    @id_sesion VARCHAR(20),
    @codigo_universitario VARCHAR(12),
    @estado VARCHAR(12)
AS
BEGIN
    UPDATE dbo.Asistencia
    SET estado = @estado
    WHERE id_sesion = @id_sesion AND codigo_universitario = @codigo_universitario;
END
GO

CREATE PROCEDURE usp_ListarAsistencias
AS
BEGIN
    SELECT id_asistencia, id_sesion, codigo_universitario, estado FROM dbo.Asistencia;
END
GO

