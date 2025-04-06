package org.acelera.blogmaker.controller.v1;

import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.acelera.blogmaker.model.dto.request.CreateThemeRequest;
import org.acelera.blogmaker.model.dto.response.ThemeResponse;
import org.acelera.blogmaker.services.ThemeService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/v1/themes")
public class ThemeController {

    private final ThemeService themeService;

    public ThemeController(ThemeService themeService) {
        this.themeService = themeService;
    }

    @PostMapping
    @Transactional
    public ResponseEntity<Void> createTheme(@Valid @RequestBody CreateThemeRequest request) {
        Long themeId = themeService.createTheme(request);
        return ResponseEntity.created(URI.create("/api/v1/themes/" + themeId)).build();
    }

    @GetMapping("/{themeId}")
    public ResponseEntity<ThemeResponse> getTheme(@PathVariable Long themeId) {
        ThemeResponse theme = themeService.getThemeById(themeId);
        return ResponseEntity.ok(theme);
    }

    @GetMapping
    public ResponseEntity<List<ThemeResponse>> getAllThemes() {
        List<ThemeResponse> themes = themeService.getAllThemes();
        return ResponseEntity.ok(themes);
    }

    @PutMapping("/{themeId}")
    @Transactional
    public ResponseEntity<ThemeResponse> updateTheme(@PathVariable Long themeId,
                                                     @Valid @RequestBody CreateThemeRequest request) {
        ThemeResponse updatedTheme = themeService.updateTheme(themeId, request);
        return ResponseEntity.ok(updatedTheme);
    }

    @DeleteMapping("/{themeId}")
    @Transactional
    public ResponseEntity<Void> deleteTheme(@PathVariable Long themeId) {
        themeService.deleteTheme(themeId);
        return ResponseEntity.noContent().build();
    }
}
