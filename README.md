# USMSocial Application

Welcome to the USMSocial application repository! This Java-based Spring Boot project offers user authentication through a RESTful API. Below, you'll find examples of REST API requests and their corresponding responses.

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
