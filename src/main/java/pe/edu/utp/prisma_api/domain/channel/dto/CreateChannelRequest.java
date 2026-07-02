package pe.edu.utp.prisma_api.domain.channel.dto;

import java.util.UUID;

public record CreateChannelRequest(String name, UUID projectId, UUID teamId, UUID transactionalId) {

}
