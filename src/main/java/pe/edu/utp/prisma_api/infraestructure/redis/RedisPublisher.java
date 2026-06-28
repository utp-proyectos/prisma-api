package pe.edu.utp.prisma_api.infraestructure.redis;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RedisPublisher {
  private final RedisTemplate<String, Object> redisTemplate;

  public void publish(String topic, Object message) {
    redisTemplate.convertAndSend(topic, message);
  }
}