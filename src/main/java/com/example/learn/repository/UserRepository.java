package com.example.learn.repository;

import com.example.learn.model.*;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface UserRepository extends MongoRepository<User, String> {
    List<User> findByRole(String role);
    User findByEmail(String email);
}

