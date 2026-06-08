package fr.isep.studycord.service;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import fr.isep.studycord.algorithm.IsolationDetectionService;
import fr.isep.studycord.dto.IntegrationSuggestionResponse;
import fr.isep.studycord.dto.RecommendedServerDto;
import fr.isep.studycord.dto.UserActivityVector;
import fr.isep.studycord.model.User;
import fr.isep.studycord.repository.ServerRepository;
import fr.isep.studycord.repository.UserRepository;
import lombok.RequiredArgsConstructor;

/**
 * Turns the internal "isolated student" detection into a positive action:
 * isolated students are offered active servers they can join. Non-students and
 * well-integrated students get an empty, neutral response.
 */
@Service
@RequiredArgsConstructor
public class IntegrationSuggestionService {

    private static final String STUDENT_ROLE = "STUDENT";
    private static final int DEFAULT_SERVER_LIMIT = 3;
    private static final String WELCOME_MESSAGE =
            "Here are some active servers you can join to start chatting.";

    private final UserRepository userRepository;
    private final ServerRepository serverRepository;
    private final IsolationDetectionService isolationDetectionService;

    public IntegrationSuggestionResponse getSuggestions(Long userId) {
        return build(userId, isolationDetectionService.findIsolatedUsers());
    }

    public IntegrationSuggestionResponse getSuggestions(Long userId, double eps, int minPts) {
        return build(userId, isolationDetectionService.findIsolatedUsers(eps, minPts));
    }

    private IntegrationSuggestionResponse build(Long userId,
            List<UserActivityVector> isolatedStudents) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "User not found: " + userId));

        // Only students are guided; teachers/admins never get suggestions.
        if (!STUDENT_ROLE.equalsIgnoreCase(user.getRole())) {
            return notIsolated();
        }

        boolean isolated = isolatedStudents.stream()
                .anyMatch(vector -> userId.equals(vector.getUserId()));
        if (!isolated) {
            return notIsolated();
        }

        List<RecommendedServerDto> servers =
                serverRepository.findPopularServersNotJoined(userId, DEFAULT_SERVER_LIMIT);
        return new IntegrationSuggestionResponse(true, WELCOME_MESSAGE, servers);
    }

    private IntegrationSuggestionResponse notIsolated() {
        return new IntegrationSuggestionResponse(false, null, List.of());
    }
}
