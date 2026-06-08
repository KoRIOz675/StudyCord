package fr.isep.studycord.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/** A server suggested to an isolated student to join. */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RecommendedServerDto {

    private Long serverId;
    private String serverName;
    private String subject;
    private String school;
    private long memberCount;
    private long messageCount;
}
