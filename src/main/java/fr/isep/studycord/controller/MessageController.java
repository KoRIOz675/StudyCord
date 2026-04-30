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

import fr.isep.studycord.dto.MessageDTO;
import fr.isep.studycord.model.Message;
import fr.isep.studycord.service.MessageService;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/messages")
public class MessageController {

	private final MessageService messageService;

	@PostMapping("/{channelId}")
	public ResponseEntity<Message> postMessage(@PathVariable Long channelId, @RequestBody MessageDTO dto) {
		return ResponseEntity.status(HttpStatus.CREATED).body(messageService.postMessage(channelId, dto));
	}

	@GetMapping("/channel/{channelId}")
	public ResponseEntity<List<Message>> getMessagesByChannel(@PathVariable Long channelId) {
		return ResponseEntity.ok(messageService.getMessagesByChannel(channelId));
	}
}
