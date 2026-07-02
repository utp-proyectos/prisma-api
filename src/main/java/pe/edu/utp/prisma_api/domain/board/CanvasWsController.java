package pe.edu.utp.prisma_api.domain.board;

import java.security.Principal;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Controller;

import lombok.RequiredArgsConstructor;
import pe.edu.utp.prisma_api.domain.board.dto.CanvasMessageDTO;
import pe.edu.utp.prisma_api.infraestructure.redis.RedisPublisher;

@Controller
@RequiredArgsConstructor
public class CanvasWsController {
  private final RedisPublisher redisPublisher;

  @MessageMapping("/canvas.update")
  public void updateCanvas(@Payload CanvasMessageDTO message, Principal principal) {
    System.out.println("Canvas update recibido: " + message.getType() + " boardId: " + message.getBoardId());

    redisPublisher.publish(
        "/topic/" + message.getTeamId() + "/canvas/" + message.getBoardId(),
        message);
  }
}
