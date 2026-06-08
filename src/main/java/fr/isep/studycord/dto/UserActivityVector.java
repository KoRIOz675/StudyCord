package fr.isep.studycord.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Activity profile of a user, used both as the Neo4j query projection and as the
 * feature vector for the DBSCAN isolation detection.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserActivityVector {

    private Long userId;
    private String username;
    private long totalMessages;
    private long serverCount;
    private long activeChannelCount;

    /** Average messages per active channel (0 when the user has no activity). */
    public double getAverageMessagesPerChannel() {
        return activeChannelCount == 0 ? 0.0 : (double) totalMessages / activeChannelCount;
    }

    /** Raw feature vector fed to the distance function (order must stay stable). */
    public double[] toFeatureArray() {
        return new double[] {
                totalMessages,
                serverCount,
                activeChannelCount,
                getAverageMessagesPerChannel()
        };
    }
}
