package com.snippet.gig.controller;

import com.snippet.gig.dto.UserDto;
import com.snippet.gig.entity.Comment;
import com.snippet.gig.entity.Project;
import com.snippet.gig.entity.Task;
import com.snippet.gig.entity.User;
import com.snippet.gig.enums.Status;
import com.snippet.gig.requestDto.ChangePasswordRequest;
import com.snippet.gig.requestDto.CreateUserRequest;
import com.snippet.gig.requestDto.UpdateUserRequest;
import com.snippet.gig.response.ApiResponse;
import com.snippet.gig.service.user.IUserService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("${api.prefix}/users")
@Tag(name = "User APIs")
public class UserController {
    private final IUserService userService;

    @Autowired
    public UserController(IUserService userService) {
        this.userService = userService;
    }

    @GetMapping("/public/user")
    public ResponseEntity<ApiResponse> getUserById(
            @RequestParam Long userId
    ) {
        User user = userService.getUserById(userId);
        UserDto userDto = userService.convertUserToDto(user);
        return ResponseEntity.ok(
                new ApiResponse(
                        "User Fetched Successfully",
                        userDto
                ));
    }

    @PostMapping("/public/create-user")
    public ResponseEntity<ApiResponse> createUser(
            @RequestBody @Valid CreateUserRequest request
    ) {
        User user = userService.createUser(request);
        UserDto userDto = userService.convertUserToDto(user);
        return ResponseEntity.ok(
                new ApiResponse(
                        "Create User Success! user will receive an email with login credentials",
                        userDto
                ));
    }

    @PutMapping("/public/update-user")
    public ResponseEntity<ApiResponse> updateUser(
            @RequestBody @Valid UpdateUserRequest request,
            @RequestParam Long userId
    ) {
        User user = userService.updateUserDetails(request, userId);
        UserDto userDto = userService.convertUserToDto(user);
        return ResponseEntity.ok(
                new ApiResponse(
                        "Update User Success!",
                        userDto
                ));
    }

    @PreAuthorize("hasAnyRole('MANAGER','ADMIN','USER')")
    @PutMapping("/private/update-telegram-chat-id")
    public ResponseEntity<ApiResponse> updateTelegramChatId(
            @RequestParam String username
    ) {
        User user = userService.updateTelegramChatId(username);

        return ResponseEntity.ok(
                new ApiResponse(
                        "Telegram chat id updated Successfully!",
                        user
                ));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/private/delete-user")
    public ResponseEntity<ApiResponse> deleteUser(
            @RequestParam Long userId
    ) {
        userService.deleteUser(userId);
        return ResponseEntity.ok(
                new ApiResponse(
                        "Delete User Success! User will receive an email with account deletion confirmation",
                        null
                ));
    }

    @GetMapping("/public/get-all-users")
    public ResponseEntity<ApiResponse> getAllUsers() {
        List<User> users = userService.getAllUsers();
        return ResponseEntity.ok(
                new ApiResponse(
                        "All Users Fetched successfully",
                        users
                ));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/private/delete-all-users")
    public ResponseEntity<ApiResponse> deleteAllUsers() {
        userService.deleteAllUsers();
        return ResponseEntity.ok(
                new ApiResponse(
                        "all users deleted!",
                        null
                ));
    }

    @GetMapping("/public/get-user-by-email")
    public ResponseEntity<ApiResponse> getUserByEmail(
            @RequestParam String email
    ) {
        User user = userService.getUserByEmail(email);
        UserDto userDto = userService.convertUserToDto(user);
        return ResponseEntity.ok(
                new ApiResponse(
                        "Success",
                        userDto
                ));
    }

    @GetMapping("/public/get-user-by-username")
    public ResponseEntity<ApiResponse> getUserByUsername(
            @RequestParam String username
    ) {
        User user = userService.getUserByUsername(username);
        UserDto userDto = userService.convertUserToDto(user);
        return ResponseEntity.ok(
                new ApiResponse(
                        "User found!",
                        userDto
                ));
    }

   /* @GetMapping("/getUsersByRole")
    public ResponseEntity<ApiResponse> getUsersByRole(
            @RequestParam String role
    ) {
        List<User> users = userService.getUsersByRole(role);
        return ResponseEntity.ok(new ApiResponse("Users found!", users));
        *//*try {
            List<User> users = userService.getUsersByRole(role);
            return ResponseEntity.ok(new ApiResponse("Users found!", users));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
        } catch (Exception e) {
            return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(new ApiResponse(e.getMessage(), null));
        }*//*
    }*/

    @GetMapping("/public/get-user-tasks")
    public ResponseEntity<ApiResponse> getUserTasks(
            @RequestParam Long userId
    ) {
        List<Task> tasks = userService.getUserTasks(userId);
        return ResponseEntity.ok(
                new ApiResponse(
                        "Users Tasks fetched successfully",
                        tasks
                ));
    }

    @GetMapping("/public/get-user-projects")
    public ResponseEntity<ApiResponse> getUserProjects(
            @RequestParam Long userId
    ) {
        List<Project> projects = userService.getUserProjects(userId);
        return ResponseEntity.ok(
                new ApiResponse(
                        "Users Projects fetched successfully",
                        projects
                ));
    }

    @GetMapping("/public/get-kanban-board")
    public ResponseEntity<ApiResponse> getKanbanBoard(
            @RequestParam Long userId
    ) {
        Map<Status, List<Task>> kanbanBoard = userService.getKanbanBoard(userId);
        return ResponseEntity.ok(
                new ApiResponse(
                        "kanban board generated successfully",
                        kanbanBoard
                ));
    }

    @PreAuthorize("hasAnyRole('MANAGER','ADMIN')")
    @DeleteMapping("/private/delete-all-user-tasks")
    public ResponseEntity<ApiResponse> deleteAllUserTasks(
            @RequestParam Long userId
    ) {
        userService.deleteUsersAllTasks(userId);
        return ResponseEntity.ok(
                new ApiResponse(
                        "All Tasks removed!",
                        null
                ));
    }

    @PreAuthorize("hasAnyRole('MANAGER','ADMIN')")
    @DeleteMapping("/private/delete-user-task")
    public ResponseEntity<ApiResponse> deleteAllUserTasks(
            @RequestParam Long userId,
            @RequestParam Long taskId
    ) {
        userService.deleteUserTask(userId, taskId);
        return ResponseEntity.ok(
                new ApiResponse(
                        "Task removed Successfully",
                        null
                ));
    }

    @PutMapping("/public/change-password")
    public ResponseEntity<ApiResponse> changePassword(@Valid @RequestBody ChangePasswordRequest request) {
        userService.changePassword(request);

        return ResponseEntity.ok(
                new ApiResponse(
                        "Password changed Successfully! User will receive an email with confirmation",
                        null
                ));
    }

    @GetMapping("/public/get-mentioned-comments")
    public ResponseEntity<ApiResponse> getMentionedComments(
            @RequestParam String username
    ) {
        List<Comment> comments = userService.getMentionedComments(username);
        return ResponseEntity.ok(
                new ApiResponse(
                        "comment Created Successfully",
                        comments
                ));
    }
}
