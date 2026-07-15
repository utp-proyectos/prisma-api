package pe.edu.utp.prisma_api.domain.folder;

import java.util.List;
import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import pe.edu.utp.prisma_api.domain.folder.dto.FolderResponseDTO;

@RestController
@RequiredArgsConstructor
public class FolderController {

  private final FolderService folderService;

  @GetMapping("/api/projects/{projectId}/folders")
  public ResponseEntity<List<FolderResponseDTO>> getAll(
      @PathVariable UUID projectId,
      @RequestParam Boolean isPrivate,
      @AuthenticationPrincipal UUID userId) {
    return ResponseEntity.ok(folderService.getAll(projectId, isPrivate, userId));
  }

  @GetMapping("/api/folders/{folderId}")
  public ResponseEntity<FolderResponseDTO> findById(@PathVariable UUID folderId) {
    return ResponseEntity.ok(folderService.findById(folderId));
  }

}