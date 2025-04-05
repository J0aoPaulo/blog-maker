package org.acelera.blogmaker.services;

import io.micrometer.common.util.StringUtils;
import org.acelera.blogmaker.exception.PostAlreadyExistException;
import org.acelera.blogmaker.exception.PostNotFoundException;
import org.acelera.blogmaker.model.Post;
import org.acelera.blogmaker.model.Theme;
import org.acelera.blogmaker.model.User;
import org.acelera.blogmaker.model.dto.request.CreatePostRequest;
import org.acelera.blogmaker.model.dto.response.PostResponse;
import org.acelera.blogmaker.repository.PostRepository;
import org.acelera.blogmaker.repository.ThemeRepository;
import org.acelera.blogmaker.services.mapper.PostMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PostService {

    private final PostRepository repository;
    private final ThemeRepository themeRepository;
    private final PostMapper mapper;
    private static final String POST_NOT_FOUND = "Post not found with id: ";

    public PostService(PostRepository repository, PostMapper mapper, ThemeRepository themeRepository) {
        this.repository = repository;
        this.mapper = mapper;
        this.themeRepository = themeRepository;
    }

    public Long createPost(CreatePostRequest request, User user, Theme theme) {
        boolean postTitleExists = repository.existsByTitle(request.title());
        boolean postContentExists = repository.existsByContent(request.content());

        if (postTitleExists && postContentExists) {
            throw new PostAlreadyExistException("Post already exists with the same title and content");
        }

        Post post = mapper.toPost(request, user, theme);
        post = repository.save(post);
        return post.getId();
    }

    public PostResponse getPostById(Long postId) {
        return repository
                .findById(postId)
                .map(mapper::fromPost)
                .orElseThrow(() -> new PostNotFoundException(POST_NOT_FOUND));
    }

    public List<PostResponse> getAllPosts() {
        return repository.findAll()
                .stream()
                .map(mapper::fromPost)
                .toList();
    }

    public PostResponse updatePost(Long postId, CreatePostRequest request, Long themeId) {
        var post = repository
                .findById(postId)
                .orElseThrow(() -> new PostNotFoundException(POST_NOT_FOUND));

        var theme = themeId == null ? null : themeRepository.findById(themeId)
                .orElseThrow(() -> new ThemeNotFoundException("Theme not found with id: " + themeId));
        setPost(post, request, theme);
        return mapper.fromPost(post);
    }

    private void setPost(Post post, CreatePostRequest request, Theme theme) {
        mergePost(post, request,theme);
        repository.save(post);
    }

    private void mergePost(Post post, CreatePostRequest request, Theme theme) {
        if (StringUtils.isNotBlank(request.title())) post.setTitle(request.title());

        if(StringUtils.isNotBlank(request.content())) post.setContent(request.content());

        post.setTheme(theme);
    }

    public void deletePost(Long postId) {
        var post = repository
                .findById(postId)
                .orElseThrow(() -> new PostNotFoundException(POST_NOT_FOUND));

        repository.delete(post);
    }
}
