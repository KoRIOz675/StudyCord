package fr.isep.studycord.repository;

import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import fr.isep.studycord.model.Channel;
import fr.isep.studycord.model.Message;

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

    @Transactional
    @Query("MATCH (s:Server) WHERE id(s) = $serverId "
            + "CREATE (c:Channel {name: $name, topic: $topic}) "
            + "CREATE (s)-[:HAS_CHANNEL]->(c) "
            + "RETURN c")
    Channel createInServer(@Param("serverId") Long serverId, @Param("name") String name, @Param("topic") String topic);

    @Transactional
    @Query("MATCH (c:Channel) WHERE id(c) = $channelId "
            + "MATCH (u:User) WHERE id(u) = $authorId "
            + "CREATE (m:Message {content: $content, createdAt: $createdAt}) "
            + "CREATE (m)-[r:POSTED_BY]->(u) "
            + "CREATE (c)-[:HAS_MESSAGE]->(m) "
            + "RETURN m, collect(r), collect(u)")
    Message createMessage(@Param("channelId") Long channelId, @Param("authorId") Long authorId,
            @Param("content") String content, @Param("createdAt") LocalDateTime createdAt);
}
