package com.ntq.demo.security;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * This class is used to configure Spring Security: public/private endpoints, JWT filter, stateless session
 * and AuthenticationManager
 *
 * @author Quang
 * @since 2026-04-29
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {
	@Autowired
	private JwtAuthenticationFilter jwtAuthenticationFilter;

	/**
	 * AuthenticationManager bean
	 * Required to suppress auto-generated password warning
	 */
	@Bean
	public AuthenticationManager authenticationManager(
			AuthenticationConfiguration config) throws Exception {
		return config.getAuthenticationManager();
	}

	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		http
				.cors(cors -> {})
				/**
				 * Disable CSRF because already has JWT (stateless, no need CSRF token)
				 */
				.csrf(AbstractHttpConfigurer::disable)
				/**
				 * STATELESS
				 */
				.sessionManagement(session ->
						session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
				/**
				 * Handle 401 and 403 separately
				 * 401 Unauthorized — no token / invalid token (not authenticated)
				 * 403 Forbidden — has token but wrong role (authenticated but not authorized)
				 */
				.exceptionHandling(ex -> ex
						/**
						 * AuthenticationEntryPoint — triggered when no token or invalid token
						 * Return 401
						 */
						.authenticationEntryPoint(
								(request,
								 response,
								 authException) -> {
									response.setContentType("application/json");
									response.setCharacterEncoding("UTF-8");
									response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
									response.getWriter().write(
											"{\"responseCode\":401,\"responseMsg\":\"Unauthorized - Please login first\",\"data\":null}"
									);
								})
						/**
						 * AccessDeniedHandler — triggered when has token but wrong role
						 * Return 403
						 */
						.accessDeniedHandler((request, response, accessDeniedException) -> {
							response.setContentType("application/json");
							response.setCharacterEncoding("UTF-8");
							response.setStatus(HttpServletResponse.SC_FORBIDDEN);
							response.getWriter().write(
									"{\"responseCode\":403,\"responseMsg\":\"Forbidden - You do not have permission\",\"data\":null}"
							);
						})
				)
				/**
				 * Endpoint permission
				 */
				.authorizeHttpRequests(auth -> auth
						/**
						 * Public auth APIs
						 */
						.requestMatchers("/api/auth/login").permitAll()
						.requestMatchers("/api/auth/refresh").permitAll()
						/**
						 * Public images
						 */
						.requestMatchers("/images/**").permitAll()
						/**
						 * Guest can view/search products & brands
						 */
						.requestMatchers(HttpMethod.GET, "/api/products/**").permitAll()
						.requestMatchers(HttpMethod.GET, "/api/brands/**").permitAll()
						/**
						 * ADMIN can CRUD products & brands
						 */
						.requestMatchers(HttpMethod.POST, "/api/products/**", "/api/brands/**").hasRole("ADMIN")
						.requestMatchers(HttpMethod.PUT, "/api/products/**", "/api/brands/**").hasRole("ADMIN")
						.requestMatchers(HttpMethod.DELETE, "/api/products/**", "/api/brands/**").hasRole("ADMIN")
						/**
						 * Remaining requests require login
						 */
						.anyRequest().authenticated()
				)
				// Add JWT filter run before UsernamePasswordAuthenticationFilter (Filter Login)
				.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

		return http.build();
	}
}