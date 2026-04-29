package fr.isep.studycord.model;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.neo4j.core.schema.GeneratedValue;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Relationship;
import static org.springframework.data.neo4j.core.schema.Relationship.Direction.OUTGOING;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Neo4j node representing a registered user.
 *
 * Users can be members of multiple {@link Server} nodes via outgoing
 * {@code MEMBER_OF} relationships.
 */
@Node("User")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {

    /**
     * Auto-generated unique identifier for this user.
     */
    @Id
    @GeneratedValue
    private Long id;

    /**
     * Unique login name chosen by the user.
     */
    private String username;

    /**
     * Contact email address of the user.
     */
    private String email;

    /**
     * Role of the user within the platform (e.g. "student", "teacher").
     */
    private String role;

    /**
     * School or institution the user is affiliated with.
     */
    private String school;

    /**
     * Servers the user has joined, stored as outgoing {@code MEMBER_OF}
     * relationships.
     */
    @Relationship(type = "MEMBER_OF", direction = OUTGOING)
    private List<Server> servers = new ArrayList<>();

}
