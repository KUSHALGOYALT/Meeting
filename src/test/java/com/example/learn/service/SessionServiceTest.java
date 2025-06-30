package com.example.learn.service;

import com.example.learn.model.Session;
import com.example.learn.repository.SessionRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SessionServiceTest {

    @Mock
    private SessionRepository sessionRepository;

    @Mock
    private QuizService quizService;

    @InjectMocks
    private SessionService sessionService;

    @Test
    void testGenerateSessionQuiz() {
        // Arrange
        String sessionId = "session1";
        String studentId = "student1";
        String teacherId = "teacher1";

        // Mock sessionRepository.findById
        Session session = new Session(sessionId, studentId, "algebra", LocalDateTime.now(), teacherId);
        when(sessionRepository.findById(sessionId)).thenReturn(Optional.of(session));

        // Mock quizService.createQuiz (no return value needed for void method)
        when(quizService.createQuiz(any())).thenReturn(null);

        // Act
        List<String> quiz = (List<String>) sessionService.generateSessionQuiz(sessionId, studentId);

        // Assert
        assertThat(quiz).isNotEmpty();
        assertThat(quiz).hasSize(2);
        assertThat(quiz).containsExactly("What is 2 + 2?", "Solve x in x + 3 = 7");
    }
}