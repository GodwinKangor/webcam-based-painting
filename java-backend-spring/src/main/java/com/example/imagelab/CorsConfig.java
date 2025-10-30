// package com.example.imagelab;

// import org.springframework.context.annotation.Bean;
// import org.springframework.context.annotation.Configuration;
// import org.springframework.web.servlet.config.annotation.CorsRegistry;
// import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

// @Configuration
// public class CorsConfig {
//   @Bean
//   public WebMvcConfigurer corsConfigurer() {
//     return new WebMvcConfigurer() {
//       @Override
//       public void addCorsMappings(CorsRegistry registry) {
//         registry.addMapping("/api/**")
//             .allowedOriginPatterns("http://localhost:*", "http://127.0.0.1:*")
//             .allowedMethods("GET","POST","PUT","DELETE","PATCH","OPTIONS")
//             .allowedHeaders("*")
//             .exposedHeaders("*")
//             .allowCredentials(true);
//       }
//     };
//   }
// }
package com.example.imagelab;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

@Configuration
public class CorsConfig {
  @Bean
  public CorsFilter corsFilter() {
    CorsConfiguration cfg = new CorsConfiguration();
    cfg.setAllowCredentials(true);
    cfg.addAllowedOriginPattern("http://localhost:*");
    cfg.addAllowedOriginPattern("http://127.0.0.1:*");
    cfg.addAllowedHeader("*");
    cfg.addExposedHeader("*");
    cfg.addAllowedMethod("*"); // includes OPTIONS

    UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
    source.registerCorsConfiguration("/api/**", cfg);
    return new CorsFilter(source);
  }
}