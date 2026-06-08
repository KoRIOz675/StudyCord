package fr.isep.studycord.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;

import fr.isep.studycord.dto.MessageDTO;
import fr.isep.studycord.model.Channel;
import fr.isep.studycord.model.Message;
import fr.isep.studycord.repository.ChannelRepository;
import fr.isep.studycord.repository.UserRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MessageService {

    private final ChannelRepository channelRepository;
    private final UserRepository userRepository;

    public Message postMessage(Long channelId, MessageDTO dto) {
        if (!channelRepository.existsById(channelId)) {
            throw new RuntimeException("Channel not found: " + channelId);
        }
        if (!userRepository.existsById(dto.getAuthorId())) {
            throw new RuntimeException("User not found: " + dto.getAuthorId());
        }

        return channelRepository.createMessage(
                channelId, dto.getAuthorId(), dto.getContent(), LocalDateTime.now());
    }

    public List<Message> getMessagesByChannel(Long channelId) {
        Channel channel = channelRepository.findByIdWithMessages(channelId)
                .orElseThrow(() -> new RuntimeException("Channel not found: " + channelId));
        return channel.getMessages();
    }
}