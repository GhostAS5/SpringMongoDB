package com.SpringMongoDB.web.controllers;

import com.SpringMongoDB.mapper.TaskMapper;
import com.SpringMongoDB.model.Task;
import com.SpringMongoDB.services.TaskService;
import com.SpringMongoDB.web.dto.TaskRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/tasks")
@RequiredArgsConstructor
public class TaskController {

    private final TaskService taskService;

    private final TaskMapper mapper;

    @GetMapping
    public Flux<Task> getAllUsers() {
        return taskService.findAll();
    }

    @GetMapping("/{id}")
    public Mono<ResponseEntity<Task>> getUserById(@PathVariable String id) {
        return taskService.findById(id)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @PostMapping
    public Mono<ResponseEntity<Task>> createUser(@RequestBody TaskRequest request) {
        return taskService.save(mapper.taskRequestToTask(request))
                .map(ResponseEntity::ok);
    }

    @PutMapping("/{id}")
    public Mono<ResponseEntity<Task>> updateUser(@PathVariable String id, @RequestBody Task task) {
        return taskService.update(id, task)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<Void>> deleteUser(@PathVariable String id) {
        return taskService.deleteById(id).then(Mono.just(ResponseEntity.noContent().build()));
    }
}
