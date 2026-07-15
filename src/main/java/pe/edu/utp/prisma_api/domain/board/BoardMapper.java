package pe.edu.utp.prisma_api.domain.board;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import pe.edu.utp.prisma_api.domain.board.dto.BoardDetailDTO;
import pe.edu.utp.prisma_api.domain.board.dto.BoardRequestDTO;
import pe.edu.utp.prisma_api.domain.board.dto.BoardResponseDTO;

@Mapper(componentModel = "spring")
public interface BoardMapper {

  @Mapping(source = "id", target = "id")
  @Mapping(source = "folder.id", target = "folderId")
  @Mapping(source = "private", target = "isPrivate")
  @Mapping(source = "creator.id", target = "creatorId")
  BoardResponseDTO toResponse(Board board);

  @Mapping(source = "id", target = "id")
  @Mapping(source = "folder.id", target = "folderId")
  @Mapping(source = "private", target = "isPrivate")
  @Mapping(source = "creator.id", target = "creatorId")
  BoardDetailDTO toDetailDto(Board board);

  @Mapping(target = "id", ignore = true)
  @Mapping(target = "folder", ignore = true)
  @Mapping(target = "project", ignore = true)
  @Mapping(target = "private", ignore = true)
  @Mapping(target = "thumbnailUrl", ignore = true)
  @Mapping(target = "createdAt", ignore = true)
  @Mapping(target = "updatedAt", ignore = true)
  @Mapping(target = "creator", ignore = true)
  void toEntity(BoardRequestDTO dto, @MappingTarget Board board);
}