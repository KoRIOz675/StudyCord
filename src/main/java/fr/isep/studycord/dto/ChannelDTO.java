package fr.isep.studycord.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Data Transfer Object used to create a new {@link fr.isep.studycord.model.Channel}.
 */
@Data
@NoArgsConstructor
public class ChannelDTO {

    /** Display name of the channel to create. */
    private String name;

    /** Optional topic describing the channel's purpose. */
    private String topic;

}
