package com.mugi.kcb.interview.service;

import com.mugi.kcb.interview.dto.ProjectSummary;
import com.mugi.kcb.interview.dto.TaskDto;
import com.mugi.kcb.interview.entity.Project;
import com.mugi.kcb.interview.entity.Task;
import com.mugi.kcb.interview.entity.TaskStatusEnum;
import com.mugi.kcb.interview.exception.ProjectNotFoundException;
import com.mugi.kcb.interview.exception.TaskNotFoundException;
import com.mugi.kcb.interview.repos.ProjectRepository;
import com.mugi.kcb.interview.repos.TaskRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TaskServiceTest {
    @Mock
    private TaskRepository taskRepository;
    @Mock
    private ProjectRepository projectRepository;

    @InjectMocks
    private TaskService taskService;



    @Test
    void updateTask_shouldUpdateAndReturnTask() {
        // Arrange
        Long taskId = 1L;
        Task existingTask = Task.builder()
                .id(taskId)
                .title("Old Title")
                .description("Old Description")
                .status(TaskStatusEnum.TO_DO)
                .dueDate(LocalDate.of(2024, 1, 1))
                .build();
        TaskDto taskDto = new TaskDto("New Title", "New Description", TaskStatusEnum.DONE, LocalDate.of(2024, 12, 31));
        Task updatedTask=existingTask;
        updatedTask.setTitle("New Title");
        updatedTask.setDescription("New Description");
        updatedTask.setStatus(TaskStatusEnum.DONE);
        updatedTask.setDueDate(LocalDate.of(2024, 12, 31));
        when(taskRepository.findById(taskId)).thenReturn(Optional.of(existingTask));
        when(taskRepository.save(any(Task.class))).thenReturn(updatedTask);

        // Act
        Task result = taskService.updateTask(taskId, taskDto);

        // Assert
        assertNotNull(result);
        assertEquals("New Title", result.getTitle());
        assertEquals("New Description", result.getDescription());
        assertEquals(TaskStatusEnum.DONE, result.getStatus());
        assertEquals(LocalDate.of(2024, 12, 31), result.getDueDate());
        verify(taskRepository, times(1)).save(existingTask);
    }

    @Test
    void updateTask_shouldThrowTaskNotFoundException() {
        // Arrange
        Long taskId = 1L;
        TaskDto taskDto = new TaskDto("Title", "Description", TaskStatusEnum.TO_DO, LocalDate.now());

        when(taskRepository.findById(taskId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(TaskNotFoundException.class, () -> taskService.updateTask(taskId, taskDto));
        verify(taskRepository, never()).save(any(Task.class));
    }

    @Test
    void deleteTask_shouldCallDeleteById() {
        // Arrange
        Long taskId = 1L;
        doNothing().when(taskRepository).deleteById(taskId);

        // Act
        taskService.deleteTask(taskId);

        // Assert
        verify(taskRepository, times(1)).deleteById(taskId);
    }

    @Test
    void addProjectTask_shouldAddAndReturnTask() {
        // Arrange
        Long projectId = 1L;
        Long taskId = 1L;

        Project project = new Project(projectId, "Project 1", "Description", null);
        TaskDto taskDto = new TaskDto("Task Title", "Task Description", TaskStatusEnum.TO_DO, LocalDate.now());
        Task projectTask = Task.builder()
                .project(project)
                .id(taskId)
                .title(taskDto.getTitle())
                .description(taskDto.getDescription())
                .status(taskDto.getStatus())
                .dueDate(taskDto.getDueDate())
                .build();
        when(projectRepository.findById(projectId)).thenReturn(Optional.of(project));
        when(taskRepository.save(any(Task.class))).thenReturn(projectTask);

        // Act
        Task result = taskService.addProjectTask(projectId, taskDto);

        // Assert
        assertNotNull(result);
        assertEquals("Task Title", result.getTitle());
        assertEquals(TaskStatusEnum.TO_DO, result.getStatus());
        assertEquals(project, result.getProject());
        verify(taskRepository, times(1)).save(any(Task.class));
    }

    @Test
    void addProjectTask_shouldThrowProjectNotFoundException() {
        // Arrange
        Long projectId = 1L;
        TaskDto taskDto = new TaskDto("Title", "Description", TaskStatusEnum.TO_DO, LocalDate.now());

        when(projectRepository.findById(projectId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ProjectNotFoundException.class, () -> taskService.addProjectTask(projectId, taskDto));
        verify(taskRepository, never()).save(any(Task.class));
    }

    @Test
    void getProjectTasks_shouldReturnFilteredTasks() {
        // Arrange
        Long projectId = 1L;
        Project project = new Project(projectId, "Project 1", "Description", null);
        Pageable pageable = PageRequest.of(0, 10);
        TaskStatusEnum status = TaskStatusEnum.TO_DO;
        LocalDate dueDate = LocalDate.now();

        List<Task> tasks = List.of(
                new Task(1L, "Task 1", "Description 1", status, dueDate, project),
                new Task(2L, "Task 2", "Description 2", status, dueDate, project)
        );
        Page<Task> pagedTasks = new PageImpl<>(tasks, pageable, tasks.size());

        when(projectRepository.findById(projectId)).thenReturn(Optional.of(project));
        when(taskRepository.findByStatusOrDueDate(status, dueDate, pageable)).thenReturn(pagedTasks);

        // Act
        Page<Task> result = taskService.getProjectTasks(projectId, pageable, status, dueDate);

        // Assert
        assertNotNull(result);
        assertEquals(2, result.getTotalElements());
        verify(taskRepository, times(1)).findByStatusOrDueDate(status, dueDate, pageable);
    }

    @Test
    void getProjectTasks_shouldThrowProjectNotFoundException() {
        // Arrange
        Long projectId = 1L;
        Pageable pageable = PageRequest.of(0, 10);

        when(projectRepository.findById(projectId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ProjectNotFoundException.class, () -> taskService.getProjectTasks(projectId, pageable, null, null));
        verify(taskRepository, never()).findByStatusOrDueDate(any(), any(), any());
    }

    @Test
    void getProjectSummary_shouldReturnProjectSummaries() {
        // Arrange
        Project project = new Project(1L, "Project 1", "Description", List.of(
                new Task(1L, "Task 1", "Description 1", TaskStatusEnum.TO_DO, LocalDate.now(), null),
                new Task(2L, "Task 2", "Description 2", TaskStatusEnum.DONE, LocalDate.now(), null)
        ));
        when(projectRepository.findAll()).thenReturn(List.of(project));

        // Act
        List<ProjectSummary> result = taskService.getProjectSummary();

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Project 1", result.get(0).getProjectName());
        assertEquals(2, result.get(0).getTaskCountsByStatus().size());
        assertEquals(1L, result.get(0).getTaskCountsByStatus().get(TaskStatusEnum.TO_DO));
        assertEquals(1L, result.get(0).getTaskCountsByStatus().get(TaskStatusEnum.DONE));
        verify(projectRepository, times(1)).findAll();
    }
}