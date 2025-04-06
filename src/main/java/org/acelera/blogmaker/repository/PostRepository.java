package org.acelera.blogmaker.repository;

import jakarta.validation.constraints.NotBlank;
import org.acelera.blogmaker.model.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {
    boolean existsByTitle(@NotBlank(message = "{required.title}") String title);

    boolean existsByContent(@NotBlank(message = "{required.content}") String content);

    List<Post> findByUserIdAndThemeId(UUID userId, Long themeId);

    List<Post> findByUserId(UUID userId);

    List<Post> findByThemeId(Long themeId);
}
