package org.acelera.blogmaker.config;

import org.acelera.blogmaker.model.Theme;
import org.acelera.blogmaker.repository.ThemeRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;
import java.util.List;

@Configuration
public class DataInitializer {

    @Bean
    public CommandLineRunner initThemes(ThemeRepository themeRepository) {
        return args -> {
            List<String> defaultThemes = Arrays.asList(
                    "React", 
                    "Spring Boot", 
                    "Git", 
                    "Angular", 
                    "Outros"
            );

            for (String themeName : defaultThemes) {
                if (!themeRepository.existsByDescription(themeName)) {
                    Theme theme = new Theme();
                    theme.setDescription(themeName);
                    themeRepository.save(theme);
                }
            }
        };
    }
} 