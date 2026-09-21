package com.nagraj.linkdin.posts_service.controller;

import com.nagraj.linkdin.posts_service.dto.PostCreateRequestDto;
import com.nagraj.linkdin.posts_service.dto.PostDto;
import com.nagraj.linkdin.posts_service.entity.Post;
import com.nagraj.linkdin.posts_service.service.PostsService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/posts")
@RequiredArgsConstructor
public class PostsController {
    private final PostsService postsService;

    @PostMapping
    public ResponseEntity<PostDto> createPost(@RequestBody PostCreateRequestDto postCreateRequestDto, HttpServletRequest httpServletRequest){
        PostDto createdPost = postsService.createPost(postCreateRequestDto,1L);
        return new ResponseEntity<>(createdPost, HttpStatus.CREATED);
    }

    @GetMapping("/{postId}")
    public ResponseEntity<PostDto> getPostById(@PathVariable Long postId){
        PostDto PostDto = postsService.getPostById(postId);
        return ResponseEntity.ok(PostDto);
    }
    @GetMapping("/user/{userId}/allPosts")
    public ResponseEntity<List<PostDto>> getAllPostsOfUser(@PathVariable Long userId){
        List<PostDto> posts = postsService.getAllPostsOfUser(userId);
        return ResponseEntity.ok(posts);
    }
}
