package pe.edu.utp.prisma_api.config;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import pe.edu.utp.prisma_api.common.response.ApiResponse;
import pe.edu.utp.prisma_api.security.CustomUserDetailsService;
import pe.edu.utp.prisma_api.security.jwt.JwtAuthenticationFilter;
import pe.edu.utp.prisma_api.security.oauth2.CustomOAuth2UserService;
import pe.edu.utp.prisma_api.security.oauth2.OAuth2SuccessHandler;
import tools.jackson.databind.ObjectMapper;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

  @Autowired
  private JwtAuthenticationFilter jwtAuthFilter;
  @Autowired
  private CustomUserDetailsService userDetailsService;
  @Autowired
  private CustomOAuth2UserService oAuth2UserService;
  @Autowired
  private OAuth2SuccessHandler oAuth2SuccessHandler;

  @Bean
  public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    http
        .csrf(csrf -> csrf.disable())
        .sessionManagement(session -> session
            .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
        .authorizeHttpRequests(auth -> auth
            .requestMatchers("/api/auth/me").authenticated()
            .requestMatchers("/api/auth/**").permitAll()
            .requestMatchers("/oauth2/**").permitAll()
            .requestMatchers("/login/oauth2/**").permitAll()
            .requestMatchers("/ws/**").permitAll()
            .anyRequest().authenticated())

        .oauth2Login(oauth -> oauth
            .userInfoEndpoint(userInfo -> userInfo
                .userService(oAuth2UserService))
            .successHandler(oAuth2SuccessHandler))

        .exceptionHandling(ex -> ex
            .authenticationEntryPoint(this::authenticationEntryPoint)
            .accessDeniedHandler(this::accessDeniedHandler))

        .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

    return http.build();
  }

  @Bean
  public AuthenticationProvider authenticationProvider() {
    DaoAuthenticationProvider provider = new DaoAuthenticationProvider(userDetailsService);
    provider.setPasswordEncoder(passwordEncoder());
    return provider;
  }

  @Bean
  public AuthenticationManager authenticationManager(AuthenticationConfiguration config)
      throws Exception {
    return config.getAuthenticationManager();
  }

  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

  private void authenticationEntryPoint(
      HttpServletRequest request,
      HttpServletResponse response,
      AuthenticationException authException) throws IOException {
    response.setStatus(HttpStatus.UNAUTHORIZED.value());
    response.setContentType("application/json");

    ApiResponse<Void> apiResponse = ApiResponse.error("Acceso no autorizado");

    ObjectMapper mapper = new ObjectMapper();

    response.getWriter().write(mapper.writeValueAsString(apiResponse));
  }

  private void accessDeniedHandler(HttpServletRequest request,
      HttpServletResponse response,
      AccessDeniedException accessDeniedException) throws IOException {
    response.setStatus(HttpStatus.FORBIDDEN.value());
    response.setContentType("application/json");

    ApiResponse<Void> apiResponse = ApiResponse.error("No tienes permisos para acceder a este recurso");

    ObjectMapper mapper = new ObjectMapper();

    response.getWriter().write(mapper.writeValueAsString(apiResponse));

  }
}
