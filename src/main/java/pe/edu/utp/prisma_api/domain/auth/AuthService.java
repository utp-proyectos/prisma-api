package pe.edu.utp.prisma_api.domain.auth;

import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import pe.edu.utp.prisma_api.common.enums.AuthProvider;
import pe.edu.utp.prisma_api.common.enums.Role;
import pe.edu.utp.prisma_api.common.exception.EmailAlreadyExistsException;
import pe.edu.utp.prisma_api.common.exception.InvalidTokenException;
import pe.edu.utp.prisma_api.common.exception.ResourceNotFoundException;
import pe.edu.utp.prisma_api.domain.EmailVerification.EmailVerification;
import pe.edu.utp.prisma_api.domain.EmailVerification.EmailVerificationRepository;
import pe.edu.utp.prisma_api.domain.auth.dto.AuthResponse;
import pe.edu.utp.prisma_api.domain.auth.dto.LoginRequest;
import pe.edu.utp.prisma_api.domain.auth.dto.CurrentUserResponse;
import pe.edu.utp.prisma_api.domain.auth.dto.RegisterRequest;
import pe.edu.utp.prisma_api.domain.user.User;
import pe.edu.utp.prisma_api.domain.user.UserRepository;
import pe.edu.utp.prisma_api.infraestructure.mail.MailService;
import pe.edu.utp.prisma_api.security.jwt.JwtService;

@Service
@RequiredArgsConstructor
public class AuthService {
  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;
  private final JwtService jwtService;
  private final MailService mailService;
  private final AuthenticationManager authenticationManager;
  private final EmailVerificationRepository emailVerificationRepository;

  public AuthResponse register(RegisterRequest request) throws MessagingException {
    if (userRepository.existsByEmail(request.getEmail())) {
      throw new EmailAlreadyExistsException(request.getEmail());
    }

    User user = new User();
    user.setName(request.getName());
    user.setLastName(request.getLastName());
    user.setUsername(request.getUsername());
    user.setEmail(request.getEmail());
    user.setPassword(passwordEncoder.encode(request.getPassword()));
    user.setProvider(AuthProvider.LOCAL);
    user.setRole(Role.USER);
    user.setAvatar("https://pbs.twimg.com/profile_images/1593304942210478080/TUYae5z7_400x400.jpg");
    user.setEmailVerified(false);
    userRepository.save(user);

    sendVerificationEmail(user);

    return buildAuthResponse(user);
  }

  public AuthResponse login(LoginRequest request) {
    Authentication auth = authenticationManager
        .authenticate(new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));

    User user = userRepository.findByEmail(auth.getName())
        .orElseThrow(() -> new ResourceNotFoundException("Credenciales inválidas"));

    if (!user.isEmailVerified()) {
      throw new InvalidTokenException("Debes verificar tu email antes de iniciar sesión");
    }

    return buildAuthResponse(user);
  }

  public CurrentUserResponse getCurrentUserByToken(String token) {
    if (!jwtService.isValid(token)) {
      throw new InvalidTokenException("Token inválido");
    }

    String email = jwtService.extractEmail(token);

    User user = userRepository.findByEmail(email)
        .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado"));

    return new CurrentUserResponse(user.getId(), user.getName(), user.getLastName(), user.getUsername(),
        user.getEmail(),
        user.getAvatar(), user.getRole().name());
  }

  public void verifyEmail(String token) {
    EmailVerification verification = emailVerificationRepository.findByToken(token)
        .orElseThrow(() -> new InvalidTokenException("Token de verificación inválido"));

    if (verification.getExpiresAt().isBefore(LocalDateTime.now())) {
      throw new InvalidTokenException("El token de verificación ha expirado");
    }

    User user = verification.getUser();
    user.setEmailVerified(true);
    userRepository.save(user);

    emailVerificationRepository.delete(verification);
  }

  public void resendVerification(String email) throws MessagingException {
    User user = userRepository.findByEmail(email)
        .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado"));

    if (user.isEmailVerified()) {
      throw new InvalidTokenException("El email ya está verificado");
    }

    emailVerificationRepository.findByUserId(user.getId())
        .ifPresent(emailVerificationRepository::delete);

    sendVerificationEmail(user);
  }

  private void sendVerificationEmail(User user) throws MessagingException {
    String token = UUID.randomUUID().toString();

    EmailVerification verification = new EmailVerification();
    verification.setUser(user);
    verification.setToken(token);
    verification.setExpiresAt(LocalDateTime.now().plusHours(24));

    System.out.println(verification);

    emailVerificationRepository.save(verification);

    mailService.sendVerificationEmail(user.getEmail(), token);
  }

  private AuthResponse buildAuthResponse(User user) {
    String token = jwtService.generateToken(user);

    return new AuthResponse(
        token,
        user.getId(),
        user.getName(),
        user.getLastName(),
        user.getUsername(),
        user.getEmail(),
        user.getAvatar(),
        user.getRole().name());
  }
}