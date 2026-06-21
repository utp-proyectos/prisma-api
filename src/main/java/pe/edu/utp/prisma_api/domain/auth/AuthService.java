package pe.edu.utp.prisma_api.domain.auth;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import pe.edu.utp.prisma_api.common.enums.AuthProvider;
import pe.edu.utp.prisma_api.common.enums.Role;
import pe.edu.utp.prisma_api.common.exception.EmailAlreadyExistsException;
import pe.edu.utp.prisma_api.common.exception.InvalidTokenException;
import pe.edu.utp.prisma_api.common.exception.ResourceNotFoundException;
import pe.edu.utp.prisma_api.domain.auth.dto.AuthResponse;
import pe.edu.utp.prisma_api.domain.auth.dto.LoginRequest;
import pe.edu.utp.prisma_api.domain.auth.dto.MeResponse;
import pe.edu.utp.prisma_api.domain.auth.dto.RegisterRequest;
import pe.edu.utp.prisma_api.domain.user.User;
import pe.edu.utp.prisma_api.domain.user.UserRepository;
import pe.edu.utp.prisma_api.infraestructure.mail.MailService;
import pe.edu.utp.prisma_api.security.jwt.JwtService;

@Service
public class AuthService {

  private final UserRepository userRepository;
  // private EmailVerificationRepository emailVerificationRepository;
  private final PasswordEncoder passwordEncoder;
  private final JwtService jwtService;
  private final MailService mailService;
  private final AuthenticationManager authenticationManager;

  AuthService(AuthenticationManager authenticationManager, PasswordEncoder passwordEncoder, JwtService jwtService,
      UserRepository userRepository, MailService mailService) {
    this.authenticationManager = authenticationManager;
    this.passwordEncoder = passwordEncoder;
    this.jwtService = jwtService;
    this.userRepository = userRepository;
    this.mailService = mailService;
  }

  public AuthResponse register(RegisterRequest request) {
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
    user.setEmailVerified(true);
    userRepository.save(user);

    // crea y guarda el token de verificación
    // String verificationToken = UUID.randomUUID().toString();
    // EmailVerification verification = new EmailVerification();
    // verification.setUser(user);
    // verification.setToken(verificationToken);
    // verification.setExpiresAt(LocalDateTime.now().plusHours(24));
    // emailVerificationRepository.save(verification);

    // envía el email de verificación
    mailService.sendVerificationEmail(user.getEmail(), "testing token");

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

  public MeResponse getCurrentUserByEmail(String email) {
    User user = userRepository.findByEmail(email)
        .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado"));

    return new MeResponse(user.getId(), user.getName(), user.getLastName(), user.getUsername(), user.getEmail(),
        user.getAvatar(), user.getRole().name());
  }

  // public void verifyEmail(String token) {
  // EmailVerification verification =
  // emailVerificationRepository.findByToken(token)
  // .orElseThrow(() -> new InvalidTokenException("Token de verificación
  // inválido"));

  // if (verification.getExpiresAt().isBefore(LocalDateTime.now())) {
  // throw new InvalidTokenException("El token de verificación ha expirado");
  // }

  // User user = verification.getUser();
  // user.setEmailVerified(true);
  // userRepository.save(user);

  // emailVerificationRepository.delete(verification);
  // }

  // public void resendVerification(String email) {
  // User user = userRepository.findByEmail(email)
  // .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado"));

  // if (user.isEmailVerified()) {
  // throw new InvalidTokenException("El email ya está verificado");
  // }

  // // elimina el token anterior si existe
  // emailVerificationRepository.findByUserId(user.getId())
  // .ifPresent(emailVerificationRepository::delete);

  // String token = UUID.randomUUID().toString();
  // EmailVerification verification = new EmailVerification();
  // verification.setUser(user);
  // verification.setToken(token);
  // verification.setExpiresAt(LocalDateTime.now().plusHours(24));
  // emailVerificationRepository.save(verification);

  // mailService.sendVerificationEmail(email, token);
  // }

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