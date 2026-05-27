package pe.edu.utp.prisma_api.domain.message;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;

import pe.edu.utp.prisma_api.infraestructure.redis.RedisPublisher;

@Controller
public class ChatWsController {
  @Autowired
  private RedisPublisher redisPublisher;

  @Autowired
  private RedisTemplate<String, Object> redisTemplate;

  @MessageMapping("/chat.send")
  public void sendMessage(ChatMessage message) {
    message.setTimestamp(LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH:mm")));
    redisTemplate.opsForValue().set("test:mensaje", message);
    redisPublisher.publish("/topic/chat", message);
  }
}
