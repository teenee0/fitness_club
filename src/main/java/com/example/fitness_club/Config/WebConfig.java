package com.example.fitness_club.Config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // Удаляем обработку статики для папки img/Trainers
        registry.addResourceHandler("/img/Trainers/**")
                .addResourceLocations("file:src/main/resources/static/img/Trainers/")
                .resourceChain(false); // Отключаем обработку кэша
    }
}


