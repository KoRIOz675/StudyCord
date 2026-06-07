package fr.isep.studycord.repository;

import java.util.List;

import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import fr.isep.studycord.model.Message;

@Repository
public interface MessageRepository extends Neo4jRepository<Message, Long> {

    @Query("MATCH (c:Channel) WHERE id(c) = $channelId "
            + "MATCH (c)-[:HAS_MESSAGE]->(m:Message)-[rc:CONTAINS]->(w:Word) "
            + "WHERE w.value IN $words "
            + "WITH m, sum(rc.tfidf) AS score "
            + "WHERE score >= $threshold "
            + "OPTIONAL MATCH (m)-[rb:POSTED_BY]->(u:User) "
            + "WITH m, score, rb, u "
            + "OPTIONAL MATCH (m)-[rc2:CONTAINS]->(w2:Word) "
            + "RETURN m, rb, u, collect(rc2), collect(w2), score "
            + "ORDER BY score DESC")
    List<Message> findSimilarByWords(
            @Param("channelId") Long channelId,
            @Param("words") List<String> words,
            @Param("threshold") double threshold);
}