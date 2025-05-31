# Bookstore Microservices Architecture

![Java](https://img.shields.io/badge/Java-21%2B-blue?logo=java)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.5-green?logo=springboot)
![PostgreSQL](https://img.shields.io/badge/PostgreSQL-15-blue?logo=postgresql)
![License](https://img.shields.io/github/license/mdang-dev/bookstore-microservices-architecture)

A modular, production-ready microservices system for a modern online bookstore, built with Java, Spring Boot, and PostgreSQL.  
This project demonstrates scalable service design, robust CI/CD, and cloud-native best practices.

---

## Table of Contents

- [Overview](#overview)
- [Architecture](#architecture)
- [Services](#services)
- [Getting Started](#getting-started)
- [Development](#development)
- [Testing](#testing)
- [Deployment](#deployment)
- [Contributing](#contributing)
- [License](#license)

---

## Overview

This repository contains a microservices-based system for a bookstore, designed for extensibility, resilience, and observability. Each service is independently deployable and follows 12-factor app principles.

---

## Architecture

- **Service-Oriented**: Each domain (e.g., catalog, order, user) is a separate Spring Boot service.
- **Database per Service**: Each service manages its own schema (PostgreSQL).
- **Centralized Configuration**: Environment variables and externalized configs.
- **Observability**: Health checks, metrics, and distributed tracing ready.

```
bookstore-microservices-architecture/
├── catalog-service/      # Product catalog microservice
├── deployment/           # Deployment manifests (e.g., Docker Compose)
├── .github/              # CI/CD workflows
├── .mvn/                 # Maven wrapper
```


---

## Services

- [`catalog-service`](catalog-service/)  
  Manages book metadata and categories.


---

## Getting Started

### Prerequisites

- Java 21+ ([.sdkmanrc](.sdkmanrc))
- Maven 3.9+
- Docker (for local DB/testing)
- PostgreSQL (for production)

### Quick Start

1. **Clone the repo**
    ```sh
    git clone https://github.com/mdang-dev/bookstore-microservices-architecture.git
    cd bookstore-microservices-architecture
    ```

2. **Start dependencies (PostgreSQL)**
    ```sh
    docker-compose -f deployment/docker-compose/infra.yml up -d
    ```

3. **Run a service**
    ```sh
    cd catalog-service
    ./mvnw spring-boot:run
    ```

4. **Access endpoints**
    - Catalog API: [http://localhost:8081](http://localhost:8081)
    - Actuator: [http://localhost:8081/actuator/health](http://localhost:8081/actuator/health)

---

## Development

- **Code Style**: Enforced by [Spotless](https://github.com/diffplug/spotless) (Google Java Format)
- **Build**:  
    ```sh
    ./mvnw clean package
    ```
- **Configuration**:  
    See [`catalog-service/src/main/resources/application.yml`](catalog-service/src/main/resources/application.yml)

---

## Testing

- **Unit & Integration Tests**:  
    ```sh
    ./mvnw test
    ```
- **Testcontainers**: Used for spinning up ephemeral PostgreSQL instances during integration tests.

---

## Deployment

- **Docker Compose**:  
    ```sh
    docker-compose -f deployment/docker-compose/infra.yml up --build
    ```
- **CI/CD**:  
    Automated via [GitHub Actions](.github/workflows/catalog-service.yml)

---

## Contributing

We welcome contributions! Please see [CONTRIBUTING.md](CONTRIBUTING.md) (if available) or open an issue/pull request.

1. Fork the repo
2. Create a feature branch (`git checkout -b feature/your-feature`)
3. Commit and push your changes
4. Open a pull request

---

## License

This project is licensed under the Apache License 2.0.
