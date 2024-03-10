# USMSocial Application

Welcome to the USMSocial application repository! This Java-based Spring Boot project offers user authentication through a RESTful API. Below, you'll find examples of REST API requests and their corresponding responses.

## Development Setup

### Requirements

- Java 17
- MySQL server
- Maven

#### MacOS (Homebrew) Download Requirements

Install homebrew package manager: `/bin/bash -c "$(curl -fsSL https://raw.githubusercontent.com/Homebrew/install/HEAD/install.sh)"`

Install MySQL with homebrew: `brew install mysql`

Install Java 17 with homebrew: `brew install openjdk@17 `

Install maven with homebrew: `brew install maven`

#### Windows (Chocolatey) Download Requirements

Install chocolatey package manager: [Chocolate Installation Guide](https://chocolatey.org/install)

Install MySQL with chocolatey: `choco install mysql`

Install Java 17 with chocolatey: `choco install openjdk --version=17.0.2`

Install Maven with chocolatey: `choco install maven`

### Environment Variables

| Name                     | Description                    |
| ------------------------ | ------------------------------ |
| SPRING_DATABASE_PASSWORD | The password for root username |
| LOGIN_SMTP_USERNAME      | The username for gmail account |
| LOGIN_SMTP_PASSWORD      | The password for gmail account |

### Database setup

1. Login into the mysql database.
2. Create database spring_database: `CREATE DATABASE spring_database;`
3. Create root user if does not exist: 
    1. `CREATE USER 'root'@'localhost' IDENTIFIED BY '{$SPRING_DATABASE_PASSWORD}';`
    2. `GRANT ALL PRIVILEGES ON spring_database.* TO 'root'@'localhost';`
    3. `FLUSH PRIVILEGES;`

### Run Backend to localhost

Start up backend by running `mvn spring-boot:run "-Dspring-boot.run.profiles=dev"`

## Authentication Endpoints

### Register a New User

- **URL:** `/api/v1/auth/register`
- **Method:** `POST`
- **Request Body:**
  ```json
  {
    "email": "user@example.com"
    "password": "password",
  }
  ```
### Authenticate User
- **URL:** `/api/v1/auth/authenticate`
- **Method:** `POST`
- **Request Body:**
  ```json
  {
    "email": "user@example.com"
    "password": "password",
  }
  ```
### Response
```json
{
  "token": "some long token"
}
```
