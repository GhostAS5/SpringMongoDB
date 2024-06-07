package com.SpringMongoDB.web.controllers;

import com.SpringMongoDB.mapper.UserMapper;
import com.SpringMongoDB.model.Role;
import com.SpringMongoDB.model.RoleType;
import com.SpringMongoDB.services.UserService;
import com.SpringMongoDB.web.dto.UserRequest;
import com.SpringMongoDB.web.dto.UserResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final UserMapper userMapper;

    @GetMapping
    public Flux<UserResponse> getAllUsers() {
        return userService.findAll()
                .map(userMapper::userToUserResponse);
    }

    @GetMapping("/{id}")
    public Mono<ResponseEntity<UserResponse>> getUserById(@PathVariable String id) {
        return userService.findById(id)
                .map(userMapper::userToUserResponse)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @PostMapping
    public Mono<ResponseEntity<UserResponse>> createUser(@RequestBody UserRequest request,
                                                         @RequestParam RoleType roleType) {
        return userService.createNewAccount(userMapper.userRequestToUser(request), Role.from(roleType))
                .map(userMapper::userToUserResponse)
                .map(ResponseEntity::ok);
    }

    @PutMapping("/{id}")
    public Mono<ResponseEntity<UserResponse>> updateUser(@PathVariable String id, @RequestBody UserRequest request) {
        return userService.update(userMapper.userRequestToUser(id, request))
                .map(userMapper::userToUserResponse)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<Void>> deleteUser(@PathVariable String id) {
        return userService.deleteById(id).then(Mono.just(ResponseEntity.noContent().build()));
    }
}
