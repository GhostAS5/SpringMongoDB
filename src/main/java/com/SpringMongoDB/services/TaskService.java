package com.SpringMongoDB.services;

import com.SpringMongoDB.model.Task;
import com.SpringMongoDB.model.User;
import com.SpringMongoDB.repositories.TaskRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Instant;

@Service
@RequiredArgsConstructor
public class TaskService {

    private final TaskRepository taskRepository;

    private final UserService userService;

    public Flux<Task> findAll(){
        return taskRepository.findAll();
    }

    public Mono<Task> findById(String id){
        return taskRepository.findById(id);
    }

    public Mono<Task> save(Task task){
        Mono<User> author = userService.findById(task.getAuthorId());
        author.subscribe(task::setAuthor);
        Mono<User> assignee = userService.findById(task.getAssigneeId());
        assignee.subscribe(task::setAssignee);

        task.setCreatedAt(Instant.now());
        return taskRepository.save(task);
    }

    public Mono<Task> update(String id, Task task){
        return findById(id).flatMap(taskForUpdate -> {
            if (StringUtils.hasText(task.getName())){
                taskForUpdate.setName(task.getName());
            }

            if (StringUtils.hasText(task.getDescription())){
                taskForUpdate.setDescription(task.getDescription());
            }

            return taskRepository.save(taskForUpdate);
        });
    }

    public Mono<Void> deleteById(String id){
        return taskRepository.deleteById(id);
    }
}
