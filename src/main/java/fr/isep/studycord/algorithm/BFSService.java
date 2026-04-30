package fr.isep.studycord.algorithm;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Queue;
import java.util.Set;

import org.springframework.stereotype.Service;

import fr.isep.studycord.model.Server;
import fr.isep.studycord.model.User;
import fr.isep.studycord.repository.UserRepository;
import lombok.RequiredArgsConstructor;

/**
 * Service that uses Breadth-First Search (BFS) to recommend servers to a user.
 *
 * The BFS explores the graph of users and servers.
 */
@Service
@RequiredArgsConstructor
public class BFSService {

    private final UserRepository userRepository;

    /**
     * Suggests servers the given user might want to join, based on BFS over the
     * server-membership graph.
     *
     * Servers already joined by the user are excluded from the result. The
     * returned list is ordered by discovery distance (closest connections
     * first).
     *
     * @param userId the ID of the user for whom suggestions are generated
     * @return an ordered list of recommended {@link Server} objects (never
     * {@code null})
     * @throws RuntimeException if no user with {@code userId} exists
     */
    public List<Server> suggestServers(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found: " + userId));

        Set<Long> visitedIds = new HashSet<>();
        Queue<Server> queue = new java.util.LinkedList<>();
        for (Server s : user.getServers()) {
            visitedIds.add(s.getId());
            queue.add(s);
        }
        List<Server> suggestions = new ArrayList<>();

        while (!queue.isEmpty()) {
            Server server = queue.poll();

            List<User> members = userRepository.findByServerId(server.getId());

            for (User member : members) {
                if (!member.getId().equals(userId)) {
                    for (Server memberServer : member.getServers()) {
                        if (!visitedIds.contains(memberServer.getId())) {
                            visitedIds.add(memberServer.getId());
                            suggestions.add(memberServer);
                            queue.add(memberServer);
                        }
                    }
                }
            }
        }

        return suggestions;
    }
}
