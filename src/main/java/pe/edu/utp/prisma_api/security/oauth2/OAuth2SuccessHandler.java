package pe.edu.utp.prisma_api.security.oauth2;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import pe.edu.utp.prisma_api.domain.user.User;
import pe.edu.utp.prisma_api.domain.user.UserRepository;
import pe.edu.utp.prisma_api.security.jwt.JwtService;

@Component
public class OAuth2SuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

  @Autowired
  private UserRepository userRepository;
  @Autowired
  private JwtService jwtService;

  @Value("${frontend.url}")
  private String frontendUrl;

  @Override
  public void onAuthenticationSuccess(HttpServletRequest request,
      HttpServletResponse response,
      Authentication authentication) throws IOException {

    OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();

    String provider = extractProvider(request);
    String email = extractEmail(oAuth2User, provider);

    User user = userRepository.findByEmail(email)
        .orElseThrow(() -> new RuntimeException("Usuario no encontrado tras OAuth2"));

    String token = jwtService.generateToken(user);

    getRedirectStrategy().sendRedirect(
        request,
        response,
        frontendUrl + "/auth/callback?token=" + token);
  }

  private String extractProvider(HttpServletRequest request) {
    String uri = request.getRequestURI();
    if (uri.contains("google"))
      return "google";
    if (uri.contains("github"))
      return "github";
    return "";
  }

  private String extractEmail(OAuth2User user, String provider) {
    if (provider.equals("github")) {
      Object email = user.getAttribute("email");
      return email != null ? email.toString() : null;
    }
    return user.getAttribute("email");
  }
}
