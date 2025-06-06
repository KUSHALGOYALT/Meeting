package com.example.learn.repository;

import com.example.learn.model.Session;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface SessionRepository extends MongoRepository<Session, String> {
    List<Session> findBySubject(String subject);
    List<Session> findByStartTimeBetween(LocalDateTime start, LocalDateTime end);
}