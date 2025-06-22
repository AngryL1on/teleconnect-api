package dev.angryl1on.teleconnect.mainservice.dto.response;

import lombok.Builder;

@Builder
public record LoginResponseDTO(
        String token,
        boolean isAuthenticated
) {

}
