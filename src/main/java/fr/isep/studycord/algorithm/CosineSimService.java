package fr.isep.studycord.algorithm;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.data.neo4j.core.Neo4jClient;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import fr.isep.studycord.model.Message;
import fr.isep.studycord.repository.ChannelRepository;
import fr.isep.studycord.repository.MessageRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.apache.lucene.analysis.en.PorterStemFilter;
import org.apache.lucene.analysis.core.WhitespaceTokenizer;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;

@Slf4j
@Service
@RequiredArgsConstructor
public class CosineSimService {

    private static final double SIMILARITY_THRESHOLD = 0.05;
    private static final Pattern TOKEN_PATTERN = Pattern.compile("\\p{L}+[\\p{L}\\p{N}_-]*");
    private static final Set<String> STOP_WORDS = Set.of(
            "a", "an", "the", "is", "it", "in", "on", "at", "to",
            "do", "i", "how", "can", "what", "why", "who", "anyone");

    private final ChannelRepository channelRepository;
    private final MessageRepository messageRepository;
    private final Neo4jClient neo4jClient;

    // -------------------------------------------------------------------------
    // Batch reindex — single pass over all messages in a channel
    // -------------------------------------------------------------------------

    @Async("reindexExecutor")
    @Transactional
    public CompletableFuture<Void> reindexChannel(Long channelId) {
        if (!channelRepository.existsById(channelId)) {
            throw new RuntimeException("Channel not found: " + channelId);
        }

        List<Message> allMessages = messageRepository.findByChannelId(channelId);

        if (allMessages.isEmpty())
            return CompletableFuture.completedFuture(null);

        int totalDocs = allMessages.size();

        List<Map<String, Integer>> allTFs = new ArrayList<>();
        for (Message m : allMessages) {
            allTFs.add(buildTF(m.getContent()));
        }

        Map<String, Double> idf = buildIDF(allTFs, totalDocs);

        for (int i = 0; i < allMessages.size(); i++) {
            Map<String, Double> tfidf = buildTFIDF(allTFs.get(i), idf);
            persistWordRelationships(allMessages.get(i), tfidf);
        }

        return CompletableFuture.completedFuture(null);
    }

    // -------------------------------------------------------------------------
    // Called at query time — graph traversal only, no Java computation
    // -------------------------------------------------------------------------

    public List<Message> findSimilarMessages(Long channelId, String query) {
        List<String> queryWords = new ArrayList<>(buildTF(query).keySet());
        if (queryWords.isEmpty())
            return List.of();
        return messageRepository.findSimilarByWords(channelId, queryWords, SIMILARITY_THRESHOLD);
    }

    // -------------------------------------------------------------------------
    // Persist Word nodes + CONTAINS relationships to Neo4j
    // -------------------------------------------------------------------------

    private void persistWordRelationships(Message message, Map<String, Double> tfidf) {
        Long msgId = message.getId();

        // Remove stale CONTAINS relationships — no entity cascade, no User touch
        neo4jClient
                .query("MATCH (m:Message)-[r:CONTAINS]->() WHERE id(m) = $id DELETE r")
                .bindAll(Map.of("id", msgId))
                .run();

        for (Map.Entry<String, Double> entry : tfidf.entrySet()) {
            neo4jClient
                    .query("MERGE (w:Word {value: $word}) "
                            + "WITH w MATCH (m:Message) WHERE id(m) = $id "
                            + "MERGE (m)-[r:CONTAINS]->(w) SET r.tfidf = $score")
                    .bindAll(Map.of("word", entry.getKey(), "id", msgId, "score", entry.getValue()))
                    .run();
        }
    }

    // -------------------------------------------------------------------------
    // Helpers
    // -------------------------------------------------------------------------

    private Map<String, Integer> buildTF(String text) {
        Map<String, Integer> freq = new LinkedHashMap<>();
        if (text == null || text.isBlank())
            return freq;
        Matcher matcher = TOKEN_PATTERN.matcher(text.toLowerCase(Locale.ROOT));
        while (matcher.find()) {
            String token = stem(matcher.group()); // stem here
            if (!STOP_WORDS.contains(token)) {
                freq.merge(token, 1, Integer::sum);
            }
        }
        return freq;
    }

    private Map<String, Double> buildIDF(List<Map<String, Integer>> allTFs, int totalDocs) {
        Map<String, Integer> docFreq = new HashMap<>();
        for (Map<String, Integer> tf : allTFs) {
            for (String word : tf.keySet()) {
                docFreq.merge(word, 1, Integer::sum);
            }
        }
        Map<String, Double> idf = new HashMap<>();
        for (Map.Entry<String, Integer> entry : docFreq.entrySet()) {
            idf.put(entry.getKey(), Math.log((1.0 + totalDocs) / (1.0 + entry.getValue())) + 1.0);
        }
        return idf;
    }

    private Map<String, Double> buildTFIDF(Map<String, Integer> tf, Map<String, Double> idf) {
        Map<String, Double> tfidf = new HashMap<>();
        int totalTokens = tf.values().stream().mapToInt(Integer::intValue).sum();
        if (totalTokens == 0)
            return tfidf;
        for (Map.Entry<String, Integer> entry : tf.entrySet()) {
            String word = entry.getKey();
            double tfScore = (double) entry.getValue() / totalTokens;
            double idfScore = idf.getOrDefault(word, 0.0);
            tfidf.put(word, tfScore * idfScore);
        }
        return tfidf;
    }

    private String stem(String word) {
        try {
            WhitespaceTokenizer tokenizer = new WhitespaceTokenizer();
            tokenizer.setReader(new java.io.StringReader(word));
            PorterStemFilter stemFilter = new PorterStemFilter(tokenizer);
            CharTermAttribute charAttr = stemFilter.addAttribute(CharTermAttribute.class);
            stemFilter.reset();
            if (stemFilter.incrementToken()) {
                String stemmed = charAttr.toString();
                stemFilter.end();
                stemFilter.close();
                return stemmed;
            }
            stemFilter.end();
            stemFilter.close();
        } catch (Exception e) {
            // fallback to original word
        }
        return word;
    }
}