package org.acelera.blogmaker.services;

import lombok.RequiredArgsConstructor;
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
import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class AnalyticsService {

    private final PostAnalyticsRepository analyticsRepository;
    private final UserRepository userRepository;
    private final ThemeRepository themeRepository;

    public SummaryDTO summary() {
        return new SummaryDTO(
                analyticsRepository.totalPosts(),
                userRepository.count(),
                themeRepository.count());
    }

    public List<AuthorPostCountDTO> byAuthor() {
        return analyticsRepository.postsByAuthor();
    }

    public List<ThemePostCountDTO> byTheme() {
        return analyticsRepository.postsByTheme();
    }

    public List<TimeBucketDTO> timeSeries(LocalDateTime start,
                                          LocalDateTime end,
                                          String granularity) {

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
    }
}
