package com.snippet.gig.controller;

import com.snippet.gig.entity.Comment;
import com.snippet.gig.entity.User;
import com.snippet.gig.requestDto.CreateCommentRequest;
import com.snippet.gig.response.ApiResponse;
import com.snippet.gig.service.comment.ICommentService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("${api.prefix}/comments")
@Tag(name = "Comment APIs")
public class CommentController {
    private final ICommentService commentService;

    public CommentController(ICommentService commentService) {
        this.commentService = commentService;
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'USER')")
    @PostMapping("/private/create-comment")
    public ResponseEntity<ApiResponse> createComment(
            @RequestBody CreateCommentRequest request
    ) {
        Comment comment = commentService.createComment(request);
        return ResponseEntity.ok(
                new ApiResponse(
                        "comment Created Successfully",
                        comment
                ));
    }

    @GetMapping("/public/get-mentioned-users")
    public ResponseEntity<ApiResponse> getMentionedUsers(
            @RequestParam Long commentId
    ) {
        List<User> users = commentService.getMentionedUsers(commentId);
        return ResponseEntity.ok(
                new ApiResponse(
                        "comment Created Successfully",
                        users
                ));
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'USER')")
    @DeleteMapping("/private/delete-comment")
    public ResponseEntity<ApiResponse> deleteComment(
            @RequestBody Long commentId
    ) {
        commentService.deleteComment(commentId);
        return ResponseEntity.ok(
                new ApiResponse(
                        "comment deleted Successfully",
                        null
                ));
    }

    @GetMapping("/public/get-task-comments")
    public ResponseEntity<ApiResponse> getTaskComments(
            @RequestParam Long taskId) {
        List<Comment> comments = commentService.getTaskComments(taskId);
        return ResponseEntity.ok(
                new ApiResponse(
                        "Success",
                        comments
                ));
    }

    @GetMapping("/public/get-user-comments")
    public ResponseEntity<ApiResponse> getUserComments(
            @RequestParam String username) {
        List<Comment> comments = commentService.getUserComments(username);
        return ResponseEntity.ok(
                new ApiResponse(
                        "Success",
                        comments
                ));
    }
}
