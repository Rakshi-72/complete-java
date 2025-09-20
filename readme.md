# Complete Java Monorepo Documentation

This document provides an end-to-end overview of the complete Java monorepo, covering architecture, modules,
configuration, security, error handling, build and run instructions, API conventions, and operational practices. It is
intended for developers, DevOps engineers, and reviewers onboarding to the project.

## High-level Architecture

- Service Discovery: Centralized Eureka server for service registration and discovery.
- API Gateway: Spring Cloud Gateway for request routing, central error handling, and cross-cutting concerns.
- Config Server: Centralized Spring Cloud Config for externalized per-service configuration.
- Domain/DTO Layer: Shared domain models and DTOs for cross-service interoperability.
- Business Services:
    - Auth Service: Authentication, token issuance, and identity verification.
    - User Service: User identity operations and user data access.
    - Account Service: Account-related domain logic and persistence.
- Security: Spring Security with header-based context propagation, TLS/mTLS between services.
- Observability: Centralized, structured logging; consistent error payloads.

## Repository Structure

- config-server: Spring Cloud Config Server that serves configuration for all services.
- service-discovery: Eureka Server for service registration/discovery.
- gateway-service: Spring Cloud Gateway instance fronting downstream services. Includes global error handling and status
  mapping.
- auth-service: Authentication and token management service.
- user-service: User management, identity search, and validations; integrates security and custom exception handling.
- account-service: Account domain operations (CRUD, queries).
- domains and/or models-and-dtos: Shared model and DTO definitions for cross-service contracts.
- configs: Externalized configuration repository consumed by config-server.
- certificates and credentials: Keystores, truststores, and secrets locations for TLS/mTLS and secure service
  communication.
- flow-diagrams: Architecture and flow charts.
- logs: Central or local log outputs.

Note: Some directories may be used at runtime or as external resources. Sensitive contents are ignored by VCS via
.gitignore where appropriate.

## Technology Stack

- Java 20
- Spring Boot (Reactive Gateway)
- Spring Cloud (Gateway, Config, Eureka)
- Spring Security
- Spring Data JPA
- Jakarta EE (jakarta.* imports)
- Lombok for boilerplate reduction

## Service Responsibilities

- service-discovery (Eureka Server)
    - Hosts registry for dynamic service lookup.
    - Downstream services register and fetch peer locations.

- config-server
    - Serves centralized configuration from configs/.
    - Supports environment-specific and service-specific profiles.

- gateway-service
    - Routes public traffic to internal services via discovery.
    - Provides centralized error handling with standardized JSON payloads.
    - Can enforce cross-cutting policies (e.g., authentication headers, rate limiting if configured).

- auth-service
    - Issues tokens and manages authentication flows.
    - Can validate identity and roles for downstream service consumption.

- user-service
    - Exposes identity-based user retrieval.
    - Uses Spring Security to build the SecurityContext from propagated headers (e.g., x-username, x-roles).
    - Employs tailored exception handlers returning consistent error shapes and statuses.

- account-service
    - Manages account entities and related operations.
    - Integrates with service-discovery and config-server for dynamic configuration.

- domains/models-and-dtos
    - Provides shared types for request/response payloads.
    - Enables consistent serialization between services.

## Configuration and Secrets

- configs/: Contains YAML/property files consumed by the config-server. This enables per-environment overrides (e.g.,
  application.yml, application-dev.yml).
- certificates/: Holds truststores and keystores supporting TLS/mTLS between services.
- credentials/: Reserved for environment-specific sensitive data (ignored by VCS). Ensure local copies are populated
  through your secret management process.

Important:

- Do not commit live credentials or private keys.
- Validate certificate alignment between gateway-service and user-service for mTLS when enabled.
- Keep config-server pointed to the correct branch/path of configs/.

## Security Model

- Service-to-Service Transport:
    - TLS/mTLS between gateway and downstream services can be enabled using certificates and keystores.
    - Configure keystore/truststore paths and passwords in the corresponding application configuration files served by
      config-server.

- Propagated Identity:
    - Gateway or edge components forward headers such as x-username and x-roles to downstream services.
    - Downstream services (e.g., user-service) reconstruct the Spring Security context from these headers for
      authorization decisions.

- Authorization:
    - Roles are represented as delimited values (e.g., “ROLE_ADMIN|ROLE_USER”) in headers.
    - Downstream services can use role-based access checks at controller or method levels.

- Token Handling:
    - Auth service issues tokens; gateway and services can validate/propagate identity based on your chosen flow.

## Error Handling and API Conventions

- Gateway:
    - Central error handler standardizes JSON error responses across the edge.
    - Maps known framework exceptions to appropriate HTTP statuses and friendly messages.
    - Ensures consistent content-type application/json and UTF-8 encoding.

- Services:
    - Each service should implement a lightweight @ControllerAdvice for application-specific exceptions and validation
      errors, ensuring:
        - Human-readable messages.
        - Distinct HTTP status codes per error condition (e.g., 400, 403, 404, 409, 500).
    - Validation errors return structured field-level messages.

- Response Shape:
    - Errors typically return an object with fields like: status, error, message.
    - Successful responses can be wrapped consistently (if using a wrapper annotation/convention).

## Build and Run

Prerequisites:

- JDK 20 installed and selected.
- Maven or Gradle configured (use the project’s chosen build tool).
- Ports for gateway, eureka, and config-server available.
- Optionally: Docker and Docker Compose for containerized runs.

Typical Local Run Order:

1) Start config-server
2) Start service-discovery
3) Start gateway-service
4) Start downstream services (auth-service, user-service, account-service)

Example with Maven:

```shell script
# 1) Build the entire repo
mvn clean install -DskipTests

# 2) Start Config Server
cd config-server
mvn spring-boot:run

# 3) Start Eureka Server
cd ../service-discovery
mvn spring-boot:run

# 4) Start Gateway
cd ../gateway-service
mvn spring-boot:run

# 5) Start user-service, auth-service, account-service (in separate terminals)
cd ../user-service && mvn spring-boot:run
cd ../auth-service && mvn spring-boot:run
cd ../account-service && mvn spring-boot:run
```

Environment Variables / JVM Options:

- Configure keystore/truststore paths and passwords via environment variables or application properties.
- Ensure the config-server can reach configs/. If using Git-backed config, confirm repo URL and branch.

### Containerized Local Run (Optional)

- Provide a docker-compose.yml that:
    - Starts config-server, service-discovery, gateway, and services with proper dependency order.
    - Mounts configs/ for config-server (or points to a Git URL).
    - Injects keystore/truststore secrets via environment or mounted volumes.
- Use healthchecks to block gateway until config-server and discovery are ready.

## Routing and Service Discovery

- Gateway routes are configured to discover services via Eureka and forward requests appropriately.
- Patterns and predicates can be adjusted in gateway configuration (served by config-server).
- Downstream services register to Eureka with service names; the gateway uses these IDs for routing.

### Resilience and Traffic Management

- Rate Limiting:
    - Enforce per-identity/per-IP limits at the gateway; return 429 with error code E1300 when exceeded.
- Circuit Breaking and Retries:
    - Apply safe timeouts and limited retries for idempotent operations.
    - Trip circuit on repeated failures to protect downstreams; return fast with a clear error.
- Bulkheads and Timeouts:
    - Isolate service pools; set request timeouts appropriate to SLAs.

## Data Access and Persistence

- Spring Data JPA used for repository interfaces and CRUD operations.
- Read-only operations can be annotated with @Transactional(readOnly = true).
- Prefer Optional for nullable lookups to avoid null handling pitfalls.
- Ensure database connection properties are externalized via config-server per environment.

### Caching Strategy (Optional)

- Read-heavy endpoints may leverage a cache with clear TTLs and cache-key conventions.
- Invalidate caches on write operations to avoid stale reads.

## Observability and Logging

- Structured logging with clear error messages and exception types across gateway and services.
- Centralized logging configuration can be managed via config-server.
- Consider integrating metrics and tracing (e.g., Micrometer, OpenTelemetry) if required; wire exporters via
  configuration.

### Metrics, Health, and Readiness

- Expose health endpoints for orchestration:
    - Liveness: /actuator/health/liveness
    - Readiness: /actuator/health/readiness
- Emit key SLIs:
    - Request latency, error rates, saturation, dependency health
- Propagate x-correlation-id and include it in all logs.

## Local Development Tips

- Use JVM debug ports for gateway and services to step through routing and security context propagation.
- For mTLS testing, verify certificate chains and ensure both truststores and keystores are correctly configured.
- If services fail to register/discover, check:
    - Eureka server availability.
    - Correct eureka.client.serviceUrl.defaultZone in service configs.
- If config-server doesn’t serve properties, confirm:
    - Correct spring.cloud.config.server.git.uri or native file system backend pointing to configs/.
    - Matching spring.application.name and active profiles.

### Developer Workflow

- Prefer API-first: update OpenAPI and regenerate clients/stubs when applicable.
- Keep DTO changes backward-compatible; use additive changes where possible.
- Add unit and integration tests for new endpoints and error paths.

## Testing Strategy

- Unit Tests:
    - Focus on service layer and repository logic.
    - Validate exception handlers return expected payloads and statuses.

- Integration Tests:
    - Validate routing through gateway to services.
    - Test mTLS handshake when enabled.
    - Verify identity propagation and role-based access.

- Contract/DTO Tests:
    - Ensure changes to shared DTOs remain backward-compatible or include versioned endpoints.

### Performance and Load Testing

- Create representative load scenarios for critical endpoints.
- Track latency percentiles (p50/p95/p99) and error budgets against SLOs.

## Deployment Considerations

- Externalize all environment-specific values (endpoints, credentials, keystores) via config-server.
- Start order in production:
    1) Config server
    2) Service discovery
    3) Gateway
    4) Downstream services
- Use liveness/readiness probes for orchestration platforms (Kubernetes) to maintain rollout safety.
- Rotate certificates periodically; automate distribution through secret management.

### Release and Rollout Strategy

- Support blue/green or canary releases at the gateway level.
- Gradually shift traffic and monitor key SLIs before full cutover.
- Maintain rollback plans and database migration safety (backward-compatible schema changes).

## Troubleshooting

- 503 from Gateway for downstream NotFound exceptions:
    - The gateway maps certain exceptions to Service Unavailable by design; check downstream service availability.
- 401/403 on services:
    - Ensure x-username and x-roles headers are propagated, and downstream security filter is active.
- Validation errors:
    - Inspect response payload for field-level messages; correct client request shapes accordingly.
- Service not discovered:
    - Confirm registration status in Eureka dashboard and that the correct service ID is referenced by the gateway.

### Common Run Issues Checklist

- Config not loading:
    - Verify config-server endpoint and service bootstrap properties.
- SSL errors:
    - Confirm keystore/truststore paths, passwords, and compatible TLS versions.
- Intermittent timeouts:
    - Check circuit breaker status, thread pools, and connection pools.

## Governance and Best Practices

- Keep shared DTOs stable; introduce new fields as optional or add versioned endpoints when breaking changes are
  necessary.
- Centralize cross-cutting concerns (security policies, error shapes) at the gateway whenever feasible.
- Aim for idempotent APIs for robustness, especially in distributed environments.
- Document any new endpoints in a consolidated API catalog.

### Contribution Guidelines

- Open an issue for any change impacting public APIs or shared DTOs.
- Include tests and documentation updates with each change.
- Request reviews from service owners and platform owners where applicable.

## Glossary

- Eureka: Netflix OSS service discovery used via Spring Cloud.
- Spring Cloud Gateway: Reactive gateway framework for routing and filtering.
- Config Server: Centralized configuration server serving properties/yaml to clients.
- mTLS: Mutual TLS, requiring both server and client to present valid certificates.
- DTO: Data Transfer Object shared between services.

## Next Steps

- Add API reference documentation per service (OpenAPI/Swagger recommended).
- Define standard headers beyond x-username and x-roles (e.g., correlation IDs).
- Integrate distributed tracing for full request visibility across gateway and services.
- Establish CI/CD pipelines for build, test, and deploy, with secrets managed securely.

