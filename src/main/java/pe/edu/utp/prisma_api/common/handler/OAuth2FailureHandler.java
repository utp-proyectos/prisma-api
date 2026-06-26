package pe.edu.utp.prisma_api.common.handler;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class OAuth2FailureHandler extends SimpleUrlAuthenticationFailureHandler {

  @Value("${frontend.url}")
  private String frontendUrl;

  @Override
  public void onAuthenticationFailure(HttpServletRequest request,
      HttpServletResponse response,
      AuthenticationException exception) throws IOException, ServletException {

    String message = exception.getMessage() != null
        ? exception.getMessage()
        : "Error al iniciar sesión";

    getRedirectStrategy().sendRedirect(
        request,
        response,
        frontendUrl + "/auth/callback?error=" + URLEncoder.encode(message, StandardCharsets.UTF_8));
  }
}