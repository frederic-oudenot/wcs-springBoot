package org.wildcodeschool.myblog;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.core.io.FileSystemResource;

@SpringBootApplication
public class MyBlogApplication {

	public static void main(String[] args) {
		SpringApplication.run(MyBlogApplication.class, args);
	}

	/**
	 * Gestion des variables .env
	 * */
	@Configuration
	public class AppConfig {

		@Bean
		public static PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer() {
			PropertySourcesPlaceholderConfigurer configurer = new PropertySourcesPlaceholderConfigurer();
			configurer.setLocation(new FileSystemResource(".env"));
			return configurer;
		}

	}

}