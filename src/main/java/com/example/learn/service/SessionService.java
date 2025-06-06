package com.example.learn.service;

import com.example.learn.model.Session;
import com.example.learn.model.WeeklyQuiz;
import com.example.learn.repository.SessionRepository;
import com.example.learn.repository.WeeklyQuizRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;

@Service
public class SessionService {

    private final NlpService nlpService;
    private static final Logger log = LoggerFactory.getLogger(SessionService.class);

    private final SessionRepository sessionRepository;
    private final WeeklyQuizRepository weeklyQuizRepository;

    public SessionService(NlpService nlpService, SessionRepository sessionRepository, WeeklyQuizRepository weeklyQuizRepository) {
        this.nlpService = nlpService;
        this.sessionRepository = sessionRepository;
        this.weeklyQuizRepository = weeklyQuizRepository;
        log.info("SessionService initialized");
    }

    public void manageSession(String sessionId, String studentId) {
        log.debug("Managing session {} for student {}", sessionId, studentId);
        Optional<Session> sessionOpt = sessionRepository.findById(sessionId);
        if (sessionOpt.isPresent()) {
            Session session = sessionOpt.get();
            session.setNotes(session.getNotes() != null ? session.getNotes() + " Managed by " + studentId : "Managed by " + studentId);
            sessionRepository.save(session);
        } else {
            Session session = new Session();
            session.setId(sessionId);
            session.setSubject("algebra");
            session.setStartTime(LocalDateTime.now());
            session.setKeywords(List.of("math", "algebra"));
            sessionRepository.save(session);
        }
    }

    public List<String> generateSessionQuiz(String sessionId, String studentId) {
        HashMap<Object, Object> studentProfile = new HashMap<>();
        studentProfile.put("subject", "algebra");
        studentProfile.put("weakness", "linear equations");
        studentProfile.put("strength", "geometry");
        return nlpService.generateAdaptiveQuiz(sessionId, studentId, studentProfile);
    }

    public String generateSessionQuizAsString(String sessionId, String studentId) {
        List<String> quizQuestions = generateSessionQuiz(sessionId, studentId);
        return String.join("\n", quizQuestions);
    }

    public String processSessionQuestion(String sessionId, String studentId, String question) {
        String context = "Student " + studentId + " in session " + sessionId + " is studying math.";
        List<String> additionalData = List.of("algebra", "calculus");
        return nlpService.answerQuestion(question, context, additionalData);
    }

    public void recordAttendanceLogin(String sessionId, String studentId) {
        log.debug("Recording login for session {} and student {}", sessionId, studentId);
        Optional<Session> sessionOpt = sessionRepository.findById(sessionId);
        if (sessionOpt.isPresent()) {
            Session session = sessionOpt.get();
            session.setStartTime(LocalDateTime.now());
            sessionRepository.save(session);
        }
    }

    public WeeklyQuiz getSession(String sessionId) {
        log.debug("Retrieving quiz for session {}", sessionId);
        List<WeeklyQuiz> quizzes = weeklyQuizRepository.findBySessionId(sessionId);
        return quizzes.isEmpty() ? null : quizzes.get(0);
    }

    public void recordAttendanceLogout(String sessionId, String studentId) {
        log.debug("Recording logout for session {} and student {}", sessionId, studentId);
        Optional<Session> sessionOpt = sessionRepository.findById(sessionId);
        if (sessionOpt.isPresent()) {
            Session session = sessionOpt.get();
            session.setEndTime(LocalDateTime.now());
            sessionRepository.save(session);
        }
    }

    public void generateWeeklyQuiz(String sessionId) {
        log.debug("Generating weekly quiz for session {}", sessionId);
        List<String> questions = generateSessionQuiz(sessionId, "defaultStudent");
        WeeklyQuiz quiz = new WeeklyQuiz();
        quiz.setId(UUID.randomUUID().toString());
        quiz.setSessionId(sessionId);
        quiz.setContent(String.join("\n", questions));
        weeklyQuizRepository.save(quiz);
    }

    public void submitWeeklyQuizAnswers(String quizId, String studentId, Map<String, Boolean> answers) {
        log.debug("Submitting quiz answers for quiz {} and student {}", quizId, studentId);
        Optional<WeeklyQuiz> quizOpt = weeklyQuizRepository.findById(quizId);
        if (quizOpt.isPresent()) {
            WeeklyQuiz quiz = quizOpt.get();
            Map<String, Map<String, Boolean>> studentAnswers = quiz.getStudentAnswers() != null ?
                    quiz.getStudentAnswers() : new HashMap<>();
            studentAnswers.put(studentId, answers);
            quiz.setStudentAnswers(studentAnswers);
            weeklyQuizRepository.save(quiz);
        }
    }

    public void saveSession(Session session) {
        log.debug("Saving session {}", session.getId());
        sessionRepository.save(session);
    }

    public List<Session> getAllSessions(Object subject, Object startTime, Object endTime, Object keyword, Object notes) {
        log.debug("Retrieving all sessions with filters");
        if (subject instanceof String s) {
            return sessionRepository.findBySubject(s);
        }
        if (startTime instanceof LocalDateTime start && endTime instanceof LocalDateTime end) {
            return sessionRepository.findByStartTimeBetween(start, end);
        }
        return sessionRepository.findAll();
    }

    public Map<String, Object> getTeacherDashboard(String sessionId) {
        log.debug("Retrieving teacher dashboard for session {}", sessionId);
        Map<String, Object> dashboard = new HashMap<>();
        Optional<Session> sessionOpt = sessionRepository.findById(sessionId);
        if (sessionOpt.isPresent()) {
            dashboard.put("session", sessionOpt.get());
            dashboard.put("quizzes", weeklyQuizRepository.findBySessionId(sessionId));
        }
        return dashboard;
    }
}
