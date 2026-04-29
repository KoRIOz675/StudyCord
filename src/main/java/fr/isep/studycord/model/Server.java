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
 * Neo4j node representing a study server.
 *
 * A server groups students around a common {@code subject} within a
 * {@code school} and contains one or more {@link Channel} nodes linked via
 * {@code HAS_CHANNEL} edges.
 */
@Node("Server")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Server {

    /**
     * Auto-generated unique identifier for this server.
     */
    @Id
    @GeneratedValue
    private Long id;

    /**
     * Display name of the server.
     */
    private String name;

    /**
     * Academic subject this server is focused on (e.g. "Mathematics").
     */
    private String subject;

    /**
     * School or institution that the server belongs to.
     */
    private String school;

    /**
     * Channels that belong to this server, stored as outgoing
     * {@code HAS_CHANNEL} relationships.
     */
    @Relationship(type = "HAS_CHANNEL", direction = OUTGOING)
    private List<Channel> channels = new ArrayList<>();
}
