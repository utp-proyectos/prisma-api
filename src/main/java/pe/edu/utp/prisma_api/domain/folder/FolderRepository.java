package pe.edu.utp.prisma_api.domain.folder;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FolderRepository extends JpaRepository<Folder, String> {

  List<Folder> findByProjectIdAndIsPrivate(String projectId, boolean isPrivate);

}
