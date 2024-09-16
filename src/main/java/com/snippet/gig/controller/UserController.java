package com.snippet.gig.controller;

import com.snippet.gig.dto.UserDto;
import com.snippet.gig.entity.Task;
import com.snippet.gig.entity.User;
import com.snippet.gig.exception.AlreadyExistsException;
import com.snippet.gig.exception.ResourceNotFoundException;
import com.snippet.gig.requestDto.CreateUserRequest;
import com.snippet.gig.requestDto.UpdateUserRequest;
import com.snippet.gig.response.ApiResponse;
import com.snippet.gig.service.user.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.HttpStatus.CONFLICT;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@RestController
@RequestMapping("${api.prefix}/users")
public class UserController {
    private final IUserService userService;

    @Autowired
    public UserController(IUserService userService) {
        this.userService = userService;
    }

    @GetMapping("/user")
    public ResponseEntity<ApiResponse> getUserById(
            @RequestParam Long userId
    ) {
        try {
            User user = userService.getUserById(userId);
            UserDto userDto = userService.convertUserToDto(user);
            return ResponseEntity.ok(new ApiResponse("Success", userDto));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
        }
    }

    @PostMapping("/user")
    public ResponseEntity<ApiResponse> createUser(
            @RequestBody CreateUserRequest request
    ) {
        try {
            User user = userService.createUser(request);
            UserDto userDto = userService.convertUserToDto(user);
            return ResponseEntity.ok(new ApiResponse("Create User Success!", userDto));
        } catch (AlreadyExistsException e) {
            return ResponseEntity.status(CONFLICT).body(new ApiResponse(e.getMessage(), null));
        }
    }

    @PutMapping("/user")
    public ResponseEntity<ApiResponse> updateUser(
            @RequestBody UpdateUserRequest request,
            @RequestParam Long userId
    ) {
        try {
            User user = userService.updateUserDetails(request, userId);
            UserDto userDto = userService.convertUserToDto(user);
            return ResponseEntity.ok(new ApiResponse("Update User Success!", userDto));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
        }
    }

    @DeleteMapping("/user")
    public ResponseEntity<ApiResponse> deleteUser(
            @RequestParam Long userId
    ) {
        try {
            userService.deleteUser(userId);
            return ResponseEntity.ok(new ApiResponse("Delete User Success!", null));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
        }
    }

    @GetMapping("/all")
    public ResponseEntity<ApiResponse> getAllUsers() {
        try {
            List<User> users = userService.getAllUsers();
            return ResponseEntity.ok(new ApiResponse("Success", users));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
        }
    }

    @GetMapping("/getUserByEmail")
    public ResponseEntity<ApiResponse> getUserByEmail(
            @RequestParam String email
    ) {
        try {
            User user = userService.getUserByEmail(email);
            UserDto userDto = userService.convertUserToDto(user);
            return ResponseEntity.ok(new ApiResponse("Success", userDto));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
        }
    }

    @GetMapping("/getUserByUsername")
    public ResponseEntity<ApiResponse> getUserByUsername(
            @RequestParam String username
    ) {
        try {
            User user = userService.getUserByUsername(username);
            UserDto userDto = userService.convertUserToDto(user);
            return ResponseEntity.ok(new ApiResponse("User found!", userDto));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
        }
    }

    @GetMapping("/getUsersByRole")
    public ResponseEntity<ApiResponse> getUsersByRole(
            @RequestParam String role
    ) {
        try {
            List<User> users = userService.getUsersByRole(role);
            return ResponseEntity.ok(new ApiResponse("Users found!", users));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
        }
    }

    @GetMapping("/getUserTasks")
    public ResponseEntity<ApiResponse> getUserTasks(
            @RequestParam Long id
    ) {
        try {
            List<Task> tasks = userService.getUserTasks(id);
            return ResponseEntity.ok(new ApiResponse("Users Tasks", tasks));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
        }
    }

    @DeleteMapping("/all")
    public ResponseEntity<ApiResponse> deleteAllUsers() {
        try {
            userService.deleteAllUsers();
            return ResponseEntity.ok(new ApiResponse("Success", null));
        } catch (Exception e) {
            return ResponseEntity.status(CONFLICT).body(new ApiResponse(e.getMessage(), null));
        }
    }

}
