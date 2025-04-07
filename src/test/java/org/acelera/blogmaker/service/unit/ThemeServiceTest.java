package org.acelera.blogmaker.service.unit;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.acelera.blogmaker.exception.ThemeAlreadyExistException;
import org.acelera.blogmaker.exception.ThemeNotFoundException;
import org.acelera.blogmaker.model.Theme;
import org.acelera.blogmaker.model.dto.request.CreateThemeRequest;
import org.acelera.blogmaker.model.dto.response.ThemeResponse;
import org.acelera.blogmaker.repository.ThemeRepository;
import org.acelera.blogmaker.services.ThemeService;
import org.acelera.blogmaker.services.mapper.ThemeMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Collections;
import java.util.Optional;
import java.util.List;

class ThemeServiceTest {

    @Mock
    private ThemeRepository themeRepository;

    @Mock
    private ThemeMapper mapper;

    @InjectMocks
    private ThemeService themeService;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void createTheme_ShouldThrowThemeAlreadyExistException_WhenThemeExists() {
        CreateThemeRequest request = new CreateThemeRequest("Technology");
        when(themeRepository.existsByDescription("Technology")).thenReturn(true);

        ThemeAlreadyExistException exception = assertThrows(ThemeAlreadyExistException.class, () ->
                themeService.createTheme(request)
        );
        assertTrue(exception.getMessage().contains("Theme already exists"));
    }

    @Test
    void createTheme_ShouldReturnThemeId_WhenSuccessful() {
        CreateThemeRequest request = new CreateThemeRequest("Art");
        Theme theme = Theme.builder().id(10L).description("Art").build();
        when(themeRepository.existsByDescription("Art")).thenReturn(false);
        when(mapper.toTheme(request)).thenReturn(theme);
        when(themeRepository.save(theme)).thenReturn(theme);

        Long themeId = themeService.createTheme(request);
        assertEquals(10L, themeId);
    }

    @Test
    void getThemeById_ShouldThrowThemeNotFoundException_WhenNotFound() {
        when(themeRepository.findById(1L)).thenReturn(Optional.empty());
        ThemeNotFoundException exception = assertThrows(ThemeNotFoundException.class, () ->
                themeService.getThemeById(1L)
        );
        assertTrue(exception.getMessage().contains("Theme not found"));
    }

    @Test
    void getAllThemes_ShouldReturnListOfThemes() {
        Theme theme = Theme.builder().id(1L).description("Science").build();
        ThemeResponse response = new ThemeResponse("Science");
        when(themeRepository.findAll()).thenReturn(Collections.singletonList(theme));
        when(mapper.fromTheme(theme)).thenReturn(response);

        List<ThemeResponse> themes = themeService.getAllThemes();
        assertEquals(1, themes.size());
        assertEquals("Science", themes.getFirst().description());
    }

    @Test
    void updateTheme_ShouldThrowThemeNotFoundException_WhenThemeDoesNotExist() {
        CreateThemeRequest request = new CreateThemeRequest("New Description");
        when(themeRepository.findById(1L)).thenReturn(Optional.empty());
        ThemeNotFoundException exception = assertThrows(ThemeNotFoundException.class, () ->
                themeService.updateTheme(1L, request)
        );
        assertTrue(exception.getMessage().contains("Theme not found"));
    }

    @Test
    void updateTheme_ShouldThrowThemeAlreadyExistException_WhenNewDescriptionExists() {
        CreateThemeRequest request = new CreateThemeRequest("Existing");
        Theme theme = Theme.builder().id(1L).description("Old Description").build();
        when(themeRepository.findById(1L)).thenReturn(Optional.of(theme));
        when(themeRepository.existsByDescription("Existing")).thenReturn(true);

        ThemeAlreadyExistException exception = assertThrows(ThemeAlreadyExistException.class, () ->
                themeService.updateTheme(1L, request)
        );
        assertTrue(exception.getMessage().contains("Theme already exists"));
    }

    @Test
    void deleteTheme_ShouldThrowThemeNotFoundException_WhenThemeDoesNotExist() {
        when(themeRepository.findById(1L)).thenReturn(Optional.empty());
        ThemeNotFoundException exception = assertThrows(ThemeNotFoundException.class, () ->
                themeService.deleteTheme(1L)
        );
        assertTrue(exception.getMessage().contains("Theme not found"));
    }
}
