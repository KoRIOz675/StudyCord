package fr.isep.studycord.service;

import fr.isep.studycord.dto.ChannelDTO;
import fr.isep.studycord.model.Channel;
import fr.isep.studycord.model.Server;
import fr.isep.studycord.repository.ServerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ChannelService {

    private final ServerRepository serverRepository;

    public Channel createChannel(Long serverId, ChannelDTO dto) {
        Server server = serverRepository.findById(serverId)
                .orElseThrow(() -> new RuntimeException("Server not found: " + serverId));

        Channel channel = new Channel(null, dto.getName(), dto.getTopic());
        server.getChannels().add(channel);
        serverRepository.save(server);
        return channel;
    }

    public java.util.List<Channel> getChannelsByServerId(Long serverId) {
        Server server = serverRepository.findById(serverId)
                .orElseThrow(() -> new RuntimeException("Server not found: " + serverId));
        return server.getChannels();
    }
}
