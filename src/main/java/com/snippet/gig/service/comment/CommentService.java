package com.snippet.gig.service.comment;

import com.snippet.gig.entity.Comment;
import com.snippet.gig.entity.Task;
import com.snippet.gig.entity.User;
import com.snippet.gig.exception.ResourceNotFoundException;
import com.snippet.gig.repository.CommentRepository;
import com.snippet.gig.repository.TaskRepository;
import com.snippet.gig.repository.UserRepository;
import com.snippet.gig.requestDto.CreateCommentRequest;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class CommentService implements ICommentService {
    private final CommentRepository commentRepository;
    private final UserRepository userRepository;
    private final TaskRepository taskRepository;

    public CommentService(CommentRepository commentRepository, UserRepository userRepository, TaskRepository taskRepository) {
        this.commentRepository = commentRepository;
        this.userRepository = userRepository;
        this.taskRepository = taskRepository;
    }

    @Override
    public Comment createComment(CreateCommentRequest request) throws ResourceNotFoundException {
        Comment comment = new Comment();
        comment.setContent(request.getContent());

        User user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new ResourceNotFoundException("User not found with username: " + request.getUsername()));
        comment.setUser(user);
        user.getComments().add(comment);

        comment.setTimeStamp(LocalDate.now());

        Task task = taskRepository.findById(request.getTaskId())
                .orElseThrow(() -> new ResourceNotFoundException("Task not found with id: " + request.getTaskId()));
        comment.setTask(task);

        commentRepository.save(comment);

        if (request.getParentCommentId() != null) {
            Comment parentComment = commentRepository.findById(request.getParentCommentId())
                    .orElseThrow(() -> new ResourceNotFoundException("Parent comment not found with id: " + request.getParentCommentId()));
            comment.setParentComment(parentComment);
            parentComment.getReplies().add(comment);

            commentRepository.save(parentComment);
        } else {
            task.getComments().add(comment);
        }

        userRepository.save(user);
        taskRepository.save(task);

        return comment;
    }

    @Override
    public void deleteComment(Long commentId) throws ResourceNotFoundException {
        commentRepository.findById(commentId).ifPresentOrElse(commentRepository::delete, () -> {
            throw new ResourceNotFoundException("Comment not found with id: " + commentId);
        });
    }

    @Override
    public List<Comment> getTaskComments(Long taskId) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new ResourceNotFoundException("Task not found with id: " + taskId));

        if (task.getComments().isEmpty())
            throw new ResourceNotFoundException("No comments found for this task");

        return task.getComments();
    }

    @Override
    public List<Comment> getUserComments(String username) throws ResourceNotFoundException {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User with username: " + username + " not found"));

        List<Comment> comments = user.getComments();

        if (comments.isEmpty()) {
            throw new ResourceNotFoundException("No comments found for user: " + username);
        }

        return comments;
    }
}
