package com.snippet.gig.service.project;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.snippet.gig.repository.TaskRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.snippet.gig.dto.ProjectDto;
import com.snippet.gig.entity.Project;
import com.snippet.gig.entity.Task;
import com.snippet.gig.entity.User;
import com.snippet.gig.exception.AlreadyExistsException;
import com.snippet.gig.exception.ResourceNotFoundException;
import com.snippet.gig.repository.ProjectRepository;
import com.snippet.gig.repository.UserRepository;
import com.snippet.gig.requestDto.CreateProjectRequest;
import com.snippet.gig.requestDto.UpdateProjectRequest;

@Service
public class ProjectService implements IProjectService {
    private final ProjectRepository projectRepository;
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    private final TaskRepository taskRepository;

    @Autowired
    public ProjectService(ProjectRepository projectRepository, UserRepository userRepository, ModelMapper modelMapper,
                          TaskRepository taskRepository) {
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
                            existingproject.setDescription(request.getDescription());
                            existingproject.setStartDate(request.getStartDate());
                            existingproject.setEndDate(request.getEndDate());

                            projectRepository.save(existingproject);
                        }, () -> {
                            throw new ResourceNotFoundException("This project does not exists");
                        });

    }

    @Override
    public void deleteProject(Long id) throws ResourceNotFoundException {
        projectRepository.findById(id).ifPresentOrElse(project -> {

            userRepository.setProjectIdNull(id);
            taskRepository.deleteProjectTasks(id);
            projectRepository.deleteProject(id);
                },
                () -> {
                    throw new ResourceNotFoundException("Project does not exist");
                });
    }

    @Override
    public List<Task> getTasks(Long projectId) throws ResourceNotFoundException {
        Optional<Project> project = projectRepository.findById(projectId);

        if (project.isPresent()) {
            return project.get().getTasks();
        } else {
            throw new ResourceNotFoundException("Project does not exist");
        }
    }

    @Override
    public List<User> getUsers(Long projectId) throws ResourceNotFoundException {
        Optional<Project> project = projectRepository.findById(projectId);

        if (project.isPresent()) {
            return project.get().getUsers();
        } else {
            throw new ResourceNotFoundException("Project does not exist");
        }
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
                                        user.setProject(project);
                                        project.addUser(user);
                                        projectRepository.save(project);
                                        userRepository.save(user);
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
}
