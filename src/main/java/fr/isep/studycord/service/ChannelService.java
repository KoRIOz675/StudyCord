package fr.isep.studycord.service;

import java.util.ArrayList;

import org.springframework.stereotype.Service;

import fr.isep.studycord.dto.ChannelDTO;
import fr.isep.studycord.model.Channel;
import fr.isep.studycord.model.Server;
import fr.isep.studycord.repository.ServerRepository;
import lombok.RequiredArgsConstructor;

/**
 * Service layer for channel management.
 *
 * Handles creation of channels inside servers and retrieval of channels by
 * server.
 */
@Service
@RequiredArgsConstructor
public class ChannelService {

    private final ServerRepository serverRepository;

    /**
     * Creates a new channel and attaches it to the specified server.
     *
     * @param serverId the ID of the server that will own the channel
     * @param dto the channel data (name and topic)
     * @return the newly created {@link Channel}
     * @throws RuntimeException if no server with {@code serverId} exists
     */
    public Channel createChannel(Long serverId, ChannelDTO dto) {
        Server server = serverRepository.findById(serverId)
                .orElseThrow(() -> new RuntimeException("Server not found: " + serverId));

        Channel channel = new Channel(null, dto.getName(), dto.getTopic(), new ArrayList<>());
        server.getChannels().add(channel);
        serverRepository.save(server);
        return channel;
    }

    /**
     * Returns all channels that belong to the specified server.
     *
     * @param serverId the ID of the server whose channels should be retrieved
     * @return a list of {@link Channel} objects (never {@code null})
     * @throws RuntimeException if no server with {@code serverId} exists
     */
    public java.util.List<Channel> getChannelsByServerId(Long serverId) {
        Server server = serverRepository.findById(serverId)
                .orElseThrow(() -> new RuntimeException("Server not found: " + serverId));
        return server.getChannels();
    }
}
