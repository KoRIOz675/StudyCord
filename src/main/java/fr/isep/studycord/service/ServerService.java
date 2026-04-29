package fr.isep.studycord.service;

import fr.isep.studycord.dto.ServerDTO;
import fr.isep.studycord.model.Server;
import fr.isep.studycord.repository.ServerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Service layer for server management.
 *
 * Handles creation of servers and various retrieval operations.
 */
@Service
@RequiredArgsConstructor
public class ServerService {

    private final ServerRepository serverRepository;

    /**
     * Creates and persists a new server from the provided DTO.
     *
     * @param dto the server data (name, subject, school)
     * @return the saved {@link Server} with its generated ID
     */
    public Server createServer(ServerDTO dto) {
        Server server = new Server(null, dto.getName(), dto.getSubject(), dto.getSchool(), null);
        return serverRepository.save(server);
    }

    /**
     * Retrieves a server by its ID.
     *
     * @param id the server ID
     * @return the matching {@link Server}
     * @throws RuntimeException if no server with the given ID exists
     */
    public Server getServerById(Long id) {
        return serverRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Server not found: " + id));
    }

    /**
     * Returns all servers stored in the database.
     *
     * @return a list of all {@link Server} objects (never {@code null})
     */
    public List<Server> getAllServers() {
        return serverRepository.findAll();
    }

    /**
     * Returns all servers that belong to the given school.
     *
     * @param school the school name to filter by
     * @return a list of matching {@link Server} objects (never {@code null})
     */
    public List<Server> getServersBySchool(String school) {
        return serverRepository.findAllBySchool(school);
    }

}
