package dev.angryl1on.teleconnect.mainservice.dto.request;

import java.util.List;
import java.util.UUID;

public record GroupChatRequestDTO(List<UUID> userIds, String chatName) {
}
