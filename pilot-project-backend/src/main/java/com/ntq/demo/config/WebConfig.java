package com.ntq.demo.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.*;

/**
 * This class is used to configure CORS and static resources for Web application
 *
 * @author Quang
 * @since 2026-04-29
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {

	/**
	 * Allow React: localhost:3000 for calling API
	 *
	 * @param registry
	 */
	@Override
	public void addCorsMappings(CorsRegistry registry) {
		registry.addMapping("/api/**")
				.allowedOrigins("http://localhost:5173")
				.allowedMethods("GET", "POST", "PUT", "DELETE")
				.allowedHeaders(
						"Authorization",
						"Content-Type",
						"Accept"
				)
				.allowCredentials(true)
				.maxAge(3600);
	}

	/**
	 * Allow access to upload images (images/brand, images/product)
	 *
	 * @param registry
	 */
	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		registry.addResourceHandler("/images/**").addResourceLocations("file:images/");
	}
}