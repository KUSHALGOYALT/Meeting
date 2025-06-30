package com.example.learn.service;

import com.example.learn.model.Quiz;
import com.example.learn.model.QuizSubmission;
import com.example.learn.repository.QuizRepository;
import com.example.learn.repository.QuizSubmissionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

@Service
public class QuizService {
    @Autowired
    private QuizRepository quizRepository;
    @Autowired
    private QuizSubmissionRepository submissionRepository;

    public Quiz createQuiz(Quiz quiz) {
        quiz.setId(UUID.randomUUID().toString());
        return quizRepository.save(quiz);
    }

    public List<Quiz> getTeacherQuizzes(String teacherId) {
        return quizRepository.findByTeacherId(teacherId);
    }

    public List<Quiz> getAvailableQuizzes() {
        return quizRepository.findByStatus("available");
    }

    public QuizSubmission submitQuiz(QuizSubmission submission) {
        try {
            submission.setId(UUID.randomUUID().toString());
            submission.setSubmittedAt(LocalDateTime.now());
            Quiz quiz = quizRepository.findById(submission.getQuizId())
                    .orElseThrow(() -> new IllegalArgumentException("Quiz not found: " + submission.getQuizId()));
            AtomicInteger score = new AtomicInteger();
            int maxScore = quiz.getQuestions().size();
            for (var entry : submission.getAnswers().entrySet()) {
                quiz.getQuestions().stream()
                        .filter(q -> q.getId().equals(entry.getKey()) && q.getCorrectAnswer().equals(entry.getValue()))
                        .findFirst()
                        .ifPresent(q -> score.getAndIncrement());
            }
            submission.setScore(score.get());
            submission.setMaxScore(maxScore);
            submission.setGrade(calculateGrade(score.get(), maxScore));
            return submissionRepository.save(submission);
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    public List<QuizSubmission> getStudentSubmissions(String studentId) {
        return submissionRepository.findByStudentId(studentId);
    }

    public List<QuizSubmission> getQuizSubmissions(String quizId) {
        return submissionRepository.findByQuizId(quizId);
    }

    private String calculateGrade(int score, int maxScore) {
        double percentage = (double) score / maxScore * 100;
        if (percentage >= 90) return "A+";
        if (percentage >= 85) return "A";
        if (percentage >= 80) return "A-";
        if (percentage >= 75) return "B+";
        if (percentage >= 70) return "B";
        if (percentage >= 65) return "B-";
        if (percentage >= 60) return "C+";
        if (percentage >= 55) return "C";
        return "C-";
    }
}