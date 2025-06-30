package com.example.learn.repository;

import com.example.learn.model.Session;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface SessionRepository extends MongoRepository<Session, String> {
    List<Session> findByStudentId(String studentId);
    List<Session> findByTeacherId(String teacherId);
}