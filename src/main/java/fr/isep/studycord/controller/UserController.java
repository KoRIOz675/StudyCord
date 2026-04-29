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

import fr.isep.studycord.dto.UserDTO;
import fr.isep.studycord.model.User;
import fr.isep.studycord.service.UserService;
import lombok.RequiredArgsConstructor;

/**
 * REST controller exposing user endpoints under {@code /api/users}.
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    /**
     * Registers a new user.
     *
     * HTTP {@code POST /api/users}
     *
     * @param dto the user payload (username, email, role, school)
     * @return {@code 201 Created} with the persisted {@link User} in the body
     */
    @PostMapping
    public ResponseEntity<User> createUser(@RequestBody UserDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(userService.createUser(dto));
    }

    /**
     * Returns all users.
     *
     * HTTP {@code GET /api/users}
     *
     * @return {@code 200 OK} with a list of all {@link User} objects
     */
    @GetMapping
    public ResponseEntity<List<User>> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }

    /**
     * Returns a single user by their username.
     *
     * HTTP {@code GET /api/users/{username}}
     *
     * @param username the unique username to look up
     * @return {@code 200 OK} with the matching {@link User}
     */
    @GetMapping("/{username}")
    public ResponseEntity<User> getUserByUsername(@PathVariable String username) {
        return ResponseEntity.ok(userService.getUserByUsername(username));
    }

    /**
     * Adds the user as a member of the specified server.
     *
     * HTTP {@code POST /api/users/{userId}/join/{serverId}}
     *
     * @param userId the ID of the user who wants to join
     * @param serverId the ID of the server to join
     * @return {@code 200 OK} with the updated {@link User}
     */
    @PostMapping("/{userId}/join/{serverId}")
    public ResponseEntity<User> joinServer(@PathVariable Long userId, @PathVariable Long serverId) {
        return ResponseEntity.ok(userService.joinServer(userId, serverId));
    }
}
