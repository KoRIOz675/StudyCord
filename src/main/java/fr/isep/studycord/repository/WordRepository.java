package fr.isep.studycord.repository;

import java.util.Optional;

import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.stereotype.Repository;

import fr.isep.studycord.model.Word;

@Repository
public interface WordRepository extends Neo4jRepository<Word, Long> {
    Optional<Word> findByValue(String value);
}