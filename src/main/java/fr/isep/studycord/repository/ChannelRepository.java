package fr.isep.studycord.repository;

import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.stereotype.Repository;

import fr.isep.studycord.model.Channel;

@Repository
public interface ChannelRepository extends Neo4jRepository<Channel, Long> {
}
