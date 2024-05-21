package com.SpringMongoDB.services;

import com.SpringMongoDB.model.User;
import com.SpringMongoDB.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public Flux<User> findAll(){
        return userRepository.findAll();
    }

    public Mono<User> findById(String id){
        return userRepository.findById(id);
    }

    public Mono<User> save(User user){
        return userRepository.save(user);
    }

    public Mono<User> update(User user){
        return findById(user.getId()).flatMap(userForUpdate -> {
            if (StringUtils.hasText(user.getUsername())){
                userForUpdate.setUsername(user.getUsername());
            }

            if (StringUtils.hasText(user.getEmail())){
                userForUpdate.setEmail(user.getEmail());
            }

            return userRepository.save(userForUpdate);
        });
    }

    public Mono<Void> deleteById(String id){
        return userRepository.deleteById(id);
    }
}
