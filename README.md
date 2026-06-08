# StudyCord

StudyCord is a Discord-inspired platform designed for school collaboration.

---

## Tech Stack

**Backend**
- **Java 21** + **Spring Boot 4**
- **Neo4j** (graph database)
- **Spring Data Neo4j** (ORM)
- **Lombok** (boilerplate reduction)
- **Springdoc OpenAPI** (Swagger UI)
- **Docker** (Neo4j container)

**Frontend**
- **React** (single-page app)

---

## Project Structure

```
studycord/
├── frontend/                              # React app
│   └── src/
│       ├── api/                           # API call helpers
│       └── components/                    # UI components
├── src/main/java/fr/isep/studycord/
│   ├── algorithm/                         # Graph algorithms (BFS, Cosine Sim, DBSCAN)
│   ├── controller/                        # REST endpoints
│   ├── dto/                               # Request/Response objects
│   ├── model/                             # Neo4j node entities
│   ├── repository/                        # Spring Data Neo4j repositories
│   ├── service/                           # Business logic
│   └── StudycordApplication.java          # Entry point
└── src/main/resources/
    └── application.yaml
```

---

## Getting Started

### Prerequisites

- Java 21+
- Maven
- Docker & Docker Compose
- Node.js 18+

### 1. Clone the repository

```bash
git clone https://github.com/KoRIOz675/StudyCord.git
cd studycord
```

### 2. Start the Neo4j database

```bash
docker-compose up -d
```

### 3. Run the backend

```bash
./mvnw spring-boot:run
```

### 4. Run the frontend

```bash
cd frontend
npm install
npm start
```

### 5. Initialize sample data

```bash
./init-data.sh
```

Seeds 5 servers, 6 users, 13 channels, ~35 messages with BFS-friendly memberships, then triggers a full reindex.

---

## API Documentation

Swagger UI: `http://localhost:8080/swagger-ui.html`

### Users

| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/api/users` | Create a user |
| GET | `/api/users` | Get all users |
| GET | `/api/users/{id}` | Get user by ID |
| GET | `/api/users/{id}/servers` | Get servers for a user |
| POST | `/api/users/{userId}/join/{serverId}` | Join a server |

### Servers

| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/api/servers` | Create a server |
| GET | `/api/servers` | Get all servers |
| GET | `/api/servers/{id}` | Get server by ID |
| GET | `/api/servers/school/{school}` | Get servers by school |

### Channels

| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/api/channels/{serverId}` | Create a channel in a server |
| GET | `/api/channels/server/{serverId}` | Get channels of a server |

### Messages

| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/api/messages/{channelId}` | Post a message |
| GET | `/api/messages/channel/{channelId}` | Get messages in a channel |

### Algorithms

| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/api/algorithms/suggest-servers/{userId}` | BFS server suggestions |
| GET | `/api/algorithms/similar-messages/{channelId}` | Find similar messages (cosine similarity) |
| POST | `/api/algorithms/reindex-channel/{channelId}` | Trigger TF-IDF reindex (202 Accepted) |
| GET | `/api/algorithms/isolated-users` | Detect isolated users (DBSCAN) |

---

## Algorithms

### BFS — Server Suggestions
Traverses the user graph to find servers joined by users with common memberships. Returns ranked suggestions for servers the current user hasn't joined yet.

### Cosine Similarity + TF-IDF — Similar Message Detection
Builds a TF-IDF vector for each message in a channel (smoothed IDF: `log((1+N)/(1+df)) + 1`), then ranks messages by cosine similarity. Reindex runs daily at midnight and can be triggered manually. Used in the UI for teacher search and auto duplicate detection on post.

### DBSCAN — Isolation Detection
Identifies users with few or no server connections. Used in the frontend to surface an overlay suggesting servers to join.

---

## Team

- Thomasset Léopold
- Mafille Thomas
- Nakoula Julien
