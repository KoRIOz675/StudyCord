package fr.isep.studycord.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Data Transfer Object used to create a new {@link fr.isep.studycord.model.Server}.
 */
@Data
@NoArgsConstructor
public class ServerDTO {

    /** Display name of the server to create. */
    private String name;

    /** Academic subject the server will focus on. */
    private String subject;

    /** School or institution the server belongs to. */
    private String school;
}
