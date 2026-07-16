package pe.edu.utp.prisma_api.config;

import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.gmail.Gmail;
import com.google.auth.http.HttpCredentialsAdapter;
import com.google.auth.oauth2.UserCredentials;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;
import java.security.GeneralSecurityException;

@Configuration
public class GmailConfig {
  @Value("${google.api.client-id}")
  private String clientId;

  @Value("${google.api.client-secret}")
  private String clientSecret;

  @Value("${google.api.refresh-token}")
  private String refreshToken;

  private static final String APPLICATION_NAME = "Prisma";

  @Bean
  public Gmail gmailService() throws GeneralSecurityException, IOException {
    final NetHttpTransport httpTransport = new NetHttpTransport();

    UserCredentials credentials = UserCredentials.newBuilder()
        .setClientId(clientId)
        .setClientSecret(clientSecret)
        .setRefreshToken(refreshToken)
        .build();

    HttpRequestInitializer requestInitializer = new HttpCredentialsAdapter(credentials);

    return new Gmail.Builder(httpTransport, GsonFactory.getDefaultInstance(), requestInitializer)
        .setApplicationName(APPLICATION_NAME)
        .build();
  }
}
