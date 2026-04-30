package fr.isep.studycord.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;

import fr.isep.studycord.dto.MessageDTO;
import fr.isep.studycord.model.Channel;
import fr.isep.studycord.model.Message;
import fr.isep.studycord.model.User;
import fr.isep.studycord.repository.ChannelRepository;
import fr.isep.studycord.repository.UserRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MessageService {

    private final ChannelRepository channelRepository;
    private final UserRepository userRepository;

    public Message postMessage(Long channelId, MessageDTO dto) {
        Channel channel = channelRepository.findByIdWithMessages(channelId)
                .orElseThrow(() -> new RuntimeException("Channel not found: " + channelId));

        User author = userRepository.findById(dto.getAuthorId())
                .orElseThrow(() -> new RuntimeException("User not found: " + dto.getAuthorId()));

        Message message = new Message(null, dto.getContent(), LocalDateTime.now(), author);
        channel.getMessages().add(message);
        channelRepository.save(channel);
        return message;
    }

    public List<Message> getMessagesByChannel(Long channelId) {
        Channel channel = channelRepository.findByIdWithMessages(channelId)
                .orElseThrow(() -> new RuntimeException("Channel not found: " + channelId));
        return channel.getMessages();
    }

}
