package fr.isep.studycord.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import fr.isep.studycord.dto.ChannelDTO;
import fr.isep.studycord.model.Channel;
import fr.isep.studycord.service.ChannelService;
import lombok.RequiredArgsConstructor;

/**
 * REST controller exposing channel endpoints under {@code /api/channels}.
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/channels")
public class ChannelController {

    private final ChannelService channelService;

    /**
     * Creates a new channel inside the specified server.
     *
     * HTTP {@code POST /api/channels/{serverId}}
     *
     * @param serverId the ID of the server that will own the new channel
     * @param dto the channel payload (name, topic)
     * @return {@code 201 Created} with the persisted {@link Channel} in the
     * body
     */
    @PostMapping("/{serverId}")
    public ResponseEntity<Channel> createChannel(@PathVariable Long serverId, @RequestBody ChannelDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(channelService.createChannel(serverId, dto));
    }

    /**
     * Returns all channels that belong to the specified server.
     *
     * HTTP {@code GET /api/channels/server/{serverId}}
     *
     * @param serverId the ID of the server whose channels should be listed
     * @return {@code 200 OK} with a list of {@link Channel} objects
     */
    @GetMapping("/server/{serverId}")
    public ResponseEntity<List<Channel>> getChannelsByServerId(@PathVariable Long serverId) {
        return ResponseEntity.ok(channelService.getChannelsByServerId(serverId));
    }

}
