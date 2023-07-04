package com.example.springjwt.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import lombok.RequiredArgsConstructor;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class WebSecurityConfig {
	private final JwtTokenFilter jwtTokenFilter;

	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
		httpSecurity
			.csrf(csrf -> csrf.disable())
			.authorizeHttpRequests(auth -> auth.requestMatchers("/api/v1/admin/**").hasRole("ADMIN"))
			.authorizeHttpRequests(auth -> auth.requestMatchers("/api/v2/admin/**").hasRole("ADMIN"))
			.authorizeHttpRequests(auth -> auth.requestMatchers("/api/v1/auth/**").permitAll())
			.authorizeHttpRequests(auth -> auth.requestMatchers("/api/v2/auth/**").permitAll())
			.authorizeHttpRequests(auth -> auth.requestMatchers("/api/v1/user/check/**").permitAll())
			.authorizeHttpRequests(auth -> auth.requestMatchers("/api/v2/user/check/**").permitAll())
			.authorizeHttpRequests(auth -> auth.requestMatchers("/api/v1/user/**").authenticated())
			.authorizeHttpRequests(auth -> auth.requestMatchers("/api/v2/user/**").authenticated())
			.authorizeHttpRequests(auth -> auth.requestMatchers(HttpMethod.GET, "/api/v1/attachment/**").permitAll())
			.authorizeHttpRequests(auth -> auth.requestMatchers(HttpMethod.GET, "/api/v2/attachment/**").permitAll())
			.authorizeHttpRequests(auth -> auth.requestMatchers(HttpMethod.GET, "/api/v1/comment/**").permitAll())
			.authorizeHttpRequests(auth -> auth.requestMatchers(HttpMethod.GET, "/api/v2/comment/**").permitAll())
			.authorizeHttpRequests(auth -> auth.requestMatchers(HttpMethod.GET, "/api/v1/post/**").permitAll())
			.authorizeHttpRequests(auth -> auth.requestMatchers(HttpMethod.GET, "/api/v2/post/**").permitAll())
			.authorizeHttpRequests(auth -> auth.requestMatchers("/api/v1/**").authenticated())
			.authorizeHttpRequests(auth -> auth.requestMatchers("/api/v2/**").authenticated())
			.sessionManagement(sess -> sess.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
			.addFilterBefore(this.jwtTokenFilter, UsernamePasswordAuthenticationFilter.class);

		return httpSecurity.build();
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
}