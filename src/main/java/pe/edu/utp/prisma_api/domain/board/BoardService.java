package pe.edu.utp.prisma_api.domain.board;

import java.util.List;
import java.util.UUID;

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

@Service
@RequiredArgsConstructor
public class BoardService {

  private final BoardRepository boardRepository;
  private final FolderRepository folderRepository;
  private final ProjectRepository projectRepository;
  private final BoardMapper boardMapper;

  // CREATE
  public BoardResponseDTO create(UUID projectId, BoardRequestDTO dto) {
    Project project = projectRepository.findById(projectId)
        .orElseThrow(() -> new EntityNotFoundException("Project not found"));

    Board board = new Board();
    boardMapper.toEntity(dto, board);

    board.setProject(project);
    board.setPrivate(dto.getIsPrivate());

    if (dto.getFolderId() != null) {
      Folder folder = folderRepository.findById(dto.getFolderId())
          .orElseThrow(() -> new EntityNotFoundException("Folder not found"));
      board.setFolder(folder);
    } else {
      board.setFolder(null);
    }

    return boardMapper.toResponse(boardRepository.save(board));
  }

  // GET ALL
  public List<BoardResponseDTO> getAll(UUID projectId, Boolean isPrivate) {
    List<Board> boards = boardRepository
        .findByProjectIdAndFolderIsNullAndIsPrivate(projectId, isPrivate);
    return boards.stream().map(boardMapper::toResponse).toList();
  }

  // FIND BY ID
  public BoardDetailDTO findById(UUID id) {
    return boardMapper.toDetailDto(findEntityById(id));
  }

  // UPDATE
  public BoardResponseDTO update(UUID id, BoardRequestDTO dto) {
    Board board = findEntityById(id);
    board.setName(dto.getName());
    return boardMapper.toResponse(boardRepository.save(board));
  }

  // MOVE TO FOLDER
  public BoardResponseDTO moveToFolder(UUID boardId, UUID folderId) {
    Board board = findEntityById(boardId);
    Folder folder = folderRepository.findById(folderId)
        .orElseThrow(() -> new EntityNotFoundException("Folder not found"));
    board.setFolder(folder);
    return boardMapper.toResponse(boardRepository.save(board));
  }

  // REMOVE FROM FOLDER
  public BoardResponseDTO removeFromFolder(UUID boardId) {
    Board board = findEntityById(boardId);
    board.setFolder(null);
    return boardMapper.toResponse(boardRepository.save(board));
  }

  // DELETE
  public void delete(UUID id) {
    boardRepository.delete(findEntityById(id));
  }

  // helper
  private Board findEntityById(UUID id) {
    return boardRepository.findById(id)
        .orElseThrow(() -> new EntityNotFoundException("Board not found"));
  }

}