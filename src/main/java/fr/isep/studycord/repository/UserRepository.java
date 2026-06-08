package fr.isep.studycord.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import fr.isep.studycord.dto.UserActivityVector;
import fr.isep.studycord.model.User;

/**
 * Spring Data Neo4j repository for {@link User} nodes.
 *
 * Extends {@link Neo4jRepository} with username and server-membership lookup
 * methods.
 */
@Repository
public interface UserRepository extends Neo4jRepository<User, Long> {

    /**
     * One activity vector per student (STUDENT only). OPTIONAL MATCH keeps
     * students with no server/message so isolated ones are included.
     *
     * @return one {@link UserActivityVector} per student
     */
    @Query("MATCH (u:User) WHERE u.role = 'STUDENT' "
            + "OPTIONAL MATCH (u)-[:MEMBER_OF]->(s:Server) "
            + "WITH u, count(DISTINCT s) AS serverCount "
            + "OPTIONAL MATCH (m:Message)-[:POSTED_BY]->(u) "
            + "OPTIONAL MATCH (c:Channel)-[:HAS_MESSAGE]->(m) "
            + "RETURN id(u) AS userId, "
            + "       u.username AS username, "
            + "       count(DISTINCT m) AS totalMessages, "
            + "       serverCount AS serverCount, "
            + "       count(DISTINCT c) AS activeChannelCount")
    List<UserActivityVector> computeStudentActivityVectors();

    /**
     * Finds a user by their unique username.
     *
     * @param username the username to search for
     * @return an {@link Optional} containing the matching user, or empty if
     * none found
     */
    Optional<User> findByUsername(String username);

    /**
     * Returns all users who are members of the given server.
     *
     * @param server the server whose members should be retrieved
     * @return a list of members (never {@code null})
     */
    @Query("MATCH (u:User)-[r:MEMBER_OF]->(s:Server) WHERE id(s) = $serverId "
            + "MATCH (u)-[r2:MEMBER_OF]->(s2:Server) "
            + "RETURN u, collect(r2), collect(s2)")
    List<User> findByServerId(Long serverId);

    @Query("MATCH (u:User) WHERE id(u) = $userId "
            + "OPTIONAL MATCH (u)-[r:MEMBER_OF]->(s:Server) "
            + "OPTIONAL MATCH (s)-[rc:HAS_CHANNEL]->(c:Channel) "
            + "RETURN u, collect(r), collect(s), collect(rc), collect(c)")
    Optional<User> findByIdWithServers(@Param("userId") Long userId);

    @Query("MATCH (u:User) WHERE id(u) = $userId "
            + "MATCH (s:Server) WHERE id(s) = $serverId "
            + "MERGE (u)-[:MEMBER_OF]->(s)")
    void createMembershipRelationship(@Param("userId") Long userId, @Param("serverId") Long serverId);
}
