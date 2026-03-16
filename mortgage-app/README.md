**Mortgage Eligibility Service**
A Java, Spring Boot–based REST API for mortgage eligibility based 
on income, loan value, home value, and maturity period. The service applies 
business rules, calculates monthly EMI, and exposes endpoints for 
mortgage checks and interest‑rate retrieval. 
**Features**
- Mortgage feasibility calculation
- Business rule validation:
  - Loan value ≤ 4 × income
  - Loan value ≤ home value
  - Maturity period must exist in configured interest‑rate list
- Request validation
- Exception handling with error responses
- Logging
- Swagger/OpenAPI UI for API testing
- Actuator endpoints for health
- Dockerfile + docker‑compose for containerized execution
- Unit tests and integration tests for service, controller, and validation layers
**Tech Stack**
- Java 21
- Spring Boot
- Spring Web
- Spring Validation
- Spring Actuator
- Springdoc OpenAPI (Swagger UI)
- JUnit 5 + MockMvc
- Maven
- Docker
**Running with Docker**
docker-compose up --build
**Running locally**
- mvn clean install
  mvn spring-boot:run
**Service available at**
- http://localhost:8080
**API Endpoints**
- Check Mortgage Feasibility
  - POST /api/mortgage-check
  - Get Interest Rates
  - GET /api/interest-rates
**Swagger**
  - http://localhost:8080/swagger-ui.html
  - http://localhost:8080/v3/api-docs
**Actuator**
  - http://localhost:8080//actuator/health
**Tests**
  -  MortgageServiceTest
  - MortgageControllerTest
  - ValidationTest
**Configuration**
  - Interest rates are configured in application.yml
**Project Structure**
src/main/java
  - config
  - controller
  - dto
  - exception
  - model
  - service
src/test/java
  - controller
    - MortgageControllerTest
  - service
    - MortgageServiceTest
  - validation
    - ValidationTest
To make the assignment production ready, need to consider below enhancements further -
  - Environment specific profiles
  - Database table for storing interest rate for various terms
  - Caching the interest rate
  - Observability and monitoring to improve MTTD and MMTR
  - API authentication using JWT/OAUTH2
  - Rate limiting
  - Resilience using Retry, Circuit breaker, Timeout
  - Test improvements by adding contract and performance tests
  - Golden template for CICD automation
  - Further extension for business functionality considering different mortgage types







