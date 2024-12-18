package com.snippet.gig.service.user;

import com.snippet.gig.dto.UserDto;
import com.snippet.gig.entity.Task;
import com.snippet.gig.entity.User;
import com.snippet.gig.exception.AlreadyExistsException;
import com.snippet.gig.exception.BadRequestException;
import com.snippet.gig.exception.ResourceNotFoundException;
import com.snippet.gig.requestDto.CreateUserRequest;
import com.snippet.gig.requestDto.UpdateUserRequest;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;

public interface IUserService extends UserDetailsService {
    // basic crud operations
    User createUser(CreateUserRequest user) throws AlreadyExistsException;

    User getUserById(Long id) throws ResourceNotFoundException;

    User updateUserDetails(UpdateUserRequest request, Long id) throws ResourceNotFoundException;

    void deleteUser(Long id) throws ResourceNotFoundException;

    // Other Read Operations
    User getUserByEmail(String email) throws ResourceNotFoundException;

    User getUserByUsername(String username) throws ResourceNotFoundException;

    List<User> getAllUsers() throws ResourceNotFoundException;

//    List<User> getUsersByRole(String role) throws ResourceNotFoundException;

    List<Task> getUserTasks(Long id) throws ResourceNotFoundException;

    // Other Update Operations
//    void updateUserRole(Long id, String role) throws UnsupportedOperationException;

    void changePassword(Long id, String username, String email, String password) throws UnsupportedOperationException ;

    // Other Delete Operations
    void deleteUserTask(Long userId, Long taskId) throws ResourceNotFoundException;

    void deleteUsersAllTasks(Long userId) throws ResourceNotFoundException;

    void deleteAllUsers();

//    void deleteUsersByRole(String role) throws UnsupportedOperationException;

    // DTO method
    UserDto convertUserToDto(User user);
}
