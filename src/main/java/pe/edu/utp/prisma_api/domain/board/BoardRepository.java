package pe.edu.utp.prisma_api.domain.board;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface BoardRepository extends JpaRepository<Board, String> {
  List<Board> findByProjectIdAndFolderIsNullAndIsPrivate(
      String projectId, boolean isPrivate);
}
