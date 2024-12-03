package com.mugi.kcb.interview.entity;


import jakarta.persistence.*;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Table(name = "tasks")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Task {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Title is required")
    private String title;

    private String description;

    @Enumerated(EnumType.STRING)
    private TaskStatusEnum status;

    @FutureOrPresent(message = "Due date must be in the future or today")
    private LocalDate dueDate;

    @ManyToOne
    @JoinColumn(name = "project", nullable = false)
    private Project project;
}