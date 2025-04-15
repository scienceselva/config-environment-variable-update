# Dynamic Configuration Service

A Spring Boot service that allows dynamic updates of configuration properties via REST API.

## Features

- Update application properties at runtime
- Update environment variables (JVM only)
- List all current properties
- Basic authentication with role-based access

## Quick Start

1. Build the application:
   ```bash
   mvn clean install

## sample data

http://localhost:8080/api/config/update?key=app.timeout&value=5000
http://localhost:8080/api/config/update?key=app.message&value=Hello%20World
http://localhost:8080/api/config/list