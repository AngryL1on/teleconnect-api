package dev.angryl1on.teleconnect.mainservice.dto.request;

import java.util.UUID;

public record SendMessageRequestDTO(UUID chatId, String content) {
}
