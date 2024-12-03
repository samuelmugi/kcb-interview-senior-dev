package com.mugi.kcb.interview.controller;

import com.mugi.kcb.interview.dto.ProjectDto;
import com.mugi.kcb.interview.dto.ProjectSummary;
import com.mugi.kcb.interview.dto.TaskDto;
import com.mugi.kcb.interview.entity.Project;
import com.mugi.kcb.interview.entity.Task;
import com.mugi.kcb.interview.entity.TaskStatusEnum;
import com.mugi.kcb.interview.service.ProjectService;
import com.mugi.kcb.interview.service.TaskService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@Tag(name = "Projects", description = "Operations pertaining to projects")
@RestController
@RequestMapping("/api/Projects")
@RequiredArgsConstructor
public class ProjectController {

    private final ProjectService projectService;
    private final TaskService taskService;

    @Operation(summary = "Add a new project", description = "Add a new project to the collection")
    @PostMapping
    public Project createProject(@RequestBody ProjectDto project) {
        return projectService.createProject(project);
    }

    @Operation(summary = "Get all projects", description = "Retrieve a list of all projects")
    @GetMapping
    public Page<Project> getAllProjects(Pageable pageable) {
        return projectService.getAllProjects(pageable);
    }

    @Operation(summary = "Get project by ID", description = "Retrieve a project by its ID")
    @GetMapping("/{id}")
    public Project getProjectById(
            @Parameter(description = "ID of the project to be retrieved", required = true) @PathVariable Long id) {
        return projectService.getProjectById(id);
    }

    @Operation(summary = "Update a project", description = "Update details of an existing project")
    @PutMapping("/{id}")
    public Project updateProject(
            @PathVariable Long id,
            @RequestBody ProjectDto project) {
        return projectService.updateProject(id, project);
    }

    @Operation(summary = "Delete a project", description = "Remove a project from the collection")
    @DeleteMapping("/{id}")
    public void deleteProject(@PathVariable Long id) {
        projectService.deleteProject(id);
    }

    @Operation(summary = "Add task to project", description = "Add a task to a project")
    @PostMapping("/{id}/tasks")
    public Task addProjectTask(
            @PathVariable Long id,
            @RequestBody TaskDto task) {
        return taskService.addProjectTask(id, task);
    }

    @Operation(summary = "Get tasks for a project", description = "Retrieve all tasks for a project, with optional filters by status and dueDate.")
    @GetMapping("/{id}/tasks")
    public Page<Task> getProjectTasks(
            @Parameter(description = "ID of the project", required = true) @PathVariable Long id,
            Pageable pageable,
            @RequestParam(required = false) TaskStatusEnum status,
            @RequestParam(required = false) LocalDate dueDate) {
        return taskService.getProjectTasks(id, pageable, status, dueDate);
    }

    @Operation(summary = "Get project summary tasks", description = "returns a list of all projects with a count of tasks grouped by status.")
    @GetMapping("/summary")
    public List<ProjectSummary> getProjectSummary() {
        return taskService.getProjectSummary();
    }
}
