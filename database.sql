-- =============================================================================
-- ASSET MANAGEMENT - SCRIPT DE BASE DE DATOS POSTGRESQL (asset_management_db)
-- Puerto configurado por defecto: 5433 (o 5432) | Usuario: postgres
-- =============================================================================

-- 1. Crear base de datos (ejecutar en pgAdmin o psql si no existe aún):
-- CREATE DATABASE asset_management_db;

-- 2. Limpieza de tablas previas (en orden de dependencias)
DROP TABLE IF EXISTS asignaciones CASCADE;
DROP TABLE IF EXISTS activos CASCADE;
DROP TABLE IF EXISTS departamentos CASCADE;
DROP TABLE IF EXISTS categorias CASCADE;
DROP TABLE IF EXISTS usuarios CASCADE;

-- 3. Tabla: usuarios (Gestión de usuarios y autenticación JWT)
CREATE TABLE usuarios (
    id BIGSERIAL PRIMARY KEY,
    username VARCHAR(100) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    rol VARCHAR(50) NOT NULL DEFAULT 'USER',
    activo BOOLEAN NOT NULL DEFAULT TRUE,
    creado TIMESTAMP WITHOUT TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    modificado TIMESTAMP WITHOUT TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

-- 4. Tabla: categorias (Clasificación de activos fijos e intangibles)
CREATE TABLE categorias (
    id BIGSERIAL PRIMARY KEY,
    codigo VARCHAR(20) NOT NULL UNIQUE,
    nombre VARCHAR(100) NOT NULL UNIQUE,
    descripcion VARCHAR(255),
    vida_util_anios INT NOT NULL DEFAULT 5,
    tasa_depreciacion_anual NUMERIC(5, 2) NOT NULL DEFAULT 20.00,
    creado TIMESTAMP WITHOUT TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    modificado TIMESTAMP WITHOUT TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

-- 5. Tabla: departamentos (Áreas organizacionales con cupo de inversión en activos)
CREATE TABLE departamentos (
    id BIGSERIAL PRIMARY KEY,
    codigo VARCHAR(20) NOT NULL UNIQUE,
    nombre VARCHAR(100) NOT NULL UNIQUE,
    edificio VARCHAR(100),
    piso VARCHAR(50),
    presupuesto_maximo NUMERIC(14, 2) NOT NULL DEFAULT 50000.00,
    valor_actual_activos NUMERIC(14, 2) NOT NULL DEFAULT 0.00,
    creado TIMESTAMP WITHOUT TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    modificado TIMESTAMP WITHOUT TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

-- 6. Tabla: activos (Inventario central de activos)
CREATE TABLE activos (
    id BIGSERIAL PRIMARY KEY,
    codigo_inventario VARCHAR(50) NOT NULL UNIQUE,
    nombre VARCHAR(150) NOT NULL,
    marca VARCHAR(100),
    modelo VARCHAR(100),
    numero_serie VARCHAR(100) NOT NULL UNIQUE,
    costo_adquisicion NUMERIC(14, 2) NOT NULL,
    moneda VARCHAR(10) NOT NULL DEFAULT 'USD',
    estado VARCHAR(30) NOT NULL DEFAULT 'DISPONIBLE', -- DISPONIBLE, ASIGNADO, EN_MANTENIMIENTO, DE_BAJA
    fecha_adquisicion DATE NOT NULL,
    fin_garantia DATE,
    categoria_id BIGINT NOT NULL,
    departamento_id BIGINT NOT NULL,
    creado TIMESTAMP WITHOUT TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    modificado TIMESTAMP WITHOUT TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_activo_categoria FOREIGN KEY (categoria_id) REFERENCES categorias(id) ON DELETE RESTRICT,
    CONSTRAINT fk_activo_departamento FOREIGN KEY (departamento_id) REFERENCES departamentos(id) ON DELETE RESTRICT
);

-- 7. Tabla: asignaciones (Custodia y préstamo de activos a empleados/usuarios)
CREATE TABLE asignaciones (
    id BIGSERIAL PRIMARY KEY,
    activo_id BIGINT NOT NULL,
    usuario_id BIGINT NOT NULL,
    fecha_asignacion DATE NOT NULL,
    fecha_devolucion DATE,
    estado_asignacion VARCHAR(30) NOT NULL DEFAULT 'ACTIVA', -- ACTIVA, DEVUELTO
    observaciones TEXT,
    creado TIMESTAMP WITHOUT TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    modificado TIMESTAMP WITHOUT TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_asignacion_activo FOREIGN KEY (activo_id) REFERENCES activos(id) ON DELETE RESTRICT,
    CONSTRAINT fk_asignacion_usuario FOREIGN KEY (usuario_id) REFERENCES usuarios(id) ON DELETE RESTRICT
);

-- =============================================================================
-- 8. DATOS DE PRUEBA INICIALES (SEED DATA)
-- =============================================================================

-- Usuarios iniciales (password BCrypt de 'admin123')
INSERT INTO usuarios (username, password, rol, activo) VALUES
('admin', '$2a$10$CpkoQnPZgLWaqjEyBPSB1uxiKeP8WJgt5c4VMHMxTGt0.9q23FTWq', 'ADMIN', true),
('operador', '$2a$10$CpkoQnPZgLWaqjEyBPSB1uxiKeP8WJgt5c4VMHMxTGt0.9q23FTWq', 'USER', true),
('juan.perez', '$2a$10$CpkoQnPZgLWaqjEyBPSB1uxiKeP8WJgt5c4VMHMxTGt0.9q23FTWq', 'USER', true)
ON CONFLICT (username) DO UPDATE 
SET password = EXCLUDED.password, rol = EXCLUDED.rol;

-- Categorías
INSERT INTO categorias (codigo, nombre, descripcion, vida_util_anios, tasa_depreciacion_anual) VALUES
('COMP', 'Equipos de Computo', 'Laptops, computadoras de escritorio, workstations', 3, 33.33),
('SERV', 'Servidores y Redes', 'Servidores rack, switches, routers, firewalls', 5, 20.00),
('MOBI', 'Mobiliario y Enseres', 'Escritorios, sillas ergonomicas, estanterias', 10, 10.00),
('VEHI', 'Vehiculos Operativos', 'Camionetas de logistica, motocicletas de entrega', 5, 20.00);

-- Departamentos
INSERT INTO departamentos (codigo, nombre, edificio, piso, presupuesto_maximo, valor_actual_activos) VALUES
('TI-01', 'Tecnologia de la Informacion', 'Edificio Central', 'Piso 3', 35000.00, 4300.00),
('FIN-01', 'Finanzas y Contabilidad', 'Edificio Central', 'Piso 2', 20000.00, 1500.00),
('LOG-01', 'Logistica y Operaciones', 'Edificio Operativo', 'Planta Baja', 40000.00, 0.00);

-- Activos
INSERT INTO activos (codigo_inventario, nombre, marca, modelo, numero_serie, costo_adquisicion, moneda, estado, fecha_adquisicion, fin_garantia, categoria_id, departamento_id) VALUES
('ACT-COMP-001', 'Laptop Dell Precision 5570', 'Dell', 'Precision 5570', 'SN-DELL-883921', 2500.00, 'USD', 'ASIGNADO', '2026-01-15', '2029-01-15', 1, 1),
('ACT-COMP-002', 'MacBook Pro 16 M3 Max', 'Apple', 'MacBook Pro 16', 'SN-APPL-992102', 3200.00, 'USD', 'DISPONIBLE', '2026-02-01', '2028-02-01', 1, 1),
('ACT-SERV-001', 'Servidor PowerEdge R750', 'Dell', 'PowerEdge R750', 'SN-RACK-004412', 4300.00, 'USD', 'DISPONIBLE', '2026-01-10', '2031-01-10', 2, 1),
('ACT-MOBI-001', 'Silla Ergonomica Aeron', 'Herman Miller', 'Aeron Remastered', 'SN-HM-331299', 1500.00, 'USD', 'DISPONIBLE', '2026-01-20', '2036-01-20', 3, 2);

-- Asignaciones
INSERT INTO asignaciones (activo_id, usuario_id, fecha_asignacion, estado_asignacion, observaciones) VALUES
(1, 3, '2026-01-20', 'ACTIVA', 'Entrega de equipo de computo principal para desarrollador senior.');
