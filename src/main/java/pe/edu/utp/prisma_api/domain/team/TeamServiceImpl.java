package pe.edu.utp.prisma_api.domain.team;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import pe.edu.utp.prisma_api.common.enums.TeamRole;
import pe.edu.utp.prisma_api.domain.team.dto.TeamRequestDTO;
import pe.edu.utp.prisma_api.domain.team.dto.TeamResponseDTO;
import pe.edu.utp.prisma_api.domain.user.User;
import pe.edu.utp.prisma_api.domain.user.UserRepository;

@Service
public class TeamServiceImpl implements TeamService {
    @Autowired
    private TeamRepository teamRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TeamMemberRepository teamMemberRepository;

    @Autowired
    private TeamMapper teamMapper;

    @Override
    public List<TeamResponseDTO> getAll() {
        return teamRepository.findAll().stream()
                .map(teamMapper::toResponseDTO)
                .toList();
    }

    @Override
    public TeamResponseDTO save(TeamRequestDTO dto, String userId) {

        User owner = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        Team team = teamMapper.toEntity(dto);

        TeamMember members = new TeamMember();
        members.setUser(owner);
        members.setTeam(team);
        members.setRole(TeamRole.OWNER);
        members.setJoinedAt(LocalDate.now());

        team.setMembers(new ArrayList<>());
        team.getMembers().add(members);

        Team savedTeam = teamRepository.save(team);

        return teamMapper.toResponseDTO(savedTeam);
    }

    @Override
    public Optional<TeamResponseDTO> update(String id, TeamRequestDTO dto) {

        Team existingTeam = teamRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Equipo no encontrado"));

        teamMapper.updateEntityFromDto(dto, existingTeam);
        return Optional.of(teamMapper.toResponseDTO(teamRepository.save(existingTeam)));
    }

    @Override
    public void delete(String teamId) {
        teamRepository.deleteById(teamId);
    }

    // Permisos y roles: solo el OWNER o ADMIN pueden cambiar roles de otros
    @Override
    public void updateMemberRole(String teamId, String adminOwnerId, String targetUserId, TeamRole newRole) {
        // 1. Validar que el que intenta cambiar el rol sea ADMIN en ese equipo
        TeamMember adminMembership = teamMemberRepository.findByUserIdAndTeamId(adminOwnerId, teamId)
                .orElseThrow(() -> new RuntimeException("No perteneces a este equipo"));

        if (adminMembership.getRole() != TeamRole.ADMIN && adminMembership.getRole() != TeamRole.OWNER) {
            throw new RuntimeException("No tienes permisos para cambiar roles");
        }

        // 2. Buscar la membresía del usuario a quien queremos cambiar el rol
        TeamMember targetMembership = teamMemberRepository.findByUserIdAndTeamId(targetUserId, teamId)
                .orElseThrow(() -> new RuntimeException("El usuario no pertenece a este equipo"));

        // 3. Cambiar el rol
        targetMembership.setRole(newRole);

        // 4. Guardar
        teamMemberRepository.save(targetMembership);
    }

    // Método para obtener los equipos de un usuario
    @Override
    public List<TeamResponseDTO> getUserTeams(String userId) {
        // 1. Obtenemos las entidades Team de la DB
        List<Team> teams = teamRepository.findByMembersUserId(userId);

        // 2. Convertimos a DTOs simples (solo ID y Nombre)
        return teams.stream()
                .map(teamMapper::toResponseDTO)
                .toList();
    }
}
