package com.snippet.gig.service.comment;

import com.snippet.gig.entity.Comment;
import com.snippet.gig.exception.ResourceNotFoundException;
import com.snippet.gig.requestDto.CreateCommentRequest;

import java.util.List;

public interface ICommentService {
    Comment createComment(CreateCommentRequest request) throws ResourceNotFoundException;

    void deleteComment(Long commentId) throws ResourceNotFoundException;

    List<Comment> getTaskComments(Long taskId) throws ResourceNotFoundException;

    List<Comment> getUserComments(String username) throws ResourceNotFoundException;

//    TODO(Implement Comment Service)
    // basic crud operations
    // Other Read Operations
    // Other Update Operations
    // Other Delete Operations
}
