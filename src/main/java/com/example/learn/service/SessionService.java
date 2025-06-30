package com.example.learn.service;

import com.example.learn.model.Question;
import com.example.learn.model.Quiz;
import com.example.learn.model.Session;
import com.example.learn.repository.SessionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class SessionService {
    @Autowired
    private SessionRepository sessionRepository;
    @Autowired
    private QuizService quizService;

    public Session manageSession(String sessionId, String studentId, String teacherId) {
        Session session = sessionRepository.findById(sessionId)
                .orElse(new Session(sessionId, studentId, "algebra", LocalDateTime.now(), teacherId));
        session.setCreatedAt(LocalDateTime.now());
        return sessionRepository.save(session);
    }

    public Quiz generateSessionQuiz(String sessionId, String studentId) {
        Session session = sessionRepository.findById(sessionId)
                .orElseThrow(() -> new IllegalArgumentException("Session not found"));

        List<Question> questions = new ArrayList<>();
        questions.add(new Question(UUID.randomUUID().toString(), "What is 2 + 2?", List.of("3", "4", "5", "6"), "4"));
        questions.add(new Question(UUID.randomUUID().toString(), "Solve x in x + 3 = 7", List.of("2", "3", "4", "5"), "4"));

        Quiz quiz = new Quiz(
                UUID.randomUUID().toString(),
                "Session Quiz: " + session.getSubject(),
                session.getSubject(),
                questions,
                "system",
                "15 min",
                "Easy",
                "1 day",
                "available"
        );
        return quizService.createQuiz(quiz);
    }

    public List<Session> getStudentSessions(String studentId) {
        return sessionRepository.findByStudentId(studentId);
    }

    public List<Session> getTeacherSessions(String teacherId) {
        return sessionRepository.findByTeacherId(teacherId);
    }
}