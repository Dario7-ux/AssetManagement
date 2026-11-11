# 🚀 Guía Completa para Probar AssetManagement con Postman

Esta guía detalla paso a paso cómo probar cada uno de los endpoints de **AssetManagement** aplicando **Arquitectura Hexagonal**, seguridad **JWT (Bearer Token)** y las **Reglas de Negocio** de inventario, asignaciones, multidivisa y cupo departamental.

---

## 1. Pasos Previos y Puesta en Marcha

### Paso 1.1: Base de Datos PostgreSQL
Asegúrate de tener corriendo PostgreSQL y haber ejecutado el script [`database.sql`](database.sql) en la base de datos `asset_management_db`:
```sql
CREATE DATABASE asset_management_db;
```
Ejecuta el contenido del archivo [`database.sql`](database.sql) en pgAdmin, DBeaver o CLI `psql`.

### Paso 1.2: Iniciar la Aplicación Spring Boot
Inicia la aplicación en Windows PowerShell:
```powershell
$env:JAVA_HOME = "C:\Program Files\Java\jdk-25.0.3"
.\mvnw.cmd spring-boot:run
```
Verifica en consola que Tomcat arranque en el puerto `8080`:
```text
Tomcat started on port 8080 (http) with context path '/'
AssetManagementApplication started in ... seconds
```

---

## 2. Configuración en Postman

1. **Host y Puerto por Defecto:**
   * La API corre en: `http://localhost:8080`
   * Todas las peticiones van directamente a `http://localhost:8080/api/v1/...`
2. **Encabezados Globales:**
   * `Content-Type`: `application/json`
3. **Peticiones Protegidas:**
   * En la pestaña **Authorization**, selecciona **Type:** `Bearer Token`.
   * En el campo **Token**, coloca `{{token}}` (o pega directamente el token JWT obtenido en el login).

---

## 3. Flujo de Pruebas Paso a Paso

---

### 1️⃣ Autenticación: Login de Administrador
* **Método:** `POST`
* **URL:** `http://localhost:8080/api/v1/auth/login`
* **Headers:** `Content-Type: application/json`
* **Body (raw JSON):**
```json
{
  "username": "admin",
  "password": "admin123"
}
```
* **Script Automático de Postman (Pestaña "Scripts -> Post-response"):**
```javascript
const res = pm.response.json();
if (res.results && res.results.token) {
    pm.environment.set("token", res.results.token);
    console.log("Token JWT guardado exitosamente en {{token}}");
}
```
* **Respuesta Esperada (200 OK):**
```json
{
  "success": true,
  "message": "Autenticacion exitosa",
  "results": {
    "token": "eyJhbGciOiJIUzI1NiJ9...",
    "tipo": "Bearer",
    "username": "admin",
    "rol": "ADMIN"
  }
}
```

---

### 2️⃣ Departamentos: Consultar Departamentos y Presupuesto
* **Método:** `GET`
* **URL:** `http://localhost:8080/api/v1/departamentos`
* **Auth:** Bearer Token `{{token}}`
* **Respuesta Esperada (200 OK):**
```json
{
  "success": true,
  "message": "Departamentos obtenidos exitosamente",
  "results": [
    {
      "id": 1,
      "codigo": "TI-01",
      "nombre": "Tecnologia de la Informacion",
      "edificio": "Edificio Central",
      "piso": "Piso 3",
      "presupuestoMaximo": 35000.00,
      "valorActualActivos": 4300.00,
      "presupuestoDisponible": 30700.00
    }
  ]
}
```

---

### 3️⃣ Departamentos: Registrar Nuevo Departamento
* **Método:** `POST`
* **URL:** `http://localhost:8080/api/v1/departamentos`
* **Auth:** Bearer Token `{{token}}`
* **Body (raw JSON):**
```json
{
  "codigo": "DES-01",
  "nombre": "I+D e Innovacion",
  "edificio": "Torre B",
  "piso": "Piso 4",
  "presupuestoMaximo": 45000.00
}
```
* **Respuesta Esperada (201 Created):**
```json
{
  "success": true,
  "message": "Departamento registrado exitosamente",
  "results": {
    "id": 4,
    "codigo": "DES-01",
    "nombre": "I+D e Innovacion",
    "presupuestoMaximo": 45000.00,
    "valorActualActivos": 0.00,
    "presupuestoDisponible": 45000.00
  }
}
```

---

### 4️⃣ Categorías: Registrar Nueva Categoría
* **Método:** `POST`
* **URL:** `http://localhost:8080/api/v1/categorias`
* **Auth:** Bearer Token `{{token}}`
* **Body (raw JSON):**
```json
{
  "codigo": "AUD-01",
  "nombre": "Equipos Audiovisuales",
  "descripcion": "Proyectores laser, microfonos de sala y camaras de conferencia",
  "vidaUtilAnios": 4,
  "tasaDepreciacionAnual": 25.00
}
```
* **Respuesta Esperada (201 Created):**
```json
{
  "success": true,
  "message": "Categoria registrada exitosamente",
  "results": {
    "id": 5,
    "codigo": "AUD-01",
    "nombre": "Equipos Audiovisuales",
    "vidaUtilAnios": 4,
    "tasaDepreciacionAnual": 25.00
  }
}
```

---

### 5️⃣ Activos: Registrar Nuevo Activo (Éxito en USD)
* **Método:** `POST`
* **URL:** `http://localhost:8080/api/v1/activos`
* **Auth:** Bearer Token `{{token}}`
* **Body (raw JSON):**
```json
{
  "codigoInventario": "ACT-COMP-003",
  "nombre": "Lenovo ThinkPad X1 Carbon Gen 11",
  "marca": "Lenovo",
  "modelo": "X1 Carbon Gen 11",
  "numeroSerie": "SN-LNV-2026-X1C",
  "costoAdquisicion": 1850.00,
  "moneda": "USD",
  "fechaAdquisicion": "2026-03-01",
  "finGarantia": "2029-03-01",
  "categoriaId": 1,
  "departamentoId": 1
}
```
* **Respuesta Esperada (201 Created):**
```json
{
  "success": true,
  "message": "Activo registrado exitosamente en el inventario",
  "results": {
    "id": 5,
    "codigoInventario": "ACT-COMP-003",
    "nombre": "Lenovo ThinkPad X1 Carbon Gen 11",
    "costoAdquisicion": 1850.00,
    "moneda": "USD",
    "estado": "DISPONIBLE",
    "categoria": {
      "id": 1,
      "nombre": "Equipos de Computo"
    },
    "departamento": {
      "id": 1,
      "nombre": "Tecnologia de la Informacion"
    }
  }
}
```

---

### 6️⃣ Activos: Registro con Conversión Multidivisa (EUR a USD)
* **Método:** `POST`
* **URL:** `http://localhost:8080/api/v1/activos`
* **Auth:** Bearer Token `{{token}}`
* **Body (raw JSON):**
```json
{
  "codigoInventario": "ACT-SERV-002",
  "nombre": "Switch Cisco Catalyst 9300",
  "marca": "Cisco",
  "modelo": "Catalyst 9300",
  "numeroSerie": "SN-CSCO-9300-EU",
  "costoAdquisicion": 1500.00,
  "moneda": "EUR",
  "fechaAdquisicion": "2026-03-02",
  "finGarantia": "2031-03-02",
  "categoriaId": 2,
  "departamentoId": 1
}
```
* *El sistema consulta la tasa en tiempo real y convierte el costo de EUR a USD en el balance.*

---

### 7️⃣ Activos: Listar Inventario de Activos (con Filtros)
* **Método:** `GET`
* **URL:** `http://localhost:8080/api/v1/activos` (o `http://localhost:8080/api/v1/activos?estado=DISPONIBLE`)
* **Auth:** Bearer Token `{{token}}`
* **Respuesta Esperada (200 OK):**
```json
{
  "success": true,
  "message": "Inventario de activos obtenido",
  "results": [
    {
      "id": 2,
      "codigoInventario": "ACT-COMP-002",
      "nombre": "MacBook Pro 16 M3 Max",
      "estado": "DISPONIBLE"
    }
  ]
}
```

---

### 8️⃣ Asignaciones: Asignar Activo a Custodio
* **Método:** `POST`
* **URL:** `http://localhost:8080/api/v1/asignaciones`
* **Auth:** Bearer Token `{{token}}`
* **Body (raw JSON):**
```json
{
  "activoId": 2,
  "usuarioId": 3,
  "fechaAsignacion": "2026-03-05",
  "observaciones": "Entrega de portatil para proyectos de arquitectura de software."
}
```
* **Respuesta Esperada (201 Created):**
```json
{
  "success": true,
  "message": "Activo asignado exitosamente al custodio",
  "results": {
    "id": 2,
    "activo": {
      "id": 2,
      "codigoInventario": "ACT-COMP-002",
      "estado": "ASIGNADO"
    },
    "usuario": {
      "id": 3,
      "username": "juan.perez"
    },
    "estadoAsignacion": "ACTIVA"
  }
}
```

---

### 9️⃣ Asignaciones: Registrar Devolución de Activo
* **Método:** `PUT`
* **URL:** `http://localhost:8080/api/v1/asignaciones/2/devolver`
* **Auth:** Bearer Token `{{token}}`
* **Body (raw JSON):**
```json
{
  "fechaDevolucion": "2026-03-10",
  "observaciones": "Equipo retornado en perfecto estado fisico y funcional tras finalizacion del proyecto."
}
```
* **Respuesta Esperada (200 OK):**
```json
{
  "success": true,
  "message": "Devolucion procesada exitosamente. Activo disponible nuevamente",
  "results": {
    "id": 2,
    "estadoAsignacion": "DEVUELTO",
    "activo": {
      "id": 2,
      "estado": "DISPONIBLE"
    }
  }
}
```

---

### 🔟 Activos: Cambiar Estado (Mantenimiento / Baja)
* **Método:** `PUT`
* **URL:** `http://localhost:8080/api/v1/activos/2/estado`
* **Auth:** Bearer Token `{{token}}`
* **Body (raw JSON):**
```json
{
  "estado": "EN_MANTENIMIENTO"
}
```
* **Respuesta Esperada (200 OK):**
```json
{
  "success": true,
  "message": "Estado del activo actualizado a EN_MANTENIMIENTO",
  "results": {
    "id": 2,
    "estado": "EN_MANTENIMIENTO"
  }
}
```

---

## 🛑 4. Pruebas de Reglas de Negocio (Escenarios de Excepción)

### Prueba de Error 1: Asignar Activo No Disponible
* **Método:** `POST`
* **URL:** `http://localhost:8080/api/v1/asignaciones`
* **Body (raw JSON):**
```json
{
  "activoId": 2,
  "usuarioId": 3,
  "observaciones": "Intento de asignacion cuando esta en mantenimiento"
}
```
* **Respuesta Esperada (400 Bad Request):**
```json
{
  "success": false,
  "message": "El activo 'MacBook Pro 16 M3 Max' (ACT-COMP-002) no se puede asignar. Estado actual: EN_MANTENIMIENTO",
  "results": null
}
```

---

### Prueba de Error 2: Exceder Cupo Presupuestario Departamental
* **Método:** `POST`
* **URL:** `http://localhost:8080/api/v1/activos`
* **Body (raw JSON):**
```json
{
  "codigoInventario": "ACT-SUPER-001",
  "nombre": "Cluster Supercomputo AI",
  "marca": "Nvidia",
  "modelo": "DGX H100",
  "numeroSerie": "SN-NV-DGX-9999",
  "costoAdquisicion": 100000.00,
  "moneda": "USD",
  "fechaAdquisicion": "2026-03-01",
  "categoriaId": 2,
  "departamentoId": 1
}
```
* **Respuesta Esperada (400 Bad Request):**
```json
{
  "success": false,
  "message": "El departamento 'Tecnologia de la Informacion' ha excedido su presupuesto maximo de activos. Limite: $35000.00, Total con nuevo activo: $104300.00",
  "results": null
}
```
* *Adicionalmente, se registra en logs la notificación crítica de alerta de cupo excedido.*

---

### Prueba de Error 3: Intento de Registro sin Permisos de Administrador
* Inicia sesión con el usuario `operador` (Rol `USER`).
* Intenta hacer `POST` a `http://localhost:8080/api/v1/activos` con el token del operador.
* **Respuesta Esperada (403 Forbidden):**
```json
{
  "success": false,
  "message": "Acceso denegado: permisos insuficientes para esta operacion",
  "results": null
}
```
