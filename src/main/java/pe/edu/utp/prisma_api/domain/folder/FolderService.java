package pe.edu.utp.prisma_api.domain.folder;

import java.util.List;

import org.springframework.stereotype.Service;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

import pe.edu.utp.prisma_api.domain.folder.dto.FolderRequestDTO;
import pe.edu.utp.prisma_api.domain.folder.dto.FolderResponseDTO;
import pe.edu.utp.prisma_api.domain.project.Project;
import pe.edu.utp.prisma_api.domain.project.ProjectRepository;

@Service
@RequiredArgsConstructor
public class FolderService {

  private final FolderRepository folderRepository;
  private final ProjectRepository projectRepository;
  private final FolderMapper folderMapper;

  // CREATE
  public FolderResponseDTO create(String projectId, FolderRequestDTO dto) {
    Project project = projectRepository.findById(projectId)
        .orElseThrow(() -> new EntityNotFoundException("Project not found"));

    Folder folder = new Folder();
    folderMapper.toEntity(dto, folder);

    folder.setProject(project);
    folder.setPrivate(dto.getIsPrivate());

    return folderMapper.toResponse(folderRepository.save(folder));
  }

  // GET ALL
  public List<FolderResponseDTO> getAll(String projectId, Boolean isPrivate) {
    List<Folder> folders = folderRepository
        .findByProjectIdAndIsPrivate(projectId, isPrivate);
    return folders.stream().map(folderMapper::toResponse).toList();
  }

  // FIND BY ID
  public FolderResponseDTO findById(String id) {
    return folderMapper.toResponse(findEntityById(id));
  }

  // UPDATE
  public FolderResponseDTO update(String id, FolderRequestDTO dto) {
    Folder folder = findEntityById(id);
    folder.setName(dto.getName());
    return folderMapper.toResponse(folderRepository.save(folder));
  }

  // DELETE
  public void delete(String id) {
    folderRepository.delete(findEntityById(id));
  }

  // helper
  private Folder findEntityById(String id) {
    return folderRepository.findById(id)
        .orElseThrow(() -> new EntityNotFoundException("Folder not found"));
  }
}