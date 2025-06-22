package dev.angryl1on.teleconnect.mainservice.controllers;

import dev.angryl1on.teleconnect.mainservice.dto.response.TypingDTO;
import dev.angryl1on.teleconnect.mainservice.exception.ChatException;
import dev.angryl1on.teleconnect.mainservice.model.Chat;
import dev.angryl1on.teleconnect.mainservice.model.Message;
import dev.angryl1on.teleconnect.mainservice.model.User;
import dev.angryl1on.teleconnect.mainservice.service.ChatService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Slf4j
@Controller
@RequiredArgsConstructor
public class RealtimeChatController {

    private final SimpMessagingTemplate messagingTemplate;
    private final ChatService chatService;

    /** Приём обычных сообщений */
    @MessageMapping("/messages")
    public void receiveMessage(@Payload Message message) {
        broadcastToChat(message.getChat(), "/topic/", message);
    }

    /** Приём событий «печатает» */
    @MessageMapping("/typing")
    public void typing(@Payload TypingDTO dto) throws ChatException {
        Chat chat = chatService.findChatById(dto.getChatId());

        // Рассылаем всем, КРОМЕ печатающего
        for (User u : chat.getUsers()) {
            if (!u.getId().equals(dto.getUserId())) {
                messagingTemplate.convertAndSend(
                        "/topic/" + u.getId() + "/typing",
                        dto
                );
            }
        }
        log.debug("User {} typing={} in chat {}", dto.getUserId(), dto.isTyping(), dto.getChatId());
    }

    private void broadcastToChat(Chat chat, String baseDest, Object payload) {
        for (User u : chat.getUsers()) {
            messagingTemplate.convertAndSend(baseDest + u.getId(), payload);
        }
    }
}
