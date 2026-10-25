# 📋 Global

> **Project status:** Developing user Authentication and Authorization logic

> **Closed tasks:**
  - [ ] Authentication and Authorization logic
  - [ ] ... (Pending planning)

---

## 📅 Log dated [24.10.2026]

### 🎯  Current tasks
- [ ] Implement authentication logic
- [ ] Implement authorization logic
- [ ] Implement JWT token generation

---

### 💻 Backend tasks (Tasks for @pepegazxc)
- [ ] Implement user password hashing
- [ ] Implement login method
- [ ] Implement email verification flow
- [ ] Implement JWT token issuance
- [ ] Implement JWT token refreshing
- [ ] Write unit tests
- [ ] Implement global exception handling
- [ ] ... (Scope is subject to expansion)

---

### ⚙️ DevOps tasks (Tasks for @LinuxMaster1234)
- [ ] Configure Docker Compose with a PostgreSQL container
- [ ] Create a Dockerfile for the application container
- [ ] Create a .env template file for environment variables 
- [ ] Refactor application.yaml to use environment variables 
- [ ] ... (Scope is subject to expansion)

#### 1. Docker Compose 
> There is a file at the root of our project named **"compose.yaml** which should contain the configuration for the PostgreSQL container.
Here is a basic example of simple PostgreSQL 16-alpine configuration:

``` yaml
services:
  postgres:
    image: postgres:16-alpine
    container_name: postgres_db
    restart: always

    environment:
      POSTGRES_DB: db_name
      POSTGRES_USER: user
      POSTGRES_PASSWORD: super_secret_password

    ports:
      - "5432:5432"

    volumes:
      - pgdata:/var/lib/postgresql/data

volumes:
  pgdata:
```

> A quick explanation of terms:
- *services*: The section where all services/containers used in the project are defined
- *postgres*: The namme of this specific service
- *image*: The Docker image the Docker Hub will pull for this
- *container_name*: The custom name assigned to the running container
- *restart*: Defines the restart policy (`always` means Docker will always restart the container if it stops)
- *environment*:The environment variables passed into the container
- *ports*: Maps the host port to the container port
- *volumes*: Persistent storage location to ensure the database data is not lost when the container restarts

Here is [an official guide for PostgreSQL container for Docker and Docker compose](https://docs.docker.com/guides/postgresql/)

Also a task:
- [ ] Study and understand the writing structure and main terms of a compose file on your own

#### 2. Dockerfile 
> This file does not exist yet at the root of our project. The first step is to create a file named **Dockerfile** (with a capital D, no extension) at the project root.
Here is a basic example of a simple build configuration for a Java/Spring Boot application:

``` dockerfile
FROM eclipse-temurin:21-jre-alpine

WORKDIR /app

COPY build/libs/*.jar app.jar

ENTRYPOINT ["java", "-jar", "app.jar"]
```

> Quick explanation of terms:
- **FROM**: Defines the base image (the environment and Java runtime version) used to run the application.
- **WORKDIR**: Sets the working directory inside the container where all subsequent commands will run.
- **COPY**: Copies the compiled application file (JAR) from the host machine into the container's working directory.
- **ENTRYPOINT**: Configures the default command that will execute when the container starts up.

Also a task:
- [ ] Take the initiative to study the core terminology, understand how commands interact with one another, and explore more optimized ways to write Dockerfiles.

#### 3. .env file
> This file will store all of our secrets and environment variables. This file **MUST NOT BE COMMITTED OR PUSHED TO THE REMOTE REPOSITORY**. You must also update the `.gitignore` file.

Here are the steps to complete this task:

1. Open the **.gitignore** file at the root of the project. This file tells Git which files and folders it should ignore. 
Add the following lines to the bottom of the file:
```bash
### .ENV ###
.env
```
This ensures the `.env` file is safely ignored by Git.

2. Create a `.env` file at the root of the project and fill it with your initial secrets. 
The variables should follow this format:
```bash
POSTGRES_DB_PASSWORD=my_super_secret
```

Also a task:
- [ ] Define the PostgreSQL database username and password in the `.env` file (you can leave the connection string as is).

#### 4. How to edit the application.yaml file
> The `application.yaml` file stores the configuration for the project. Since the authentication service is currently under development, you can find the file at the following path:
`authentication-service/src/main/resources/application.yaml`

Once you open the file, complete the following steps:

1. Under the `spring:` block, add the following lines:
```yaml
spring:
  config:
    import: optional:file:.env[.properties]
```
> *Note: These lines tell Spring Boot to look into your `.env` file and load its secrets as environment variables.*

2. Locate the database username and password configurations under the `datasource` block. Replace the hardcoded credentials with references to your `.env` variables using the `${VARIABLE_NAME}` format. 

Example configuration:
```yaml
spring:
  datasource:
    url: jdbc:postgresql://localhost:5432/db_name
    username: ${POSTGRES_USERNAME}
    password: ${POSTGRES_PASSWORD}
```
---
