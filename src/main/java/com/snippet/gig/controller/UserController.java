package com.snippet.gig.controller;

import com.snippet.gig.dto.UserDto;
import com.snippet.gig.entity.Task;
import com.snippet.gig.entity.User;
import com.snippet.gig.exception.AlreadyExistsException;
import com.snippet.gig.exception.BadRequestException;
import com.snippet.gig.exception.ResourceNotFoundException;
import com.snippet.gig.requestDto.CreateUserRequest;
import com.snippet.gig.requestDto.UpdateUserRequest;
import com.snippet.gig.response.ApiResponse;
import com.snippet.gig.service.user.IUserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.HttpStatus.*;

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
        User user = userService.getUserById(userId);
        UserDto userDto = userService.convertUserToDto(user);
        return ResponseEntity.ok(new ApiResponse("User Fetched Successfully", userDto));
        /*try {
            User user = userService.getUserById(userId);
            UserDto userDto = userService.convertUserToDto(user);
            return ResponseEntity.ok(new ApiResponse("User Fetched Successfully", userDto));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
        } catch (Exception e) {
            return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(new ApiResponse(e.getMessage(), null));
        }*/
    }

    @PostMapping("/user")
    public ResponseEntity<ApiResponse> createUser(
            @RequestBody @Valid CreateUserRequest request
    ) {
        User user = userService.createUser(request);
        UserDto userDto = userService.convertUserToDto(user);
        return ResponseEntity.ok(new ApiResponse("Create User Success!", userDto));
        /* try {
            User user = userService.createUser(request);
            UserDto userDto = userService.convertUserToDto(user);
            return ResponseEntity.ok(new ApiResponse("Create User Success!", userDto));
        } catch (AlreadyExistsException e) {
            return ResponseEntity.status(CONFLICT).body(new ApiResponse(e.getMessage(), null));
        } catch (BadRequestException e) {
            return ResponseEntity.status(BAD_REQUEST).body(new ApiResponse(e.getMessage(), null));
        } catch (Exception e) {
            return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(new ApiResponse(e.getMessage(), null));
        }*/
    }

    @PutMapping("/user")
    public ResponseEntity<ApiResponse> updateUser(
            @RequestBody @Valid UpdateUserRequest request,
            @RequestParam Long userId
    ) {
        User user = userService.updateUserDetails(request, userId);
        UserDto userDto = userService.convertUserToDto(user);
        return ResponseEntity.ok(new ApiResponse("Update User Success!", userDto));
       /* try {
            User user = userService.updateUserDetails(request, userId);
            UserDto userDto = userService.convertUserToDto(user);
            return ResponseEntity.ok(new ApiResponse("Update User Success!", userDto));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
        } catch (Exception e) {
            return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(new ApiResponse(e.getMessage(), null));
        }*/
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @DeleteMapping("/user")
    public ResponseEntity<ApiResponse> deleteUser(
            @RequestParam Long userId
    ) {
        userService.deleteUser(userId);
        return ResponseEntity.ok(new ApiResponse("Delete User Success!", null));
        /*try {
            userService.deleteUser(userId);
            return ResponseEntity.ok(new ApiResponse("Delete User Success!", null));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
        } catch (Exception e) {
            return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(new ApiResponse(e.getMessage(), null));
        }*/
    }

    @GetMapping("/all")
    public ResponseEntity<ApiResponse> getAllUsers() {
        List<User> users = userService.getAllUsers();
        return ResponseEntity.ok(new ApiResponse("All Users Fetched successfully", users));
        /* try {
            List<User> users = userService.getAllUsers();
            return ResponseEntity.ok(new ApiResponse("All Users Fetched successfully", users));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
        } catch (Exception e) {
            return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(new ApiResponse(e.getMessage(), null));
        }*/
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @DeleteMapping("/all")
    public ResponseEntity<ApiResponse> deleteAllUsers() {
        userService.deleteAllUsers();
        return ResponseEntity.ok(new ApiResponse("all users deleted!", null));
        /*try {
            userService.deleteAllUsers();
            return ResponseEntity.ok(new ApiResponse("all users deleted!", null));
        } catch (Exception e) {
            return ResponseEntity.status(CONFLICT).body(new ApiResponse(e.getMessage(), null));
        }*/
    }

    @GetMapping("/getUserByEmail")
    public ResponseEntity<ApiResponse> getUserByEmail(
            @RequestParam String email
    ) {
        User user = userService.getUserByEmail(email);
        UserDto userDto = userService.convertUserToDto(user);
        return ResponseEntity.ok(new ApiResponse("Success", userDto));
        /*try {
            User user = userService.getUserByEmail(email);
            UserDto userDto = userService.convertUserToDto(user);
            return ResponseEntity.ok(new ApiResponse("Success", userDto));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
        } catch (Exception e) {
            return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(new ApiResponse(e.getMessage(), null));
        }*/
    }

    @GetMapping("/getUserByUsername")
    public ResponseEntity<ApiResponse> getUserByUsername(
            @RequestParam String username
    ) {
        User user = userService.getUserByUsername(username);
        UserDto userDto = userService.convertUserToDto(user);
        return ResponseEntity.ok(new ApiResponse("User found!", userDto));
        /*try {
            User user = userService.getUserByUsername(username);
            UserDto userDto = userService.convertUserToDto(user);
            return ResponseEntity.ok(new ApiResponse("User found!", userDto));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
        } catch (Exception e) {
            return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(new ApiResponse(e.getMessage(), null));
        }*/
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

    @GetMapping("/getUserTasks")
    public ResponseEntity<ApiResponse> getUserTasks(
            @RequestParam Long id
    ) {
        List<Task> tasks = userService.getUserTasks(id);
        return ResponseEntity.ok(new ApiResponse("Users Tasks", tasks));
        /*try {
            List<Task> tasks = userService.getUserTasks(id);
            return ResponseEntity.ok(new ApiResponse("Users Tasks", tasks));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
        } catch (Exception e) {
            return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(new ApiResponse(e.getMessage(), null));
        }*/
    }

    @PreAuthorize("hasRole('ROLE_MANAGER')")
    @DeleteMapping("/deleteAllUserTasks")
    public ResponseEntity<ApiResponse> deleteAllUserTasks(
            @RequestParam Long userId
    ) {
        userService.deleteUsersAllTasks(userId);
        return ResponseEntity.ok(new ApiResponse("All Tasks removed!", null));
        /*try {
            userService.deleteUsersAllTasks(userId);
            return ResponseEntity.ok(new ApiResponse("All Tasks removed!", null));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
        } catch (Exception e) {
            return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(new ApiResponse(e.getMessage(), null));
        }*/
    }

    @PreAuthorize("hasRole('ROLE_MANAGER')")
    @DeleteMapping("/deleteUserTask")
    public ResponseEntity<ApiResponse> deleteAllUserTasks(
            @RequestParam Long userId,
            @RequestParam Long taskId
    ) {
        userService.deleteUserTask(userId, taskId);
        return ResponseEntity.ok(new ApiResponse("Task removed Successfully", null));
        /*try {
            userService.deleteUserTask(userId, taskId);
            return ResponseEntity.ok(new ApiResponse("Task removed Successfully", null));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
        } catch (Exception e) {
            return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(new ApiResponse(e.getMessage(), null));
        }*/
    }
}
