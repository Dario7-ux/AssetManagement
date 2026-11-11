# 🏢 Sistema de Gestión de Activos (AssetManagement)

## 🎯 Propósito del Sistema

El sistema **AssetManagement** permite a una organización controlar y auditar el ciclo de vida completo de sus activos fijos y tecnológicos (equipos de cómputo, servidores, mobiliario, maquinaria, licencias). Administra desde su adquisición (con soporte multidivisa), categorización y ubicación física o departamental, hasta su asignación a empleados/custodios, mantenimientos y bajas definitivas.

---

## 🏗️ 1. Módulos y Dominio de AssetManagement

Definición de componentes y responsabilidades del negocio de gestión de activos:

| Módulo | Componente Principal | Propósito en el Sistema |
| :--- | :--- | :--- |
| **Activos** | **Activo (Asset)** | Representa el bien material/tecnológico con código único, costo, moneda, fecha y estado. |
| **Departamentos** | **Departamento / Cupo Presupuestario** | Límite máximo de inversión o activos asignables a un área/departamento antes de emitir alerta. |
| **Categorías** | **Categoría de Activo** | Clasificación (ej. Cómputo, Mobiliario, Redes, Vehículos) con vida útil y tasa de depreciación. |
| **Ubicaciones** | **Ubicación / Departamento** | Lugar físico o área responsable (ej. Depto TI - Piso 3, Sucursal Centro). |
| **Custodia** | **Asignación / Custodia** | Registro de préstamo/entrega del activo a un usuario/empleado con actas de entrega/devolución. |
| **Multidivisa** | **TipoCambioPort (HTTP)** | Normalización a USD de activos comprados en EUR u otra divisa vía API externa. |
| **Notificaciones** | **NotificacionPort (Email)** | Alertas por asignación, garantía vencida o superación de cupo departamental. |
| **Seguridad** | **Seguridad JWT + Usuarios** | Control de acceso por roles (`ADMIN`, `OPERADOR`, `USER`). |

---

## 🗄️ 2. Modelo de Base de Datos y Estados (PostgreSQL)

La base de datos se denomina **`asset_management_db`**.

### 📌 Ciclo de Vida y Estados del Activo (`EstadoActivo`)

* **`DISPONIBLE`**: El activo está en inventario listo para ser asignado.
* **`ASIGNADO`**: Está bajo la custodia de un usuario o departamento.
* **`EN_MANTENIMIENTO`**: En reparación técnica o calibración (no disponible para asignación).
* **`DE_BAJA`**: Retirado del inventario por obsolescencia, daño irreparable o pérdida.

---

## 🏛️ 3. Arquitectura Hexagonal (Clean Architecture)

**Paquete Base**: `org.example.assets`

```text
src/main/java/org/example/assets/
├── domain/                               # NÚCLEO DE NEGOCIO (Sin Spring/JPA)
│   ├── models/
│   │   ├── Activo.java                   # Entidad con lógica de ciclo de vida
│   │   ├── CategoriaActivo.java          # Criterios de depreciación y clasificación
│   │   ├── Departamento.java             # Control de cupo y ubicación
│   │   ├── Asignacion.java               # Vínculo custodio - activo
│   │   ├── EstadoActivo.java             # Enum: DISPONIBLE, ASIGNADO, EN_MANTENIMIENTO, DE_BAJA
│   │   └── Usuario.java y Rol.java       # Control de accesos
│   └── exception/
│       ├── ActivoNoDisponibleException.java
│       ├── CupoDepartamentoExcedidoException.java
│       ├── RecursoNoEncontradoException.java
│       └── ReglaNegocioException.java
│
├── application/                          # CASOS DE USO Y PUERTOS
│   ├── ports/
│   │   ├── in/                           # Entrada (Drivers)
│   │   │   ├── RegistrarActivoUseCase.java
│   │   │   ├── AsignarActivoUseCase.java
│   │   │   ├── CambiarEstadoActivoUseCase.java
│   │   │   └── UsuarioIn.java
│   │   └── out/                          # Salida (Driven)
│   │       ├── ActivoRepositoryPort.java
│   │       ├── CategoriaRepositoryPort.java
│   │       ├── DepartamentoRepositoryPort.java
│   │       ├── AsignacionRepositoryPort.java
│   │       ├── TipoCambioPort.java
│   │       ├── NotificacionPort.java
│   │       └── UsuarioOut.java
│   └── services/                         # Implementación de Casos de Uso
│       ├── ActivoService.java
│       ├── AsignacionService.java
│       ├── DepartamentoService.java
│       └── UsuarioService.java
│
└── infrastructure/                       # ADAPTADORES EXTERNOS
    ├── controllers/                      # REST API Endpoints con OpenAPI / Swagger
    │   ├── ActivoController.java
    │   ├── AsignacionController.java
    │   ├── CategoriaController.java
    │   ├── DepartamentoController.java
    │   └── AuthController.java
    ├── entities/                         # JPA Entities (@Table, @Column, @ManyToOne)
    ├── mappers/                          # Conversores Domain Model <-> Entity <-> DTO
    ├── repository/                       # Spring Data JPA + Adapters
    ├── adapters/                         # TipoCambioHttpAdapter, NotificacionEmailAdapter
    ├── security/                         # Spring Security 6, JWTFilter, PasswordEncoder
    └── config/                           # BeanConfig, OpenApiConfig, UsuarioInicial
```

---

## ⚡ 4. Reglas de Negocio Clave

1. **Invariante de Disponibilidad para Asignación**:
   * Un activo solo puede asignarse si su estado es `DISPONIBLE`. Si se encuentra `ASIGNADO`, `EN_MANTENIMIENTO` o `DE_BAJA`, el dominio lanza `ActivoNoDisponibleException`.
2. **Control de Presupuesto / Cupo Departamental**:
   * Al agregar un activo a un departamento, se evalúa si el costo total acumulado supera el `presupuesto_maximo` del departamento. Si se supera, se dispara una alerta vía `NotificacionPort`.
3. **Conversión Multidivisa en Adquisición**:
   * Si el activo se cotiza en EUR, GBP u otra divisa, el `TipoCambioPort` consulta la API REST externa y normaliza el costo a USD para el balance consolidado.
4. **Notificaciones de Eventos Críticos**:
   * Generación automática de constancias de entrega/devolución cuando un activo es asignado o ingresa a mantenimiento.

---

## 🔌 5. Catálogo de Endpoints REST (API v1)

| Módulo | Método | Endpoint | Rol Requerido | Descripción |
| :--- | :--- | :--- | :--- | :--- |
| **Auth** | `POST` | `/api/v1/auth/login` | Público | Autenticación y generación de token JWT. |
| **Activos** | `POST` | `/api/v1/activos` | `ADMIN` | Alta de nuevo activo con conversión de moneda. |
| **Activos** | `GET` | `/api/v1/activos` | `ADMIN`, `USER` | Listado general con filtros (estado, categoría). |
| **Activos** | `GET` | `/api/v1/activos/{id}` | `ADMIN`, `USER` | Detalle completo de un activo. |
| **Activos** | `PUT` | `/api/v1/activos/{id}/estado` | `ADMIN` | Cambiar estado (`DISPONIBLE`, `MANTENIMIENTO`, `BAJA`). |
| **Asignaciones** | `POST` | `/api/v1/asignaciones` | `ADMIN` | Asignar activo a custodio (valida estado `DISPONIBLE`). |
| **Asignaciones** | `PUT` | `/api/v1/asignaciones/{id}/devolver` | `ADMIN` | Devuelve el activo (cambia a `DISPONIBLE`). |
| **Asignaciones** | `GET` | `/api/v1/asignaciones` | `ADMIN`, `USER` | Historial de asignaciones. |
| **Departamentos** | `GET` / `POST` | `/api/v1/departamentos` | `ADMIN` | Gestión de áreas físicas y presupuestos. |
| **Categorías** | `GET` / `POST` | `/api/v1/categorias` | `ADMIN` (`GET` accesible a ambos) | Categorías de activos y vidas útiles. |

---

## 📋 6. Plan de Ejecución

1. **Base de Datos (`database.sql`)**: Crear el DDL con las tablas `usuarios`, `categorias`, `departamentos`, `activos`, `asignaciones`, constraints y datos de prueba.
2. **Dominio**: Definir los modelos puros y las excepciones de dominio.
3. **Puertos y Servicios**: Implementar los casos de uso (`RegistrarActivoUseCase`, `AsignarActivoUseCase`, etc.).
4. **Infraestructura y Persistencia**: Implementar entidades JPA, repositorios, mappers y adaptadores HTTP/Email.
5. **Controladores REST y DTOs**: Exponer los endpoints con OpenAPI y `@Valid`.
6. **Seguridad y Configuración**: Configuración de Beans, Spring Security 6 y JWT.
7. **Documentación y Pruebas**: Crear tests unitarios del dominio (`ActivoDomainTest`) y guías para Postman.