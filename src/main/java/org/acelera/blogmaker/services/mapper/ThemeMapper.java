package org.acelera.blogmaker.services.mapper;

import org.acelera.blogmaker.model.Theme;
import org.acelera.blogmaker.model.dto.request.CreateThemeRequest;
import org.acelera.blogmaker.model.dto.response.ThemeResponse;
import org.springframework.stereotype.Service;

@Service
public class ThemeMapper {

    public Theme toTheme(CreateThemeRequest request) {
        return Theme.builder()
                .id(null)
                .description(request.description())
                .build();
    }

    public ThemeResponse fromTheme(Theme theme) {
        return new ThemeResponse(
                theme.getDescription()
        );
    }
}