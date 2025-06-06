package com.example.learn.repository;

import com.example.learn.model.StudyBuddyInteraction;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface StudyBuddyInteractionRepository extends MongoRepository<StudyBuddyInteraction, String> {
    List<StudyBuddyInteraction> findByStudentIdAndSessionId(String studentId, String sessionId);
}