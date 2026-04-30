package fr.isep.studycord.algorithm;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.stereotype.Service;

import fr.isep.studycord.model.Channel;
import fr.isep.studycord.model.Message;
import fr.isep.studycord.repository.ChannelRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CosineSimService {

    private static final double SIMILARITY_THRESHOLD = 0.2;
    private static final Pattern TOKEN_PATTERN = Pattern.compile("\\p{L}+[\\p{L}\\p{N}_-]*");
    private static final Set<String> STOP_WORDS = Set.of(
            "a", "an", "the", "is", "it", "in", "on", "at", "to",
            "do", "i", "how", "can", "what", "why", "who", "anyone"
    );

    private final ChannelRepository channelRepository;

    public double computeSimilarity(String text1, String text2) {
        Map<String, Integer> vector1 = buildTermFrequencyVector(text1);
        Map<String, Integer> vector2 = buildTermFrequencyVector(text2);

        if (vector1.isEmpty() || vector2.isEmpty()) {
            return 0.0;
        }

        double dotProduct = 0.0;
        for (Map.Entry<String, Integer> entry : vector1.entrySet()) {
            Integer frequencyInOtherText = vector2.get(entry.getKey());
            if (frequencyInOtherText != null) {
                dotProduct += entry.getValue() * frequencyInOtherText;
            }
        }

        double magnitude1 = vectorMagnitude(vector1);
        double magnitude2 = vectorMagnitude(vector2);
        if (magnitude1 == 0.0 || magnitude2 == 0.0) {
            return 0.0;
        }

        return dotProduct / (magnitude1 * magnitude2);
    }

    public List<Message> findSimilarMessages(Long channelId, String query) {
        Channel channel = channelRepository.findByIdWithMessages(channelId)
                .orElseThrow(() -> new RuntimeException("Channel not found: " + channelId));

        List<ScoredMessage> scoredMessages = new ArrayList<>();
        for (Message message : Optional.ofNullable(channel.getMessages()).orElseGet(ArrayList::new)) {
            double score = computeSimilarity(query, message.getContent());
            if (score >= SIMILARITY_THRESHOLD) {
                scoredMessages.add(new ScoredMessage(message, score));
            }
        }

        scoredMessages.sort(Comparator.comparingDouble(ScoredMessage::score).reversed());

        List<Message> result = new ArrayList<>();
        for (ScoredMessage scoredMessage : scoredMessages) {
            result.add(scoredMessage.message());
        }
        return result;
    }

    private Map<String, Integer> buildTermFrequencyVector(String text) {

        Map<String, Integer> frequencies = new LinkedHashMap<>();
        if (text == null || text.isBlank()) {
            return frequencies;
        }

        Matcher matcher = TOKEN_PATTERN.matcher(text.toLowerCase(Locale.ROOT));

        while (matcher.find()) {
            String token = matcher.group();
            if (!STOP_WORDS.contains(token)) {
                frequencies.put(token, frequencies.getOrDefault(token, 0) + 1);
            }
        }
        return frequencies;
    }

    private double vectorMagnitude(Map<String, Integer> vector) {
        double sumOfSquares = 0.0;
        for (Integer frequency : vector.values()) {
            sumOfSquares += frequency * frequency;
        }
        return Math.sqrt(sumOfSquares);
    }

    private record ScoredMessage(Message message, double score) {

    }

}
