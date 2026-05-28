package pe.edu.utp.prisma_api.domain.user;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import pe.edu.utp.prisma_api.domain.user.dto.UserResponseDTO;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserMapper userMapper;

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
