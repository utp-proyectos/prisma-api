package pe.edu.utp.prisma_api.domain.message.dto;

import java.time.LocalDateTime;
import java.util.UUID;

public record MessageResponse(UUID id, String content, String username, String avatar, UUID userId,
    LocalDateTime createdAt) {

}
