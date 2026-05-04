# StudyCord

StudyCord is a discord-inspired plateform designed for school collaboration.

---

## Tech Stack

- **Java 21** + **Spring Boot 4**
- **Neo4j** (graph database)
- **Spring Data Neo4j** (ORM)
- **Lombok** (boilerplate reduction)
- **Springdoc OpenAPI** (Swagger UI)
- **Docker** (Neo4j container)

## Project Structure

```
studycord/
├── src/main/java/com/studycord/
│   ├── algorithm/                         # Your custom graph algorithms
│   ├── controller/                        # REST endpoints
│   ├── dto/                               # Request/Response objects (keeps model clean)
│   ├── model/                             # Neo4j node entities
│   ├── repository/                        # Spring Data Neo4j repositories
│   ├── service/                           # Business logic
│   ├── StudyCordApplication.java          # Entry point
└── src/main/resources/
    └── application.yaml
```

---

## Getting Started

### Prerequisites

- Java 21+
- Maven
- Docker & Docker Compose

### 1. Clone the repository

```bash
git clone https://github.com/KoRIOz675/StudyCord.git
cd studycord
```

### 2. Start the Neo4j database

```bash
docker-compose up -d
```

### 3. Run the application

```bash
./mvnw spring-boot:run
```

### 4. Initialize sample data

```bash
./init-data.sh
```

This script creates 3 servers, 3 users, and joins them to different servers
to test the BFS recommendation algorithm.

---

## API Documentation

Once the application is running, open Swagger UI:

`http://localhost:8080/swagger-ui.html`

### Main endpoints

| Method | Endpoint                                   | Description                  |
| ------ | ------------------------------------------ | ---------------------------- |
| POST   | `/api/users`                               | Create a user                |
| GET    | `/api/users`                               | Get all users                |
| GET    | `/api/users/{username}`                    | Get user by username         |
| POST   | `/api/users/{userId}/join/{serverId}`      | Join a server                |
| POST   | `/api/servers`                             | Create a server              |
| GET    | `/api/servers`                             | Get all servers              |
| GET    | `/api/servers/{id}`                        | Get server by ID             |
| GET    | `/api/servers/school/{school}`             | Get servers by school        |
| POST   | `/api/channels/{serverId}`                 | Create a channel in a server |
| GET    | `/api/channels/server/{serverId}`          | Get channels of a server     |
| GET    | `/api/algorithms/suggest-servers/{userId}` | BFS server suggestions       |

---

## Team

- Thomasset Léopold
- Mafille Thomas
- Nakoula Julien
