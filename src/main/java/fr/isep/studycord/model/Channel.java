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
 * Neo4j node representing a text channel inside a {@link Server}.
 *
 * Each channel has a name and an optional topic describing its purpose.
 */
@Node("Channel")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Channel {

    /**
     * Auto-generated unique identifier for this channel.
     */
    @Id
    @GeneratedValue
    private Long id;

    /**
     * Display name of the channel (e.g. "general", "homework").
     */
    private String name;

    /**
     * Short description of what the channel is about.
     */
    private String topic;

    @Relationship(type = "HAS_MESSAGE", direction = OUTGOING)
    private List<Message> messages = new ArrayList<>();
}
