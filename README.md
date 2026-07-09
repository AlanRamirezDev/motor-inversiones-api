🌐 **Read this in other language:** [English](README-en.md)

# 🏦 API Motor Transaccional - Núcleo de Inversiones

![Java](https://img.shields.io/badge/Java_21-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white)
![Spring Boot](https://img.shields.io/badge/Spring_Boot_3.5-6DB33F?style=for-the-badge&logo=springboot&logoColor=white)
![PostgreSQL](https://img.shields.io/badge/PostgreSQL_16-316192?style=for-the-badge&logo=postgresql&logoColor=white)
![Docker](https://img.shields.io/badge/Docker-2496ED?style=for-the-badge&logo=docker&logoColor=white)

¡Te doy la bienvenida al motor transaccional y de inversiones de mi portafolio!

Este proyecto actúa como una API RESTful de alta precisión y rendimiento optimizado para simular procesos operativos de plataformas Fintech. Está diseñado para gestionar la inyección de capitales, consultas de portafolio y conversión atómica de activos de pesos mexicanos (MXN) a dólares digitales (USDC), garantizando la consistencia de los datos bajo escenarios de alta concurrencia.

## 🚀 Características del Proyecto & Arquitectura Backend

* **Concurrencia y Bloqueo Pesimista (Propiedades ACID):** Implementación estricta de mecanismos de aislamiento transaccional a nivel de persistencia mediante `@Lock(LockModeType.PESSIMISTIC_WRITE)`. Esto fuerza al motor de base de datos a congelar las filas operativas durante las mutaciones, anulando por completo las Race Conditions ante peticiones concurrentes simultáneas.
* **Aritmética Inmutable de Precisión Arbitraria:** Uso absoluto de la clase `BigDecimal` complementado con políticas de redondeo bancario estrictas (`RoundingMode.HALF_UP`). Mitiga los fallos e imprecisiones numéricas de punto flotante (`double`/`float`), asegurando la integridad de cada centavo en el cálculo contable de divisas.
* **Perímetro de Validación y Errores Homologados:** Centralización y saneamiento de Payloads a través de Jakarta Validation en la capa HTTP controladora (`@Valid`). Los fallos estructurales o de negocio son capturados por un interceptor global (`@ControllerAdvice`), transformando excepciones como `MethodArgumentNotValidException` en respuestas limpias estructuradas en formato JSON.
* **Contenedores Inmutables y Seguros (IaC):** Pipeline de empaquetado estructurado bajo el patrón *Multi-stage Build* basado en una imagen de ejecución ultraligera de **Alpine Linux (JRE)**. Incorpora la inyección nativa de banderas de afinación para la máquina virtual de Java (`-XX:MaxRAMPercentage`) y el aislamiento de privilegios mediante un usuario del sistema no-root (`spring:spring`).

---

## 🛠️ Stack Tecnológico

| Tecnología | Versión | Propósito en el proyecto |
| :--- | :--- | :--- |
| **Spring Boot** | `^3.5.14` | Framework base, Inversión de Control (IoC) y controladores RESTful |
| **Java** | `21` | Lenguaje de programación principal con tipado estricto y records |
| **Spring Data JPA**| `^3.5.11` | Abstracción de datos, ORM con Hibernate 6 y control de bloqueos |
| **PostgreSQL** | `16` | Motor relacional de base de datos (Docker local y Neon DB producción) |
| **Jakarta Validation**| `^3.0` | Validación de contratos de datos y restricciones monetarias en el perímetro |

---

## 💻 Comandos de Desarrollo y Despliegue

Instrucciones para levantar el entorno localmente. El backend requiere que el puerto `8080` esté disponible y que Docker orqueste la base de datos local en el puerto reconfigurado `5433`.

| Comando | Acción |
| :--- | :--- |
| `docker-compose up -d` | Inicializa el contenedor aislado de PostgreSQL con volumen persistente |
| `mvn clean package` | Compila el código, descarga dependencias y genera el archivo ejecutable `.jar` |
| `mvn spring-boot:run` | Inicia la aplicación web embebida en el servidor Tomcat local |

---

## 📡 Documentación de la API (Endpoints)

Todas las respuestas con códigos de error de negocio o validación HTTP 400 devuelven la estructura estandarizada: `{"error": "Detalle del fallo"}`.

### Cuentas y Consultas (`/api/v1/portafolios`)
| Método | Endpoint | Descripción | Acceso / Payload |
| :--- | :--- | :--- | :--- |
| `POST` | `/inicializar/{usuarioId}` | Registra e inicializa un nuevo portafolio financiero en cero. | Público / Ninguno |
| `GET`  | `/{usuarioId}` | Obtiene los saldos actuales detallados de la cuenta en MXN y USDC. | Público / Solo Lectura |

### Mutaciones Financieras (`/api/v1/portafolios/{usuarioId}`)
| Método | Endpoint | Descripción | Payload Requerido | Restricciones / Validaciones |
| :--- | :--- | :--- | :--- | :--- |
| `PUT` | `/inyeccion` | Ejecuta la adición y fondeo seguro de capital en pesos al balance. | JSON con `monto` | `@NotNull`, `@Positive` (Monto > 0) |
| `PUT` | `/comprar-usdc` | Realiza el Swap atómico de saldo MXN para adquirir dólares cripto. | JSON con `montoMxn` y `tipoCambio` | Fondos Suficientes, `@Positive` |
| `PUT` | `/reiniciar` | Operación idempotente que restablece los saldos a cero (Modo Demo). | Ninguno | Restablece el balance completo |

---
