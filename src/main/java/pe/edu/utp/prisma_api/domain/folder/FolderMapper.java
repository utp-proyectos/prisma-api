package pe.edu.utp.prisma_api.domain.folder;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import pe.edu.utp.prisma_api.domain.board.BoardMapper;
import pe.edu.utp.prisma_api.domain.folder.dto.FolderRequestDTO;
import pe.edu.utp.prisma_api.domain.folder.dto.FolderResponseDTO;

@Mapper(componentModel = "spring", uses = { BoardMapper.class })
public interface FolderMapper {

  @Mapping(source = "private", target = "isPrivate")
  @Mapping(source = "boards", target = "boards")
  @Mapping(source = "creator.id", target = "creatorId")
  FolderResponseDTO toResponse(Folder folder);

  @Mapping(target = "id", ignore = true)
  @Mapping(target = "project", ignore = true)
  @Mapping(target = "boards", ignore = true)
  @Mapping(target = "private", ignore = true)
  @Mapping(target = "createdAt", ignore = true)
  @Mapping(target = "updatedAt", ignore = true)
  @Mapping(target = "creator", ignore = true)
  void toEntity(FolderRequestDTO dto, @MappingTarget Folder folder);
}