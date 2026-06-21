package pe.edu.utp.prisma_api.domain.user;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import pe.edu.utp.prisma_api.domain.user.dto.UserResponseDTO;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    private final UserMapper userMapper;

    public UserServiceImpl(UserRepository userRepository, UserMapper userMapper) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
    }

    @Override
    public List<UserResponseDTO> getAll() {
        return userRepository.findAll()
                .stream()
                .map(userMapper::toResponseDTO)
                .toList();
    }

    @Override
    public Optional<UserResponseDTO> get(String userId) {
        return userRepository.findById(userId)
                .map(userMapper::toResponseDTO);
    }

}
