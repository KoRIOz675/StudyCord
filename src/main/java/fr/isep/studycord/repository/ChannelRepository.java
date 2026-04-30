package fr.isep.studycord.repository;

import java.util.Optional;

import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;
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

    @Query("MATCH (c:Channel) WHERE id(c) = $id "
            + "OPTIONAL MATCH (c)-[r:HAS_MESSAGE]->(m:Message) "
            + "OPTIONAL MATCH (m)-[rb:POSTED_BY]->(u:User) "
            + "RETURN c, collect(r), collect(m), collect(rb), collect(u)")
    Optional<Channel> findByIdWithMessages(Long id);
}
