package com.mugi.kcb.interview.dto;

import com.mugi.kcb.interview.entity.TaskStatusEnum;
import lombok.Builder;
import lombok.Data;

import java.util.Map;

@Data
@Builder
public class ProjectSummary {
    private Long projectId;
    private String projectName;
    private Map<TaskStatusEnum, Long> taskCountsByStatus;
}
