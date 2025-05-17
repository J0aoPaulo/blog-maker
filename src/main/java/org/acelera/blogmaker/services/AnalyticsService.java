package org.acelera.blogmaker.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.acelera.blogmaker.analytics.dto.AuthorPostCountDTO;
import org.acelera.blogmaker.analytics.dto.SummaryDTO;
import org.acelera.blogmaker.analytics.dto.ThemePostCountDTO;
import org.acelera.blogmaker.analytics.dto.TimeBucketDTO;
import org.acelera.blogmaker.repository.PostAnalyticsRepository;
import org.acelera.blogmaker.repository.ThemeRepository;
import org.acelera.blogmaker.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

@Service
@Slf4j
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class AnalyticsService {

    private final PostAnalyticsRepository analyticsRepository;
    private final UserRepository userRepository;
    private final ThemeRepository themeRepository;

    public SummaryDTO summary() {
        try {
            return new SummaryDTO(
                    analyticsRepository.totalPosts(),
                    userRepository.count(),
                    themeRepository.count());
        } catch (Exception e) {
            log.error("Error fetching analytics summary", e);
            return new SummaryDTO(0L, 0L, 0L);
        }
    }

    public List<AuthorPostCountDTO> byAuthor() {
        try {
            return analyticsRepository.postsByAuthor();
        } catch (Exception e) {
            log.error("Error fetching posts by author", e);
            return Collections.emptyList();
        }
    }

    public List<ThemePostCountDTO> byTheme() {
        try {
            return analyticsRepository.postsByTheme();
        } catch (Exception e) {
            log.error("Error fetching posts by theme", e);
            return Collections.emptyList();
        }
    }

    public List<TimeBucketDTO> timeSeries(LocalDateTime start,
                                          LocalDateTime end,
                                          String granularity) {
        try {
            var bucket = switch (granularity) {
                case "month" -> "month";
                case "week"  -> "week";
                default      -> "day";
            };
            var format = switch (granularity) {
                case "month" -> "YYYY-MM";
                case "week"  -> "IYYY-IW";
                default      -> "YYYY-MM-DD";
            };

            return analyticsRepository.timeSeriesNative(bucket, format, start, end)
                    .stream()
                    .map(r -> new TimeBucketDTO((String) r[0],
                            ((Number) r[1]).longValue()))
                    .toList();
        } catch (Exception e) {
            log.error("Error fetching time series data: start={}, end={}, granularity={}", 
                     start, end, granularity, e);
            return Collections.emptyList();
        }
    }
}
