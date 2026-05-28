package pe.edu.utp.prisma_api.domain.folder;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import pe.edu.utp.prisma_api.domain.folder.dto.FolderDetailDTO;
import pe.edu.utp.prisma_api.domain.folder.dto.FolderRequestDTO;
import pe.edu.utp.prisma_api.domain.folder.dto.FolderResponseDTO;

@Mapper(componentModel = "spring")
public interface FolderMapper {

  // Entidad → DTO ligero (para cards del listado)
  @Mapping(source = "creator.id", target = "teamMemberId")
  @Mapping(source = "project.id", target = "projectId")
  FolderResponseDTO toResponse(Folder folder);

  // Entidad → DTO pesado (para abrir el folder con sus pizarras)
  // MapStruct mapea automáticamente el List<Board> → List<BoardResponseDTO>
  // usando el BoardMapper que está en el contexto de Spring
  @Mapping(source = "creator.id", target = "teamMemberId")
  @Mapping(source = "project.id", target = "projectId")
  @Mapping(source = "boards", target = "boards")
  FolderDetailDTO toDetailDto(Folder folder);

  // DTO → Entidad (solo campos planos: nombre)
  // creator y project se asignan manualmente en el servicio
  @Mapping(target = "id", ignore = true)
  @Mapping(target = "creator", ignore = true)
  @Mapping(target = "project", ignore = true)
  @Mapping(target = "boards", ignore = true)
  @Mapping(target = "createdAt", ignore = true)
  @Mapping(target = "updatedAt", ignore = true)
  void toEntity(FolderRequestDTO dto, @MappingTarget Folder folder);
}