package org.acelera.blogmaker.services.mapper;

import org.acelera.blogmaker.model.Post;
import org.acelera.blogmaker.model.Theme;
import org.acelera.blogmaker.model.User;
import org.acelera.blogmaker.model.dto.request.CreatePostRequest;
import org.acelera.blogmaker.model.dto.response.PostResponse;
import org.acelera.blogmaker.model.dto.response.ThemeResponse;
import org.springframework.stereotype.Service;

@Service
public class PostMapper {

    private final UserMapper userMapper;

    public PostMapper(UserMapper userMapper) {
        this.userMapper = userMapper;
    }

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
                post.getTitle(),
                post.getContent(),
                post.getCreatedAt(),
                post.getUpdatedAt(),
                new ThemeResponse(post.getTheme().getDescription()),
                userMapper.fromUser(post.getUser())
        );
    }
}