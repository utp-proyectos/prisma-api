package pe.edu.utp.prisma_api.domain.board;

import java.security.Principal;
import java.util.UUID;

import jakarta.validation.Valid;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Controller;

import lombok.RequiredArgsConstructor;
import pe.edu.utp.prisma_api.domain.board.dto.BoardRequestDTO;
import pe.edu.utp.prisma_api.domain.board.dto.BoardResponseDTO;
import pe.edu.utp.prisma_api.domain.board.dto.DeleteBoardDTO;
import pe.edu.utp.prisma_api.domain.board.dto.MoveBoardDTO;
import pe.edu.utp.prisma_api.infraestructure.redis.RedisPublisher;

@Controller
@RequiredArgsConstructor
public class BoardWsController {

  private final BoardService boardService;
  private final RedisPublisher redisPublisher;

  @MessageMapping("/board.delete")
  public void deleteBoard(
      @Payload DeleteBoardDTO dto,
      Principal principal) {
    System.out.println("Eliminando board: " + dto.getBoardId());

    boardService.delete(dto.getBoardId());

    redisPublisher.publish("/topic/" + dto.getTeamId() + "/project/" + dto.getProjectId() + "/boards/delete", dto);
  }

  @MessageMapping("/board.move")
  public void moveToFolder(
      @Payload MoveBoardDTO dto,
      Principal principal) {

    BoardResponseDTO board = dto.getFolderId() != null
        ? boardService.moveToFolder(dto.getBoardId(), dto.getFolderId())
        : boardService.removeFromFolder(dto.getBoardId());

    redisPublisher.publish("/topic/" + dto.getTeamId() + "/project/" + dto.getProjectId() + "/boards", board);
  }
}