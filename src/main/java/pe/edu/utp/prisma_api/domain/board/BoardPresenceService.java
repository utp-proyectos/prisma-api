package pe.edu.utp.prisma_api.domain.board;

import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BoardPresenceService {

  private final SimpMessagingTemplate messagingTemplate;

  // Almacena: BoardId -> Set de Usuarios Conectados
  private final Map<String, Set<Map<String, Object>>> activeUsersByBoard = new ConcurrentHashMap<>();

  // Almacena: SessionId de WebSocket -> Datos del usuario, boardId y teamId
  private final Map<String, Map<String, Object>> sessionUserMap = new ConcurrentHashMap<>();

  public synchronized void joinBoard(String boardId, String sessionId, Map<String, Object> user) {
    user.put("boardId", boardId);
    sessionUserMap.put(sessionId, user);

    activeUsersByBoard.computeIfAbsent(boardId, k -> Collections.synchronizedSet(new LinkedHashSet<>()));
    activeUsersByBoard.get(boardId).add(user);

    // Obtenemos el teamId enviado desde el frontend o un default
    String teamId = (String) user.getOrDefault("teamId", "default");
    broadcastPresence(boardId, teamId);
  }

  public synchronized void leaveBoard(String sessionId) {
    Map<String, Object> user = sessionUserMap.remove(sessionId);
    if (user != null) {
      String boardId = (String) user.get("boardId");
      String teamId = (String) user.getOrDefault("teamId", "default");

      Set<Map<String, Object>> users = activeUsersByBoard.get(boardId);
      if (users != null) {
        users.remove(user);
        if (users.isEmpty()) {
          activeUsersByBoard.remove(boardId);
        }
      }
      broadcastPresence(boardId, teamId);
    }
  }

  private void broadcastPresence(String boardId, String teamId) {
    Set<Map<String, Object>> users = activeUsersByBoard.getOrDefault(boardId, Collections.emptySet());

    String topic = "/topic/" + teamId + "/presence/" + boardId;
    messagingTemplate.convertAndSend(topic, users);
  }

  public synchronized void leaveBoardExplicitly(String boardId, Map<String, Object> userPayload) {
    Set<Map<String, Object>> users = activeUsersByBoard.get(boardId);
    if (users != null) {
      // Buscamos al usuario por su ID y lo removemos
      String userIdToRemove = String.valueOf(userPayload.get("id"));
      users.removeIf(user -> String.valueOf(user.get("id")).equals(userIdToRemove));

      if (users.isEmpty()) {
        activeUsersByBoard.remove(boardId);
      }
    }

    String teamId = (String) userPayload.getOrDefault("teamId", "default");
    broadcastPresence(boardId, teamId);
  }
}