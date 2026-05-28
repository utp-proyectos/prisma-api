package pe.edu.utp.prisma_api.domain.board;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import pe.edu.utp.prisma_api.domain.board.dto.BoardDetailDTO;
import pe.edu.utp.prisma_api.domain.board.dto.BoardRequestDTO;
import pe.edu.utp.prisma_api.domain.board.dto.BoardResponseDTO;

@Mapper(componentModel = "spring")
public interface BoardMapper {

  // Entidad → DTO ligero (para listas, create, move, etc.)
  @Mapping(source = "folder.id", target = "folderId")
  @Mapping(source = "project.id", target = "projectId")
  @Mapping(source = "creator.id", target = "teamMemberId") // creator es TeamMember
  BoardResponseDTO toResponse(Board board);

  // Entidad → DTO pesado (solo para abrir el editor)
  @Mapping(source = "folder.id", target = "folderId")
  @Mapping(source = "project.id", target = "projectId")
  @Mapping(source = "creator.id", target = "teamMemberId") // creator es TeamMember
  BoardDetailDTO toDetailDto(Board board);

  // DTO → Entidad (solo campos planos: name, konvaData)
  // Las relaciones (project, creator, folder) se asignan manualmente en el
  // servicio
  @Mapping(target = "id", ignore = true)
  @Mapping(target = "project", ignore = true)
  @Mapping(target = "creator", ignore = true) // TeamMember se asigna en el servicio
  @Mapping(target = "folder", ignore = true)
  @Mapping(target = "isPrivate", ignore = true)
  void toEntity(BoardRequestDTO dto, @MappingTarget Board board);
}