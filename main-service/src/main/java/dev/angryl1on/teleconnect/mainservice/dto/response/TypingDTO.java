package dev.angryl1on.teleconnect.mainservice.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TypingDTO {
    private UUID chatId;   // в каком чате
    private UUID userId;   // кто печатает
    private boolean typing; // true = начал, false = прекратил
}
