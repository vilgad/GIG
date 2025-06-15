package com.snippet.gig.service.project;

import com.snippet.gig.dto.ProjectDto;
import com.snippet.gig.entity.Project;
import com.snippet.gig.entity.Task;
import com.snippet.gig.entity.User;
import com.snippet.gig.enums.Status;
import com.snippet.gig.exception.AlreadyExistsException;
import com.snippet.gig.exception.BadRequestException;
import com.snippet.gig.exception.ResourceNotFoundException;
import com.snippet.gig.repository.ProjectRepository;
import com.snippet.gig.repository.TaskRepository;
import com.snippet.gig.repository.UserRepository;
import com.snippet.gig.requestDto.CreateProjectRequest;
import com.snippet.gig.requestDto.SendEmailRequest;
import com.snippet.gig.requestDto.UpdateProjectRequest;
import com.snippet.gig.service.email.IEmailService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class ProjectService implements IProjectService {
    private final IEmailService emailService;
    private final ProjectRepository projectRepository;
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    private final TaskRepository taskRepository;

    @Autowired
    public ProjectService(
            IEmailService emailService,
            ProjectRepository projectRepository,
            UserRepository userRepository,
            ModelMapper modelMapper,
            TaskRepository taskRepository
    ) {
        this.emailService = emailService;
        this.projectRepository = projectRepository;
        this.userRepository = userRepository;
        this.modelMapper = modelMapper;
        this.taskRepository = taskRepository;
    }

    @Override
    public Project createProject(CreateProjectRequest request) throws AlreadyExistsException {
        return Optional.of(request)
                .filter(project -> !projectRepository.existsByName(request.getName()))
                .map(req -> {
                    Project p = new Project();
                    p.setName(request.getName());
                    p.setDescription(request.getDescription());

                    if (request.getStartDate() == null || request.getEndDate() == null) {
                        throw new BadRequestException("Start date and end date cannot be null");
                    }

                    p.setStartDate(request.getStartDate());
                    p.setEndDate(request.getEndDate());

                    return projectRepository.save(p);
                }).orElseThrow(
                        () -> new AlreadyExistsException("This project is already present")
                );
    }

    @Override
    public Project getProject(Long id) throws ResourceNotFoundException {
        return projectRepository.findById(id)
                .orElseThrow(
                        () -> new ResourceNotFoundException("Project not present"));
    }

    @Override
    public void updateProject(UpdateProjectRequest request, Long id) throws ResourceNotFoundException {
        projectRepository.findById(id)
                .ifPresentOrElse(
                        existingproject -> {
                            if (request.getDescription() != null) {
                                existingproject.setDescription(request.getDescription());
                            }
                            if (request.getStartDate() != null) {
                                existingproject.setStartDate(request.getStartDate());
                            }
                            if (request.getEndDate() != null) {
                                existingproject.setEndDate(request.getEndDate());
                            }

                            SendEmailRequest sendEmailRequest = new SendEmailRequest();

                            // send email to all users associated with the project
                            for (User user : existingproject.getUsers()) {
                                sendEmailRequest.setTo(user.getEmail());
                                sendEmailRequest.setSubject("Project Updated");
                                sendEmailRequest.setBody("The project '" + existingproject.getName() + "' has been updated.\n" +
                                        "Description: " + existingproject.getDescription() + "\n" +
                                        "Start Date: " + existingproject.getStartDate() + "\n" +
                                        "End Date: " + existingproject.getEndDate());
                                emailService.sendEmail(sendEmailRequest);
                            }

                            projectRepository.save(existingproject);
                        }, () -> {
                            throw new ResourceNotFoundException("This project does not exists");
                        });
    }

    @Override
    public void deleteProject(Long id) throws ResourceNotFoundException {
        projectRepository.findById(id).ifPresentOrElse(project -> {
                    SendEmailRequest sendEmailRequest = new SendEmailRequest();
//                    userRepository.setProjectIdNull(id);
                    // send email to all users associated with the project
                    for (User user : project.getUsers()) {
                        sendEmailRequest.setTo(user.getEmail());
                        sendEmailRequest.setSubject("Project Deleted");
                        sendEmailRequest.setBody("The project '" + project.getName() + "' has been shut down.");
                        emailService.sendEmail(sendEmailRequest);
                    }

                    // Clear the users associated with the project before deletion
                    project.setUsers(null);
                    projectRepository.save(project);
                    taskRepository.deleteProjectTasks(id);
                    projectRepository.deleteProject(id);
                },
                () -> {
                    throw new ResourceNotFoundException("Project does not exist");
                });
    }

    @Override
    public List<Task> getTasks(Long projectId) throws ResourceNotFoundException {
        Project project = projectRepository.findById(projectId).orElseThrow(() -> new ResourceNotFoundException("Project does not exist"));

        if (project.getTasks().isEmpty()) {
            throw new ResourceNotFoundException("No Tasks FOund for this project");
        }
        return project.getTasks();
    }

    @Override
    public List<User> getUsers(Long projectId) throws ResourceNotFoundException {
        Project project = projectRepository.findById(projectId).orElseThrow(() -> new ResourceNotFoundException("Project does not exist"));

        if (project.getUsers().isEmpty()) {
            throw new ResourceNotFoundException("No users FOund for this project");
        }
        return project.getUsers();
    }

    @Override
    public List<Project> getAll() {
        List<Project> projects = projectRepository.findAll();

        if (projects.isEmpty()) {
            throw new ResourceNotFoundException("Project list is empty");
        }

        return projects;
    }

    @Override
    public void deleteAll() {
        List<Project> projects = projectRepository.findAll();

        if (projects.isEmpty()) {
            throw new ResourceNotFoundException("Project list is empty");
        }

        projectRepository.deleteAll();
    }

    @Override
    public void assignProjectToUser(Long projectId, Long userId)
            throws ResourceNotFoundException, AlreadyExistsException {
        projectRepository.findById(projectId)
                .ifPresentOrElse(
                        project -> {
                            userRepository.findById(userId).ifPresentOrElse(
                                    user -> {
                                        if (project.getUsers().contains(user)) {
                                            throw new AlreadyExistsException(
                                                    "This project is already assigned to the user");
                                        }
                                        user.getProjects().add(project);
                                        project.addUser(user);
                                        projectRepository.save(project);
                                        userRepository.save(user);

                                        // sending email notification
                                        emailService.sendEmail(
                                                new SendEmailRequest(
                                                        user.getEmail(),
                                                        "Project Assigned",
                                                        "You have been assigned to the project: " + project.getName() +
                                                                ".\nDescription: " + project.getDescription() + "\nStart Date: " +
                                                                project.getStartDate() + "\nEnd Date: " + project.getEndDate()
                                                ));
                                    }, () -> {
                                        throw new ResourceNotFoundException("This user does not exist");
                                    });
                        },
                        () -> {
                            throw new ResourceNotFoundException("Project do not exist");
                        });
    }

    @Override
    public ProjectDto convertProjectToProjectDto(Project project) {
        return modelMapper.map(project, ProjectDto.class);
    }

    @Override
    public Map<Status, List<Task>> getKanbanBoard(String projectName) throws ResourceNotFoundException {
        Project project = projectRepository.findByName(projectName)
                .orElseThrow(() -> new ResourceNotFoundException("Project with name: " + projectName + " not found"));

        Map<Status, List<Task>> kanbanBoard = new HashMap<>();

        for (Status status : Status.values()) {
            List<Task> tasksByStatus = taskRepository.findAllByProjectAndStatus(project, status);
            kanbanBoard.put(status, tasksByStatus);
        }

        return kanbanBoard;
//        return taskRepository.findAllByProject(project)
//                .stream()
//                .collect(Collectors.groupingBy(Task::getStatus));
    }
}
