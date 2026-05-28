package pe.edu.utp.prisma_api.domain.folder;

import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FolderRepository extends JpaRepository<Folder, String> {

  // Lista todos los folders del proyecto (sección "Tus folders")
  List<Folder> findByProjectId(String projectId, Sort sort);

  // Búsqueda por nombre dentro del proyecto
  List<Folder> findByProjectIdAndNombreContainingIgnoreCase(
      String projectId, String nombre, Sort sort);
}
