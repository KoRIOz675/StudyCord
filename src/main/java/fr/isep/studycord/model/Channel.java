package fr.isep.studycord.model;

import org.springframework.data.neo4j.core.schema.GeneratedValue;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;

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
}
