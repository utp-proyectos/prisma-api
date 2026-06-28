package pe.edu.utp.prisma_api.domain.board;

import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import pe.edu.utp.prisma_api.domain.board.dto.BoardDetailDTO;
import pe.edu.utp.prisma_api.domain.board.dto.BoardRequestDTO;
import pe.edu.utp.prisma_api.domain.board.dto.BoardResponseDTO;
import pe.edu.utp.prisma_api.domain.folder.Folder;
import pe.edu.utp.prisma_api.domain.folder.FolderRepository;
import pe.edu.utp.prisma_api.domain.project.Project;
import pe.edu.utp.prisma_api.domain.project.ProjectRepository;
import pe.edu.utp.prisma_api.domain.team.TeamMember;
import pe.edu.utp.prisma_api.domain.team.TeamMemberRepository;

@Service
@RequiredArgsConstructor
public class BoardService {

  private final BoardRepository boardRepository;
  private final FolderRepository folderRepository;
  private final ProjectRepository projectRepository;
  private final TeamMemberRepository teamMemberRepository;
  private final BoardMapper boardMapper;

  // 💾 CREATE — projectId viene de la URL, no del body
  public BoardResponseDTO create(UUID projectId, BoardRequestDTO dto) {
    Project project = projectRepository.findById(projectId)
        .orElseThrow(() -> new EntityNotFoundException("Project not found"));

    TeamMember creator = teamMemberRepository.findById(dto.getTeamMemberId())
        .orElseThrow(() -> new EntityNotFoundException("TeamMember not found"));

    Board board = new Board();
    boardMapper.toEntity(dto, board); // mapea solo: name, konvaData

    board.setProject(project);
    board.setCreator(creator); // asigna TeamMember como creador

    // Si viene folderId la crea dentro de esa carpeta y hereda privacidad
    if (dto.getFolderId() != null) {
      Folder folder = folderRepository.findById(dto.getFolderId())
          .orElseThrow(() -> new EntityNotFoundException("Folder not found"));
      board.setFolder(folder);
      board.setPrivate(folder.isPrivate());
    } else {
      // Por defecto: raíz del proyecto y pública
      board.setFolder(null);
      board.setPrivate(false);
    }

    Board saved = boardRepository.save(board);
    return boardMapper.toResponse(saved);
  }

  // GET ALL — pizarras públicas en la raíz (sección "Tus pizarras")
  public List<BoardResponseDTO> getAll(String projectId, String order) {
    List<Board> boards = boardRepository
        .findByProjectIdAndFolderIsNullAndIsPrivateFalse(projectId, buildSort(order));

    return boards.stream().map(boardMapper::toResponse).toList();
  }

  // SEARCH — búsqueda por nombre en la raíz del proyecto
  public List<BoardResponseDTO> search(String projectId, String name, String order) {
    List<Board> boards = boardRepository
        .findByProjectIdAndFolderIsNullAndIsPrivateFalseAndNameContainingIgnoreCase(
            projectId, name, buildSort(order));

    return boards.stream().map(boardMapper::toResponse).toList();
  }

  // GET BY FOLDER — pizarras dentro de una carpeta (sección "Folder X")
  public List<BoardResponseDTO> getByFolder(String folderId, String order) {
    List<Board> boards = boardRepository.findByFolderId(folderId, buildSort(order));
    return boards.stream().map(boardMapper::toResponse).toList();
  }

  // SEARCH IN FOLDER — búsqueda por nombre dentro de una carpeta
  public List<BoardResponseDTO> searchInFolder(String folderId, String name, String order) {
    List<Board> boards = boardRepository
        .findByFolderIdAndNameContainingIgnoreCase(folderId, name, buildSort(order));

    return boards.stream().map(boardMapper::toResponse).toList();
  }

  // FIND BY ID — trae la pizarra completa con konvaData (abrir editor)
  public BoardDetailDTO findById(String id) {
    Board board = findEntityById(id);
    return boardMapper.toDetailDto(board);
  }

  // UPDATE / AUTO-SAVE — solo actualiza name y konvaData
  public BoardResponseDTO update(String id, BoardRequestDTO dto) {
    Board board = findEntityById(id);

    board.setName(dto.getName());
    board.setKonvaData(dto.getKonvaData());

    Board saved = boardRepository.save(board);
    return boardMapper.toResponse(saved);
  }

  // MOVE TO FOLDER — drag & drop hacia una carpeta, hereda privacidad
  public BoardResponseDTO moveToFolder(String boardId, String folderId) {
    Board board = findEntityById(boardId);

    Folder folder = folderRepository.findById(folderId)
        .orElseThrow(() -> new EntityNotFoundException("Folder not found"));

    board.setFolder(folder);
    board.setPrivate(folder.isPrivate());

    Board saved = boardRepository.save(board);
    return boardMapper.toResponse(saved);
  }

  // REMOVE FROM FOLDER — drag & drop hacia la raíz, vuelve pública
  public BoardResponseDTO removeFromFolder(String boardId) {
    Board board = findEntityById(boardId);
    board.setFolder(null);
    board.setPrivate(false);

    Board saved = boardRepository.save(board);
    return boardMapper.toResponse(saved);
  }

  // DELETE
  public void delete(String id) {
    Board board = findEntityById(id);
    boardRepository.delete(board);
  }

  // INTERNAL HELPER — evita repetir el findById + orElseThrow en cada método
  private Board findEntityById(String id) {
    return boardRepository.findById(id)
        .orElseThrow(() -> new EntityNotFoundException("Board not found"));
  }

  // INTERNAL HELPER — construye el Sort según el parámetro recibido
  private Sort buildSort(String order) {
    if (order == null || order.isBlank()) {
      return Sort.by("createdAt").descending();
    }
    return switch (order) {
      case "oldest" -> Sort.by("createdAt").ascending();
      case "alphabetical" -> Sort.by("name").ascending();
      default -> Sort.by("createdAt").descending();
    };
  }
}