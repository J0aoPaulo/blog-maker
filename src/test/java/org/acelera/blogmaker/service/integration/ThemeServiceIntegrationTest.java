package org.acelera.blogmaker.service.integration;

import org.acelera.blogmaker.exception.ThemeAlreadyExistException;
import org.acelera.blogmaker.exception.ThemeNotFoundException;
import org.acelera.blogmaker.model.Theme;
import org.acelera.blogmaker.model.dto.request.CreateThemeRequest;
import org.acelera.blogmaker.model.dto.response.ThemeResponse;
import org.acelera.blogmaker.repository.ThemeRepository;
import org.acelera.blogmaker.services.ThemeService;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
class ThemeServiceIntegrationTest {
    private final ThemeService themeService;
    private final ThemeRepository themeRepository;

    public ThemeServiceIntegrationTest(ThemeService themeService, ThemeRepository themeRepository) {
        this.themeService = themeService;
        this.themeRepository = themeRepository;
    }

    @Test
    void testCreateTheme_Success() {
        CreateThemeRequest request = new CreateThemeRequest("Technology");
        Long themeId = themeService.createTheme(request);
        assertNotNull(themeId);

        Theme theme = themeRepository.findById(themeId).orElse(null);
        assertNotNull(theme);
        assertEquals("Technology", theme.getDescription());
    }

    @Test
    void testCreateTheme_Failure_Duplicate() {
        CreateThemeRequest request = new CreateThemeRequest("Art");
        Long themeId = themeService.createTheme(request);
        assertNotNull(themeId);

        CreateThemeRequest duplicateRequest = new CreateThemeRequest("Art");
        assertThrows(ThemeAlreadyExistException.class, () -> themeService.createTheme(duplicateRequest));
    }

    @Test
    void testGetThemeById_Success() {
        CreateThemeRequest request = new CreateThemeRequest("Science");
        Long themeId = themeService.createTheme(request);

        ThemeResponse themeResponse = themeService.getThemeById(themeId);
        assertNotNull(themeResponse);
        assertEquals("Science", themeResponse.description());
    }

    @Test
    void testGetAllThemes() {
        CreateThemeRequest request1 = new CreateThemeRequest("Music");
        CreateThemeRequest request2 = new CreateThemeRequest("Travel");
        themeService.createTheme(request1);
        themeService.createTheme(request2);

        List<ThemeResponse> themes = themeService.getAllThemes();
        assertTrue(themes.size() >= 2);
    }

    @Test
    void testUpdateTheme_Success() {
        CreateThemeRequest request = new CreateThemeRequest("Old Description");
        Long themeId = themeService.createTheme(request);

        CreateThemeRequest updateRequest = new CreateThemeRequest("New Description");
        ThemeResponse updatedTheme = themeService.updateTheme(themeId, updateRequest);
        assertNotNull(updatedTheme);
        assertEquals("New Description", updatedTheme.description());
    }

    @Test
    void testUpdateTheme_Failure_ThemeNotFound() {
        CreateThemeRequest updateRequest = new CreateThemeRequest("Any Description");
        assertThrows(ThemeNotFoundException.class, () -> themeService.updateTheme(999L, updateRequest));
    }

    @Test
    void testDeleteTheme_Success() {
        CreateThemeRequest request = new CreateThemeRequest("Delete Theme");
        Long themeId = themeService.createTheme(request);

        themeService.deleteTheme(themeId);
        assertFalse(themeRepository.findById(themeId).isPresent());
    }

    @Test
    void testDeleteTheme_Failure_ThemeNotFound() {
        assertThrows(ThemeNotFoundException.class, () -> themeService.deleteTheme(999L));
    }
}
