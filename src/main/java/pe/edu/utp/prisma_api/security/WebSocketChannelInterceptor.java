package pe.edu.utp.prisma_api.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Component;

import pe.edu.utp.prisma_api.domain.team.TeamMemberRepository;
import pe.edu.utp.prisma_api.security.jwt.JwtService;

@Component
public class WebSocketChannelInterceptor implements ChannelInterceptor {

  @Autowired
  private JwtService jwtService;
  @Autowired
  private TeamMemberRepository teamMemberRepository;

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

    return message;
  }

  // valida el JWT cuando el cliente se conecta
  private void handleConnect(StompHeaderAccessor accessor) {
    String token = accessor.getFirstNativeHeader("Authorization");

    if (token == null || !token.startsWith("Bearer ")) {
      throw new AccessDeniedException("Token requerido para conectar");
    }

    token = token.substring(7);

    if (!jwtService.isValid(token)) {
      throw new AccessDeniedException("Token inválido o expirado");
    }

    String userId = jwtService.extractUserId(token);
    accessor.setUser(() -> userId); // guarda el userId en la sesión WebSocket
  }

  // verifica que el usuario pertenece al equipo del topic
  private void handleSubscribe(StompHeaderAccessor accessor) {
    String destination = accessor.getDestination();
    if (destination == null)
      return;

    // topic formato: /topic/{teamId}/{projectId}/...
    String teamId = extractTeamId(destination);
    if (teamId == null)
      return;

    String userId = accessor.getUser() != null
        ? accessor.getUser().getName()
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

  // verifica que el usuario está autenticado para enviar mensajes
  private void handleSend(StompHeaderAccessor accessor) {
    if (accessor.getUser() == null) {
      throw new AccessDeniedException("No autenticado");
    }
  }

  // extrae el teamId del topic: /topic/team123/project456/chat/general → team123
  private String extractTeamId(String destination) {
    String[] parts = destination.split("/");
    // partes: ["", "topic", "team123", "project456", ...]
    if (parts.length >= 3 && destination.startsWith("/topic/")) {
      return parts[2];
    }
    return null;
  }
}