package org.acelera.blogmaker.controller.v1;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.acelera.blogmaker.analytics.dto.AuthorPostCountDTO;
import org.acelera.blogmaker.analytics.dto.SummaryDTO;
import org.acelera.blogmaker.analytics.dto.ThemePostCountDTO;
import org.acelera.blogmaker.analytics.dto.TimeBucketDTO;
import org.acelera.blogmaker.services.AnalyticsService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/api/v1/analytics")
@Tag(name = "Analytics")
@RequiredArgsConstructor
@Slf4j
public class AnalyticsController {

    private final AnalyticsService service;

    @Operation(summary = "Visão geral de contagem de entidades")
    @GetMapping("/summary")
    public ResponseEntity<SummaryDTO> summary() { 
        try {
            return ResponseEntity.ok(service.summary()); 
        } catch (Exception e) {
            log.error("Error getting summary", e);
            return ResponseEntity.ok(new SummaryDTO(0L, 0L, 0L));
        }
    }

    @Operation(summary = "Número de posts por autor")
    @GetMapping("/posts-by-author")
    public ResponseEntity<List<AuthorPostCountDTO>> byAuthor() { 
        try {
            return ResponseEntity.ok(service.byAuthor()); 
        } catch (Exception e) {
            log.error("Error getting posts by author", e);
            return ResponseEntity.ok(Collections.emptyList());
        }
    }

    @Operation(summary = "Número de posts por tema")
    @GetMapping("/posts-by-theme")
    public ResponseEntity<List<ThemePostCountDTO>> byTheme() { 
        try {
            return ResponseEntity.ok(service.byTheme()); 
        } catch (Exception e) {
            log.error("Error getting posts by theme", e);
            return ResponseEntity.ok(Collections.emptyList());
        }
    }

    @Operation(summary = "Distribuição temporal de posts")
    @GetMapping("/posts-over-time")
    public ResponseEntity<List<TimeBucketDTO>> postsOverTime(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate start,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate end,
            @RequestParam(defaultValue = "day") String granularity) {

        try {
            // Validate parameters
            if (start == null || end == null) {
                log.warn("Invalid date parameters: start={}, end={}", start, end);
                return ResponseEntity.ok(Collections.emptyList());
            }
            
            if (start.isAfter(end)) {
                log.warn("Start date is after end date: start={}, end={}", start, end);
                LocalDate temp = start;
                start = end;
                end = temp;
            }
            
            // Validate granularity
            if (!isValidGranularity(granularity)) {
                log.warn("Invalid granularity: {}, defaulting to 'day'", granularity);
                granularity = "day";
            }

            LocalDateTime startDateTime = start.atStartOfDay();
            LocalDateTime endDateTime   = end.atTime(LocalTime.MAX);
            
            log.info("Fetching time series data: start={}, end={}, granularity={}", 
                    startDateTime, endDateTime, granularity);

            return ResponseEntity.ok(service.timeSeries(startDateTime, endDateTime, granularity));
        } catch (Exception e) {
            log.error("Error getting posts over time", e);
            return ResponseEntity.ok(Collections.emptyList());
        }
    }
    
    private boolean isValidGranularity(String granularity) {
        return granularity != null && 
               (granularity.equals("day") || 
                granularity.equals("week") || 
                granularity.equals("month"));
    }
}
