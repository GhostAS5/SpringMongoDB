package com.SpringMongoDB.repositories;

import com.SpringMongoDB.model.User;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

public interface UserRepository extends ReactiveMongoRepository<User, String> {
}
