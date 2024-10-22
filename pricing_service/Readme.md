# Pricing service

## Overview

The Pricing Service is a Spring Boot application designed to provide pricing information for products based on given criteria. This service retrieves the applicable price for a product and brand on a specific date.

## Project structure

This project follows the principles of hexagonal architecture (also known as ports and adapters) and I have tried to follow the Domain Driven Design (DDD) approach to ensure maintainability and scalability. Here is an overview of the project structure:

- **`application`**: Contains the application logic, including service interfaces and their implementations. It serves as the core of the business logic.
    - **`service`**: Contains service interfaces and their implementations, such as `PriceService` and `PriceServiceImpl`.
    - **`validation`**: Contains validation classes like `ValidationResult`.

- **`domain`**: Contains domain models and business logic. It includes:
    - **`model`**: Defines the core business entities such as `Price`.
    - **`dto`**: Data Transfer Objects (DTOs) used for communication between layers, such as `PriceRequest`.
    - **`port`**: Interfaces that define the interaction with the core business logic, including repository ports.
    - **`validators`**: Custom validation annotations and their implementations. For example, `ValidDateRange` is a custom annotation used to ensure that the end date is after the start date in `PriceEntity`, validated by `DateRangeValidator`.

- **`infrastructure`**: Contains the implementation of the ports defined in the domain layer. It includes:
  - **`repository`**: Implements data access layers, such as `PriceRepositoryImpl`.
  - **`entity`**: JPA entities like `PriceEntity`.
  - **`adapters`**: Contains classes that adapt the core domain logic to external systems, including implementations for repository interfaces.
  - **`commons`**: Includes constants and custom exceptions used across the application.
  - **`configuration`**: Contains configuration classes, such as `DataLoader`, which sets up initial data in the database.
  - **`rest`**: Includes REST controllers and mappers. For example, `PriceControllerImpl` implements the `PriceController` interface from the `ports` package. The `mapper` subpackage contains classes like `PriceMapper` for mapping between entities and domain objects.

## Running the application

To run the application, ensure you have Java 17 or higher installed, along with Maven. You can start the application with the following command:

```bash
mvn spring-boot:run
```

## Test structure

- **`src/test/java`**: Contains the test suite for the application. This directory is organized into various subdirectories to cover different aspects of testing.

  - **`controller`**:
    - **Purpose**: This directory contains tests for the REST controllers. These tests ensure that the API endpoints handle HTTP requests and responses correctly and interact as expected with the service layer.
    - **Contents**: Tests for validating controller logic.

  - **`entity`**:
    - **Purpose**: This directory includes tests for JPA entities. These tests verify that the entities are correctly mapped to the database schema and that entity constraints are properly enforced.
    - **Contents**: Tests for entity relationships, data integrity constraints, and custom validation annotations like `ValidDateRange` which ensures that the end date is after the start date, as validated by `DateRangeValidator`.

  - **`service`**:
    - **Purpose**: This directory is dedicated to testing the service layer. These tests focus on the core business logic implemented in the services, ensuring that service methods perform correctly and adhere to business rules.
    - **Contents**: Tests for service methods that handle operations such as retrieving applicable prices and interacting with the repository layer.

### Running the application

```bash
mvn test
```