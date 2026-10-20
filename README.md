# Streaming API

API REST backend para gestionar la venta de cuentas de streaming: cuentas, plataformas, planes, membresías, órdenes y pagos, con autenticación JWT stateless.

## Tabla de contenidos

- [Funcionalidades](#funcionalidades)
- [Stack tecnológico](#stack-tecnológico)
- [Arquitectura](#arquitectura)
- [Requisitos](#requisitos)
- [Configuración](#configuración)
- [Ejecución local](#ejecución-local)
- [Despliegue con Docker](#despliegue-con-docker)
- [Documentación de la API](#documentación-de-la-api)
- [Endpoints](#endpoints)
- [Pruebas](#pruebas)

## Funcionalidades

- Gestión de cuentas, plataformas, planes (configuración de membresías), membresías, órdenes y pagos.
- Dashboard con métricas: totales, cuentas por plataforma, membresías y órdenes próximas a vencer.
- Gestión de tareas programadas (cron tasks): consulta y activación/desactivación.
- Identidad y autenticación mediante JWT (sesiones stateless).
- Paginación y filtros dinámicos por campo y criterio.

## Stack tecnológico

| Componente | Tecnología |
| --- | --- |
| Lenguaje | Java 25 |
| Framework | Spring Boot 4.0.6 (Web MVC, Data JPA, Security) |
| Base de datos | MySQL (H2 en pruebas) |
| Autenticación | JWT (jjwt 0.12.6) |
| Mapeo de objetos | MapStruct 1.6.3 |
| Documentación | springdoc-openapi (Swagger UI) |
| Build | Maven (wrapper incluido) |

## Arquitectura

El proyecto sigue **Clean Architecture** combinada con **DDD (Domain-Driven Design)** para aislar el dominio (core) de los detalles del framework y lograr mantenibilidad y escalabilidad.

- Principios **SOLID**.
- Patrones: **Factory** (creación de objetos de instancia única), **Strategy** (filtros extensibles) y **CQRS** (consultas sobre tablas cruzadas sin acoplar repositorios).

> **Trade-off:** al ser un sistema pequeño, esta arquitectura genera bastante boilerplate (mapeos y DTOs entre capas). Se eligió deliberadamente para practicar cómo se ve un desarrollo con este enfoque a esta escala.

## Requisitos

- JDK 25
- MySQL 8+ (o Docker)
- Maven no es necesario: se usa `./mvnw`

## Configuración

La aplicación se configura por variables de entorno (o `application.properties`):

| Variable | Descripción | Ejemplo |
| --- | --- | --- |
| `SPRING_DATASOURCE_URL` | URL JDBC de MySQL | `jdbc:mysql://localhost:3306/streaming` (valor por defecto) |
| `SPRING_DATASOURCE_USERNAME` | Usuario de BD | `streaming_user` |
| `SPRING_DATASOURCE_PASSWORD` | Contraseña de BD | — |
| `SPRING_JPA_HIBERNATE_DDL_AUTO` | Estrategia de esquema (`update` en desarrollo) | `update` |

> TODO: documentar aquí la clave de firma JWT y su expiración.

Cualquier propiedad de `application.yml` se puede sobreescribir con variables de entorno (por ejemplo, `spring.datasource.url` → `SPRING_DATASOURCE_URL`).

## Ejecución local

```bash
git clone https://github.com/BienavNet/streaming.git
cd streaming
./mvnw spring-boot:run
```

La API queda disponible en `http://localhost:8080`.

## Despliegue con Docker

1. Copia las variables de entorno y edítalas:

   ```bash
   cp .env.example .env
   ```

2. Construye y levanta la API junto con MySQL:

   ```bash
   docker compose up -d --build
   ```

3. Verifica en `http://localhost:8080/docs`.

Para detener todo: `docker compose down` (agrega `-v` para borrar también los datos de la BD).

Solo la imagen de la API:

```bash
docker build -t streaming-api .
docker run -p 8080:8080 --env-file .env streaming-api
```

## Documentación de la API

Swagger UI: `/docs`

## Endpoints

Todas las respuestas (salvo `204`) usan el formato `{ id, status, message, data }`.

| Recurso | Base | Operaciones |
| --- | --- | --- |
| Cuentas | `/api/account` | `POST /`, `GET /{id}`, `PUT /{id}`, `DELETE /{id}`, `GET /details/{page}/{offset}`, `GET /filter/{field}/{criteria}` |
| Plataformas | `/api/platform` | `POST /`, `GET /`, `GET /{id}`, `PUT /{id}`, `DELETE /{id}`, `GET /filter/{field}/{criteria}` |
| Planes | `/api/membership/config` | `POST /`, `GET /{id}`, `DELETE /{id}`, `GET /platform/{id}` |
| Membresías | `/api/membership` | `POST /`, `PUT /{id}`, `GET /account/{id}`, `DELETE /account/{id}` |
| Órdenes | `/api/order` | `POST /`, `GET /{id}`, `DELETE /{id}`, `GET /orders/{page}/{offset}`, `GET /records/{page}/{offset}` |
| Pagos | `/api/payment` | `POST /`, `GET /{id}`, `DELETE /{id}`, `GET /{page}/{offset}`, `GET /filter/{field}/{criteria}` |
| Cron tasks | `/api/crontasks` | `GET /`, `PUT /status/{id}/{status}` |
| Dashboard | `/api/dashboard` | `GET /` |

Para los esquemas de request/response, consulta Swagger.

### Ejemplo: crear una cuenta

```http
POST /api/account
Content-Type: application/json
Authorization: Bearer <token>

{
  "email": "cuenta@ejemplo.com",
  "password": "********",
  "platform": "<id-plataforma>",
  "payment": "<id-medio-de-pago>"
}
```

## Pruebas

```bash
./mvnw test
```
