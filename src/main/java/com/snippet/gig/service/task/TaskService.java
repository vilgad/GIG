package com.snippet.gig.service.task;

import java.util.List;
import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.snippet.gig.dto.TaskDto;
import com.snippet.gig.dto.UserDto;
import com.snippet.gig.entity.Project;
import com.snippet.gig.entity.Task;
import com.snippet.gig.entity.User;
import com.snippet.gig.exception.AlreadyExistsException;
import com.snippet.gig.exception.ResourceNotFoundException;
import com.snippet.gig.repository.ProjectRepository;
import com.snippet.gig.repository.TaskRepository;
import com.snippet.gig.repository.UserRepository;
import com.snippet.gig.requestDto.CreateTaskRequest;
import com.snippet.gig.requestDto.UpdateTaskRequest;

@Service
public class TaskService implements ITaskService {
    private final TaskRepository taskRepository;
    private final UserRepository userRepository;
    private final ProjectRepository projectRepository;
    private final ModelMapper modelMapper;

    @Autowired
    public TaskService(
        TaskRepository taskRepository,
        UserRepository userRepository,
        ProjectRepository projectRepository, 
        ModelMapper modelMapper
    ) {
        this.taskRepository = taskRepository;
        this.userRepository = userRepository;
        this.projectRepository = projectRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public Task createTask(CreateTaskRequest request) {
        Task task = new Task();
        task.setTitle(request.getTitle());
        task.setDescription(request.getDescription());
        task.setDueDate(request.getDueDate());
        task.setPriority(request.getPriority());
        task.setStatus(request.getStatus());

        taskRepository.save(task);

        return task;
    }

    @Override
    public void updateTask(UpdateTaskRequest request, Long id) throws ResourceNotFoundException {
        taskRepository.findById(id).ifPresentOrElse(
                t -> {
                    t.setDueDate(request.getDueDate());
                    t.setDescription(request.getDescription());
                    t.setPriority(request.getPriority());
                    t.setStatus(request.getStatus());
                    t.setTitle(request.getTitle());

                    taskRepository.save(t);
                }, () -> {
                    throw new ResourceNotFoundException("Task Not present");
                });
    }

    @Override
    public Task getTaskById(Long id) throws ResourceNotFoundException {
        return taskRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Task Not found"));
    }

    @Override
    public void deleteTask(Long id) throws ResourceNotFoundException {
        taskRepository.findById(id).ifPresentOrElse(taskRepository::delete, () -> {
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
    public void updateStatus(Long id, String status) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'updateStatus'");
    }

    @Override
    public void updatePriority(Long id, String priority) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'updatePriority'");
    }

    @Override
    public void changeDueDate(Long id, String dueDate) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'changeDueDate'");
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

}
