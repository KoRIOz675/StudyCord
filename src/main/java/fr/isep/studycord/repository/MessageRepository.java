package fr.isep.studycord.repository;

import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.stereotype.Repository;

import fr.isep.studycord.model.Message;

/**
 * Spring Data Neo4j repository for {@link Message} nodes.
 *
 * Inherits standard CRUD and pagination operations from
 * {@link Neo4jRepository}.
 */
@Repository
public interface MessageRepository extends Neo4jRepository<Message, Long> {

}
