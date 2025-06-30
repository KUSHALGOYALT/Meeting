package com.example.learn.repository;

import com.example.learn.model.SwotAnalysis;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SwotAnalysisRepository extends MongoRepository<SwotAnalysis, String> {
    List<SwotAnalysis> findByStudentId(String studentId);
    SwotAnalysis findByStudentIdAndSubject(String studentId, String subject);
}
