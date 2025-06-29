package com.example.learn.service;

import com.example.learn.exception.DatabaseException;
import com.example.learn.exception.NotFoundException;
import com.example.learn.model.Attendance;
import com.example.learn.model.Session;
import com.example.learn.model.SwotAnalysis;
import com.example.learn.repository.AttendanceRepository;
import com.example.learn.repository.SessionRepository;
import com.example.learn.repository.SwotAnalysisRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Arrays;

@Service
public class SwotAnalysisService {
    @Autowired
    private SwotAnalysisRepository swotAnalysisRepository;
    @Autowired
    private SessionRepository sessionRepository;
    @Autowired
    private AttendanceRepository attendanceRepository;

    public SwotAnalysis generateSwotAnalysis(String sessionId, String studentId) {
        try {
            // Validate session and attendance
            Session session = sessionRepository.findById(sessionId)
                    .orElseThrow(() -> new NotFoundException("Session not found: " + sessionId));
            Attendance attendance = (Attendance) attendanceRepository.findBySessionIdAndStudentId(sessionId, studentId)
                    .orElseThrow(() -> new NotFoundException("Attendance not found for student: " + studentId));
            if (!attendance.isPresent()) {
                throw new DatabaseException("Student must be marked present to generate SWOT analysis");
            }

            // Create or update SWOT analysis
            SwotAnalysis swot = swotAnalysisRepository.findBySessionIdAndStudentId(sessionId, studentId)
                    .orElse(new SwotAnalysis());
            swot.setSessionId(sessionId);
            swot.setStudentId(studentId);
            swot.setStrengths(Arrays.asList("Mastered: " + session.getKeywords().get(0)));
            swot.setWeaknesses(Arrays.asList("Struggles with: " + session.getKeywords().get(1)));
            swot.setOpportunities(Arrays.asList("Explore " + session.getSubject() + " applications"));
            swot.setThreats(Arrays.asList("Time management issues"));
            swot.setTimestamp(LocalDateTime.now());

            return swotAnalysisRepository.save(swot);
        } catch (NotFoundException | DatabaseException e) {
            throw e;
        } catch (Exception e) {
            throw new DatabaseException("Failed to generate SWOT analysis: " + e.getMessage());
        }
    }
}