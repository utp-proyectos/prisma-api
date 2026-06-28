package pe.edu.utp.prisma_api.domain.project;

import org.mapstruct.Mapper;

import pe.edu.utp.prisma_api.domain.kanban.KanbanMapper;

@Mapper(componentModel = "spring", uses = KanbanMapper.class)
public interface ProjectMapper {

}