package com.example.learn.controller;

import com.example.learn.model.FaceData;
import com.example.learn.service.FaceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/face")
@CrossOrigin(origins = "http://localhost:3000")
public class FaceController {
    @Autowired
    private FaceService faceService;

    @PostMapping("/save")
    public FaceData saveFaceData(@RequestBody FaceData faceData) {
        return faceService.saveFaceData(faceData);
    }

    @PostMapping("/verify")
    public boolean verifyFace(@RequestParam String studentId, @RequestParam String meetingId, @RequestParam String faceEncoding) {
        return faceService.verifyFace(studentId, meetingId, faceEncoding);
    }

    @GetMapping("/student/{studentId}")
    public List<FaceData> getStudentFaceData(@PathVariable String studentId) {
        return faceService.getStudentFaceData(studentId);
    }
}
