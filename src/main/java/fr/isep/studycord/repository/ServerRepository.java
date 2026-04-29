package fr.isep.studycord.repository;

import java.util.Optional;

import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.stereotype.Repository;

import fr.isep.studycord.model.Server;

@Repository
public interface ServerRepository extends Neo4jRepository<Server, Long> {

    Optional<Server> findBySchool(String name);

    java.util.List<Server> findAllBySchool(String school);
}
