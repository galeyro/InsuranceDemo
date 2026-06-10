# Insurance API — Sistema de Gestión de Pólizas de Seguros

Este proyecto es una API REST para la cotización, emisión y gestión del ciclo de vida de pólizas de seguros. Está construido siguiendo los principios de la **Arquitectura Hexagonal**, **SOLID**, **Clean Code** y aplicando **seis patrones de diseño** de manera coherente para resolver las necesidades del dominio.

---

## 1. Stack Tecnológico

* **Backend:** Java 21, Spring Boot 4.0.x, JPA (Hibernate), Maven.
* **Frontend:** Angular, TypeScript, Vanilla CSS (Estilos Premium y Responsive).
* **Base de Datos:** PostgreSQL (con campos JSONB para coberturas y perfiles de riesgo).
* **Broker de Mensajería:** Apache Kafka (KRaft/Zookeeper con seguridad SASL PLAIN).
* **Calidad y Cobertura:** JaCoCo (cobertura mínima de líneas establecida en 80%).
* **Contenedores y Orquestación:** Docker, Docker Compose, Kubernetes (Minikube con FluxCD GitOps).

---

## 2. Arquitectura del Proyecto (Hexagonal)

El backend está estructurado en módulos aislados (`customers`, `policies`, `notifications`), divididos cada uno en tres capas bien definidas según los principios de *Ports and Adapters*:

1. **Domain (Core del Negocio):** Contiene las entidades puras, Value Objects, excepciones de dominio, puertos de interfaces (`ports`), estrategias, estados y fábricas. Esta capa no tiene ninguna dependencia de frameworks, bases de datos o HTTP.
2. **Application (Casos de Uso):** Contiene la orquestación del negocio (los casos de uso) y coordina la ejecución de las fábricas, constructores y estrategias utilizando inversión de dependencias (DIP).
3. **Infrastructure (Adaptadores):** Implementa los puertos definidos en el dominio para comunicarse con el mundo exterior (Base de datos JPA, Web Controllers, DTOs, mappers, consumidores y publicadores de Kafka).

---

## 3. Guía de Arranque Rápido (Local)

### Requisitos Previos
* Docker Desktop instalado.
* Java 21 y Maven (opcional, si compilas localmente sin wrapper).

### Paso 1: Levantar la Infraestructura
En la raíz del proyecto, ejecuta el siguiente comando para levantar PostgreSQL, Apache Kafka (con SASL habilitado) y Kafka UI:
```bash
docker-compose up -d
```

### Paso 2: Ejecutar el Backend (Spring Boot)
1. Ve al directorio del backend:
   ```bash
   cd Insurance-Backend
   ```
2. Asegúrate de tener las credenciales correctas en tu archivo `.env` o en las variables de entorno locales:
   * `SPRING_DATASOURCE_URL=jdbc:postgresql://localhost:5433/insurance_db`
   * `SPRING_DATASOURCE_USERNAME=postgres`
   * `SPRING_DATASOURCE_PASSWORD=postgres`
   * `KAFKA_USERNAME=user1`
   * `KAFKA_PASSWORD=amIkM2sFfr`
3. Ejecuta la aplicación:
   ```bash
   ./mvnw spring-boot:run
   ```

### URLs de Acceso y Herramientas:
* **API Swagger UI (Docs):** [http://localhost:8082/api/docs](http://localhost:8082/api/docs) (o puerto 8080 si se ejecuta directo).
* **Kafka UI:** [http://localhost:8083](http://localhost:8083) (Usuario: `user1`, Contraseña: `amIkM2sFfr`).
* **PostgreSQL:** `localhost:5433` (Base de datos: `insurance_db`).

---

## 4. Mapa de Patrones de Diseño (Los 6 Patrones)

A continuación, se detalla la ubicación exacta de los patrones de diseño aplicados en el módulo de pólizas (`policies`):

### A. Factory Method (Creación por Ramo)
Encapsula la creación de coberturas por defecto y primas base de cada ramo (`AUTO`, `LIFE`, `HOME`, `HEALTH`), evitando que el usecase use sentencias condicionales (`switch`/`if`).
* **Puerto/Interfaz:** [PolicyFactoryPort.java](file:///c:/Users/G_Laptop/InsuranceDemo/Insurance-Backend/src/main/java/edu/softkau/dev/insurancebackend/policy/domain/ports/PolicyFactoryPort.java)
* **Fábricas Concretas:**
  * [AutoPolicyFactory.java](file:///c:/Users/G_Laptop/InsuranceDemo/Insurance-Backend/src/main/java/edu/softkau/dev/insurancebackend/policy/application/factory/AutoPolicyFactory.java)
  * [HomePolicyFactory.java](file:///c:/Users/G_Laptop/InsuranceDemo/Insurance-Backend/src/main/java/edu/softkau/dev/insurancebackend/policy/application/factory/HomePolicyFactory.java)
  * [LifePolicyFactory.java](file:///c:/Users/G_Laptop/InsuranceDemo/Insurance-Backend/src/main/java/edu/softkau/dev/insurancebackend/policy/application/factory/LifePolicyFactory.java)
  * [HealthPolicyFactory.java](file:///c:/Users/G_Laptop/InsuranceDemo/Insurance-Backend/src/main/java/edu/softkau/dev/insurancebackend/policy/application/factory/HealthPolicyFactory.java)
* **Registro/Despachador:** [PolicyFactoryRegistry.java](file:///c:/Users/G_Laptop/InsuranceDemo/Insurance-Backend/src/main/java/edu/softkau/dev/insurancebackend/policy/application/factory/PolicyFactoryRegistry.java)

### B. Strategy (Tarificación de la Prima)
Define algoritmos intercambiables para ajustar la prima base de acuerdo a la estrategia seleccionada (`STANDARD`, `RISK_BASED`, `LOYALTY`), permitiendo añadir nuevas estrategias sin modificar los casos de uso (OCP).
* **Puerto/Interfaz:** [RatingStrategyPort.java](file:///c:/Users/G_Laptop/InsuranceDemo/Insurance-Backend/src/main/java/edu/softkau/dev/insurancebackend/policy/domain/ports/RatingStrategyPort.java)
* **Estrategias Concretas:**
  * [StandardRatingStrategy.java](file:///c:/Users/G_Laptop/InsuranceDemo/Insurance-Backend/src/main/java/edu/softkau/dev/insurancebackend/policy/domain/strategy/StandardRatingStrategy.java)
  * [RiskBasedRatingStrategy.java](file:///c:/Users/G_Laptop/InsuranceDemo/Insurance-Backend/src/main/java/edu/softkau/dev/insurancebackend/policy/domain/strategy/RiskBasedRatingStrategy.java)
  * [LoyaltyRatingStrategy.java](file:///c:/Users/G_Laptop/InsuranceDemo/Insurance-Backend/src/main/java/edu/softkau/dev/insurancebackend/policy/domain/strategy/LoyaltyRatingStrategy.java)
* **Registro/Despachador:** [RatingStrategyRegistry.java](file:///c:/Users/G_Laptop/InsuranceDemo/Insurance-Backend/src/main/java/edu/softkau/dev/insurancebackend/policy/application/strategy/RatingStrategyRegistry.java)

### C. Builder (Construcción de la Póliza)
Permite la creación fluida, paso a paso, del agregado complejo `Policy`, validando la integridad del objeto en el método `build()` antes de instanciarlo.
* **Builder Interno:** [Policy.java#L106-205](file:///c:/Users/G_Laptop/InsuranceDemo/Insurance-Backend/src/main/java/edu/softkau/dev/insurancebackend/policy/domain/model/Policy.java#L106-205)

### D. State (Máquina de Estados del Ciclo de Vida)
Controla las transiciones válidas del ciclo de vida de la póliza (`QUOTED`, `ISSUED`, `ACTIVE`, `SUSPENDED`, `CANCELLED`). Cada estado encapsula su propia lógica y reglas de transición en clases específicas.
* **Clase Base del Estado:** [PolicyStatePort.java](file:///c:/Users/G_Laptop/InsuranceDemo/Insurance-Backend/src/main/java/edu/softkau/dev/insurancebackend/policy/domain/states/PolicyStatePort.java)
* **Estados Concretos:**
  * [QuotedState.java](file:///c:/Users/G_Laptop/InsuranceDemo/Insurance-Backend/src/main/java/edu/softkau/dev/insurancebackend/policy/domain/states/QuotedState.java)
  * [IssuedState.java](file:///c:/Users/G_Laptop/InsuranceDemo/Insurance-Backend/src/main/java/edu/softkau/dev/insurancebackend/policy/domain/states/IssuedState.java)
  * [ActiveState.java](file:///c:/Users/G_Laptop/InsuranceDemo/Insurance-Backend/src/main/java/edu/softkau/dev/insurancebackend/policy/domain/states/ActiveState.java)
  * [SuspendedState.java](file:///c:/Users/G_Laptop/InsuranceDemo/Insurance-Backend/src/main/java/edu/softkau/dev/insurancebackend/policy/domain/states/SuspendedState.java)
  * [CancelledState.java](file:///c:/Users/G_Laptop/InsuranceDemo/Insurance-Backend/src/main/java/edu/softkau/dev/insurancebackend/policy/domain/states/CancelledState.java)

### E. Observer (Publicación y Consumo de Eventos)
Notifica de manera asíncrona y desacoplada los cambios de estado de las pólizas a través del broker de mensajería (Kafka).
* **Puerto de Publicación:** [PolicyEventPublisherPort.java](file:///c:/Users/G_Laptop/InsuranceDemo/Insurance-Backend/src/main/java/edu/softkau/dev/insurancebackend/policy/domain/ports/PolicyEventPublisherPort.java)
* **Adaptador de Publicación (Kafka):** [KafkaEventPublisher.java](file:///c:/Users/G_Laptop/InsuranceDemo/Insurance-Backend/src/main/java/edu/softkau/dev/insurancebackend/policy/infraestructure/adapters/messaging/KafkaEventPublisher.java)
* **Observadores (Consumidores Desacoplados):**
  * **Notificaciones:** [PolicyNotificationConsumer.java](file:///c:/Users/G_Laptop/InsuranceDemo/Insurance-Backend/src/main/java/edu/softkau/dev/insurancebackend/policy/infraestructure/adapters/messaging/consumers/PolicyNotificationConsumer.java) (Notifica al cliente).
  * **Auditoría:** [PolicyAuditConsumer.java](file:///c:/Users/G_Laptop/InsuranceDemo/Insurance-Backend/src/main/java/edu/softkau/dev/insurancebackend/policy/infraestructure/adapters/messaging/consumers/PolicyAuditConsumer.java) (Registra logs históricos).

### F. Singleton (Reto Adicional)
Garantiza que el generador de secuencias de números de póliza sea único en memoria.
* **Ubicación:** [PolicyNumberSequencer.java](file:///c:/Users/G_Laptop/InsuranceDemo/Insurance-Backend/src/main/java/edu/softkau/dev/insurancebackend/policy/domain/model/PolicyNumberSequencer.java)

---

## 5. Investigación del Patrón Singleton

### ¿Por qué se amerita un Singleton en este recurso?
En nuestro dominio, la creación de pólizas requiere la asignación de un `policyNumber` consecutivo, único e inmutable (p. ej. `POL-2026-000001`, `POL-2026-000002`). Si tuviéramos múltiples instancias del secuenciador ejecutándose de forma concurrente, correríamos el riesgo de generar números duplicados o saltos en la secuencia debido a condiciones de carrera (Race Conditions). 

El `PolicyNumberSequencer` encapsula una variable de tipo `AtomicInteger` y un método que incrementa de forma segura este valor, requiriendo existir exactamente una sola vez en toda la aplicación para actuar como el "único punto de verdad" del conteo.

### ¿Cómo se garantizó la unicidad en Java?
Implementamos el patrón utilizando la técnica del **Bill Pugh Singleton** (también conocida como *Initialization-on-demand holder idiom*):
```java
public class PolicyNumberSequencer {
    private final AtomicInteger counter;

    private PolicyNumberSequencer() {
        this.counter = new AtomicInteger(0);
    }

    private static class SingletonHolder {
        private static final PolicyNumberSequencer INSTANCE = new PolicyNumberSequencer();
    }

    public static PolicyNumberSequencer getInstance() {
        return SingletonHolder.INSTANCE;
    }
}
```
**Mecanismo de Unicidad:**
1. El constructor es `private`, impidiendo la creación directa con `new`.
2. La clase interna estática `SingletonHolder` no se carga en memoria al iniciar la clase `PolicyNumberSequencer`, sino únicamente cuando se llama por primera vez a `getInstance()`. 
3. El estándar de carga de clases de la JVM (Java Virtual Machine) garantiza de forma nativa que la instanciación sea **segura frente a hilos (Thread-Safe)** y realizada una sola vez, sin necesidad de usar bloques de sincronización costosos (`synchronized`).

### ¿Qué riesgos del patrón se mitigaron y por qué el Singleton del contenedor de DI es preferible?
El patrón Singleton clásico ("a mano") tiene críticas conocidas en la industria:
1. **Acoplamiento global y ocultación de dependencias:** Dificulta las pruebas unitarias porque las clases invocan directamente a `PolicyNumberSequencer.getInstance()`, impidiendo mockear el secuenciador.
2. **Dificultad para pruebas aisladas:** Al ser una variable global estática, su estado persiste entre tests. 
   * *Mitigación:* Agregamos un método `reset()` que se ejecuta en el `@BeforeEach` de nuestras pruebas unitarias para aislar el contador entre ejecuciones.

**La alternativa ideal (Scope Singleton de Spring / Inyección de Dependencias):**
En un entorno de producción real utilizando Spring Framework, en lugar de un Singleton "a mano" con constructor privado, se prefiere registrar el recurso como un **Spring Bean** con alcance (Scope) Singleton (que es el valor predeterminado).
* **Por qué es preferible:** El contenedor de Spring se encarga de crear una única instancia y la inyecta donde sea requerida. Esto mitiga el acoplamiento global, permite mockear el secuenciador en las pruebas unitarias reemplazándolo con facilidad (`@MockBean`), y respeta de forma estricta el principio de inversión de dependencias (DIP) sin ocultar la llamada estática.
