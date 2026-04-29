package fr.isep.studycord.service;

import fr.isep.studycord.dto.UserDTO;
import fr.isep.studycord.model.User;
import fr.isep.studycord.model.Server;
import fr.isep.studycord.repository.UserRepository;
import fr.isep.studycord.repository.ServerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final ServerRepository serverRepository;

    public User createUser(UserDTO dto) {
        User user = new User(null, dto.getUsername(), dto.getEmail(), dto.getRole(), dto.getSchool(), null);
        return userRepository.save(user);
    }

    public User getUserByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found: " + username));
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public User joinServer(Long userId, Long serverId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found: " + userId));
        Server server = serverRepository.findById(serverId)
                .orElseThrow(() -> new RuntimeException("Server not found: " + serverId));

        user.getServers().add(server);
        return userRepository.save(user);
    }
}
