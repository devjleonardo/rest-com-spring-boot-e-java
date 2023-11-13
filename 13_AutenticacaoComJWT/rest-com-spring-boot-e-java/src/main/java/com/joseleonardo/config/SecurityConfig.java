package com.joseleonardo.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import com.joseleonardo.security.jwt.JwtConfigurer;
import com.joseleonardo.security.jwt.JwtTokenProvider;

@Configuration
public class SecurityConfig {
	
	@Autowired
	private JwtTokenProvider jwtTokenProvider;
	
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
    	httpSecurity
        	.httpBasic(httpBasic -> httpBasic.disable())
        	.csrf(csrf -> csrf.disable())
        	.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
        	.authorizeHttpRequests(authorizeHttpRequests -> authorizeHttpRequests
                    .requestMatchers(
                    		"/autenticacao/signin",
                    		"/autenticacao/refresh/**",
                    		"/v3/api-docs/**",
                    		"/swagger-ui/**").permitAll()
                    .requestMatchers("/api/**").authenticated()
                    .requestMatchers("/users").denyAll()
        	)
        	.apply(new JwtConfigurer(jwtTokenProvider));
    	
    	return httpSecurity.build();
    }
    
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) 
    		throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }
    
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}
