package pe.edu.utp.prisma_api.domain.folder;

import java.util.List;
import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import pe.edu.utp.prisma_api.domain.folder.dto.FolderRequestDTO;
import pe.edu.utp.prisma_api.domain.folder.dto.FolderResponseDTO;

@RestController
@RequiredArgsConstructor
public class FolderController {

  private final FolderService folderService;

  @PostMapping("/api/projects/{projectId}/folders")

  public ResponseEntity<FolderResponseDTO> create(
      @PathVariable UUID projectId,
      @RequestBody FolderRequestDTO dto) {
    return ResponseEntity.ok(folderService.create(projectId, dto));
  }

  @GetMapping("/api/projects/{projectId}/folders")

  public ResponseEntity<List<FolderResponseDTO>> getAll(
      @PathVariable UUID projectId,
      @RequestParam Boolean isPrivate) {
    return ResponseEntity.ok(folderService.getAll(projectId, isPrivate));
  }

  @GetMapping("/api/folders/{folderId}")
  public ResponseEntity<FolderResponseDTO> findById(@PathVariable UUID folderId) {
    return ResponseEntity.ok(folderService.findById(folderId));
  }

  @PutMapping("/api/folders/{folderId}")
  public ResponseEntity<FolderResponseDTO> update(
      @PathVariable UUID folderId,
      @RequestBody FolderRequestDTO dto) {
    return ResponseEntity.ok(folderService.update(folderId, dto));
  }

  @DeleteMapping("/api/folders/{folderId}")
  public ResponseEntity<Void> delete(@PathVariable UUID folderId) {
    folderService.delete(folderId);
    return ResponseEntity.noContent().build();
  }
}