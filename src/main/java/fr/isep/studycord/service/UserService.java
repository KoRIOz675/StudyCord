package fr.isep.studycord.service;

import java.util.ArrayList;
import java.util.List;

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
    // private final ServerRepository serverRepository;

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
        // User user = userRepository.findById(userId)
        //         .orElseThrow(() -> new RuntimeException("User not found: " + userId));
        // Server server = serverRepository.findById(serverId)
        //         .orElseThrow(() -> new RuntimeException("Server not found: " + serverId));

        // Create the MEMBER_OF relationship explicitly in Neo4j
        userRepository.createMembershipRelationship(userId, serverId);

        // Return the user with updated relationships
        return userRepository.findById(userId).get();
    }

    /**
     * Retrieves all servers that the specified user is a member of.
     *
     * @param userId the ID of the user whose servers to retrieve
     * @return a list of {@link Server} objects (never {@code null})
     * @throws RuntimeException if no user with {@code userId} exists
     */
    public List<Server> getServersByUserId(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found: " + userId));
        return new ArrayList<>(user.getServers());
    }
}
