package it.aulab.aulabchronicle.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
  public SecurityFilterChain filterchain(HttpSecurity http) throws Exception{
http
.csrf(csrf->csrf.disable())
.authorizeHttpRequests((authorize)->authorize
.requestMatchers("/register/**")
.permitAll()
.anyRequest()
.authenticated()
)
.formLogin(login->login
.loginPage("/login")
.loginProcessingUrl("/login")
.defaultSuccessUrl("/")
.permitAll()
)
.logout(logout->logout
.logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
.permitAll()
)
.exceptionHandling(exception->exception
.accessDeniedPage("/error/403")
)
.sessionManagement(session-> session
.sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)
.maximumSessions(1)
.expiredUrl("/login?session-expired=true")
);
  return http.build();
  }
}
