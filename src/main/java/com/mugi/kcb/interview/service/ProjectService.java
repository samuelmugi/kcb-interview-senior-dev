package com.mugi.kcb.interview.service;

import com.mugi.kcb.interview.dto.ProjectDto;
import com.mugi.kcb.interview.entity.Project;
import com.mugi.kcb.interview.exception.ConstraintViolationException;
import com.mugi.kcb.interview.exception.ProjectNotFoundException;
import com.mugi.kcb.interview.repos.ProjectRepository;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProjectService {

    private final ProjectRepository projectRepository;

    public Project createProject(ProjectDto project) {
        var name = project.getName();
        if (StringUtils.isEmpty(project.getName())) {
            throw new ConstraintViolationException("Project name is empty");
        }
        Project newProject = Project.builder()
                .description(project.getDescription())
                .name(name).build();
        return projectRepository.save(newProject);
    }

    public Page<Project> getAllProjects(Pageable pageable) {
        return projectRepository.findAll(pageable);
    }

    public Project getProjectById(Long id) {
        return projectRepository.findById(id)
                .orElseThrow(() -> new ProjectNotFoundException("Project not found with id " + id));
    }

    public Project updateProject(Long id, ProjectDto projectDto) {
        return projectRepository.findById(id).map(project -> {
            project.setName(projectDto.getName());
            project.setDescription(projectDto.getDescription());
            return projectRepository.save(project);
        }).orElseThrow(() -> new ProjectNotFoundException("Project not found with id " + id));
    }

    public void deleteProject(Long id) {
        projectRepository.deleteById(id);
    }
}
