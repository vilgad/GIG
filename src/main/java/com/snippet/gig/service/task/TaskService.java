package com.snippet.gig.service.task;

import com.snippet.gig.dto.TaskDto;
import com.snippet.gig.entity.Project;
import com.snippet.gig.entity.Task;
import com.snippet.gig.entity.User;
import com.snippet.gig.enums.Priority;
import com.snippet.gig.enums.Status;
import com.snippet.gig.exception.AlreadyExistsException;
import com.snippet.gig.exception.BadRequestException;
import com.snippet.gig.exception.ResourceNotFoundException;
import com.snippet.gig.repository.ProjectRepository;
import com.snippet.gig.repository.TaskRepository;
import com.snippet.gig.repository.UserRepository;
import com.snippet.gig.requestDto.CreateTaskRequest;
import com.snippet.gig.requestDto.SendEmailRequest;
import com.snippet.gig.requestDto.UpdateTaskRequest;
import com.snippet.gig.service.email.IEmailService;
import com.snippet.gig.service.user.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class TaskService implements ITaskService {
    private final IEmailService emailService;
    private final TaskRepository taskRepository;
    private final UserRepository userRepository;
    private final ProjectRepository projectRepository;
    private final UserService userService;
    private final ModelMapper modelMapper;

    @Autowired
    public TaskService(
            IEmailService emailService, TaskRepository taskRepository,
            UserRepository userRepository,
            ProjectRepository projectRepository,
            UserService userService,
            ModelMapper modelMapper
    ) {
        this.emailService = emailService;
        this.taskRepository = taskRepository;
        this.userRepository = userRepository;
        this.projectRepository = projectRepository;
        this.userService = userService;
        this.modelMapper = modelMapper;
    }

    @Override
    public Task createTask(CreateTaskRequest request) {
        Task task = new Task();
        task.setTitle(request.getTitle());
        task.setDescription(request.getDescription());
        task.setCreatedAt(LocalDateTime.now());
        task.setDueDate(request.getDueDate());
        task.setPriority(Priority.fromValue(request.getPriority().getValue()));
        task.setStatus(Status.fromValue("to do"));

        taskRepository.save(task);

        return task;
    }

    @Override
    public void updateTask(UpdateTaskRequest request, Long taskId) throws ResourceNotFoundException {
        taskRepository.findById(taskId).ifPresentOrElse(
                t -> {
                    if (request.getPriority() != null) {
                        t.setPriority(Priority.fromValue(request.getPriority().getValue()));
                    }

                    if (request.getStatus() != null) {
                        if (request.getStatus().getValue().equalsIgnoreCase("completed"))
                            t.setCompletedAt(LocalDateTime.now());

                        t.setStatus(Status.fromValue(request.getStatus().getValue()));
                    }

                    if (request.getDueDate() != null) {
                        t.setDueDate(request.getDueDate());
                    }

                    if (request.getDescription() != null) {
                        t.setDescription(request.getDescription());
                    }

                    if (request.getTitle() != null) {
                        t.setTitle(request.getTitle());
                    }

                    taskRepository.save(t);
                }, () -> {
                    throw new ResourceNotFoundException("Task Not present");
                });
    }

    @Override
    public Task getTaskById(Long taskId) throws ResourceNotFoundException {
        return taskRepository.findById(taskId).orElseThrow(() -> new ResourceNotFoundException("Task Not found"));
    }

    @Override
    public void deleteTask(Long taskId) throws ResourceNotFoundException {
        taskRepository.findById(taskId).ifPresentOrElse(task -> {
            taskRepository.deleteAllUsersFromTask(taskId);
//            taskRepository.deleteById(taskId);
        }, () -> {
            throw new ResourceNotFoundException("Task not present");
        });
    }

    @Override
    public List<User> getUsersAssigned(Long taskId) throws ResourceNotFoundException {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new ResourceNotFoundException("Task Not Present"));

        List<User> users = task.getUsers();

        if (users.isEmpty()) {
            throw new ResourceNotFoundException("No users assigned");
        }

        return users;
    }

    @Override
    public Project getProject(Long taskId) throws ResourceNotFoundException {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new ResourceNotFoundException("Task Not Present"));

        var project = task.getProject();

        if (project != null) {
            return project;
        } else {
            throw new ResourceNotFoundException("Project is not assigned");
        }
    }

    @Override
    public void updateStatus(Long taskId, String status) {
        taskRepository.findById(taskId).ifPresentOrElse(task -> {
            if (!status.equals("to do")) {
                if (task.getProject() != null) {
                    if (status.equals("completed"))
                        task.setCompletedAt(LocalDateTime.now());

                    task.setStatus(Status.fromValue(status));
                    taskRepository.save(task);
                } else {
                    throw new BadRequestException("No project assigned to this task, cannot update status");
                }
            }
        }, () -> {
            throw new ResourceNotFoundException("Task not found");
        });
    }

    @Override
    public void updatePriority(Long taskId, String priority) {
        taskRepository.findById(taskId).ifPresentOrElse(task -> {
            task.setPriority(Priority.fromValue(priority));
            taskRepository.save(task);
        }, () -> {
            throw new ResourceNotFoundException("Task not found");
        });
    }

    @Override
    public List<Task> getAllTasks() throws ResourceNotFoundException {
        List<Task> tasks = taskRepository.findAll();

        if (tasks.isEmpty()) {
            throw new ResourceNotFoundException("No Tasks Created");
        }

        return tasks;
    }

    @Override
    public void assignTaskToUser(Long taskId, Long userId) throws ResourceNotFoundException, AlreadyExistsException {
        taskRepository.findById(taskId).ifPresentOrElse(
                t -> userRepository.findById(userId).ifPresentOrElse(
                        user -> {
                            if (user.getTasks().contains(t)) {
                                throw new AlreadyExistsException("this task is already assigned to user");
                            } else {
                                user.addTask(t);
                                userRepository.save(user);

                                // sending email notification
                                SendEmailRequest request = new SendEmailRequest();
                                request.setTo(user.getEmail());
                                request.setSubject(t.getTitle() + " Task Assigned");
                                request.setBody("You have been assigned a new task: " + t.getTitle() +
                                        ". Please check your task list for details.");
                                emailService.sendEmail(request);
                            }
                        },
                        () -> {
                            throw new ResourceNotFoundException("User with userId: " + userId + " not present");
                        }),
                () -> {
                    throw new ResourceNotFoundException("Task doesn't exist");
                });
    }

    @Override
    public void assignProjectToTask(Long projectId, Long taskId)
            throws ResourceNotFoundException, AlreadyExistsException {
        taskRepository.findById(taskId).ifPresentOrElse(
                t -> projectRepository.findById(projectId).ifPresentOrElse(
                        project -> {
                            if (project.getTasks().contains(t)) {
                                throw new AlreadyExistsException("this task is already assigned to this project");
                            } else {
                                t.setProject(project);
                                taskRepository.save(t);
                            }
                        },
                        () -> {
                            throw new ResourceNotFoundException(
                                    "project with projectId: " + projectId + " not present");
                        }),
                () -> {
                    throw new ResourceNotFoundException("Task doesn't exist");
                });
    }

    @Override
    public TaskDto convertTasktoTaskDto(Task task) {
        return modelMapper.map(task, TaskDto.class);
    }

    /* @Override
    public void deleteUsersAssigned(Long id) throws ResourceNotFoundException {
        taskRepository.findById(id)
                .ifPresentOrElse(task -> {
                    if (task.getUsers().isEmpty()) {
                        throw new ResourceNotFoundException("Task is not assigned to any user");
                    } else {
//                        TODO()
                    }
                }, () -> {
                    throw new ResourceNotFoundException("Task not present");
                });
    } */

}
