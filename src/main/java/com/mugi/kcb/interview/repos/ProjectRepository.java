package com.mugi.kcb.interview.repos;

import com.mugi.kcb.interview.entity.Project;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProjectRepository extends JpaRepository<Project, Long> {

}
