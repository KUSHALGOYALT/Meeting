package com.example.learn.repository;

import com.example.learn.model.FaceData;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FaceDataRepository extends MongoRepository<FaceData, String> {
    List<FaceData> findByStudentId(String studentId);
    List<FaceData> findByMeetingId(String meetingId);
    FaceData findByStudentIdAndMeetingId(String studentId, String meetingId);
}
