package com.snippet.gig.service.user;

import com.snippet.gig.dto.UserDto;
import com.snippet.gig.entity.Task;
import com.snippet.gig.entity.User;
import com.snippet.gig.exception.AlreadyExistsException;
import com.snippet.gig.exception.ResourceNotFoundException;
import com.snippet.gig.repository.UserRepository;
import com.snippet.gig.requestDto.CreateUserRequest;
import com.snippet.gig.requestDto.UpdateUserRequest;
import com.snippet.gig.response.ApiResponse;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.swing.text.html.Option;
import java.util.List;
import java.util.Optional;

import static org.springframework.http.HttpStatus.NOT_FOUND;

@Service
public class UserService implements IUserService{
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;

    @Autowired
    public UserService(UserRepository userRepository, ModelMapper modelMapper) {
        this.userRepository = userRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public User createUser(CreateUserRequest request) throws AlreadyExistsException {
        return Optional.of(request)
                .filter(user -> !userRepository.existsByEmail(request.getEmail()) && !userRepository.existsByUsername(request.getUsername()))
                .map(req -> {
                    User user = new User();
                    user.setEmail(request.getEmail());
                    user.setPassword(request.getPassword());
                    user.setName(request.getName());
                    user.setDob(request.getDob());
                    user.setUsername(request.getUsername());
                    // TODO(Lookout for this)
                    user.setRole(request.getRole());
                    return userRepository.save(user);
                }).orElseThrow(() -> new AlreadyExistsException("User with this email or username already exists"));
    }

    @Override
    public User getUserById(Long id) throws ResourceNotFoundException {
        return userRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("User Not Found"));
    }

    @Override
    public User updateUserDetails(UpdateUserRequest request, Long id) throws ResourceNotFoundException {
        return userRepository.findById(id).map(existingUser -> {
            existingUser.setName(request.getName());
            existingUser.setDob(request.getDob());
            return userRepository.save(existingUser);
        }).orElseThrow(() -> new ResourceNotFoundException("User Not Found"));
    }

    @Override
    public void deleteUser(Long id) throws ResourceNotFoundException {
        userRepository.findById(id).ifPresentOrElse(userRepository::delete, () -> {
                throw new ResourceNotFoundException("User with id: " + id + " doesn't exist");
        });
    }

    @Override
    public User getUserByEmail(String email) throws ResourceNotFoundException {
        User user = userRepository.findByEmail(email);
        return Optional.ofNullable(user)
                .orElseThrow(() -> new ResourceNotFoundException("User with this email not present"));
    }

    @Override
    public User getUserByUsername(String username) throws ResourceNotFoundException {
        User user = userRepository.findByUsername(username);
        return Optional.ofNullable(user)
                .orElseThrow(() -> new ResourceNotFoundException("User with username:" + username +  " not present"));
    }

    @Override
    public List<User> getAllUsers() throws ResourceNotFoundException {
        List<User> users = userRepository.findAll();

        if (users.isEmpty()) {
            throw new ResourceNotFoundException("No Users Found");
        }

        return users;
    }

    @Override
    public List<User> getUsersByRole(String role) throws ResourceNotFoundException {
        List<User> users = userRepository.findByRole(role);

        if (users.isEmpty()) {
            throw new ResourceNotFoundException("No Users Found");
        }

        return users;
    }

    @Override
    public List<Task> getUserTasks(Long id) throws ResourceNotFoundException {
        Optional<User> user = userRepository.findById(id);

        if (user.isPresent()) {
            return user.get().getTasks();
        } else {
            throw new ResourceNotFoundException("User Not Found");
        }
    }

    @Override
    public void updateUserRole(Long id, String role) {
        // TODO()
    }

    @Override
    public void changePassword(Long id, String username, String email, String password) {
        // TODO()
    }

    @Override
    public void deleteAllUsers() {
        userRepository.deleteAll();
    }

    @Override
    public void deleteUsersByRole(String role) {
        // TODO()
    }

    @Override
    public UserDto convertUserToDto(User user) {
        return modelMapper.map(user, UserDto.class);
    }
}
