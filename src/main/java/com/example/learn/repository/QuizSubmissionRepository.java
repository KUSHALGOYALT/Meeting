package com.example.learn.repository;

import com.example.learn.model.QuizSubmission;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface QuizSubmissionRepository extends MongoRepository<QuizSubmission, String> {
    List<QuizSubmission> findByStudentId(String studentId);
    List<QuizSubmission> findByQuizId(String quizId);
}