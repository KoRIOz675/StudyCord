package fr.isep.studycord.model;

import org.springframework.data.neo4j.core.schema.RelationshipId;
import org.springframework.data.neo4j.core.schema.RelationshipProperties;
import org.springframework.data.neo4j.core.schema.TargetNode;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@RelationshipProperties
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MessageWord {

    @RelationshipId
    private Long id;

    private Double tfidf;

    @TargetNode
    private Word word;
}