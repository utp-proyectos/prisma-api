package pe.edu.utp.prisma_api.domain.message;

import java.security.Principal;
import java.util.UUID;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;

import lombok.RequiredArgsConstructor;
import pe.edu.utp.prisma_api.common.enums.WsAction;
import pe.edu.utp.prisma_api.common.response.WsResponse;
import pe.edu.utp.prisma_api.domain.message.dto.CreateMessageRequest;
import pe.edu.utp.prisma_api.domain.message.dto.MessageResponse;
import pe.edu.utp.prisma_api.infraestructure.redis.RedisPublisher;

@Controller
@RequiredArgsConstructor
public class MessageWsController {
  private final MessageService messageService;
  private final RedisPublisher redisPublisher;

  @MessageMapping("/message.create")
  public void sendMessage(CreateMessageRequest request, Principal principal) {

    MessageResponse message = messageService.save(request, UUID.fromString(principal.getName()));

    WsResponse<MessageResponse> response = new WsResponse<>(WsAction.CREATE, message);

    redisPublisher.publish(
        "/topic/" + request.teamId() + "/" + request.projectId() + "/" + request.channelId() + "/messages", response);
  }
}
