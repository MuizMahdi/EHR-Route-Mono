package com.project.EMRChain.Configuration;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/*
*   Enables Cross Origin Resource Sharing (CORS) requests globally
*/

@Configuration
@EnableWebMvc
public class WebMvcConfig implements WebMvcConfigurer
{
    private final long maxAgeInSec = 3600;

    @Override
    public void addCorsMappings(CorsRegistry registry)
    {
        registry.addMapping("/**")
        .allowedOrigins("*") // Allow all origins
        .allowedMethods("HEAD", "OPTIONS", "GET", "POST", "PUT", "PATCH", "DELETE")
        .maxAge(maxAgeInSec);
    }
}
