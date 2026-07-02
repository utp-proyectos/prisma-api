package pe.edu.utp.prisma_api.infraestructure.redis;

import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import tools.jackson.databind.json.JsonMapper;
import java.nio.charset.StandardCharsets;

@Service
@RequiredArgsConstructor
public class RedisSubscriber implements MessageListener {

  private final SimpMessagingTemplate messagingTemplate;
  private final JsonMapper jsonMapper;

  @Override
  public void onMessage(Message message, byte[] pattern) {
    try {
      String channel = new String(message.getChannel(), StandardCharsets.UTF_8);
      String body = new String(message.getBody(), StandardCharsets.UTF_8);
      messagingTemplate.convertAndSend(channel, jsonMapper.readTree(body));

    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
