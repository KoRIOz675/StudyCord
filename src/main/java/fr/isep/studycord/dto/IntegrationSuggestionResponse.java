package fr.isep.studycord.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/** Response of the integration-suggestions endpoint (server recommendations). */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class IntegrationSuggestionResponse {

    private boolean isolated;
    private String message;
    private List<RecommendedServerDto> recommendedServers;
}
