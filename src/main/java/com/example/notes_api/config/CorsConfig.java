package com.example.notes_api.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfig {


    @Bean
    public WebMvcConfigurer corsConfigurer() {
     return new WebMvcConfigurer(){
         @Override
         public void addCorsMappings(CorsRegistry r){
             r.addMapping("/**")
                     .allowedOrigins("http://localhost:5173",
                             "https://YOUR-VERCEL-DOMAIN.vercel.app")
                     .allowedMethods("GET","POST","PUT","DELETE")
                     .allowCredentials(true)
                     .allowedHeaders("*");
         }
     };
    }
}
