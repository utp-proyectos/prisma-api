package pe.edu.utp.prisma_api.domain.folder;

import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import pe.edu.utp.prisma_api.domain.folder.dto.FolderDetailDTO;
import pe.edu.utp.prisma_api.domain.folder.dto.FolderRequestDTO;
import pe.edu.utp.prisma_api.domain.folder.dto.FolderResponseDTO;
import pe.edu.utp.prisma_api.domain.project.Project;
import pe.edu.utp.prisma_api.domain.project.ProjectRepository;
import pe.edu.utp.prisma_api.domain.team.TeamMember;
import pe.edu.utp.prisma_api.domain.team.TeamMemberRepository;

@Service
@RequiredArgsConstructor
public class FolderService {
  private final FolderRepository folderRepository;
  private final ProjectRepository projectRepository;
  private final TeamMemberRepository teamMemberRepository;
  private final FolderMapper folderMapper;

  // CREATsE — projectId viene de la URL
  public FolderResponseDTO create(String projectId, FolderRequestDTO dto) {
    Project project = projectRepository.findById(projectId)
        .orElseThrow(() -> new EntityNotFoundException("Project not found"));

    TeamMember creator = teamMemberRepository.findById(dto.getTeamMemberId())
        .orElseThrow(() -> new EntityNotFoundException("TeamMember not found"));

    Folder folder = new Folder();
    folderMapper.toEntity(dto, folder);

    folder.setProject(project);
    folder.setCreator(creator);
    folder.setPrivate(dto.getIsPrivate() != null ? dto.getIsPrivate() : false);

    Folder saved = folderRepository.save(folder);
    return folderMapper.toResponse(saved);
  }

  // GET ALL — lista folders del proyecto (sección "Tus folders")
  public List<FolderResponseDTO> getAll(String projectId, String order) {
    List<Folder> folders = folderRepository.findByProjectId(projectId, buildSort(order));
    return folders.stream().map(folderMapper::toResponse).toList();
  }

  // SEARCH — busca folder por nombre en el proyecto
  public List<FolderResponseDTO> search(String projectId, String nombre, String order) {
    List<Folder> folders = folderRepository
        .findByProjectIdAndNombreContainingIgnoreCase(projectId, nombre, buildSort(order));

    return folders.stream().map(folderMapper::toResponse).toList();
  }

  // FIND BY ID — abre el folder y trae sus pizarras
  public FolderDetailDTO findById(String id) {
    Folder folder = findEntityById(id);
    return folderMapper.toDetailDto(folder);
  }

  // UPDATE — renombra el folder
  public FolderResponseDTO update(String id, FolderRequestDTO dto) {
    Folder folder = findEntityById(id);
    folder.setNombre(dto.getNombre());

    Folder saved = folderRepository.save(folder);
    return folderMapper.toResponse(saved);
  }

  // TOGGLE PRIVACY — cambia privacidad del folder y la propaga a sus pizarras
  public FolderResponseDTO togglePrivacy(String id) {
    Folder folder = findEntityById(id);
    boolean newPrivacy = !folder.isPrivate();

    folder.setPrivate(newPrivacy);

    // Propaga la privacidad a todas las pizarras dentro del folder
    folder.getBoards().forEach(board -> board.setPrivate(newPrivacy));

    Folder saved = folderRepository.save(folder); // CascadeType.ALL guarda las pizarras también
    return folderMapper.toResponse(saved);
  }

  // DELETE — elimina el folder y sus pizarras en cascada (orphanRemoval = true)
  public void delete(String id) {
    Folder folder = findEntityById(id);
    folderRepository.delete(folder);
  }

  // aux
  private Folder findEntityById(String id) {
    return folderRepository.findById(id)
        .orElseThrow(() -> new EntityNotFoundException("Folder not found"));
  }

  // aux
  private Sort buildSort(String order) {
    if (order == null || order.isBlank()) {
      return Sort.by("createdAt").descending();
    }
    return switch (order) {
      case "oldest" -> Sort.by("createdAt").ascending();
      case "alphabetical" -> Sort.by("nombre").ascending();
      default -> Sort.by("createdAt").descending();
    };
  }
}