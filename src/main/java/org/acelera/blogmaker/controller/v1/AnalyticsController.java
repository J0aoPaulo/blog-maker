package org.acelera.blogmaker.controller.v1;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.acelera.blogmaker.analytics.dto.AuthorPostCountDTO;
import org.acelera.blogmaker.analytics.dto.SummaryDTO;
import org.acelera.blogmaker.analytics.dto.ThemePostCountDTO;
import org.acelera.blogmaker.analytics.dto.TimeBucketDTO;
import org.acelera.blogmaker.services.AnalyticsService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@RestController
@RequestMapping("/api/v1/analytics")
@Tag(name = "Analytics")
@RequiredArgsConstructor
public class AnalyticsController {

    private final AnalyticsService service;

    @Operation(summary = "Visão geral de contagem de entidades")
    @GetMapping("/summary")
    public SummaryDTO summary() { return service.summary(); }

    @Operation(summary = "Número de posts por autor")
    @GetMapping("/posts-by-author")
    public List<AuthorPostCountDTO> byAuthor() { return service.byAuthor(); }

    @Operation(summary = "Número de posts por tema")
    @GetMapping("/posts-by-theme")
    public List<ThemePostCountDTO> byTheme() { return service.byTheme(); }

    @Operation(summary = "Distribuição temporal de posts")
    @GetMapping("/posts-over-time")
    public List<TimeBucketDTO> postsOverTime(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate start,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)  LocalDate end,
            @RequestParam(defaultValue = "day") String granularity) {

        LocalDateTime startDateTime = start.atStartOfDay();
        LocalDateTime endDateTime   = end.atTime(LocalTime.MAX);

        return service.timeSeries(startDateTime, endDateTime, granularity);

    }
}
