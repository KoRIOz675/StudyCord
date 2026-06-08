package fr.isep.studycord.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.data.neo4j.core.Neo4jClient;
import org.springframework.stereotype.Service;

import fr.isep.studycord.dto.UserDTO;
import fr.isep.studycord.model.Server;
import fr.isep.studycord.model.User;
import fr.isep.studycord.repository.UserRepository;
import lombok.RequiredArgsConstructor;

/**
 * Service layer for user management.
 *
 * Handles user registration, lookup, and server membership operations.
 */
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final Neo4jClient neo4jClient;

    /**
     * Creates and persists a new user from the provided DTO.
     *
     * @param dto the user data (username, email, role, school)
     * @return the saved {@link User} with its generated ID
     */
    public User createUser(UserDTO dto) {
        User user = new User();
        user.setUsername(dto.getUsername());
        user.setEmail(dto.getEmail());
        user.setRole(dto.getRole());
        user.setSchool(dto.getSchool());
        return userRepository.save(user);
    }

    /**
     * Retrieves a user by their unique username.
     *
     * @param username the username to search for
     * @return the matching {@link User}
     * @throws RuntimeException if no user with the given username exists
     */
    public User getUserByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found: " + username));
    }

    /**
     * Returns all users stored in the database.
     *
     * @return a list of all {@link User} objects (never {@code null})
     */
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    /**
     * Returns all users who are members of the specified server.
     *
     * @param serverId the ID of the server whose members should be retrieved
     * @return a list of {@link User} objects (never {@code null})
     * @throws RuntimeException if no server with {@code serverId} exists
     */
    public List<User> getUsersByServerId(Long serverId) {
        return userRepository.findByServerId(serverId);
    }

    /**
     * Adds the specified server to the user's membership list.
     *
     * @param userId the ID of the user who wants to join
     * @param serverId the ID of the server to join
     * @return the updated {@link User} with the new server membership persisted
     * @throws RuntimeException if the user or server does not exist
     */
    public User joinServer(Long userId, Long serverId) {
        neo4jClient
                .query("MATCH (u:User) WHERE id(u) = $userId "
                        + "MATCH (s:Server) WHERE id(s) = $serverId "
                        + "MERGE (u)-[:MEMBER_OF]->(s)")
                .bindAll(Map.of("userId", userId, "serverId", serverId))
                .run();
        return userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found: " + userId));
    }

    /**
     * Retrieves all servers that the specified user is a member of.
     *
     * @param userId the ID of the user whose servers to retrieve
     * @return a list of {@link Server} objects (never {@code null})
     * @throws RuntimeException if no user with {@code userId} exists
     */
    public List<Server> getServersByUserId(Long userId) {
        User user = userRepository.findByIdWithServers(userId)
                .orElseThrow(() -> new RuntimeException("User not found: " + userId));
        return new ArrayList<>(user.getServers());
    }
}
