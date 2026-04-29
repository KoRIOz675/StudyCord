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


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/servers")
public class ServerController {

    private final ServerService serverService;


    @PostMapping
    public ResponseEntity<Server> createServer(@RequestBody ServerDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(serverService.createServer(dto));
    }

    @GetMapping
    public ResponseEntity<List<Server>> getAllServers() {
        return ResponseEntity.ok(serverService.getAllServers());
    }

    @GetMapping("/{serverId}")
    public ResponseEntity<Server> getServerById(@PathVariable Long serverId) {
        return ResponseEntity.ok(serverService.getServerById(serverId));
    }

    @GetMapping("/school/{school}")
    public ResponseEntity<List<Server>> getServersBySchool(@PathVariable String school) {
        return ResponseEntity.ok(serverService.getServersBySchool(school));
    }
    

}
