package com.snippet.gig.service.project;

import com.snippet.gig.dto.ProjectDto;
import com.snippet.gig.entity.Project;
import com.snippet.gig.entity.Task;
import com.snippet.gig.entity.User;
import com.snippet.gig.exception.AlreadyExistsException;
import com.snippet.gig.exception.ResourceNotFoundException;
import com.snippet.gig.repository.ProjectRepository;
import com.snippet.gig.repository.TaskRepository;
import com.snippet.gig.repository.UserRepository;
import com.snippet.gig.requestDto.CreateProjectRequest;
import com.snippet.gig.requestDto.UpdateProjectRequest;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class ProjectServiceTest {
    @Mock
    private ProjectRepository projectRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private  ModelMapper modelMapper;
    @Mock
    private  TaskRepository taskRepository;
    @InjectMocks
    private ProjectService projectService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateProject_Success() {
        // Arrange
        CreateProjectRequest request = new CreateProjectRequest();
        request.setName("New Project");
        request.setDescription("Project Description");
        request.setStartDate(LocalDate.now().toString());
        request.setEndDate(LocalDate.now().plusDays(10).toString());

        Mockito.when(projectRepository.existsByName(request.getName())).thenReturn(false);
        Mockito.when(projectRepository.save(Mockito.any(Project.class)))
                .thenAnswer(invocation -> {
                    Project project = invocation.getArgument(0);
                    project.setId(1L);
                    return project;
                });

        // Act
        Project project = projectService.createProject(request);

        // Assert
        Assertions.assertNotNull(project);
        Assertions.assertEquals("New Project", project.getName());
        Assertions.assertEquals("Project Description", project.getDescription());
        Mockito.verify(projectRepository, Mockito.times(1)).save(Mockito.any(Project.class));
    }

    @Test
    void testCreateProject_AlreadyExists() {
        // Arrange
        CreateProjectRequest request = new CreateProjectRequest();
        request.setName("Existing Project");
        request.setDescription("Existing Description");
        request.setStartDate(LocalDate.now().toString());
        request.setEndDate(LocalDate.now().plusDays(10).toString());

        Mockito.when(projectRepository.existsByName(request.getName())).thenReturn(true);

        // Act & Assert
        AlreadyExistsException exception = Assertions.assertThrows(AlreadyExistsException.class, () -> {
            projectService.createProject(request);
        });

        Assertions.assertEquals("This project is already present", exception.getMessage());
        Mockito.verify(projectRepository, Mockito.never()).save(Mockito.any(Project.class));
    }

    @Test
    void testUpdateProject_Success() {
        // Arrange
        Long projectId = 1L;
        UpdateProjectRequest request = new UpdateProjectRequest();
        request.setDescription("Updated Description");
        request.setStartDate(LocalDate.now().toString());
        request.setEndDate(LocalDate.now().plusDays(10).toString());

        Project existingProject = new Project();
        existingProject.setId(projectId);
        existingProject.setDescription("Old Description");
        existingProject.setStartDate(LocalDate.now().minusDays(10).toString());
        existingProject.setEndDate(LocalDate.now().toString());

        Mockito.when(projectRepository.findById(projectId)).thenReturn(Optional.of(existingProject));

        // Act
        projectService.updateProject(request, projectId);

        // Assert
        Mockito.verify(projectRepository, Mockito.times(1)).save(existingProject);
        Assertions.assertEquals("Updated Description", existingProject.getDescription());
        Assertions.assertEquals(request.getStartDate(), existingProject.getStartDate());
        Assertions.assertEquals(request.getEndDate(), existingProject.getEndDate());
    }

    @Test
    void testUpdateProject_ResourceNotFound() {
        // Arrange
        Long projectId = 2L;
        UpdateProjectRequest request = new UpdateProjectRequest();
        request.setDescription("Some Description");
        request.setStartDate(LocalDate.now().toString());
        request.setEndDate(LocalDate.now().plusDays(10).toString());

        Mockito.when(projectRepository.findById(projectId)).thenReturn(Optional.empty());

        // Act & Assert
        ResourceNotFoundException exception = Assertions.assertThrows(
                ResourceNotFoundException.class,
                () -> projectService.updateProject(request, projectId)
        );

        Assertions.assertEquals("This project does not exists", exception.getMessage());
        Mockito.verify(projectRepository, Mockito.never()).save(Mockito.any(Project.class));
    }

    @Test
    void testDeleteProject_Success() {
        // Arrange
        Long projectId = 1L;
        Project project = new Project();
        project.setId(projectId);
        project.setName("Test Project");

        Mockito.when(projectRepository.findById(projectId)).thenReturn(Optional.of(project));

        // Act
        projectService.deleteProject(projectId);

        // Assert
        Mockito.verify(userRepository, Mockito.times(1)).setProjectIdNull(projectId);
        Mockito.verify(taskRepository, Mockito.times(1)).deleteProjectTasks(projectId);
        Mockito.verify(projectRepository, Mockito.times(1)).deleteProject(projectId);
    }

    @Test
    void testDeleteProject_ResourceNotFound() {
        // Arrange
        Long projectId = 2L;

        Mockito.when(projectRepository.findById(projectId)).thenReturn(Optional.empty());

        // Act & Assert
        ResourceNotFoundException exception = Assertions.assertThrows(
                ResourceNotFoundException.class,
                () -> projectService.deleteProject(projectId)
        );

        Assertions.assertEquals("Project does not exist", exception.getMessage());
        Mockito.verify(userRepository, Mockito.never()).setProjectIdNull(Mockito.anyLong());
        Mockito.verify(taskRepository, Mockito.never()).deleteProjectTasks(Mockito.anyLong());
        Mockito.verify(projectRepository, Mockito.never()).deleteProject(Mockito.anyLong());
    }

    @Test
    void testGetProject_Success() {
        // Arrange
        Long projectId = 1L;
        Project project = new Project();
        project.setId(projectId);
        project.setName("Test Project");

        Mockito.when(projectRepository.findById(projectId)).thenReturn(Optional.of(project));

        // Act
        Project retrievedProject = projectService.getProject(projectId);

        // Assert
        Assertions.assertNotNull(retrievedProject);
        Assertions.assertEquals(projectId, retrievedProject.getId());
        Assertions.assertEquals("Test Project", retrievedProject.getName());
        Mockito.verify(projectRepository, Mockito.times(1)).findById(projectId);
    }

    @Test
    void testGetProject_ResourceNotFound() {
        // Arrange
        Long projectId = 2L;

        Mockito.when(projectRepository.findById(projectId)).thenReturn(Optional.empty());

        // Act & Assert
        ResourceNotFoundException exception = Assertions.assertThrows(
                ResourceNotFoundException.class,
                () -> projectService.getProject(projectId)
        );

        Assertions.assertEquals("Project not present", exception.getMessage());
        Mockito.verify(projectRepository, Mockito.times(1)).findById(projectId);
    }

    @Test
    void testGetTasks_Success() {
        // Arrange
        Long projectId = 1L;
        Project project = new Project();
        project.setId(projectId);

        Task task1 = new Task();
        task1.setId(101L);
        task1.setDescription("Task 1");

        Task task2 = new Task();
        task2.setId(102L);
        task2.setDescription("Task 2");

        List<Task> tasks = List.of(task1, task2);
        project.setTasks(tasks);

        Mockito.when(projectRepository.findById(projectId)).thenReturn(Optional.of(project));

        // Act
        List<Task> retrievedTasks = projectService.getTasks(projectId);

        // Assert
        Assertions.assertNotNull(retrievedTasks);
        Assertions.assertEquals(2, retrievedTasks.size());
        Assertions.assertEquals("Task 1", retrievedTasks.get(0).getDescription());
        Assertions.assertEquals("Task 2", retrievedTasks.get(1).getDescription());
        Mockito.verify(projectRepository, Mockito.times(1)).findById(projectId);
    }

    @Test
    void testGetTasks_ResourceNotFound() {
        // Arrange
        Long projectId = 2L;

        Mockito.when(projectRepository.findById(projectId)).thenReturn(Optional.empty());

        // Act & Assert
        ResourceNotFoundException exception = Assertions.assertThrows(
                ResourceNotFoundException.class,
                () -> projectService.getTasks(projectId)
        );

        Assertions.assertEquals("Project does not exist", exception.getMessage());
        Mockito.verify(projectRepository, Mockito.times(1)).findById(projectId);
    }

    @Test
    void testGetUsers_Success() {
        // Arrange
        Long projectId = 1L;
        Project project = new Project();
        project.setId(projectId);

        User user1 = new User();
        user1.setId(101L);
        user1.setName("User 1");

        User user2 = new User();
        user2.setId(102L);
        user2.setName("User 2");

        List<User> users = List.of(user1, user2);
        project.setUsers(users);

        Mockito.when(projectRepository.findById(projectId)).thenReturn(Optional.of(project));

        // Act
        List<User> retrievedUsers = projectService.getUsers(projectId);

        // Assert
        Assertions.assertNotNull(retrievedUsers);
        Assertions.assertEquals(2, retrievedUsers.size());
        Assertions.assertEquals("User 1", retrievedUsers.get(0).getName());
        Assertions.assertEquals("User 2", retrievedUsers.get(1).getName());
        Mockito.verify(projectRepository, Mockito.times(1)).findById(projectId);
    }

    @Test
    void testGetUsers_ResourceNotFound() {
        // Arrange
        Long projectId = 2L;

        Mockito.when(projectRepository.findById(projectId)).thenReturn(Optional.empty());

        // Act & Assert
        ResourceNotFoundException exception = Assertions.assertThrows(
                ResourceNotFoundException.class,
                () -> projectService.getUsers(projectId)
        );

        Assertions.assertEquals("Project does not exist", exception.getMessage());
        Mockito.verify(projectRepository, Mockito.times(1)).findById(projectId);
    }

    @Test
    void testDeleteAll_Success() {
        // Arrange
        Project project1 = new Project();
        project1.setId(1L);
        project1.setName("Project 1");

        Project project2 = new Project();
        project2.setId(2L);
        project2.setName("Project 2");

        List<Project> projects = List.of(project1, project2);

        Mockito.when(projectRepository.findAll()).thenReturn(projects);

        // Act
        projectService.deleteAll();

        // Assert
        Mockito.verify(projectRepository, Mockito.times(1)).findAll();
        Mockito.verify(projectRepository, Mockito.times(1)).deleteAll();
    }

    @Test
    void testDeleteAll_EmptyList() {
        // Arrange
        Mockito.when(projectRepository.findAll()).thenReturn(Collections.emptyList());

        // Act & Assert
        ResourceNotFoundException exception = Assertions.assertThrows(
                ResourceNotFoundException.class,
                () -> projectService.deleteAll()
        );

        Assertions.assertEquals("Project list is empty", exception.getMessage());
        Mockito.verify(projectRepository, Mockito.times(1)).findAll();
        Mockito.verify(projectRepository, Mockito.never()).deleteAll();
    }

    @Test
    void testAssignProjectToUser_Success() {
        // Arrange
        Long projectId = 1L;
        Long userId = 1L;
        Project project = new Project();
        project.setId(projectId);

        User user = new User();
        user.setId(userId);

        Mockito.when(projectRepository.findById(projectId)).thenReturn(Optional.of(project));
        Mockito.when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        // Act
        projectService.assignProjectToUser(projectId, userId);

        // Assert
        Mockito.verify(projectRepository, Mockito.times(1)).save(project);
        Mockito.verify(userRepository, Mockito.times(1)).save(user);
        Assertions.assertEquals(project, user.getProject());
        Assertions.assertTrue(project.getUsers().contains(user));
    }

    @Test
    void testAssignProjectToUser_UserNotFound() {
        // Arrange
        Long projectId = 1L;
        Long userId = 1L;
        Project project = new Project();
        project.setId(projectId);

        Mockito.when(projectRepository.findById(projectId)).thenReturn(Optional.of(project));
        Mockito.when(userRepository.findById(userId)).thenReturn(Optional.empty());

        // Act & Assert
        ResourceNotFoundException exception = Assertions.assertThrows(
                ResourceNotFoundException.class,
                () -> projectService.assignProjectToUser(projectId, userId)
        );

        Assertions.assertEquals("This user does not exist", exception.getMessage());
        Mockito.verify(projectRepository, Mockito.never()).save(project);
        Mockito.verify(userRepository, Mockito.never()).save(Mockito.any());
    }

    @Test
    void testAssignProjectToUser_ProjectNotFound() {
        // Arrange
        Long projectId = 1L;
        Long userId = 1L;

        Mockito.when(projectRepository.findById(projectId)).thenReturn(Optional.empty());

        // Act & Assert
        ResourceNotFoundException exception = Assertions.assertThrows(
                ResourceNotFoundException.class,
                () -> projectService.assignProjectToUser(projectId, userId)
        );

        Assertions.assertEquals("Project do not exist", exception.getMessage());
        Mockito.verify(projectRepository, Mockito.never()).save(Mockito.any());
        Mockito.verify(userRepository, Mockito.never()).save(Mockito.any());
    }

    @Test
    void testAssignProjectToUser_AlreadyAssigned() {
        // Arrange
        Long projectId = 1L;
        Long userId = 1L;
        Project project = new Project();
        project.setId(projectId);

        User user = new User();
        user.setId(userId);
        project.addUser(user); // The user is already assigned to the project

        Mockito.when(projectRepository.findById(projectId)).thenReturn(Optional.of(project));
        Mockito.when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        // Act & Assert
        AlreadyExistsException exception = Assertions.assertThrows(
                AlreadyExistsException.class,
                () -> projectService.assignProjectToUser(projectId, userId)
        );

        Assertions.assertEquals("This project is already assigned to the user", exception.getMessage());
        Mockito.verify(projectRepository, Mockito.never()).save(Mockito.any());
        Mockito.verify(userRepository, Mockito.never()).save(Mockito.any());
    }

    @Test
    void testConvertProjectToProjectDto() {
        // Arrange
        Project project = new Project();
        project.setId(1L);
        project.setName("Project 1");
        project.setDescription("This is a sample project");

        ProjectDto expectedProjectDto = new ProjectDto();
        expectedProjectDto.setId(1L);
        expectedProjectDto.setName("Project 1");
        expectedProjectDto.setDescription("This is a sample project");

        Mockito.when(modelMapper.map(project, ProjectDto.class)).thenReturn(expectedProjectDto);

        // Act
        ProjectDto actualProjectDto = projectService.convertProjectToProjectDto(project);

        // Assert
        Assertions.assertNotNull(actualProjectDto);
        Assertions.assertEquals(expectedProjectDto.getId(), actualProjectDto.getId());
        Assertions.assertEquals(expectedProjectDto.getName(), actualProjectDto.getName());
        Assertions.assertEquals(expectedProjectDto.getDescription(), actualProjectDto.getDescription());

        // Verify that the modelMapper.map method was called with correct parameters
        Mockito.verify(modelMapper, Mockito.times(1)).map(project, ProjectDto.class);
    }
}