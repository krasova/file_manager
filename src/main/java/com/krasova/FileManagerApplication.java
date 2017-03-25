package com.krasova;

import com.krasova.service.FileManagerService;
import com.krasova.config.FileManagerProperties;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.h2.server.web.WebServlet;


@SpringBootApplication
@EnableConfigurationProperties(FileManagerProperties.class)
public class FileManagerApplication {

	public static void main(String[] args) {
		SpringApplication.run(FileManagerApplication.class, args);
	}

    @Bean
    CommandLineRunner init(FileManagerService fileManagerService) {
        return (args) -> {
            fileManagerService.deleteAll();
            fileManagerService.init();
        };
    }

    @Bean
    public ServletRegistrationBean h2servletRegistration() {
        ServletRegistrationBean registration = new ServletRegistrationBean(new WebServlet());
        registration.addUrlMappings("/console/*");
        return registration;
    }
}
