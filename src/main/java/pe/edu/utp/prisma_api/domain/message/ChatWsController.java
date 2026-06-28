package pe.edu.utp.prisma_api.domain.message;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;

import lombok.RequiredArgsConstructor;
import pe.edu.utp.prisma_api.infraestructure.redis.RedisPublisher;

@Controller
@RequiredArgsConstructor
public class ChatWsController {
  private final RedisPublisher redisPublisher;

  @MessageMapping("/chat.send")
  public void sendMessage(ChatMessage message) {
    message.setTimestamp(LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH:mm")));
    redisPublisher.publish("/topic/chat", message);
  }
}
