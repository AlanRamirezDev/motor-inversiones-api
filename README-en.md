🌐 **Leer en otro idioma:** [Español](README.md)

# 🏦 Transactional Engine API - Investment Core

![Java](https://img.shields.io/badge/Java_21-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white)
![Spring Boot](https://img.shields.io/badge/Spring_Boot_3.5-6DB33F?style=for-the-badge&logo=springboot&logoColor=white)
![PostgreSQL](https://img.shields.io/badge/PostgreSQL_16-316192?style=for-the-badge&logo=postgresql&logoColor=white)
![Docker](https://img.shields.io/badge/Docker-2496ED?style=for-the-badge&logo=docker&logoColor=white)

Welcome to my portfolio's transactional and investment engine!

This project acts as a high-precision, performance-optimized RESTful API designed to simulate Fintech platform operations. It handles capital injections, portfolio queries, and atomic asset conversion from Mexican Pesos (MXN) to digital dollars (USDC), guaranteeing data consistency under high-concurrency scenarios.

## 🚀 Project Features & Backend Architecture

* **Concurrency and Pessimistic Locking (ACID Properties):** Strict implementation of persistence-level transactional isolation mechanisms using `@Lock(LockModeType.PESSIMISTIC_WRITE)`. This forces the database engine to freeze operational rows during mutations, completely eliminating Race Conditions against simultaneous concurrent requests.
* **Arbitrary-Precision Immutable Arithmetic:** Absolute use of the `BigDecimal` class complemented by strict banking rounding policies (`RoundingMode.HALF_UP`). It mitigates floating-point (`double`/`float`) numerical flaws and inaccuracies, ensuring the integrity of every single cent in currency exchange bookkeeping.
* **Validation Perimeter and Standardized Errors:** Real-time payload sanitization and centralization via Jakarta Validation at the HTTP controller layer (`@Valid`). Structural or business failures are caught by a global exception interceptor (`@ControllerAdvice`), transforming exceptions like `MethodArgumentNotValidException` into clean, structured JSON responses.
* **Immutable and Secure Containers (IaC):** Packaging pipeline structured under the Multi-stage Build pattern based on an ultra-lightweight Alpine Linux execution image (JRE). It incorporates native Java Virtual Machine tuning flags injection (`-XX:MaxRAMPercentage`) and privilege isolation using a non-root system user (`spring:spring`).

---

## 🛠️ Tech Stack

| Technology | Version | Purpose in the project |
| :--- | :--- | :--- |
| **Spring Boot** | `^3.5.14` | Base framework, Inversion of Control (IoC), and RESTful controllers |
| **Java** | `21` | Main programming language with strict typing and records |
| **Spring Data JPA**| `^3.5.11` | Data abstraction, ORM with Hibernate 6, and lock control |
| **PostgreSQL** | `16` | Relational database engine (local Docker and Neon DB production) |
| **Jakarta Validation**| `^3.0` | Data contract validation and monetary restrictions at the perimeter |

---

## 💻 Development and Deployment Commands

Instructions to boot the environment locally. The backend requires port `8080` to be available and Docker to orchestrate the local database on the reconfigured port `5433`.

| Command | Action |
| :--- | :--- |
| `docker-compose up -d` | Initializes the isolated PostgreSQL container with a persistent volume |
| `mvn clean package` | Compiles the code, downloads dependencies, and generates the executable `.jar` file |
| `mvn spring-boot:run` | Starts the embedded web application on the local Tomcat server |

---

## 📡 API Documentation (Endpoints)

All responses with business error codes or HTTP 400 validation failures return the standardized structure: `{"error": "Failure details"}`.

### Accounts and Queries (`/api/v1/portafolios`)
| Method | Endpoint | Description | Access / Payload |
| :--- | :--- | :--- | :--- |
| `POST` | `/inicializar/{usuarioId}` | Registers and initializes a new financial portfolio at zero. | Public / None |
| `GET`  | `/{usuarioId}` | Retrieves detailed current account balances in MXN and USDC. | Public / Read-Only |

### Financial Mutations (`/api/v1/portafolios/{usuarioId}`)
| Method | Endpoint | Description | Required Payload | Restrictions / Validaciones |
| :--- | :--- | :--- | :--- | :--- |
| `PUT` | `/inyeccion` | Executes safe capital injection and funding in pesos to the balance. | JSON with `monto` | `@NotNull`, `@Positive` (Amount > 0) |
| `PUT` | `/comprar-usdc` | Performs the atomic Swap of MXN balance to acquire crypto dollars. | JSON with `montoMxn` and `tipoCambio` | Sufficient Funds, `@Positive` |
| `PUT` | `/reiniciar` | Idempotent operation that resets balances to zero (Demo Mode). | None | Resets the entire balance |

---