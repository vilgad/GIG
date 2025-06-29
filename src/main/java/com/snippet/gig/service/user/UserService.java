package com.snippet.gig.service.user;

import com.snippet.gig.dto.UserDto;
import com.snippet.gig.entity.Comment;
import com.snippet.gig.entity.Project;
import com.snippet.gig.entity.Task;
import com.snippet.gig.entity.User;
import com.snippet.gig.enums.Status;
import com.snippet.gig.exception.AlreadyExistsException;
import com.snippet.gig.exception.BadRequestException;
import com.snippet.gig.exception.ResourceNotFoundException;
import com.snippet.gig.pojo.Result;
import com.snippet.gig.repository.RoleRepository;
import com.snippet.gig.repository.TaskRepository;
import com.snippet.gig.repository.UserRepository;
import com.snippet.gig.requestDto.ChangePasswordRequest;
import com.snippet.gig.requestDto.CreateUserRequest;
import com.snippet.gig.requestDto.SendEmailRequest;
import com.snippet.gig.requestDto.UpdateUserRequest;
import com.snippet.gig.service.email.IEmailService;
import com.snippet.gig.service.telegram.ITelegramService;
import com.snippet.gig.utils.UserDetail;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class UserService implements IUserService {
    private final IEmailService emailService;
    private final ITelegramService telegramService;
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    private final TaskRepository taskRepository;

    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(12);
    private final RoleRepository roleRepository;

    @Autowired
    public UserService(IEmailService emailService, ITelegramService telegramService, UserRepository userRepository, ModelMapper modelMapper,
                       TaskRepository taskRepository, RoleRepository roleRepository) {
        this.emailService = emailService;
        this.telegramService = telegramService;
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

                    // Send welcome email
                    SendEmailRequest emailRequest = new SendEmailRequest();
                    emailRequest.setTo(user.getEmail());
                    emailRequest.setSubject("Welcome to Our Gig Platform");
                    emailRequest.setBody("Hello " + user.getName() + ",\n\nWelcome to our Gig platform! We're excited to have you on board.\n\nBest regards,\nGig Team");
                    emailService.sendEmail(emailRequest);

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
        userRepository.findById(id).ifPresentOrElse(user -> {
            userRepository.delete(user);

            // Send email notification
            SendEmailRequest emailRequest = new SendEmailRequest();
            emailRequest.setTo(user.getEmail());
            emailRequest.setSubject("Account deleted Successfully");
            emailRequest.setBody("Hello " + user.getName() + ",\n\nYour account has been deleted successfully.\nIf you did not request this, please contact support immediately.\n\nBest regards,\nGig Team");
            emailService.sendEmail(emailRequest);
        }, () -> {
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
        User user = userRepository.findByUsername(username).get();
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
    public void changePassword(ChangePasswordRequest request) throws ResourceNotFoundException {
        userRepository.findByUsername(request.getUsername()).ifPresentOrElse(
                user -> {
                    if (encoder.matches(request.getOldPassword(), user.getPassword())) {
                        user.setPassword(encoder.encode(request.getNewPassword()));

                        // Send email notification
                        SendEmailRequest emailRequest = new SendEmailRequest();
                        emailRequest.setTo(user.getEmail());
                        emailRequest.setSubject("Password Changed Successfully");
                        emailRequest.setBody("Hello " + user.getName() + ",\n\nYour password has been changed successfully.\nIf you did not request this change, please contact support immediately.\n\nBest regards,\nGig Team");
                        emailService.sendEmail(emailRequest);
                        userRepository.save(user);
                    } else {
                        throw new BadRequestException("Old password is incorrect");
                    }
                }, () -> {
                    throw new ResourceNotFoundException("User not found");
                }
        );
    }

    @Override
    public List<Project> getUserProjects(Long userId) throws ResourceNotFoundException {
        User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User Not Found"));

        if (user.getProjects().isEmpty()) {
            throw new ResourceNotFoundException("No projects Found");
        }

        return user.getProjects();
    }

    @Override
    public Map<Status, List<Task>> getKanbanBoard(Long userId) throws ResourceNotFoundException {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User with id: " + userId + " not found"));

        Map<Status, List<Task>> kanbanBoard = new HashMap<>();

        for (Status status : Status.values()) {
            List<Task> tasksByStatus = taskRepository.findAllByUsersIdAndStatus(user.getId(), status);
            kanbanBoard.put(status, tasksByStatus);
        }

        return kanbanBoard;
    }

    @Override
    public List<Comment> getMentionedComments(String username) throws ResourceNotFoundException {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User with username: " + username + " not found"));

        List<Comment> mentionedComments = user.getMentionedComments();

        if (mentionedComments.isEmpty()) {
            throw new ResourceNotFoundException("No mentioned comments found for user: " + username);
        }

        return mentionedComments;
    }

    @Override
    public User updateTelegramChatId(String username) throws ResourceNotFoundException, AlreadyExistsException {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User with username: " + username + " not found"));

        if (user.getTelegramChatId() != null) {
            throw new AlreadyExistsException("Telegram chat ID already exists for this user");
        }

        for (Result result : telegramService.getTelegramResponse().result()) {
            if (user.getTelegramUsername().equals(
                    result.message().chat().username()
            )) {
                user.setTelegramChatId(
                        result.message().chat().id()
                );
                break;
            }
        }

        return userRepository.save(user);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = getUserByUsername(username);

        return new UserDetail(user);
    }
}
