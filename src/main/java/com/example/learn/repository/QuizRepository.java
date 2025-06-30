package com.example.learn.repository;

import com.example.learn.model.Quiz;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface QuizRepository extends MongoRepository<Quiz, String> {
    java.util.List<Quiz> findByTeacherId(String teacherId);
    java.util.List<Quiz> findByStatus(String status);
}