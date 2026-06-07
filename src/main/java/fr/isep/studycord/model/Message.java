package fr.isep.studycord.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.data.neo4j.core.schema.GeneratedValue;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Relationship;
import static org.springframework.data.neo4j.core.schema.Relationship.Direction.OUTGOING;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Neo4j node representing a text message inside a {@link Channel}.
 *
 * Each message has a content and a creation timestamp.
 */
@Node("Message")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Message {

    @Id
    @GeneratedValue
    private Long id;

    private String content;

    private LocalDateTime createdAt;

    @Relationship(type = "POSTED_BY", direction = OUTGOING)
    private User author;

    @JsonIgnore
    @Relationship(type = "CONTAINS", direction = OUTGOING)
    private List<MessageWord> words = new ArrayList<>();

}
