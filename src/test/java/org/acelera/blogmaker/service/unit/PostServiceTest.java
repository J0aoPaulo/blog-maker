package org.acelera.blogmaker.service.unit;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.acelera.blogmaker.exception.PostAlreadyExistException;
import org.acelera.blogmaker.exception.PostNotFoundException;
import org.acelera.blogmaker.exception.ThemeNotFoundException;
import org.acelera.blogmaker.exception.UserNotFoundException;
import org.acelera.blogmaker.model.Post;
import org.acelera.blogmaker.model.Role;
import org.acelera.blogmaker.model.Theme;
import org.acelera.blogmaker.model.User;
import org.acelera.blogmaker.model.dto.request.CreatePostRequest;
import org.acelera.blogmaker.model.dto.request.UpdatePostRequest;
import org.acelera.blogmaker.model.dto.response.PostResponse;
import org.acelera.blogmaker.repository.PostRepository;
import org.acelera.blogmaker.repository.ThemeRepository;
import org.acelera.blogmaker.repository.UserRepository;
import org.acelera.blogmaker.services.PostService;
import org.acelera.blogmaker.services.mapper.PostMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

class PostServiceTest {

    @Mock
    private PostRepository repository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private ThemeRepository themeRepository;

    @Mock
    private PostMapper mapper;

    @InjectMocks
    private PostService postService;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void createPost_ShouldThrowUserNotFound_WhenUserDoesNotExist() {
        CreatePostRequest request = new CreatePostRequest("Title", "Content", 1L, UUID.randomUUID());
        UUID userId = request.userId();
        Long themeId = request.themeId();

        when(userRepository.findById(any(UUID.class))).thenReturn(Optional.empty());

        UserNotFoundException exception = assertThrows(UserNotFoundException.class, () ->
                postService.createPost(request, userId, themeId)
        );
        assertTrue(exception.getMessage().contains("User not found"));
    }

    @Test
    void createPost_ShouldThrowThemeNotFound_WhenThemeDoesNotExist() {
        UUID userId = UUID.randomUUID();
        CreatePostRequest request = new CreatePostRequest("Title", "Content", 1L, userId);
        Long themeId = request.themeId();
        User user = new User();
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(themeRepository.findById(1L)).thenReturn(Optional.empty());

        ThemeNotFoundException exception = assertThrows(ThemeNotFoundException.class, () ->
                postService.createPost(request, userId, themeId)
        );
        assertTrue(exception.getMessage().contains("Theme not found"));
    }

    @Test
    void createPost_ShouldThrowPostAlreadyExistException_WhenTitleAndContentExist() {
        UUID userId = UUID.randomUUID();
        CreatePostRequest request = new CreatePostRequest("Title", "Content", null, userId);
        Long themeId = request.themeId();
        User user = new User();
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(repository.existsByTitle("Title")).thenReturn(true);
        when(repository.existsByContent("Content")).thenReturn(true);

        PostAlreadyExistException exception = assertThrows(PostAlreadyExistException.class, () ->
                postService.createPost(request, userId, themeId)
        );
        assertTrue(exception.getMessage().contains("Post already exists"));
    }

    @Test
    void createPost_ShouldReturnPostId_WhenSuccessful() {
        UUID userId = UUID.randomUUID();
        CreatePostRequest request = new CreatePostRequest("Title", "Content", null, userId);
        Long themeId = request.themeId();
        User user = new User();
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(repository.existsByTitle("Title")).thenReturn(false);
        when(repository.existsByContent("Content")).thenReturn(false);

        Post post = new Post();
        post.setId(100L);
        when(mapper.toPost(request, user, null)).thenReturn(post);
        when(repository.save(post)).thenReturn(post);

        Long postId = postService.createPost(request, userId, themeId);
        assertEquals(100L, postId);
    }

    @Test
    void getPostById_ShouldThrowPostNotFoundException_WhenPostDoesNotExist() {
        when(repository.findById(1L)).thenReturn(Optional.empty());
        PostNotFoundException exception = assertThrows(PostNotFoundException.class, () ->
                postService.getPostById(1L)
        );
        assertTrue(exception.getMessage().contains("Post not found"));
    }

    @Test
    void updatePost_ShouldThrowPostNotFoundException_WhenPostDoesNotExist() {
        UpdatePostRequest request = new UpdatePostRequest("New Title", "New Content", null);
        Long themeId = request.themeId();
        when(repository.findById(1L)).thenReturn(Optional.empty());
        PostNotFoundException exception = assertThrows(PostNotFoundException.class, () ->
                postService.updatePost(request, 1L, themeId)
        );
        assertTrue(exception.getMessage().contains("Post not found"));
    }

    @Test
    void updatePost_ShouldThrowThemeNotFoundException_WhenThemeDoesNotExist() {
        UpdatePostRequest request = new UpdatePostRequest("New Title", "New Content", 1L);
        Long themeId = request.themeId();
        Post existingPost = new Post();
        when(repository.findById(1L)).thenReturn(Optional.of(existingPost));
        when(themeRepository.findById(1L)).thenReturn(Optional.empty());
        ThemeNotFoundException exception = assertThrows(ThemeNotFoundException.class, () ->
                postService.updatePost(request, 1L, themeId)
        );
        assertTrue(exception.getMessage().contains("Theme not found"));
    }

    @Test
    void deletePost_ShouldThrowPostNotFoundException_WhenPostDoesNotExist() {
        when(repository.findById(1L)).thenReturn(Optional.empty());
        PostNotFoundException exception = assertThrows(PostNotFoundException.class, () ->
                postService.deletePost(1L)
        );
        assertTrue(exception.getMessage().contains("Post not found"));
    }

    @Test
    void filterPosts_ShouldReturnAllPosts_WhenNoFilter() {
        Theme theme = Theme.builder()
                .id(1L)
                .description("Theme")
                .build();
        User user = User.builder()
                .id(UUID.randomUUID())
                .name("User Name")
                .email("user@example.com")
                .password("password")
                .photo("photo.png")
                .role(Role.USER)
                .build();

        LocalDateTime now = LocalDateTime.now();
        Post post = Post.builder()
                .id(1L)
                .title("Title")
                .content("Content")
                .createdAt(now)
                .updatedAt(now)
                .theme(theme)
                .user(user)
                .build();

        PostResponse response = new PostResponse(
                1L,
                "Title",
                "Content",
                now,
                now,
                "Theme",
                1L,
                "User Name",
                user.getId(),
                "USER",
                "photo.png"
        );

        when(repository.findAll()).thenReturn(Collections.singletonList(post));
        when(mapper.fromPost(post)).thenReturn(response);

        List<PostResponse> result = postService.getAllPosts();

        assertEquals(1, result.size(), "Deve retornar exatamente um post");
        assertEquals("Title", result.getFirst().title(), "O título deve ser 'Title'");
    }
}
