package fr.isep.studycord.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Data Transfer Object used to create a new
 * {@link fr.isep.studycord.model.Message}.
 */
@Data
@NoArgsConstructor
public class MessageDTO {

    /**
     * Content of the message to create.
     */
    private String content;

    /**
     * ID of the author of the message.
     */
    private Long authorId;
}
