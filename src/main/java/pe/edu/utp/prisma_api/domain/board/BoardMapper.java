package pe.edu.utp.prisma_api.domain.board;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import pe.edu.utp.prisma_api.domain.board.dto.BoardDetailDTO;
import pe.edu.utp.prisma_api.domain.board.dto.BoardRequestDTO;
import pe.edu.utp.prisma_api.domain.board.dto.BoardResponseDTO;

@Mapper(componentModel = "spring")
public interface BoardMapper {

  // 1. Entidad → DTO ligero (BoardResponseDTO)
  @Mapping(source = "id", target = "id")
  @Mapping(source = "folder.id", target = "folderId")
  @Mapping(source = "project.id", target = "projectId")
  @Mapping(source = "creator.id", target = "teamMemberId")
  @Mapping(source = "private", target = "isPrivate") // Mapea el getter 'isPrivate()' de Lombok
  BoardResponseDTO toResponse(Board board);

  // 2. Entidad → DTO pesado (BoardDetailDTO)
  @Mapping(source = "id", target = "id")
  @Mapping(source = "folder.id", target = "folderId")
  @Mapping(source = "project.id", target = "projectId")
  @Mapping(source = "creator.id", target = "teamMemberId")
  @Mapping(source = "private", target = "isPrivate") // Mapea el getter 'isPrivate()' de Lombok
  BoardDetailDTO toDetailDto(Board board);

  // 3. DTO → Entidad (BoardRequestDTO)
  @Mapping(target = "id", ignore = true)
  @Mapping(target = "project", ignore = true)
  @Mapping(target = "creator", ignore = true)
  @Mapping(target = "private", ignore = true) // Se ignora porque BoardRequestDTO no tiene este campo
  @Mapping(target = "createdAt", ignore = true)
  @Mapping(target = "updatedAt", ignore = true)
  void toEntity(BoardRequestDTO dto, @MappingTarget Board board);
}