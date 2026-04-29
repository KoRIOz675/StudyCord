package fr.isep.studycord.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Data Transfer Object used to register a new {@link fr.isep.studycord.model.User}.
 */
@Data
@NoArgsConstructor
public class UserDTO {

    /** Desired unique login name for the new user. */
    private String username;

    /** Contact email address of the new user. */
    private String email;

    /** Role of the user within the platform (e.g. "student", "teacher"). */
    private String role;

    /** School or institution the new user is affiliated with. */
    private String school;
}
