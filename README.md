# Changa API

A modern RESTful API built with **Spring Boot 4.1.0** and **Java 21**, designed to provide robust backend services with comprehensive data management capabilities.

## 📋 Table of Contents

- [Overview](#overview)
- [Features](#features)
- [Prerequisites](#prerequisites)
- [Installation](#installation)
- [Configuration](#configuration)
- [Project Structure](#project-structure)
- [Building & Running](#building--running)
- [API Documentation](#api-documentation)
- [Database Setup](#database-setup)
- [Testing](#testing)
- [Development Guidelines](#development-guidelines)
- [Contributing](#contributing)
- [License](#license)

## 🎯 Overview

Changa API is a Spring Boot-based backend service that provides RESTful endpoints for managing and accessing data. Built with modern Java practices and best-in-class frameworks, it's designed for scalability, maintainability, and ease of integration.

### Tech Stack

- **Runtime**: Java 21
- **Framework**: Spring Boot 4.1.0
- **Build Tool**: Maven
- **ORM**: Spring Data JPA / Hibernate
- **Database**: MySQL
- **Documentation**: SpringDoc OpenAPI (Swagger)
- **Validation**: Spring Validation
- **Code Generation**: Lombok
- **Testing**: Spring Boot Test Suite with H2 in-memory database

## ✨ Features

- ✅ **RESTful API Design** - Clean, intuitive endpoints
- ✅ **Database Integration** - MySQL with JPA/Hibernate ORM
- ✅ **Auto-generated API Documentation** - Swagger UI via SpringDoc OpenAPI
- ✅ **Input Validation** - Built-in request validation
- ✅ **Comprehensive Testing** - Unit and integration tests
- ✅ **Clean Code** - Leverages Lombok for boilerplate reduction
- ✅ **Spring MVC** - Full web framework support

## 📦 Prerequisites

Before you begin, ensure you have the following installed:

- **Java 21** or higher
  - Download from [oracle.com](https://www.oracle.com/java/technologies/downloads/#java21) or use [OpenJDK](https://openjdk.java.net/)
  - Verify installation: `java -version`

- **Maven 3.8.0** or higher
  - Download from [maven.apache.org](https://maven.apache.org/download.cgi)
  - Verify installation: `mvn --version`

- **MySQL 8.0** or higher
  - Download from [mysql.com](https://dev.mysql.com/downloads/mysql/)
  - Or use Docker: `docker run --name mysql -e MYSQL_ROOT_PASSWORD=root -p 3306:3306 -d mysql:8.0`

- **Git** - For version control

## 🚀 Installation

### 1. Clone the Repository

```bash
git clone https://github.com/lopezenzoa/changa-api.git
cd changa-api
```

### 2. Install Dependencies

Maven will automatically download all dependencies defined in `pom.xml`:

```bash
mvn clean install
```

This command will:
- Clean any previous builds
- Download all project dependencies
- Compile the source code
- Run the test suite
- Package the application

## ⚙️ Configuration

### 1. Database Configuration

Create or edit `src/main/resources/application.properties` (or `application.yml`):

```properties
# MySQL Database Configuration
spring.datasource.url=jdbc:mysql://localhost:3306/changa_db
spring.datasource.username=root
spring.datasource.password=your_password
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver

# JPA/Hibernate Configuration
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=false
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.MySQL8Dialect
spring.jpa.properties.hibernate.format_sql=true

# Server Configuration
server.port=8080
server.servlet.context-path=/api

# Logging
logging.level.root=INFO
logging.level.com.portfolio=DEBUG
```

### 2. Create the Database

If the database doesn't exist:

```sql
CREATE DATABASE changa_db;
```

The application will automatically create tables based on your JPA entities when `ddl-auto=update` is set.

### 3. Environment Variables (Optional)

For production deployments, use environment variables:

```bash
export DB_URL=jdbc:mysql://your-host:3306/changa_db
export DB_USER=your_user
export DB_PASSWORD=your_password
export SERVER_PORT=8080
```

Then reference them in `application.properties`:

```properties
spring.datasource.url=${DB_URL}
spring.datasource.username=${DB_USER}
spring.datasource.password=${DB_PASSWORD}
server.port=${SERVER_PORT:8080}
```

## 📁 Project Structure

```
changa-api/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/portfolio/
│   │   │       ├── controller/      # REST Controllers
│   │   │       ├── service/         # Business Logic
│   │   │       ├── repository/      # Data Access Layer
│   │   │       ├── entity/          # JPA Entities
│   │   │       ├── dto/             # Data Transfer Objects
│   │   │       ├── exception/       # Custom Exceptions
│   │   │       └── ChangaApiApplication.java
│   │   └── resources/
│   │       ├── application.properties
│   │       └── application-prod.properties
│   └── test/
│       ├── java/
│       │   └── com/portfolio/       # Test Classes
│       └── resources/
│           └── application-test.properties
├── pom.xml
├── README.md
└── .gitignore
```

## 🏗️ Building & Running

### Build the Application

```bash
# Full build with tests
mvn clean package

# Build without running tests
mvn clean package -DskipTests

# Compile only
mvn compile
```

### Run the Application

#### Using Maven

```bash
mvn spring-boot:run
```

#### Using Java Directly

```bash
# After building with `mvn clean package`
java -jar target/changa-api-0.0.1-SNAPSHOT.jar
```

#### Custom Configuration

```bash
java -jar target/changa-api-0.0.1-SNAPSHOT.jar --server.port=9090 --spring.profiles.active=prod
```

### Verify the Application

Once running, the API will be available at:

- **Base URL**: `http://localhost:8080/api`
- **Swagger UI**: `http://localhost:8080/swagger-ui.html`
- **OpenAPI JSON**: `http://localhost:8080/v3/api-docs`

## 📚 API Documentation

This project uses **SpringDoc OpenAPI** to automatically generate API documentation.

### Accessing Documentation

1. **Swagger UI** (Interactive):
   ```
   http://localhost:8080/swagger-ui.html
   ```

2. **OpenAPI JSON** (Machine-readable):
   ```
   http://localhost:8080/v3/api-docs
   ```

### Documenting Your Endpoints

Use `@Operation` and `@Parameter` annotations on your controllers:

```java
@GetMapping("/{id}")
@Operation(summary = "Get user by ID", description = "Retrieve a specific user by their ID")
public ResponseEntity<UserDTO> getUserById(
    @Parameter(description = "User ID", required = true)
    @PathVariable Long id) {
    return ResponseEntity.ok(userService.findById(id));
}
```

## 🗄️ Database Setup

### Manual Setup

```bash
# Connect to MySQL
mysql -u root -p

# Create database
CREATE DATABASE changa_db;
USE changa_db;

# Hibernate will create tables automatically on startup
```

### Docker Setup (Recommended for Development)

```bash
# Start MySQL container
docker run --name changa-mysql \
  -e MYSQL_ROOT_PASSWORD=root \
  -e MYSQL_DATABASE=changa_db \
  -p 3306:3306 \
  -d mysql:8.0

# Verify connection
docker logs changa-mysql
```

## 🧪 Testing

### Run All Tests

```bash
mvn test
```

### Run Specific Test Class

```bash
mvn test -Dtest=UserServiceTest
```

### Run Tests with Coverage

```bash
mvn test jacoco:report
```

### Test Configuration

Tests use an in-memory H2 database (configured in `application-test.properties`):

```properties
spring.datasource.url=jdbc:h2:mem:testdb
spring.datasource.driverClassName=org.h2.Driver
spring.jpa.database-platform=org.hibernate.dialect.H2Dialect
spring.jpa.hibernate.ddl-auto=create-drop
```

## 💻 Development Guidelines

### Code Style

- Follow [Google Java Style Guide](https://google.github.io/styleguide/javaguide.html)
- Use Lombok to reduce boilerplate (`@Getter`, `@Setter`, `@Builder`, etc.)
- Keep classes focused and follow SOLID principles

### Layered Architecture

```
Controller (HTTP Endpoints)
    ↓
Service (Business Logic)
    ↓
Repository (Data Access)
    ↓
Entity (Database Models)
```

### Creating a New Feature

1. **Create Entity**
   ```java
   @Entity
   @Data
   @NoArgsConstructor
   public class YourEntity {
       @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
       private Long id;
   }
   ```

2. **Create Repository**
   ```java
   public interface YourRepository extends JpaRepository<YourEntity, Long> {}
   ```

3. **Create Service**
   ```java
   @Service
   public class YourService {
       @Autowired
       private YourRepository repository;
   }
   ```

4. **Create Controller**
   ```java
   @RestController
   @RequestMapping("/api/your-endpoint")
   public class YourController {}
   ```

## 🤝 Contributing

Contributions are welcome! Please follow these steps:

1. **Fork** the repository
2. **Create** a feature branch: `git checkout -b feature/your-feature`
3. **Commit** your changes: `git commit -am 'Add new feature'`
4. **Push** to the branch: `git push origin feature/your-feature`
5. **Submit** a Pull Request with a clear description

### Before Submitting a PR

- ✅ Run `mvn clean test` to ensure all tests pass
- ✅ Follow the code style guidelines
- ✅ Add unit tests for new features
- ✅ Update documentation if needed

## 📝 License

This project is licensed under the MIT License - see the LICENSE file for details.

---

## 🆘 Troubleshooting

### MySQL Connection Issues

**Problem**: `Communications link failure`

**Solution**:
```bash
# Verify MySQL is running
mysql -u root -p

# Check port (default 3306)
netstat -an | grep 3306

# Update application.properties with correct credentials
spring.datasource.url=jdbc:mysql://localhost:3306/changa_db
```

### Java Version Issues

**Problem**: `Unsupported Java version`

**Solution**:
```bash
# Check Java version
java -version

# Set JAVA_HOME
export JAVA_HOME=/path/to/java21
```

### Build Failures

**Problem**: `Build fails during compilation`

**Solution**:
```bash
# Clean and rebuild
mvn clean install -U

# If Lombok issues, ensure annotation processing is enabled in your IDE
```

## 📞 Support

For issues, questions, or suggestions:
- Open an [Issue](https://github.com/lopezenzoa/changa-api/issues)
- Submit a [Pull Request](https://github.com/lopezenzoa/changa-api/pulls)

---

**Happy Building!** 🚀
