# StudyCord

StudyCord is a discord-inspired plateform designed for school collaboration.

---

## Project Structure

```
studycord/
├── src/main/java/com/studycord/
│   │
│   ├── StudyCordApplication.java          # Entry point
│   │
│   ├── model/                             # Neo4j node entities
│   │   ├── User.java
│   │   ├── Server.java
│   │   └── Channel.java
│   │
│   ├── repository/                        # Spring Data Neo4j repositories
│   │   ├── UserRepository.java
│   │   ├── ServerRepository.java
│   │   └── ChannelRepository.java
│   │
│   ├── service/                           # Business logic
│   │   ├── UserService.java
│   │   ├── ServerService.java
│   │   └── ChannelService.java
│   │
│   ├── controller/                        # REST endpoints
│   │   ├── UserController.java
│   │   ├── ServerController.java
│   │   └── ChannelController.java
│   │
│   ├── dto/                               # Request/Response objects (keeps model clean)
│   │   ├── UserDTO.java
│   │   ├── ServerDTO.java
│   │   └── ChannelDTO.java
│   │
│   ├── algorithm/                         # Your custom graph algorithms
│   │   ├── BFSService.java                # BFS traversal (server suggestions)
│   │   └── CosineSimService.java          # Duplicate thread detection
│   │
│   └── config/                            # Config classes
│       ├── Neo4jConfig.java
│       └── SwaggerConfig.java
│
└── src/main/resources/
    └── application.properties
```
