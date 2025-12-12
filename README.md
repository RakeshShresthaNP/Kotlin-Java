Java >= 17

javawebservice is a Javalin (Service Oriented Architecture) based MicroFramework rest api server application.

Javalin has automated plugins for JSON mapping, Web Sockets, template rendering, and OpenAPI (Swagger).

As to me it's better than spark framework for many startup projects.

This codebase demonstrates implementation of crud operations, authentication, routing, pagination, unit test and openapi (partial).

Full OpenApi or Swagger automated documentation of implemented api and parameters can be put adding some extra lines of code only. 

This we normally enable in demo app not operational app.

### Enter the javawebservice

### Build and Run Locally

```bash
gradlew build
java -jar build/libs/kotlinapp-1.0.jar --add-to-start=conscrypt
```
### Clean compiled resources

```bash
gradlew clean
```

### The server is running

http://localhost:8080/api

### HTTPS
1. Conscrypt : A Java Security Provider
2. Conscrypt-openjdk-uber : Library from Uber

### Kotlinapp Extra Dependency

1. KOIN : A pragmatic lightweight dependency injection framework for Kotlin developers
2. Jackson : Jackson has been known as "the Java JSON library" or "the best JSON parser for Java"
3. Java-jwt : A Java implementation of JSON Web Token (JWT) - RFC 7519
4. Exposed : A lightweight ORM framework for Kotlin - H2, MySQL, MariaDB, Oracle, PostgreSQL, MSSQL, SQLite
5. HikariCP : HikariCP is a "zero-overhead" lightweight JDBC connection pool
6. MariaDB : MariaDB Database Engine

### Unit Testing
1. Unirest-java : Simplified, lightweight HTTP client library
2. JUnit : Java Unit Test Framework


