# Motor Inversiones API 🚀

API RESTful desarrollada para gestionar operaciones financieras esenciales de un portafolio de inversiones. Construida con un enfoque en arquitectura limpia y precisión financiera.

## 🛠️ Stack Tecnológico
* **Lenguaje:** Java 21
* **Framework:** Spring Boot 3
* **Base de Datos:** PostgreSQL
* **Infraestructura:** Docker (para contenedores de base de datos local)
* **Gestor de dependencias:** Maven

## ⚙️ Características Principales
* **Precisión Financiera:** Uso estricto de `BigDecimal` y `RoundingMode` para evitar pérdida de decimales en transacciones y conversiones.
* **Transaccionalidad:** Integración de `@Transactional` para asegurar las propiedades ACID de la base de datos (si algo falla, se hace rollback).
* **Arquitectura por Capas:** Separación clara entre Controladores, Servicios, Repositorios y DTOs (Data Transfer Objects mediante Java Records).

## 🚀 Endpoints Principales
La API se expone bajo la ruta base `/api/v1/portafolios`:

* `POST /inicializar/{usuarioId}`: Crea un nuevo portafolio en ceros.
* `GET /{usuarioId}`: Consulta el balance actual en MXN y USDC.
* `POST /{usuarioId}/inyeccion`: Recibe depósitos de capital en pesos (MXN).
* `POST /{usuarioId}/comprar-usdc`: Convierte MXN a USDC basándose en el tipo de cambio proporcionado.