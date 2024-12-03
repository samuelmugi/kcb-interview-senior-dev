package com.mugi.kcb.interview.service;

import com.mugi.kcb.interview.dto.ProjectDto;
import com.mugi.kcb.interview.entity.Project;
import com.mugi.kcb.interview.exception.ConstraintViolationException;
import com.mugi.kcb.interview.exception.ProjectNotFoundException;
import com.mugi.kcb.interview.repos.ProjectRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProjectServiceTest {
    @InjectMocks
    private ProjectService projectService;
    @Mock
    private ProjectRepository projectRepository;

    @Test
    void createProject_shouldSaveAndReturnProject() {
        // Arrange
        ProjectDto projectDto = new ProjectDto();
        projectDto.setName("New Project");
        projectDto.setDescription("A test project");

        Project savedProject = new Project(1L, "New Project", "A test project", null);
        when(projectRepository.save(any(Project.class))).thenReturn(savedProject);

        // Act
        Project result = projectService.createProject(projectDto);

        // Assert
        assertNotNull(result);
        assertEquals("New Project", result.getName());
        assertEquals("A test project", result.getDescription());
        verify(projectRepository, times(1)).save(any(Project.class));
    }

    @Test
    void createProject_shouldThrowConstraintViolationExceptionIfNameIsEmpty() {
        // Arrange
        ProjectDto projectDto = new ProjectDto();
        projectDto.setName("");

        // Act & Assert
        assertThrows(ConstraintViolationException.class, () -> projectService.createProject(projectDto));
        verify(projectRepository, never()).save(any(Project.class));
    }

    @Test
    void getAllProjects_shouldReturnPaginatedProjects() {
        // Arrange
        PageRequest pageable = PageRequest.of(0, 10);
        List<Project> projects = List.of(new Project(1L, "Project 1", "Description 1", null), new Project(2L, "Project 2", "Description 2", null));
        Page<Project> pagedProjects = new PageImpl<>(projects, pageable, 2);
        when(projectRepository.findAll(pageable)).thenReturn(pagedProjects);

        // Act
        Page<Project> result = projectService.getAllProjects(pageable);

        // Assert
        assertNotNull(result);
        assertEquals(2, result.getTotalElements());
        verify(projectRepository, times(1)).findAll(pageable);
    }

    @Test
    void getProjectById_shouldReturnProjectIfExists() {
        // Arrange
        Long projectId = 1L;
        Project project = new Project(projectId, "Project 1", "Description 1", null);
        when(projectRepository.findById(projectId)).thenReturn(Optional.of(project));

        // Act
        Project result = projectService.getProjectById(projectId);

        // Assert
        assertNotNull(result);
        assertEquals("Project 1", result.getName());
        verify(projectRepository, times(1)).findById(projectId);
    }

    @Test
    void getProjectById_shouldThrowProjectNotFoundExceptionIfNotExists() {
        // Arrange
        Long projectId = 1L;
        when(projectRepository.findById(projectId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ProjectNotFoundException.class, () -> projectService.getProjectById(projectId));
        verify(projectRepository, times(1)).findById(projectId);
    }

    @Test
    void updateProject_shouldUpdateAndReturnProject() {
        // Arrange
        Long projectId = 1L;
        Project existingProject = new Project(projectId, "Old Project", "Old Description", null);
        when(projectRepository.findById(projectId)).thenReturn(Optional.of(existingProject));

        ProjectDto projectDto = new ProjectDto();
        projectDto.setName("Updated Project");
        projectDto.setDescription("Updated Description");
        Project updatedProject = new Project(projectId, "Updated Project", "Updated Description", null);

        when(projectRepository.save(any(Project.class))).thenReturn(updatedProject);

        // Act
        Project result = projectService.updateProject(projectId, projectDto);

        // Assert
        assertNotNull(result);
        assertEquals("Updated Project", result.getName());
        assertEquals("Updated Description", result.getDescription());
        verify(projectRepository, times(1)).save(existingProject);
    }

    @Test
    void updateProject_shouldThrowProjectNotFoundExceptionIfNotExists() {
        // Arrange
        Long projectId = 1L;
        ProjectDto projectDto = new ProjectDto();
        projectDto.setName("Updated Project");
        projectDto.setDescription("Updated Description");

        when(projectRepository.findById(projectId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ProjectNotFoundException.class, () -> projectService.updateProject(projectId, projectDto));
        verify(projectRepository, never()).save(any(Project.class));
    }

    @Test
    void deleteProject_shouldCallRepositoryDeleteIfExists() {
        // Arrange
        Long projectId = 1L;
        doNothing().when(projectRepository).deleteById(projectId);

        // Act
        projectService.deleteProject(projectId);

        // Assert
        verify(projectRepository, times(1)).deleteById(projectId);
    }
}