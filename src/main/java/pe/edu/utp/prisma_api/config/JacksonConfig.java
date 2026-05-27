package pe.edu.utp.prisma_api.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import com.fasterxml.jackson.annotation.JsonInclude;

import tools.jackson.databind.json.JsonMapper;
import tools.jackson.databind.DeserializationFeature;

@Configuration
public class JacksonConfig {

  @Bean
  @Primary
  public JsonMapper jsonMapper() {
    return JsonMapper.builder()
        .findAndAddModules()
        .changeDefaultPropertyInclusion(include -> include.withValueInclusion(JsonInclude.Include.NON_NULL)
            .withContentInclusion(JsonInclude.Include.USE_DEFAULTS))
        .disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
        .build();
  }
}