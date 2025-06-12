package com.snippet.gig.service.user;

import com.snippet.gig.dto.UserDto;
import com.snippet.gig.entity.Task;
import com.snippet.gig.entity.User;
import com.snippet.gig.exception.AlreadyExistsException;
import com.snippet.gig.exception.BadRequestException;
import com.snippet.gig.exception.ResourceNotFoundException;
import com.snippet.gig.repository.RoleRepository;
import com.snippet.gig.repository.TaskRepository;
import com.snippet.gig.repository.UserRepository;
import com.snippet.gig.requestDto.CreateUserRequest;
import com.snippet.gig.requestDto.UpdateUserRequest;
import com.snippet.gig.utils.UserDetail;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService implements IUserService {
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    private final TaskRepository taskRepository;

    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(12);
    private final RoleRepository roleRepository;

    @Autowired
    public UserService(UserRepository userRepository, ModelMapper modelMapper,
                       TaskRepository taskRepository, RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.modelMapper = modelMapper;
        this.taskRepository = taskRepository;
        this.roleRepository = roleRepository;
    }

    @Override
    public User createUser(CreateUserRequest request) throws AlreadyExistsException {
        return Optional.of(request)
                .filter(user -> !userRepository.existsByEmail(request.getEmail()) && !userRepository.existsByUsername(request.getUsername()))
                .map(req -> {
                    User user = new User();
                    user.setEmail(request.getEmail());
                    user.setPassword(encoder.encode(request.getPassword()));
                    user.setName(request.getName());
                    user.setDob(request.getDob());
                    user.setUsername(request.getUsername());

                    for (String role : request.getRoles()) {
                        if (role.equalsIgnoreCase("ADMIN")) {
                            throw new BadRequestException("cannot assign ADMIN role during user creation");
                        }
                        roleRepository.findByName(role.toUpperCase()).ifPresentOrElse(user::addRole, () -> {
                            throw new ResourceNotFoundException("Role not found: " + role);
                        });
                    }

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
                .orElseThrow(() -> new ResourceNotFoundException("User with username:" + username + " not present"));
    }

    @Override
    public List<User> getAllUsers() throws ResourceNotFoundException {
        List<User> users = userRepository.findAll();

        if (users.isEmpty()) {
            throw new ResourceNotFoundException("No Users Found");
        }

        return users;
    }

    /*@Override
    public List<User> getUsersByRole(String role) throws ResourceNotFoundException {
        // TODO()
        List<User> users = userRepository.findByRole(role);

        if (users.isEmpty()) {
            throw new ResourceNotFoundException("No Users Found");
        }

        return users;
    }*/

    @Override
    public List<Task> getUserTasks(Long userId) throws ResourceNotFoundException {
        User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User Not Found"));

        if (user.getTasks().isEmpty()) {
            throw new ResourceNotFoundException("No Tasks Found");
        }

        return user.getTasks();
    }

   /* @Override
    public void updateUserRole(Long id, String role) throws UnsupportedOperationException {
        // TODO()
    }*/

    @Override
    public void changePassword(Long id, String username, String email, String password) throws UnsupportedOperationException {
// TODO()
    }

    @Override
    public void deleteUserTask(Long userId, Long taskId) throws ResourceNotFoundException {
        userRepository.findById(userId).ifPresentOrElse(
                user -> {
                    taskRepository.findById(taskId).ifPresentOrElse(
                            task -> {
                                if (user.getTasks().contains(task)) {
                                    task.getUsers().remove(user);
                                    user.getTasks().remove(task);
                                    userRepository.save(user);
                                    taskRepository.save(task);
                                } else
                                    throw new ResourceNotFoundException("Task not assigned to this user");
                            }, () -> {
                                throw new ResourceNotFoundException("task not present");
                            }
                    );
                }, () -> {
                    throw new ResourceNotFoundException("User not found");
                }
        );
    }

    @Override
    public void deleteUsersAllTasks(Long userId) throws ResourceNotFoundException {
        userRepository.findById(userId).ifPresentOrElse(
                user -> {
                    if (user.getTasks().isEmpty()) {
                        throw new ResourceNotFoundException("No task is assigned");
                    } else {
                        user.setTasks(null);
                        userRepository.save(user);
                    }
                }, () -> {
                    throw new ResourceNotFoundException("User not found");
                }
        );
    }

    @Override
    public void deleteAllUsers() {
        userRepository.deleteAll();
    }

    /*@Override
    public void deleteUsersByRole(String role) throws UnsupportedOperationException {
        // TODO()
    }*/

    @Override
    public UserDto convertUserToDto(User user) {
        return modelMapper.map(user, UserDto.class);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = getUserByUsername(username);

        return new UserDetail(user);
    }
}
