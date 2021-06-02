package com.ss.utopia;


import org.modelmapper.ModelMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;


@SpringBootApplication
public class UtopiauserApplication {

	public static void main(String[] args) {
		SpringApplication.run(UtopiauserApplication.class, args);
	}
	
	@Bean
	public ModelMapper modelMapper() {
	    return new ModelMapper();
	}
	
	@Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                	registry.addMapping("/**")
		               .allowedOrigins("*")
		               .allowedMethods("GET", "POST", "PUT", "DELETE")
			           .allowedHeaders("*")
		               .exposedHeaders("*");
            }
        };
    }

}
