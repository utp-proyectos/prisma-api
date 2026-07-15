package pe.edu.utp.prisma_api.domain.folder;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

import pe.edu.utp.prisma_api.domain.folder.dto.FolderRequestDTO;
import pe.edu.utp.prisma_api.domain.folder.dto.FolderResponseDTO;
import pe.edu.utp.prisma_api.domain.folder.dto.UpdateFolderDTO;
import pe.edu.utp.prisma_api.domain.project.Project;
import pe.edu.utp.prisma_api.domain.project.ProjectRepository;
import pe.edu.utp.prisma_api.domain.user.User;
import pe.edu.utp.prisma_api.domain.user.UserRepository;

@Service
@RequiredArgsConstructor
public class FolderService {

  private final FolderRepository folderRepository;
  private final ProjectRepository projectRepository;
  private final FolderMapper folderMapper;
  private final UserRepository userRepository;

  // CREATE
  public FolderResponseDTO create(UUID projectId, UUID creatorId, FolderRequestDTO dto) {
    Project project = projectRepository.findById(projectId)
        .orElseThrow(() -> new EntityNotFoundException("Project not found"));

    User creator = userRepository.findById(creatorId)
        .orElseThrow(() -> new EntityNotFoundException("User not found"));

    Folder folder = new Folder();
    folderMapper.toEntity(dto, folder);

    folder.setProject(project);
    folder.setPrivate(dto.getIsPrivate());
    folder.setCreator(creator);

    return folderMapper.toResponse(folderRepository.save(folder));
  }

  // GET ALL
  public List<FolderResponseDTO> getAll(UUID projectId, Boolean isPrivate, UUID userId) {
    List<Folder> folders;
    if (Boolean.TRUE.equals(isPrivate)) {
      folders = folderRepository.findByProjectIdAndIsPrivateAndCreatorId(projectId, true, userId);
    } else {
      folders = folderRepository.findByProjectIdAndIsPrivate(projectId, false);
    }
    return folders.stream().map(folderMapper::toResponse).toList();
  }

  // FIND BY ID
  public FolderResponseDTO findById(UUID id) {
    return folderMapper.toResponse(findEntityById(id));
  }

  // UPDATE
  public FolderResponseDTO update(UUID id, UpdateFolderDTO dto) {
    Folder folder = findEntityById(id);
    folder.setName(dto.getName());
    return folderMapper.toResponse(folderRepository.save(folder));
  }

  // DELETE
  public void delete(UUID id) {
    folderRepository.delete(findEntityById(id));
  }

  // helper
  private Folder findEntityById(UUID id) {
    return folderRepository.findById(id)
        .orElseThrow(() -> new EntityNotFoundException("Folder not found"));
  }
}