package pe.edu.utp.prisma_api.domain.board;

import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BoardRepository extends JpaRepository<Board, String> {
  // boards-tus pizarras-public-ordenados
  List<Board> findByProjectIdAndFolderIsNullAndIsPrivateFalse(
      String projectId, Sort sort);

  // todas las pizarras dentro de un folder especifico
  List<Board> findByFolderId(String folderId, Sort sort);

  // busca todas las pizarras dentro de un folder mas busqueda
  List<Board> findByFolderIdAndNameContainingIgnoreCase(String folderId, String name, Sort sort);

  // busca todas las pizarras dentro de tus pizarras
  List<Board> findByProjectIdAndFolderIsNullAndIsPrivateFalseAndNameContainingIgnoreCase(
      String projectId, String name, Sort sort);
}
