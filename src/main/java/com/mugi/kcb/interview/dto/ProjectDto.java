package com.mugi.kcb.interview.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class ProjectDto {
    @NotBlank(message = "Name is required")
    private String name;

    private String description;
}
