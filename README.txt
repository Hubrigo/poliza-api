# Poliza API – Prueba Técnica

API REST desarrollada con **Spring Boot** para la gestión de **pólizas y riesgos**, implementando las reglas de negocio definidas en la prueba técnica.

La solución implementa una arquitectura por capas, seguridad mínima mediante API Key y un mock de integración con un sistema CORE externo.

---

# Tecnologías utilizadas

* Java 17
* Spring Boot
* Spring Web
* Spring Data JPA
* H2 Database
* Lombok
* RestTemplate
* Maven

---

# Arquitectura del proyecto

El proyecto está organizado en capas para mantener una separación clara de responsabilidades.

```
src/main/java/com/hugo/poliza_api

controller   → endpoints REST
service      → lógica de negocio
repository   → acceso a base de datos
model        → entidades del dominio
dto          → objetos de transferencia
config       → configuración (API key, RestTemplate)
exception    → manejo global de errores
```

---

# Seguridad mínima

La API implementa un **header obligatorio** para consumir los servicios:

```
x-api-key: 123456
```

Si el header no está presente o es incorrecto, la API responde:

```
401 Unauthorized
```

Esto se implementa mediante un **filtro global (`ApiKeyFilter`)**.

---

# Base de datos

Se utiliza **H2 en memoria**.

Los datos iniciales se cargan automáticamente mediante:

```
src/main/resources/data.sql
```

Datos iniciales:

| Poliza | Tipo       | Estado |
| ------ | ---------- | ------ |
| 1      | COLECTIVA  | ACTIVA |
| 2      | INDIVIDUAL | ACTIVA |

Riesgos iniciales asociados.

---

# Manejo de errores

Se implementó un **GlobalExceptionHandler** para devolver errores controlados.

Ejemplo de respuesta:

```json
{
  "timestamp": "2026-03-07T14:10:00",
  "status": 400,
  "error": "Business Error",
  "message": "Solo las pólizas colectivas permiten agregar riesgos"
}
```

Esto evita exponer stacktraces internos de la aplicación.

---

# Integración con CORE (Mock)

Se implementa un mock del sistema CORE mediante el endpoint:

```
POST /core-mock/evento
```

Cada operación relevante (renovación, cancelación, creación de riesgo) registra un evento enviado al CORE.

Ejemplo en logs:

```
Evento enviado al CORE -> evento: ACTUALIZACION, polizaId: 1
```

---

# Reglas de negocio implementadas

La API implementa las siguientes reglas:

### 1. Una póliza individual solo puede tener un riesgo

Esto se garantiza restringiendo la creación de riesgos únicamente para pólizas colectivas.

---

### 2. Solo pólizas colectivas pueden agregar riesgos

Endpoint:

```
POST /polizas/{id}/riesgos
```

Validación implementada en `PolizaService`.

---

### 3. No se puede renovar una póliza cancelada

Si se intenta renovar una póliza cancelada se retorna:

```
400 Bad Request
```

---

### 4. Cancelar una póliza cancela todos sus riesgos

Al ejecutar:

```
POST /polizas/{id}/cancelar
```

todos los riesgos asociados cambian su estado a:

```
CANCELADO
```

---

### 5. Cancelación individual de riesgos

Endpoint:

```
POST /riesgos/{id}/cancelar
```

Permite cancelar un riesgo específico.

---

# Cómo ejecutar el proyecto

1. Clonar repositorio

```
git clone https://github.com/usuario/poliza-api.git
```

2. Entrar al proyecto

```
cd poliza-api
```

3. Ejecutar la aplicación

```
mvn spring-boot:run
```

La API quedará disponible en:

```
http://localhost:8080
```

---

# Colección de pruebas (Postman)

Los endpoints fueron probados mediante la siguiente secuencia:

## 01 - GET polizas sin API key

```
GET /polizas
```

Resultado esperado:

```
401 Unauthorized
```

---

## 02 - GET polizas

```
GET /polizas
```

Header requerido:

```
x-api-key: 123456
```

---

## 03 - GET polizas colectivas

```
GET /polizas?tipo=COLECTIVA
```

---

## 04 - GET polizas activas

```
GET /polizas?estado=ACTIVA
```

---

## 05 - GET riesgos por póliza

```
GET /polizas/1/riesgos
```

---

## 06 - POST agregar riesgo a póliza colectiva

```
POST /polizas/1/riesgos
```

Body:

```json
{
  "descripcion": "Bodega 402"
}
```

Resultado esperado:

Riesgo creado correctamente.

---

## 07 - POST agregar riesgo a póliza individual (error)

```
POST /polizas/2/riesgos
```

Resultado esperado:

```
400 Bad Request
Solo las pólizas colectivas permiten agregar riesgos
```

---

## 08 - POST renovar póliza

```
POST /polizas/1/renovar
```

Resultado esperado:

Estado cambia a:

```
RENOVADA
```

---

## 09 - POST cancelar póliza individual

```
POST /polizas/2/cancelar
```

---

## 10 - POST renovar póliza cancelada (error)

```
POST /polizas/2/renovar
```

Resultado esperado:

```
400 Bad Request
No se puede renovar una póliza cancelada
```

---

## 11 - POST cancelar póliza colectiva

```
POST /polizas/1/cancelar
```

---

## 12 - GET riesgos cancelados

```
GET /polizas/1/riesgos
```

Resultado esperado:

Todos los riesgos quedan en estado:

```
CANCELADO
```

---

## 13 - POST cancelar riesgo

```
POST /riesgos/1/cancelar
```

---

## 14 - POST mock CORE

```
POST /core-mock/evento
```

Body:

```json
{
  "evento": "ACTUALIZACION",
  "polizaId": 1
}
```

---

# Autor
Hugo Gabriel Salcedo Silva