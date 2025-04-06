package org.acelera.blogmaker.model.dto.request;

import org.acelera.blogmaker.model.Post;

public record UpdatePostRequest(
        String title,
        String content,
        Long themeId
) {
    public UpdatePostRequest(Post post) {
        this(post.getTitle(), post.getContent(), post.getTheme() != null ? post.getTheme().getId() : null);
    }
}
