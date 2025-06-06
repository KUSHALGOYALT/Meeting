package com.example.learn.repository;

import com.example.learn.model.SwotAnalysis;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface SwotAnalysisRepository extends MongoRepository<SwotAnalysis, String> {
    static List<SwotAnalysis> findBySessionId(String sessionId) {
        return null;
    }

    Optional<SwotAnalysis> findBySessionIdAndStudentId(String sessionId, String studentId);
}