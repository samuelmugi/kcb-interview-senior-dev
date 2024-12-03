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
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TaskService {

    private final TaskRepository taskRepository;
    private final ProjectRepository projectRepository;


    public Task updateTask(Long id, TaskDto taskDto) {
        return taskRepository.findById(id).map(task -> {
            task.setTitle(taskDto.getTitle());
            task.setDescription(taskDto.getDescription());
            task.setStatus(taskDto.getStatus());
            task.setDueDate(taskDto.getDueDate());
            return taskRepository.save(task);
        }).orElseThrow(() -> new TaskNotFoundException("Task not found with id " + id));
    }

    public void deleteTask(Long id) {
        taskRepository.deleteById(id);
    }

    public Task addProjectTask(Long id, TaskDto taskDto) {
        return projectRepository.findById(id).map(project -> {
            Task task = Task.builder()
                    .title(taskDto.getTitle())
                    .description(taskDto.getDescription())
                    .status(taskDto.getStatus())
                    .dueDate(taskDto.getDueDate())
                    .project(project)
                    .build();
            return taskRepository.save(task);
        }).orElseThrow(() -> new ProjectNotFoundException("Project not found with id " + id));
    }

    public Page<Task> getProjectTasks(Long id, Pageable pageable, TaskStatusEnum status, LocalDate dueDate) {
        return projectRepository.findById(id).map(project -> projectFilteredTasks(pageable, status, dueDate))
                .orElseThrow(() -> new ProjectNotFoundException("Project not found with id " + id));
    }

    private Page<Task> projectFilteredTasks(Pageable pageable, TaskStatusEnum status, LocalDate dueDate) {
        return taskRepository.findByStatusOrDueDate(status, dueDate, pageable);
    }

    public List<ProjectSummary> getProjectSummary() {
        List<Project> projects = projectRepository.findAll();

        return projects.stream().map(project -> {
            Map<TaskStatusEnum, Long> taskCount = project.getTasks().stream()
                    .collect(Collectors.groupingBy(Task::getStatus, Collectors.counting()));

            return  ProjectSummary.builder()
                    .projectId(project.getId())
                    .projectName(project.getName())
                    .taskCountsByStatus(taskCount)
                    .build();
         }).collect(Collectors.toList());
    }
}
