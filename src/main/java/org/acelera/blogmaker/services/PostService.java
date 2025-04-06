package org.acelera.blogmaker.services;

import io.micrometer.common.util.StringUtils;
import org.acelera.blogmaker.exception.PostAlreadyExistException;
import org.acelera.blogmaker.exception.PostNotFoundException;
import org.acelera.blogmaker.exception.ThemeNotFoundException;
import org.acelera.blogmaker.exception.UserNotFoundException;
import org.acelera.blogmaker.model.Post;
import org.acelera.blogmaker.model.Theme;
import org.acelera.blogmaker.model.dto.request.CreatePostRequest;
import org.acelera.blogmaker.model.dto.request.UpdatePostRequest;
import org.acelera.blogmaker.model.dto.response.PostResponse;
import org.acelera.blogmaker.repository.PostRepository;
import org.acelera.blogmaker.repository.ThemeRepository;
import org.acelera.blogmaker.repository.UserRepository;
import org.acelera.blogmaker.services.mapper.PostMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class PostService {

    private final PostRepository repository;
    private final UserRepository userRepository;
    private final ThemeRepository themeRepository;
    private final PostMapper mapper;
    private static final String POST_NOT_FOUND = "Post not found with id: ";

    public PostService(PostRepository repository, PostMapper mapper, ThemeRepository themeRepository, UserRepository userRepository) {
        this.repository = repository;
        this.mapper = mapper;
        this.themeRepository = themeRepository;
        this.userRepository = userRepository;
    }

    public Long createPost(CreatePostRequest request, UUID userId, Long themeId) {
        var user = userRepository
                .findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found with id: " + userId));

        var theme = themeId == null ? null : themeRepository.findById(themeId)
                .orElseThrow(() -> new ThemeNotFoundException("Theme not found with id: " + themeId));

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

    public PostResponse updatePost(UpdatePostRequest request, Long postId, Long themeId) {
        var post = repository
                .findById(postId)
                .orElseThrow(() -> new PostNotFoundException(POST_NOT_FOUND));

        var theme = themeId == null ? null : themeRepository.findById(themeId)
                .orElseThrow(() -> new PostNotFoundException("Theme not found with id: " + themeId));

        setPost(post, request, theme);
        return mapper.fromPost(post);
    }

    private void setPost(Post post, UpdatePostRequest request, Theme theme) {
        mergePost(post, request,theme);
        repository.save(post);
    }

    private void mergePost(Post post, UpdatePostRequest request, Theme theme) {
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

    public List<PostResponse> filterPosts(UUID userId, Long themeId) {
        List<Post> posts;

        if (userId != null && themeId != null) {
            posts = repository.findByUserIdAndThemeId(userId, themeId);
        } else if (userId != null) {
            posts = repository.findByUserId(userId);
        } else if (themeId != null) {
            posts = repository.findByThemeId(themeId);
        } else {
            posts = repository.findAll();
        }

        return posts.stream().map(mapper::fromPost).toList();
    }
}
