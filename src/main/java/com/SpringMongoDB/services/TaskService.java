package com.SpringMongoDB.services;

import com.SpringMongoDB.exception.UserNotFoundException;
import com.SpringMongoDB.model.Task;
import com.SpringMongoDB.model.User;
import com.SpringMongoDB.repositories.TaskRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.text.MessageFormat;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;

@Service
@RequiredArgsConstructor
@Slf4j
public class TaskService {

    private final TaskRepository taskRepository;

    private final UserService userService;

    public Flux<Task> findAll(){
        return taskRepository.findAll().flatMap(this::fullForm);
    }

    public Mono<Task> findById(String id){
        return taskRepository.findById(id).flatMap(this::fullForm);
    }

    public Mono<Task> save(Task task){
        Mono<User> author = userService.findById(task.getAuthorId())
                        .switchIfEmpty(Mono.error(
                                new UserNotFoundException(MessageFormat.format("Author {0} not found", task.getAuthorId()))));
        Mono<User> assignee = userService.findById(task.getAssigneeId())
                        .switchIfEmpty(Mono.error(
                                new UserNotFoundException(MessageFormat.format("Assignee {0} not found", task.getAuthorId()))));

        return Mono.zip(author, assignee)
                .flatMap(tuple -> {
                    task.setAuthor(tuple.getT1());
                    task.setAssignee(tuple.getT2());
                    task.setCreatedAt(Instant.now());
                    return taskRepository.save(task);
                });
    }

    public Mono<Task> update(Task task){
        return findById(task.getId()).flatMap(taskForUpdate -> {
            if (StringUtils.hasText(task.getName())){
                taskForUpdate.setName(task.getName());
            }

            if (StringUtils.hasText(task.getDescription())){
                taskForUpdate.setDescription(task.getDescription());
            }

            if (task.getStatus() != null){
                taskForUpdate.setStatus(task.getStatus());
            }

            if (StringUtils.hasText(task.getAssigneeId())) {
                return userService.findById(task.getAssigneeId())
                        .switchIfEmpty(Mono.error(new UserNotFoundException(
                                MessageFormat.format("Assignee {0} not found", task.getAssigneeId()))))
                        .flatMap(assignee -> {
                            taskForUpdate.setAssigneeId(task.getAssigneeId());
                            taskForUpdate.setAssignee(assignee);
                            taskForUpdate.setUpdatedAt(Instant.now());
                            return taskRepository.save(taskForUpdate);
                        });
            } else {
                taskForUpdate.setUpdatedAt(Instant.now());
                return taskRepository.save(taskForUpdate);
            }
        });
    }

    public Mono<Task> addObserver(String taskId, String observerId){
        return findById(taskId).flatMap(task -> userService.findById(observerId)
                .flatMap(observer -> {
                    task.addObserver(observerId, observer);
                    return taskRepository.save(task);
                }));
    }

    private Mono<Task> fullForm(Task task){

        Mono<User> author = userService.findById(task.getAuthorId());
        Mono<User> assignee = userService.findById(task.getAssigneeId());
        Mono<Set<User>> observers = Flux.fromIterable(task.getObserverIds())
                .flatMap(userService::findById)
                .collectList()
                .map(HashSet::new);

        return Mono.zip(author, assignee, observers)
                .map(tuple -> {
                    task.setAuthor(tuple.getT1());
                    task.setAssignee(tuple.getT2());
                    task.setObservers(tuple.getT3());
                    return task;
                });
    }

    public Mono<Void> deleteById(String id){
        return taskRepository.deleteById(id);
    }
}
