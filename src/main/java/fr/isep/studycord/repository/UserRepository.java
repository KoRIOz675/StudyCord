package fr.isep.studycord.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

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

    /**
     * Creates a MEMBER_OF relationship between a user and a server.
     *
     * @param userId the ID of the user
     * @param serverId the ID of the server
     */
    @Transactional
    @Query("MATCH (u:User) WHERE id(u) = $userId "
            + "MATCH (s:Server) WHERE id(s) = $serverId "
            + "CREATE (u)-[:MEMBER_OF]->(s)")
    void createMembershipRelationship(@Param("userId") Long userId, @Param("serverId") Long serverId);
}
