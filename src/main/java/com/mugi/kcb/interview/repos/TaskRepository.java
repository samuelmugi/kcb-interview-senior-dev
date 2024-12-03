package com.mugi.kcb.interview.repos;

import com.mugi.kcb.interview.entity.Task;
import com.mugi.kcb.interview.entity.TaskStatusEnum;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {
    @Query("SELECT t FROM Task t WHERE (:status IS NULL OR t.status = :status) AND (:dueDate IS NULL OR t.dueDate = :dueDate)")
    Page<Task> findByStatusOrDueDate(@Param("status") TaskStatusEnum status, @Param("dueDate") LocalDate dueDate, Pageable pageable);
}
