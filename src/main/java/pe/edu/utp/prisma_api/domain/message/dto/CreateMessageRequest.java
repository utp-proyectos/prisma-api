package pe.edu.utp.prisma_api.domain.message.dto;

import java.util.UUID;

public record CreateMessageRequest(UUID teamId, UUID projectId, UUID channelId, String content) {
}