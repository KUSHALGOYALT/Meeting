package com.example.learn.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class SessionServiceTest {
    @Autowired
    private SessionService sessionService;
    @Autowired
    private SessionRepository sessionRepository;

    @Test
    void testManageSession() {
        sessionService.manageSession("test1", "student1");
        Optional<Session> session = sessionRepository.findById("test1");
        assertThat(session).isPresent();
        assertThat(session.get().getSubject()).isEqualTo("algebra");
    }

    @Test
    void testGenerateSessionQuiz() {
        List<String> quiz = sessionService.generateSessionQuiz("session1", "student1");
        assertThat(quiz).isNotEmpty();
    }
}