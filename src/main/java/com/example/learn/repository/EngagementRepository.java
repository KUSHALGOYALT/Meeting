package com.example.learn.repository;

import com.example.learn.model.Engagement;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface EngagementRepository extends MongoRepository<Engagement, String> {
    Optional<Engagement> findBySessionIdAndStudentId(String sessionId, String studentId);
}