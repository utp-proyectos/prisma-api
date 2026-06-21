package pe.edu.utp.prisma_api.security.oauth2;

import org.springframework.security.oauth2.core.user.OAuth2User;

import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.stereotype.Service;

import pe.edu.utp.prisma_api.common.enums.AuthProvider;
import pe.edu.utp.prisma_api.common.enums.Role;
import pe.edu.utp.prisma_api.domain.user.User;
import pe.edu.utp.prisma_api.domain.user.UserRepository;

@Service
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

  private UserRepository userRepository;

  public CustomOAuth2UserService(UserRepository userRepository) {
    this.userRepository = userRepository;
  }

  @Override
  public OAuth2User loadUser(OAuth2UserRequest request) throws OAuth2AuthenticationException {
    OAuth2User oAuth2User = super.loadUser(request);

    String provider = request.getClientRegistration().getRegistrationId();
    String email = extractEmail(oAuth2User, provider);
    String name = oAuth2User.getAttribute("name");
    String avatar = provider.equals("google")
        ? oAuth2User.getAttribute("picture")
        : oAuth2User.getAttribute("avatar_url");

    if (email == null) {
      throw new OAuth2AuthenticationException("No se pudo obtener el email del proveedor");
    }

    AuthProvider authProvider = AuthProvider.valueOf(provider.toUpperCase());

    userRepository.findByEmail(email).ifPresentOrElse(
        existingUser -> {
          if (!existingUser.getProvider().equals(authProvider)) {
            throw new OAuth2AuthenticationException(
                "Este email ya está registrado con " + existingUser.getProvider().name().toLowerCase());
          }
          existingUser.setName(name);
          existingUser.setAvatar(avatar);
          userRepository.save(existingUser);
        },
        () -> {
          User newUser = new User();
          newUser.setEmail(email);
          newUser.setName(name);
          newUser.setAvatar(avatar);
          newUser.setProvider(authProvider);
          newUser.setRole(Role.USER);
          newUser.setEmailVerified(true);
          userRepository.save(newUser);
        });

    return oAuth2User;
  }

  private String extractEmail(OAuth2User user, String provider) {
    if (provider.equals("github")) {
      Object email = user.getAttribute("email");
      return email != null ? email.toString() : null;
    }
    return user.getAttribute("email");
  }
}