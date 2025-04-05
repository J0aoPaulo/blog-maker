package org.acelera.blogmaker.services;

import org.acelera.blogmaker.exception.ThemeAlreadyExistException;
import org.acelera.blogmaker.exception.ThemeNotFoundException;
import org.acelera.blogmaker.model.Theme;
import org.acelera.blogmaker.repository.ThemeRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ThemeService {

    private static final Logger logger = LoggerFactory.getLogger(ThemeService.class);

    private final ThemeRepository themeRepository;

    public ThemeService(ThemeRepository themeRepository) {
        this.themeRepository = themeRepository;
    }

    public List<Theme> findAllThemes() {
        logger.debug("Recuperando todos os temas");
        return themeRepository.findAll();
    }

    public Theme findThemeById(Long id) {
        logger.debug("Buscando tema com id: {}", id);
        return themeRepository.findById(id)
                .orElseThrow(() -> {
                    logger.error("Tema não encontrado com id: {}", id);
                    return new ThemeNotFoundException("Theme not found with id: " + id);
                });
    }

    @Transactional
    public Theme createTheme(Theme theme) {
        String description = (theme.getDescription() != null) ? theme.getDescription().trim() : "";
        if (description.isEmpty()) {
            throw new IllegalArgumentException("Theme description must not be empty");
        }

        if (themeRepository.existsByDescription(description)) {
            throw new ThemeAlreadyExistException("Theme already exists with description: " + description);
        }

        theme.setDescription(description);
        return themeRepository.save(theme);
    }

    @Transactional
    public Theme updateTheme(Long id, Theme newTheme) {
        Theme existing = findThemeById(id);
        if (newTheme.getDescription() != null && !newTheme.getDescription().trim().isEmpty()) {
            String updatedDescription = newTheme.getDescription().trim();
            if (!existing.getDescription().equals(updatedDescription) &&
                    themeRepository.existsByDescription(updatedDescription)) {
                throw new ThemeAlreadyExistException("Theme already exists with description: " + updatedDescription);
            }
            existing.setDescription(updatedDescription);
        }
        return themeRepository.save(existing);
    }

    @Transactional
    public void deleteTheme(Long id) {
        Theme theme = findThemeById(id);
        themeRepository.delete(theme);
        logger.info("Tema excluído com sucesso, id: {}", id);
    }
}
