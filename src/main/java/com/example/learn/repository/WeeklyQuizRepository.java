package com.example.learn.repository;

import com.example.learn.model.WeeklyQuiz;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface WeeklyQuizRepository extends MongoRepository<WeeklyQuiz, String> {
    List<WeeklyQuiz> findBySessionId(String sessionId);
}