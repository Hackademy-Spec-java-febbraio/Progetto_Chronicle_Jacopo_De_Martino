package it.aulab.aulabchronicle.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import it.aulab.aulabchronicle.services.CustomUserDetailsService;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

final private CustomUserDetailsService customUserDetailsService;
final private PasswordEncoder passwordEncoder;

public SecurityConfig(CustomUserDetailsService customUserDetailsService,PasswordEncoder passwordEncoder) {
    this.customUserDetailsService = customUserDetailsService;
    this.passwordEncoder = passwordEncoder;
}


    @Bean
  public SecurityFilterChain filterchain(HttpSecurity http) throws Exception{
    http
    .csrf(csrf -> csrf.disable())
    .authorizeHttpRequests(authorize -> authorize
        .requestMatchers("/register/**", "/register").permitAll()
        .anyRequest().authenticated()
    )
    .formLogin(login -> login
        .loginPage("/login")
        .loginProcessingUrl("/login")
        .defaultSuccessUrl("/")
        .permitAll()
    )
    .logout(logout -> logout
        .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
        .permitAll()
    )
    .exceptionHandling(exception -> exception
        .accessDeniedPage("/error/403")
    )
    .sessionManagement(session -> session
        .sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)
        .maximumSessions(1)
        .expiredUrl("/login?session-expired=true")
    );


return http.build();
}

 @Bean
 public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
     return configuration.getAuthenticationManager();
 }


  @Autowired
  public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
    auth.userDetailsService(customUserDetailsService).passwordEncoder(passwordEncoder);
  }

}
