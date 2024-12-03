package com.mugi.kcb.interview.config.data;

import com.mugi.kcb.interview.repos.ProjectRepository;
import com.mugi.kcb.interview.repos.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class DataInitializerService {

    @Autowired
    private ProjectRepository projectRepository;
    @Autowired
    private TaskRepository taskRepository;

    @EventListener(ApplicationReadyEvent.class)
    public void run()  {


        System.out.println("Initial data loaded into the database.");
    }
}
