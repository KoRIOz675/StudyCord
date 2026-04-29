package fr.isep.studycord.model;

import java.util.List;

import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.GeneratedValue;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Relationship;

import static org.springframework.data.neo4j.core.schema.Relationship.Direction.OUTGOING;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Node("Server")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Server {

	@Id
	@GeneratedValue
	private Long id;

	private String name;

	private String subject;

	private String school;

    @Relationship(type = "HAS_CHANNEL", direction = OUTGOING)
	private List<Channel> channels;
}
