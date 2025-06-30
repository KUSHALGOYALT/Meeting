package com.example.learn.controller;

import com.example.learn.model.Quiz;
import com.example.learn.model.QuizSubmission;
import com.example.learn.service.QuizService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/quizzes")
@CrossOrigin(origins = "http://localhost:3000")
public class QuizController {
    @Autowired
    private QuizService quizService;

    @PostMapping("/create")
    public Quiz createQuiz(@RequestBody Quiz quiz) {
        return quizService.createQuiz(quiz);
    }

    @GetMapping("/teacher/{teacherId}")
    public List<Quiz> getTeacherQuizzes(@PathVariable String teacherId) {
        return quizService.getTeacherQuizzes(teacherId);
    }

    @GetMapping("/available")
    public List<Quiz> getAvailableQuizzes() {
        return quizService.getAvailableQuizzes();
    }

    @PostMapping("/submit")
    public QuizSubmission submitQuiz(@RequestBody QuizSubmission submission) {
        return quizService.submitQuiz(submission);
    }

    @GetMapping("/submissions/student/{studentId}")
    public List<QuizSubmission> getStudentSubmissions(@PathVariable String studentId) {
        return quizService.getStudentSubmissions(studentId);
    }

    @GetMapping("/submissions/quiz/{quizId}")
    public List<QuizSubmission> getQuizSubmissions(@PathVariable String quizId) {
        return quizService.getQuizSubmissions(quizId);
    }
}