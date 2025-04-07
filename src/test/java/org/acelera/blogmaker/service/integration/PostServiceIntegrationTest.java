package org.acelera.blogmaker.service.integration;

import org.acelera.blogmaker.exception.PostAlreadyExistException;
import org.acelera.blogmaker.model.Post;
import org.acelera.blogmaker.model.User;
import org.acelera.blogmaker.model.dto.request.CreatePostRequest;
import org.acelera.blogmaker.repository.PostRepository;
import org.acelera.blogmaker.repository.UserRepository;
import org.acelera.blogmaker.services.PostService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import jakarta.transaction.Transactional;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
class PostServiceIntegrationTest {
    private final PostService postService;
    private final UserRepository userRepository;
    private final PostRepository postRepository;

    @Autowired
    public PostServiceIntegrationTest(PostService postService, UserRepository userRepository,
                                    PostRepository postRepository) {
        this.postService = postService;
        this.userRepository = userRepository;
        this.postRepository = postRepository;
    }
    private User user;

    @BeforeEach
    void setup() {
        user = User.builder().name("Integration User")
                .email("integration@example.com")
                .password("pass")
                .build();
        user = userRepository.save(user);
    }

    @Test
    @Transactional
    void createPostIntegrationTest_Success() {
        CreatePostRequest request = new CreatePostRequest("Integration Title", "Integration Content", null, user.getId());
        Long postId = postService.createPost(request, user.getId(), request.themeId());
        assertNotNull(postId);

        Post post = postRepository.findById(postId).orElse(null);
        assertNotNull(post);
        assertEquals("Integration Title", post.getTitle());
    }

    @Test
    @Transactional
    void createPostIntegrationTest_Fail_Duplicate() {
        CreatePostRequest request = new CreatePostRequest("Dup Title", "Dup Content", null, user.getId());
        postService.createPost(request, user.getId(), request.themeId());

        UUID userId = user.getId();
        Long themeId = request.themeId();
        assertThrows(PostAlreadyExistException.class,
                () -> postService.createPost(request, userId, themeId));
    }
}
