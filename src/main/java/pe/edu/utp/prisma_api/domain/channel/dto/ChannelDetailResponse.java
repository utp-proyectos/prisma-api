package pe.edu.utp.prisma_api.domain.channel.dto;

import java.util.List;
import java.util.UUID;

import pe.edu.utp.prisma_api.domain.message.dto.MessageResponse;

public record ChannelDetailResponse(UUID id, String name, List<MessageResponse> messages) {

}
