package pe.edu.utp.prisma_api.domain.auth;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import jakarta.mail.MessagingException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import pe.edu.utp.prisma_api.common.response.ApiResponse;
import pe.edu.utp.prisma_api.domain.auth.dto.AuthResponse;
import pe.edu.utp.prisma_api.domain.auth.dto.CurrentUserResponse;
import pe.edu.utp.prisma_api.domain.auth.dto.LoginRequest;
import pe.edu.utp.prisma_api.domain.auth.dto.RegisterRequest;
import org.springframework.web.bind.annotation.GetMapping;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
  private final AuthService authService;

  @PostMapping("/register")
  public ResponseEntity<ApiResponse<AuthResponse>> register(
      @Valid @RequestBody RegisterRequest request) throws MessagingException {
    return ResponseEntity
        .status(HttpStatus.CREATED)
        .body(ApiResponse.ok("Registro exitoso, verifica tu email",
            authService.register(request)));
  }

  @PostMapping("/login")
  public ResponseEntity<ApiResponse<AuthResponse>> login(
      @Valid @RequestBody LoginRequest request) {
    return ResponseEntity.ok(
        ApiResponse.ok(authService.login(request)));
  }

  @GetMapping("/me")
  public ResponseEntity<ApiResponse<CurrentUserResponse>> me(@RequestParam String token) {
    return ResponseEntity.ok(
        ApiResponse.ok(authService.getCurrentUserByToken(token)));
  }

  @GetMapping("/verify-email")
  public ResponseEntity<ApiResponse<Void>> verifyEmail(@RequestParam String token) {
    authService.verifyEmail(token);
    return ResponseEntity.ok(ApiResponse.ok("Email verificado correctamente",
        null));
  }

  @PostMapping("/resend-verification")
  public ResponseEntity<ApiResponse<Void>> resendVerification(@RequestParam String email) throws MessagingException {
    authService.resendVerification(email);
    return ResponseEntity.ok(ApiResponse.ok("Email de verificación reenviado",
        null));
  }
}
