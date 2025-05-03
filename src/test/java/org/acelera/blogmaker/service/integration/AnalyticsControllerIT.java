package org.acelera.blogmaker.service.integration;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class AnalyticsControllerIT {

    @Autowired
    private MockMvc mvc;

    @Test
    void shouldReturnSummary() throws Exception {
        mvc.perform(get("/api/v1/analytics/summary").with(jwt()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalPosts").exists())
                .andExpect(jsonPath("$.totalAuthors").exists())
                .andExpect(jsonPath("$.totalThemes").exists());
    }

    @Test
    void shouldReturnPostsByAuthor() throws Exception {
        mvc.perform(get("/api/v1/analytics/posts-by-author").with(jwt()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());
    }

    @Test
    void shouldReturnPostsByTheme() throws Exception {
        mvc.perform(get("/api/v1/analytics/posts-by-theme").with(jwt()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());
    }

    @Test
    void shouldReturnTimeDistributionByDay() throws Exception {
        String start = "2025-01-01";
        String end   = "2025-12-31";

        mvc.perform(get("/api/v1/analytics/posts-over-time")
                        .with(jwt())
                        .param("start", start)
                        .param("end", end)
                        .param("granularity", "day"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());
    }

    @Test
    void shouldAcceptMonthGranularity() throws Exception {
        String start = "2025-01-01";
        String end   = "2025-12-31";

        mvc.perform(get("/api/v1/analytics/posts-over-time")
                        .with(jwt())
                        .param("start", start)
                        .param("end", end)
                        .param("granularity", "month"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());
    }
}