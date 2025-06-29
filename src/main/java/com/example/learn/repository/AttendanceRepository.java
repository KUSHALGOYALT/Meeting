package com.example.learn.repository;

import com.example.learn.model.Attendance;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface AttendanceRepository extends MongoRepository<Attendance, String> {
    List<Attendance> findBySessionId(String sessionId);

    Optional<Attendance> findBySessionIdAndStudentId(String sessionId, String studentId); // ✅ correct method
}