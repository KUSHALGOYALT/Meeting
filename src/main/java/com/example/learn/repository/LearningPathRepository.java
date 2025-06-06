package com.example.learn.repository;

import com.example.learn.model.LearningPath;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface LearningPathRepository extends MongoRepository<LearningPath, String> {
    Optional<LearningPath> findBySessionIdAndStudentId(String sessionId, String studentId);
}