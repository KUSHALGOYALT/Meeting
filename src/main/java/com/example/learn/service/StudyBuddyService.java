package com.example.learn.service;

import com.example.learn.model.Session;
import com.example.learn.model.StudyBuddyInteraction;
import com.example.learn.repository.SessionRepository;
import com.example.learn.repository.StudyBuddyInteractionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class StudyBuddyService {
    @Autowired
    private StudyBuddyInteractionRepository interactionRepository;
    @Autowired
    private SessionRepository sessionRepository;
    @Autowired
    private NlpService nlpService;

    public StudyBuddyInteraction askQuestion(String studentId, String sessionId, String question) {
        Session session = sessionRepository.findById(sessionId)
                .orElseThrow(() -> new RuntimeException("Session not found"));
        String response = nlpService.answerQuestion(question, session.getNotes(), session.getKeywords());
        StudyBuddyInteraction interaction = new StudyBuddyInteraction();
        interaction.setStudentId(studentId);
        interaction.setSessionId(sessionId);
        interaction.setQuestion(question);
        interaction.setResponse(response);
        interaction.setTimestamp(LocalDateTime.now());
        return interactionRepository.save(interaction);
    }
}