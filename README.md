# 🏦 Motor Transaccional de Inversiones - API

API RESTful de alto rendimiento construida con **Spring Boot 3** y **Java 21** para simular procesos críticos de plataformas Fintech, tales como inyecciones de capital y conversión defensiva de activos (MXN a USDC). Este componente actúa como el núcleo de procesamiento transaccional para el ecosistema del portafolio.

## 🚀 Características Técnicas & Arquitectura

- **Manejo de Concurrencia:** Implementación de bloqueo pesimista (`PESSIMISTIC_WRITE`) a nivel de base de datos para garantizar la consistencia absoluta en el balance del portafolio durante operaciones simultáneas.
- **Transaccionalidad:** Configuración avanzada de aislamiento de transacciones (`Isolation.REPEATABLE_READ`) para mitigar lecturas no repetibles durante fluctuaciones simuladas del tipo de cambio.
- **Persistencia de Datos en la Nube:** Conexión integrada con PostgreSQL Serverless (Neon DB) para el entorno de producción, manteniendo aislamiento completo mediante variables de entorno.
- **Arquitectura de Desarrollo Aislada:** Soporte local mediante contenedores **Docker** reconfigurados al puerto `5433` para evitar colisiones con instancias nativas del sistema operativo.
- **Ecosistema Integrado:** Configuración de políticas CORS dinámicas para permitir la comunicación segura y exclusiva con el cliente SPA desplegado en Vercel.

---

## 🛠️ Stack Tecnológico

| Tecnología | Herramienta / Versión |
| :--- | :--- |
| **Lenguaje** | Java 21 |
| **Framework** | Spring Boot 3.x |
| **Persistencia** | Spring Data JPA / Hibernate 6 |
| **Base de Datos** | PostgreSQL 16 (Neon DB en la nube / Docker en local) |
| **Contenedores** | Docker & Docker Compose |

---

## 🌐 Endpoints Principales

| Método | Endpoint | Descripción |
| :--- | :--- | :--- |
| `GET` | `/api/v1/portafolios/1` | Obtiene el balance actual optimizado en MXN y USDC. |
| `POST` | `/api/v1/portafolios/inicializar/1` | Inicializa las cuentas de demostración de manera automática ante la ausencia de registros. |
| `POST` | `/api/v1/portafolios/1/inyeccion` | Registra flujos de fondeo de capital entrante. |
| `POST` | `/api/v1/portafolios/1/comprar-usdc` | Ejecuta el flujo transaccional con bloqueo para el intercambio seguro de divisas. |
| `POST` | `/api/v1/portafolios/1/reiniciar` | Restablece los balances de la base de datos a cero. |