package com.example.learn.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.Map;
@Document(collection = "quiz_submissions")
public class QuizSubmission {
    @Id
    private String id;
    private String quizId;
    private String studentId;
    private Map<String, String> answers; // Question ID to selected answer
    private int score;
    private int maxScore;
    private String grade;
    private LocalDateTime submittedAt;
    private int attemptNumber;
    private String subject;

    public QuizSubmission() {}

    public QuizSubmission(String id, String quizId, String studentId, Map<String, String> answers, int score, int maxScore, String grade, LocalDateTime submittedAt, int attemptNumber) {
        this.id = id;
        this.quizId = quizId;
        this.studentId = studentId;
        this.answers = answers;
        this.score = score;
        this.maxScore = maxScore;
        this.grade = grade;
        this.submittedAt = submittedAt;
        this.attemptNumber = attemptNumber;
    }

    // Getters and setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getQuizId() { return quizId; }
    public void setQuizId(String quizId) { this.quizId = quizId; }
    public String getStudentId() { return studentId; }
    public void setStudentId(String studentId) { this.studentId = studentId; }
    public Map<String, String> getAnswers() { return answers; }
    public void setAnswers(Map<String, String> answers) { this.answers = answers; }
    public int getScore() { return score; }
    public void setScore(int score) { this.score = score; }
    public int getMaxScore() { return maxScore; }
    public void setMaxScore(int maxScore) { this.maxScore = maxScore; }
    public String getGrade() { return grade; }
    public void setGrade(String grade) { this.grade = grade; }
    public LocalDateTime getSubmittedAt() { return submittedAt; }
    public void setSubmittedAt(LocalDateTime submittedAt) { this.submittedAt = submittedAt; }
    public int getAttemptNumber() { return attemptNumber; }
    public void setAttemptNumber(int attemptNumber) { this.attemptNumber = attemptNumber; }

    public Object getSubject() {
        return subject;
    }

    public void setSubject(Object subject) {
        this.subject = subject.toString();
    }
}