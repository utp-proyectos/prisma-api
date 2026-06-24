package pe.edu.utp.prisma_api.config;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;

import pe.edu.utp.prisma_api.common.handler.OAuth2FailureHandler;
import pe.edu.utp.prisma_api.security.CustomUserDetailsService;
import pe.edu.utp.prisma_api.security.jwt.JwtAuthenticationFilter;
import pe.edu.utp.prisma_api.security.oauth2.CustomOAuth2UserService;
import pe.edu.utp.prisma_api.security.oauth2.OAuth2SuccessHandler;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

  private final JwtAuthenticationFilter jwtAuthFilter;
  private final CustomUserDetailsService userDetailsService;
  private final CustomOAuth2UserService oAuth2UserService;
  private final OAuth2SuccessHandler oAuth2SuccessHandler;
  private final OAuth2FailureHandler oAuth2FailureHandler;
  private final HandlerExceptionResolver resolver;

  public SecurityConfig(
      JwtAuthenticationFilter jwtAuthFilter,
      CustomUserDetailsService userDetailsService,
      CustomOAuth2UserService oAuth2UserService,
      OAuth2SuccessHandler oAuth2SuccessHandler,
      OAuth2FailureHandler oAuth2FailureHandler,
      @Qualifier("handlerExceptionResolver") HandlerExceptionResolver resolver) {
    this.jwtAuthFilter = jwtAuthFilter;
    this.userDetailsService = userDetailsService;
    this.oAuth2UserService = oAuth2UserService;
    this.oAuth2SuccessHandler = oAuth2SuccessHandler;
    this.oAuth2FailureHandler = oAuth2FailureHandler;
    this.resolver = resolver;
  }

  @Bean
  public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    http
        .csrf(csrf -> csrf.disable())
        .sessionManagement(session -> session
            .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
        .authorizeHttpRequests(auth -> auth
            .requestMatchers("/api/auth/**").permitAll()
            .requestMatchers("/oauth2/**").permitAll()
            .requestMatchers("/login/oauth2/**").permitAll()
            .requestMatchers("/ws/**").permitAll()
            .anyRequest().authenticated())

        .oauth2Login(oauth -> oauth
            .userInfoEndpoint(userInfo -> userInfo
                .userService(oAuth2UserService))
            .successHandler(oAuth2SuccessHandler)
            .failureHandler(oAuth2FailureHandler))

        .exceptionHandling(ex -> ex
            .authenticationEntryPoint(
                (request, response, authException) -> resolver.resolveException(request, response, null, authException))
            .accessDeniedHandler((request, response, accessDeniedException) -> resolver.resolveException(request,
                response, null, accessDeniedException)))

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
}
