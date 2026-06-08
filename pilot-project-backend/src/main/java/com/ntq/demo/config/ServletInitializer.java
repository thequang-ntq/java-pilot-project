package com.ntq.demo.config;

import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

/**
 * Specify main class, help deploy project app to outside server (Tomcat, GlassFish,...)
 *
 * @author Quang
 * @since 2026-04-29
 */
public class ServletInitializer extends SpringBootServletInitializer {
	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
		return application.sources(PilotProjectBackendApplication.class);
	}
}
