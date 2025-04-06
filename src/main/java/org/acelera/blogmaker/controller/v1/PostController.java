package org.acelera.blogmaker.controller.v1;

import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.acelera.blogmaker.model.dto.request.CreatePostRequest;
import org.acelera.blogmaker.model.dto.request.UpdatePostRequest;
import org.acelera.blogmaker.model.dto.response.PostResponse;
import org.acelera.blogmaker.services.PostService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/posts")
public class PostController {

    private final PostService postService;

    public PostController(PostService postService) {
        this.postService = postService;
    }

    @PostMapping
    @Transactional
    public ResponseEntity<Void> createPost(@Valid @RequestBody CreatePostRequest request) {
        Long postId = postService.createPost(request, request.userId(), request.themeId());

        return ResponseEntity.created(URI.create("/api/v1/posts/" + postId)).build();
    }

    @GetMapping("/{postId}")
    public ResponseEntity<PostResponse> getPost(@PathVariable Long postId) {
        PostResponse post = postService.getPostById(postId);
        return ResponseEntity.ok(post);
    }

    @GetMapping
    public ResponseEntity<List<PostResponse>> getAllPosts() {
        List<PostResponse> posts = postService.getAllPosts();
        return ResponseEntity.ok(posts);
    }

    @GetMapping("/filter")
    public ResponseEntity<List<PostResponse>> filterPosts(
            @RequestParam(required = false) UUID userId,
            @RequestParam(required = false) Long themeId) {
        List<PostResponse> filteredPosts = postService.filterPosts(userId, themeId);
        return ResponseEntity.ok(filteredPosts);
    }

    @PutMapping("/{postId}")
    @Transactional
    public ResponseEntity<PostResponse> updatePost(@PathVariable Long postId,
                                                   @Valid @RequestBody UpdatePostRequest request) {
        PostResponse updatedPost = postService.updatePost(request, postId, request.themeId());
        return ResponseEntity.ok(updatedPost);
    }

    @DeleteMapping("/{postId}")
    @Transactional
    public ResponseEntity<Void> deletePost(@PathVariable Long postId) {
        postService.deletePost(postId);
        return ResponseEntity.noContent().build();
    }
}
