package com.SpringMongoDB.web.controllers;

import com.SpringMongoDB.mapper.TaskMapper;
import com.SpringMongoDB.services.TaskService;
import com.SpringMongoDB.web.dto.TaskRequest;
import com.SpringMongoDB.web.dto.TaskResponse;
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
    public Flux<TaskResponse> getAllTasks() {
        return taskService.findAll().map(mapper::taskToTaskResponse);
    }

    @GetMapping("/{id}")
    public Mono<ResponseEntity<TaskResponse>> getTaskById(@PathVariable String id) {
        return taskService.findById(id)
                .map(mapper::taskToTaskResponse)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @PostMapping
    public Mono<ResponseEntity<TaskResponse>> createTask(@RequestBody TaskRequest request) {
        return taskService.save(mapper.taskRequestToTask(request))
                .map(mapper::taskToTaskResponse)
                .map(ResponseEntity::ok);
    }

    @PutMapping("/{id}")
    public Mono<ResponseEntity<TaskResponse>> updateTask(@PathVariable String id, @RequestBody TaskRequest request) {
        return taskService.update(mapper.taskRequestToTask(id, request))
                .map(mapper::taskToTaskResponse)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @PutMapping("/{taskId}/{observerId}")
    public Mono<ResponseEntity<TaskResponse>> addObserver(@PathVariable String taskId, @PathVariable String observerId) {
        return taskService.addObserver(taskId, observerId)
                .map(mapper::taskToTaskResponse)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }


    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<Void>> deleteTask(@PathVariable String id) {
        return taskService.deleteById(id).then(Mono.just(ResponseEntity.noContent().build()));
    }
}
