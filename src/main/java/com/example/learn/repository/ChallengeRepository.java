package com.example.learn.repository;

import com.example.learn.model.Challenge;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface ChallengeRepository extends MongoRepository<Challenge, String> {
    List<Challenge> findBySessionId(String sessionId);
}
