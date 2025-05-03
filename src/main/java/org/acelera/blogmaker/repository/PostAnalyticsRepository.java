package org.acelera.blogmaker.repository;

import org.acelera.blogmaker.model.Post;
import org.acelera.blogmaker.analytics.dto.AuthorPostCountDTO;
import org.acelera.blogmaker.analytics.dto.ThemePostCountDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface PostAnalyticsRepository extends JpaRepository<Post, Long> {

    /* 2.1  Total de posts -------------------------------------------------- */
    @Query("SELECT COUNT(p) FROM Post p")
    Long totalPosts();

    /* 2.2  Posts por autor (User) ------------------------------------------ */
    @Query("""
           SELECT new org.acelera.blogmaker.analytics.dto.AuthorPostCountDTO(
                    u.id, u.name, COUNT(p))
             FROM Post p
             JOIN p.user u
            GROUP BY u.id, u.name
            ORDER BY COUNT(p) DESC
           """)
    List<AuthorPostCountDTO> postsByAuthor();

    /* 2.3  Posts por tema --------------------------------------------------- */
    @Query("""
           SELECT new org.acelera.blogmaker.analytics.dto.ThemePostCountDTO(
                    t.id, t.description, COUNT(p))
             FROM Post p
             JOIN p.theme t
            GROUP BY t.id, t.description
            ORDER BY COUNT(p) DESC
           """)
    List<ThemePostCountDTO> postsByTheme();

    /* 2.4  Distribuição temporal ------------------------------------------- */
    /*
     *  bucket:   "day" | "week" | "month"
     *  format:   "YYYY-MM-DD" | "IYYY-IW" | "YYYY-MM"
     *  Atenção: usa coluna 'created_at' (default naming strategy)      */
    @Query(value = """
            SELECT
              to_char(date_trunc(:bucket, p.created_at), :format) AS bucket,
              COUNT(*)                                           AS total
              FROM posts p
             WHERE p.created_at BETWEEN :start AND :end
          GROUP BY bucket
          ORDER BY bucket
          """,
            nativeQuery = true)
    List<Object[]> timeSeriesNative(@Param("bucket") String bucket,
                                    @Param("format") String format,
                                    @Param("start")  LocalDateTime start,
                                    @Param("end")    LocalDateTime end);

}