# Microservices Project – README

Welcome! This repository contains a Java microservices system built with Spring Boot, Spring Cloud, Jakarta EE (
jakarta.* imports), Spring Data JPA, Lombok, and Java 20. It is organized as multiple services with centralized
configuration and service discovery.

## Features at a glance

- Multiple independently deployable services (auth, account, user, etc.)
- Centralized configuration via Config Server (YAML files under configs/)
- Service discovery and registration
- API gateway for edge routing
- TLS/mutual TLS support (see Security section)
- Database configuration centralized
- Shared DTOs/Models module

## Repository structure

- config-server — Spring Cloud Config Server application
- service-discovery — Service registry (e.g., Eureka Server)
- gateway-service — API gateway for routing to downstream services
- auth-service — Authentication/authorization service
- account-service — Account domain service
- user-service — User domain service
- models-and-dtos — Shared models and DTOs across services
- domains — Domain-related shared artifacts (if applicable)
- configs — Centralized configuration (YAMLs) served by the Config Server
- certificates, credentials — Development certs/keystores/truststores (or placeholders)

## Tech stack

- Java 20
- Spring Boot and Spring Cloud (Gateway, Discovery, Config)
- Jakarta EE (jakarta.* imports)
- Spring Data JPA
- Lombok

## Centralized configuration

Each service consumes its configuration from the Config Server. The repository includes environment-specific YAMLs under
configs/. For example, user-service is configured to:

- Enable SSL
- Require mutual TLS (client-auth)
- Import shared config fragments for discovery and database

Example (configs/user-service.yml):

```yaml
# yaml
server:
  port: 8084
  ssl:
    enabled: true
    client-auth: need
    key-store: ${users.keystore.path}
    key-store-password: ${users.keystore.password}
    trust-store: ${users.truststore.path}
    trust-store-password: ${users.truststore.password}

spring:
  config:
    import:
      - eureka-config.yml
      - database-config.yml
  application:
    name: user-service
    jpa:
      hibernate:
        ddl-auto: update
```

Shared fragments include:

- eureka-config.yml — service discovery client configuration
- database-config.yml — datasource and JPA configuration for services
- Additional per-service files exist under configs/ for gateway, auth, account, and discovery

Tip: Check configs/*.yml to see the effective ports, profiles, and any service-specific flags.

## Prerequisites

- Java 20
- Maven 3.9+
- Valid certificates/keystores if running TLS/mTLS locally (see Security)

## Build

From the repository root:

```shell script
# bash
mvn -q -DskipTests clean install
```

## Run order (recommended)

1. Config Server
2. Service Discovery
3. Gateway
4. Domain services (auth, user, account, etc.)

Start each service from its directory (or with your IDE’s Run Configuration):

```shell script
# bash
# 1) config-server
cd config-server
mvn spring-boot:run

# 2) service-discovery
cd ../service-discovery
mvn spring-boot:run

# 3) gateway-service
cd ../gateway-service
mvn spring-boot:run

# 4) downstream services
cd ../auth-service && mvn spring-boot:run
cd ../account-service && mvn spring-boot:run
cd ../user-service && mvn spring-boot:run
```

Notes:

- Ensure the Config Server is correctly pointing to the configs/ directory or to the intended Git repository containing
  these YAMLs.
- Ensure environment variables/secrets for SSL and database are provided before launching services.

## Security and TLS/mTLS

The system supports TLS, and some services may require mutual TLS (client-auth). For example, user-service is configured
with client-auth: need, meaning:

- The server presents its certificate (key-store)
- The client must present a valid certificate (trust-store validation)

Environment variables expected by services (example for user-service):

- users.keystore.path
- users.keystore.password
- users.truststore.path
- users.truststore.password

Set them before running:

```shell script
# bash
export users.keystore.path=/absolute/path/to/user-service-keystore.p12
export users.keystore.password=changeit
export users.truststore.path=/absolute/path/to/user-service-truststore.p12
export users.truststore.password=changeit
```

If you use different names per service, refer to the corresponding service YAML under configs/ for the exact
placeholders and properties.

Certificates and keystores:

- Development artifacts may be kept under certificates/ and credentials/
- You can generate self-signed certificates for local testing using keytool or OpenSSL. Ensure CN/SANs match the
  hostname you use.

Example curl with mutual TLS to user-service (adjust paths, host, and port):

```shell script
# bash
curl "https://localhost:8084/actuator/health" \
  --cert client.crt \
  --key client.key \
  --cacert ca.crt
```

## Database

Database configuration is centralized in configs/database-config.yml and imported into services that need persistence.
Provide the appropriate environment variables (JDBC URL, username, password) or override the properties via profiles.

Typical steps:

- Start your database locally
- Set the required datasource environment variables
- Launch the services

## Service discovery and gateway

- The service discovery server exposes a dashboard where registered services can be observed.
- The gateway forwards external traffic to internal services. Routes, filters, and SSL settings are configured in
  configs/gateway-service.yml.

Refer to configs/ for the assigned ports and routing paths.

## Development tips

- Use your IDE’s Lombok plugin.
- Ensure the project SDK is set to Java 20.
- If you run services from your IDE, make sure Run/Debug configurations inherit the required environment
  variables/secrets for SSL and database.
- When changing configs/*.yml, refresh or restart the affected services (depending on whether Spring Cloud Bus/refresh
  is set up).

## Troubleshooting

- SSL handshake failures: verify keystore/truststore paths and passwords, certificate validity, and that client-auth
  matches your scenario.
- Service not registering/discoverable: confirm discovery client configuration is imported and discovery server is
  running.
- Config not applied: ensure Config Server is running and the services can reach it. Check application name and profile
  alignment with files under configs/.
- Database connection errors: confirm reachable database, correct credentials, and driver settings.

## Contributing

- Create feature branches
- Use meaningful commit messages
- Add/update configuration under configs/ as needed
- Open a PR with a clear description and test steps

