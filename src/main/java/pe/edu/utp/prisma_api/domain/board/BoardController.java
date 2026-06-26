package pe.edu.utp.prisma_api.domain.board;

import java.util.List;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
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

@RestController
@RequiredArgsConstructor
public class BoardController {

  private final BoardService boardService;

  private final RedisTemplate<String, Object> redisTemplate;

  @PostMapping("/api/projects/{projectId}/boards")
  public ResponseEntity<BoardResponseDTO> create(
      @PathVariable String projectId,
      @RequestBody BoardRequestDTO dto) {
    return ResponseEntity.ok(boardService.create(projectId, dto));
  }

  @GetMapping("/api/projects/{projectId}/boards")
  public ResponseEntity<List<BoardResponseDTO>> getAll(
      @PathVariable String projectId,
      @RequestParam Boolean isPrivate) {
    return ResponseEntity.ok(boardService.getAll(projectId, isPrivate));
  }

  @GetMapping("/api/boards/{boardId}")
  public ResponseEntity<BoardDetailDTO> findById(@PathVariable String boardId) {
    return ResponseEntity.ok(boardService.findById(boardId));
  }

  @PatchMapping("/api/boards/{boardId}/move-to-folder")
  public ResponseEntity<BoardResponseDTO> moveToFolder(
      @PathVariable String boardId,
      @RequestParam String folderId) {
    return ResponseEntity.ok(boardService.moveToFolder(boardId, folderId));
  }

  @PatchMapping("/api/boards/{boardId}/remove-from-folder")
  public ResponseEntity<BoardResponseDTO> removeFromFolder(@PathVariable String boardId) {
    return ResponseEntity.ok(boardService.removeFromFolder(boardId));
  }

  @DeleteMapping("/api/boards/{boardId}")
  public ResponseEntity<Void> delete(@PathVariable String boardId) {
    boardService.delete(boardId);
    return ResponseEntity.noContent().build();
  }

  @PatchMapping("/api/boards/{boardId}/canvas")
  public ResponseEntity<Void> saveCanvas(
      @PathVariable String boardId,
      @RequestBody String konvaData) {
    redisTemplate.opsForValue().set("canvas:" + boardId, konvaData);
    return ResponseEntity.noContent().build();
  }

  @GetMapping("/api/boards/{boardId}/canvas")
  public ResponseEntity<String> getCanvas(@PathVariable String boardId) {
    Object konvaData = redisTemplate.opsForValue().get("canvas:" + boardId);
    if (konvaData == null)
      return ResponseEntity.ok(null);
    return ResponseEntity.ok(konvaData.toString());
  }
}