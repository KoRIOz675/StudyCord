package fr.isep.studycord.repository;

import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.stereotype.Repository;

import fr.isep.studycord.model.Channel;

/**
 * Spring Data Neo4j repository for {@link Channel} nodes.
 *
 * Inherits standard CRUD and pagination operations from
 * {@link Neo4jRepository}.
 */
@Repository
public interface ChannelRepository extends Neo4jRepository<Channel, Long> {
}
