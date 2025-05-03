package org.acelera.blogmaker.services.mapper;

import org.acelera.blogmaker.model.Post;
import org.acelera.blogmaker.model.Theme;
import org.acelera.blogmaker.model.User;
import org.acelera.blogmaker.model.dto.request.CreatePostRequest;
import org.acelera.blogmaker.model.dto.response.PostResponse;
import org.springframework.stereotype.Service;

@Service
public class PostMapper {

    public Post toPost(CreatePostRequest request, User user, Theme theme) {
        return Post.builder()
                .id(null)
                .title(request.title())
                .content(request.content())
                .theme(theme)
                .user(user)
                .build();
    }

    public PostResponse fromPost(Post post) {
        return new PostResponse(
                post.getId(),
                post.getTitle(),
                post.getContent(),
                post.getCreatedAt(),
                post.getUpdatedAt(),
                post.getTheme() != null ? post.getTheme().getDescription() : null,
                post.getTheme() != null ? post.getTheme().getId() : null,
                post.getUser().getName(),
                post.getUser().getId(),
                post.getUser().getRole().name(),
                post.getUser().getPhoto() != null ? post.getUser().getPhoto() : null
        );
    }
}