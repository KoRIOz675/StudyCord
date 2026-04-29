package fr.isep.studycord.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import fr.isep.studycord.algorithm.BFSService;
import fr.isep.studycord.model.Server;
import lombok.RequiredArgsConstructor;

/**
 * REST controller exposing algorithm endpoints under {@code /api/algorithms}.
 */
@RestController
@RequestMapping("/api/algorithms")
@RequiredArgsConstructor
public class BFSController {

    private final BFSService bfsService;

    /**
     * Suggests servers for a user.
     *
     * HTTP {@code GET /api/algorithms/suggest-servers/{userId}}
     *
     * @param userId the id of the user for whom to compute suggestions
     * @return a list of suggested {@link Server} instances ordered by discovery
     * distance (closest first). Never {@code null}.
     * @throws RuntimeException if the user with the given id cannot be found
     */
    @GetMapping("/suggest-servers/{userId}")
    public ResponseEntity<List<Server>> suggestServers(@PathVariable Long userId) {
        return ResponseEntity.ok(bfsService.suggestServers(userId));
    }
}
