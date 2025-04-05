package org.acelera.blogmaker.repository;

import jakarta.validation.constraints.NotBlank;
import org.acelera.blogmaker.model.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {
    boolean existsByTitle(@NotBlank(message = "{required.title}") String title);

    boolean existsByContent(@NotBlank(message = "{required.content}") String content);
}
