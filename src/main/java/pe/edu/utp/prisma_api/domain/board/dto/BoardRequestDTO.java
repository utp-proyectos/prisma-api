package pe.edu.utp.prisma_api.domain.board.dto;

import java.util.UUID;
<<<<<<< HEAD
=======

import lombok.Data;
>>>>>>> b133345b27ff96f88b62461a690d404694cf2159

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BoardRequestDTO {
  private String name;
  private String description;
  private UUID folderId;
  private Boolean isPrivate;
}
