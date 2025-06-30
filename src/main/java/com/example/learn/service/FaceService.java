package com.example.learn.service;

import com.example.learn.model.FaceData;
import com.example.learn.repository.FaceDataRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FaceService {
    @Autowired
    private FaceDataRepository faceRepository;

    public FaceData saveFaceData(FaceData faceData) {
        return faceRepository.save(faceData);
    }

    public boolean verifyFace(String studentId, String meetingId, String faceEncoding) {
        // Simple verification logic - in real implementation, you'd use ML models
        FaceData existing = faceRepository.findByStudentIdAndMeetingId(studentId, meetingId);
        if (existing != null) {
            existing.setVerified(true);
            faceRepository.save(existing);
            return true;
        }
        return false;
    }

    public List<FaceData> getStudentFaceData(String studentId) {
        return faceRepository.findByStudentId(studentId);
    }
}
