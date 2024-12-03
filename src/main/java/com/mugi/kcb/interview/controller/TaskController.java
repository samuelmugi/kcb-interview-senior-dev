package com.mugi.kcb.interview.controller;

import com.mugi.kcb.interview.dto.TaskDto;
import com.mugi.kcb.interview.entity.Task;
import com.mugi.kcb.interview.service.TaskService;
import com.mugi.kcb.interview.service.TaskService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Tasks", description = "Operations pertaining to tasks")
@RestController
@RequestMapping("/api/v1/Tasks")
@RequiredArgsConstructor
public class TaskController {

     private final TaskService taskService;
 

    @Operation(summary = "Update a task", description = "Update details of an existing task")
    @PutMapping("/{id}")
    public Task updateTask(
            @PathVariable Long id,
            @RequestBody TaskDto task) {
        return taskService.updateTask(id, task);
    }

    @Operation(summary = "Delete a task", description = "Remove a task from the collection")
    @DeleteMapping("/{id}")
    public void deleteTask(@PathVariable Long id) {
        taskService.deleteTask(id);
    }
}
