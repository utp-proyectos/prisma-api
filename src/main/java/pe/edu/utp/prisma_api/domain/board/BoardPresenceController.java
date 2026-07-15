package pe.edu.utp.prisma_api.domain.board;

import org.springframework.context.event.EventListener;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;
import org.springframework.stereotype.Controller;
import lombok.RequiredArgsConstructor;
import java.util.Map;

@Controller
@RequiredArgsConstructor
public class BoardPresenceController {

  private final BoardPresenceService presenceService;

  @MessageMapping("/boards/{boardId}/presence.join")
  public void joinBoard(
      @DestinationVariable String boardId,
      @Payload Map<String, Object> userPayload,
      SimpMessageHeaderAccessor headerAccessor) {

    String sessionId = headerAccessor.getSessionId();
    presenceService.joinBoard(boardId, sessionId, userPayload);
  }

  @EventListener
  public void handleWebSocketDisconnectListener(SessionDisconnectEvent event) {
    presenceService.leaveBoard(event.getSessionId());
  }

  @MessageMapping("/boards/{boardId}/presence.leave")
  public void leaveBoard(
      @DestinationVariable String boardId,
      @Payload Map<String, Object> userPayload,
      SimpMessageHeaderAccessor headerAccessor) {

    // Forzamos la salida usando el sessionId o los datos que envía el payload
    presenceService.leaveBoardExplicitly(boardId, userPayload);
  }
}