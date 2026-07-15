package pe.edu.utp.prisma_api.domain.board;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

public interface BoardRepository extends JpaRepository<Board, UUID> {
  List<Board> findByProjectIdAndFolderIsNullAndIsPrivate(
      UUID projectId, boolean isPrivate);

  List<Board> findByProjectIdAndFolderIsNullAndIsPrivateAndCreatorId(UUID projectId, boolean isPrivate, UUID creatorId);
}
