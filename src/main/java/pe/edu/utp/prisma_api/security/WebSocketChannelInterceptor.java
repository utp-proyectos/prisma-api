package pe.edu.utp.prisma_api.security;

import java.time.Duration;
import java.util.UUID;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import pe.edu.utp.prisma_api.domain.team.TeamMemberRepository;
import pe.edu.utp.prisma_api.security.jwt.JwtService;

@Component
@RequiredArgsConstructor
public class WebSocketChannelInterceptor implements ChannelInterceptor {

  private final JwtService jwtService;
  private final TeamMemberRepository teamMemberRepository;
  private final RedisTemplate<String, Object> redisTemplate;

  @Override
  public Message<?> preSend(Message<?> message, MessageChannel channel) {
    StompHeaderAccessor accessor = MessageHeaderAccessor
        .getAccessor(message, StompHeaderAccessor.class);

    if (accessor == null)
      return message;

    if (StompCommand.CONNECT.equals(accessor.getCommand())) {
      handleConnect(accessor);
    }

    if (StompCommand.SUBSCRIBE.equals(accessor.getCommand())) {
      handleSubscribe(accessor);
    }

    if (StompCommand.SEND.equals(accessor.getCommand())) {
      handleSend(accessor);
    }

    if (StompCommand.DISCONNECT.equals(accessor.getCommand())) {
      handleDisconnect(accessor);
    }

    return message;
  }

  private void handleConnect(StompHeaderAccessor accessor) {
    String token = accessor.getFirstNativeHeader("Authorization");

    if (token == null || !token.startsWith("Bearer ")) {
      throw new AccessDeniedException("Token requerido para conectar");
    }

    token = token.substring(7);

    if (!jwtService.isValid(token)) {
      throw new AccessDeniedException("Token inválido o expirado");
    }

    UUID userId = jwtService.extractUserId(token);
    accessor.setUser(() -> userId.toString());

    redisTemplate.opsForValue().set(
        "session:" + userId,
        userId,
        Duration.ofHours(24));
  }

  private void handleSubscribe(StompHeaderAccessor accessor) {
    System.out.println("------------------------------------");
    System.out.println("subscribe");
    System.out.println("------------------------------------");
    String destination = accessor.getDestination();
    if (destination == null)
      return;

    UUID teamId = extractTeamId(destination);
    if (teamId == null)
      return;

    UUID userId = accessor.getUser() != null
        ? UUID.fromString(accessor.getUser().getName())
        : null;

    if (userId == null) {
      throw new AccessDeniedException("No autenticado");
    }

    boolean isMember = teamMemberRepository
        .existsByUserIdAndTeamId(userId, teamId);

    if (!isMember) {
      throw new AccessDeniedException(
          "No tienes acceso al equipo: " + teamId);
    }
  }

  private void handleSend(StompHeaderAccessor accessor) {
    System.out.println("aqui???????");
    if (accessor.getUser() == null) {
      throw new AccessDeniedException("No autenticado");
    }
  }

  private void handleDisconnect(StompHeaderAccessor accessor) {
    if (accessor.getUser() != null) {
      redisTemplate.delete("session:" + accessor.getUser().getName());
    }
  }

  private UUID extractTeamId(String destination) {
    String[] parts = destination.split("/");
    if (parts.length >= 3 && destination.startsWith("/topic/")) {
      return UUID.fromString(parts[2]);
    }
    return null;
  }
}