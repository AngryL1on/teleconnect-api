package dev.angryl1on.teleconnect.mainservice.dto.response;

import lombok.Builder;

@Builder
public record ApiResponseDTO(String message, boolean status) {
}
