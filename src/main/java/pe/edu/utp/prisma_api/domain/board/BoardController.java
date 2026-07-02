package pe.edu.utp.prisma_api.domain.board;

import java.util.List;
import java.util.UUID;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import pe.edu.utp.prisma_api.domain.board.dto.BoardDetailDTO;
import pe.edu.utp.prisma_api.domain.board.dto.BoardRequestDTO;
import pe.edu.utp.prisma_api.domain.board.dto.BoardResponseDTO;
import pe.edu.utp.prisma_api.infraestructure.redis.RedisPublisher;

@RestController
@RequiredArgsConstructor
public class BoardController {

  private final BoardService boardService;
  private final RedisTemplate<String, Object> redisTemplate;
  private final RedisPublisher redisPublisher;

  @PostMapping("/api/projects/{projectId}/boards")
  public ResponseEntity<BoardResponseDTO> create(
      @PathVariable UUID projectId,
      @RequestBody BoardRequestDTO dto) {
    BoardResponseDTO board = boardService.create(projectId, dto);
    String topic = "/topic/" + dto.getTeamId() + "/project/" + projectId + "/boards";
    System.out.println("Publicando en: " + topic);
    redisPublisher.publish("/topic/" + dto.getTeamId() + "/project/" + projectId + "/boards", board);
    return ResponseEntity.ok(board);
  }

  @GetMapping("/api/projects/{projectId}/boards")
  public ResponseEntity<List<BoardResponseDTO>> getAll(
      @PathVariable UUID projectId,
      @RequestParam Boolean isPrivate) {
    return ResponseEntity.ok(boardService.getAll(projectId, isPrivate));
  }

  @GetMapping("/api/boards/{boardId}")
  public ResponseEntity<BoardDetailDTO> findById(@PathVariable UUID boardId) {
    return ResponseEntity.ok(boardService.findById(boardId));
  }

  @PatchMapping("/api/boards/{boardId}/canvas")
  public ResponseEntity<Void> saveCanvas(
      @PathVariable UUID boardId,
      @RequestBody String konvaData) {
    redisTemplate.opsForValue().set("canvas:" + boardId, konvaData);
    return ResponseEntity.noContent().build();
  }

  @GetMapping("/api/boards/{boardId}/canvas")
  public ResponseEntity<String> getCanvas(@PathVariable UUID boardId) {
    Object konvaData = redisTemplate.opsForValue().get("canvas:" + boardId);
    if (konvaData == null)
      return ResponseEntity.ok(null);
    return ResponseEntity.ok(konvaData.toString());
  }
}