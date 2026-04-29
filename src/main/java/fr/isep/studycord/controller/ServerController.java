package fr.isep.studycord.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import fr.isep.studycord.dto.ServerDTO;
import fr.isep.studycord.model.Server;
import fr.isep.studycord.service.ServerService;
import lombok.RequiredArgsConstructor;

/**
 * REST controller exposing server endpoints under {@code /api/servers}.
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/servers")
public class ServerController {

    private final ServerService serverService;

    /**
     * Creates a new server.
     *
     * HTTP {@code POST /api/servers}
     *
     * @param dto the server payload (name, subject, school)
     * @return {@code 201 Created} with the persisted {@link Server} in the body
     */
    @PostMapping
    public ResponseEntity<Server> createServer(@RequestBody ServerDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(serverService.createServer(dto));
    }

    /**
     * Returns all servers.
     *
     * HTTP {@code GET /api/servers}
     *
     * @return {@code 200 OK} with a list of all {@link Server} objects
     */
    @GetMapping
    public ResponseEntity<List<Server>> getAllServers() {
        return ResponseEntity.ok(serverService.getAllServers());
    }

    /**
     * Returns a single server by its ID.
     *
     * HTTP {@code GET /api/servers/{serverId}}
     *
     * @param serverId the ID of the server to retrieve
     * @return {@code 200 OK} with the matching {@link Server}
     */
    @GetMapping("/{serverId}")
    public ResponseEntity<Server> getServerById(@PathVariable Long serverId) {
        return ResponseEntity.ok(serverService.getServerById(serverId));
    }

    /**
     * Returns all servers belonging to the given school.
     *
     * HTTP {@code GET /api/servers/school/{school}}
     *
     * @param school the school name to filter by
     * @return {@code 200 OK} with a list of matching {@link Server} objects
     */
    @GetMapping("/school/{school}")
    public ResponseEntity<List<Server>> getServersBySchool(@PathVariable String school) {
        return ResponseEntity.ok(serverService.getServersBySchool(school));
    }

}
