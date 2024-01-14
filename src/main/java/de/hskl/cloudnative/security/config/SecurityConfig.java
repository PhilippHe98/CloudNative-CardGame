package de.hskl.cloudnative.security.config;

import org.springframework.boot.actuate.autoconfigure.security.servlet.EndpointRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true)
public class SecurityConfig {

    @Bean
    public SecurityFilterChain customFilterChain(HttpSecurity httpSecurity) throws Exception {
        return httpSecurity
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth // TODO: Add security rules
                        // .requestMatchers("/user/register", "/error", "/swagger-ui/**", "/v3/api-docs/**").permitAll()
                        // .requestMatchers("/create-note").hasRole("WRITER")
                        // .requestMatchers("/delete-note").hasAnyRole("ADMIN","WRITER")
                        // .requestMatchers(EndpointRequest.to("health")).permitAll()  // Allow access to Actuator Health endpoint
                        // .requestMatchers(EndpointRequest.toAnyEndpoint().excluding("health")).hasRole("ADMIN")  // Restrict access to other Actuator endpoints
                        .anyRequest().authenticated())
                .httpBasic(Customizer.withDefaults())
                .formLogin(Customizer.withDefaults())
                .build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
