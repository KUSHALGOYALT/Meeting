package com.example.learn.service;

import com.example.learn.model.EmotionData;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
class EmotionServiceTest {

    @InjectMocks
    private EmotionService emotionService;

    @Test
    void testGetStudentEmotions() {
        // Arrange
        String studentId = "student1";

        // Act
        List<EmotionData> emotions = emotionService.getStudentEmotions(studentId);

        // Assert
        assertThat(emotions).hasSize(3);
        assertThat(emotions).extracting("studentId").containsOnly(studentId);
        assertThat(emotions).extracting("emotion").containsExactlyInAnyOrder("engaged", "bored", "confused");
        assertThat(emotions).extracting("timestamp").isNotNull();
    }
}