package fr.isep.studycord.algorithm;

import java.util.List;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import fr.isep.studycord.repository.ChannelRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class ReindexScheduler {

    private final CosineSimService cosineSimService;
    private final ChannelRepository channelRepository;

    @Scheduled(cron = "0 0 0 * * *")
    public void reindexAllChannels() {
        List<Long> channelIds = channelRepository.findAllIds();
        log.info("Scheduled reindex started for {} channels", channelIds.size());
        for (Long id : channelIds) {
            try {
                cosineSimService.reindexChannel(id);
            } catch (Exception e) {
                log.warn("Reindex failed for channel {}: {}", id, e.getMessage());
            }
        }
        log.info("Scheduled reindex completed");
    }
}
