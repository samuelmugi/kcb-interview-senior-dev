package com.mugi.kcb.interview.dto;

import com.mugi.kcb.interview.entity.TaskStatusEnum;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;

@Data
@AllArgsConstructor
public class TaskDto {
    @NotBlank(message = "Title is required")
    private String title;

    private String description;

    @Enumerated(EnumType.STRING)
    private TaskStatusEnum status;

    @FutureOrPresent(message = "Due date must be in the future or today")
    private LocalDate dueDate;
}
