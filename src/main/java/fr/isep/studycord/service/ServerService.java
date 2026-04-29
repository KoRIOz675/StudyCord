package fr.isep.studycord.service;

import fr.isep.studycord.dto.ServerDTO;
import fr.isep.studycord.model.Server;
import fr.isep.studycord.repository.ServerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ServerService {

    private final ServerRepository serverRepository;

    public Server createServer(ServerDTO dto) {
        Server server = new Server(null, dto.getName(), dto.getSubject(), dto.getSchool(), null);
        return serverRepository.save(server);
    }

    public Server getServerById(Long id) {
        return serverRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Server not found: " + id));
    }

    public List<Server> getAllServers() {
        return serverRepository.findAll();
    }

    public List<Server> getServersBySchool(String school) {
        return serverRepository.findAllBySchool(school);
    }

}
