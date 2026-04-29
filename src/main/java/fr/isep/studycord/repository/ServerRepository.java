package fr.isep.studycord.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.stereotype.Repository;

import fr.isep.studycord.model.Server;

/**
 * Spring Data Neo4j repository for {@link Server} nodes.
 *
 * Extends {@link Neo4jRepository} with school-based lookup methods.
 */
@Repository
public interface ServerRepository extends Neo4jRepository<Server, Long> {

    /**
     * Returns the first server matching the given school name.
     *
     * @param name the school name to search for
     * @return an {@link Optional} containing the matching server, or empty if
     * none found
     */
    Optional<Server> findBySchool(String name);

    /**
     * Returns all servers that belong to the given school.
     *
     * @param school the school name to filter by
     * @return a list of matching servers (never {@code null})
     */
    List<Server> findAllBySchool(String school);
}
