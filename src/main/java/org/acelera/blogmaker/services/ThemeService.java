package org.acelera.blogmaker.services;

import org.acelera.blogmaker.exception.ThemeAlreadyExistException;
import org.acelera.blogmaker.exception.ThemeNotFoundException;
import org.acelera.blogmaker.model.Theme;
import org.acelera.blogmaker.model.dto.request.CreateThemeRequest;
import org.acelera.blogmaker.model.dto.response.ThemeResponse;
import org.acelera.blogmaker.repository.ThemeRepository;
import org.acelera.blogmaker.services.mapper.ThemeMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ThemeService {

    private final ThemeRepository themeRepository;
    private final ThemeMapper mapper;

    public ThemeService(ThemeRepository themeRepository, ThemeMapper mapper) {
        this.themeRepository = themeRepository;
        this.mapper = mapper;
    }

    public Long createTheme(CreateThemeRequest request) {
        String description = request.description().trim();
        if (themeRepository.existsByDescription(description)) {
            throw new ThemeAlreadyExistException("Theme already exists with description: " + description);
        }

        var theme = themeRepository.save(mapper.toTheme(request));
        return theme.getId();
    }

    public ThemeResponse getThemeById(Long id) {
        return themeRepository.findById(id)
                .map(mapper::fromTheme)
                .orElseThrow(() -> new ThemeNotFoundException("Theme not found with id: " + id));
    }

    public List<ThemeResponse> getAllThemes() {
        return themeRepository.findAll()
                .stream()
                .map(mapper::fromTheme)
                .collect(Collectors.toList());
    }

    public ThemeResponse updateTheme(Long id, CreateThemeRequest request) {
        Theme theme = themeRepository.findById(id)
                .orElseThrow(() -> new ThemeNotFoundException("Theme not found with id: " + id));

        mergeTheme(theme, request);
        return mapper.fromTheme(theme);
    }

    private void mergeTheme(Theme theme, CreateThemeRequest request) {
        String newDescription = request.description().trim();

        if (!theme.getDescription().equals(newDescription) && themeRepository.existsByDescription(newDescription))
            throw new ThemeAlreadyExistException("Theme already exists with description: " + newDescription);

        themeRepository.save(theme);
        theme.setDescription(newDescription);
    }

    public void deleteTheme(Long id) {
        Theme theme = themeRepository.findById(id)
                .orElseThrow(() -> new ThemeNotFoundException("Theme not found with id: " + id));

        themeRepository.delete(theme);
    }
}
