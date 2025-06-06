package com.example.learn.service;

import com.example.learn.model.Challenge;
import com.example.learn.repository.ChallengeRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;

@Service
public class ChallengeService {

    private final ChallengeRepository challengeRepository;
    private static final Logger log = LoggerFactory.getLogger(ChallengeService.class);

    public ChallengeService(ChallengeRepository challengeRepository) {
        this.challengeRepository = challengeRepository;
        log.info("ChallengeService initialized");
    }

    public Challenge createChallenge(String sessionId, String title, String description, int points) {
        Challenge challenge = new Challenge();
        challenge.setId(UUID.randomUUID().toString());
        challenge.setSessionId(sessionId);
        challenge.setTitle(title);
        challenge.setDescription(description);
        challenge.setPoints(points);
        challenge.setStudentPoints(new HashMap<>());

        challengeRepository.save(challenge);
        log.debug("Created challenge '{}' for session {}", title, sessionId);
        return challenge;
    }

    public List<Challenge> getChallengesBySession(String sessionId) {
        return challengeRepository.findBySessionId(sessionId);
    }
    public void generateWeeklyChallenge(String sessionId) {
        String title = "Weekly Challenge for Session " + sessionId;
        String description = "Solve this week's challenge to earn extra points!";
        int points = 10; // or any logic to calculate points

        createChallenge(sessionId, title, description, points);
        log.info("Generated weekly challenge for session {}", sessionId);
    }


    public void submitChallenge(String challengeId, String studentId, int earnedPoints) {
        Optional<Challenge> challengeOpt = challengeRepository.findById(challengeId);
        if (challengeOpt.isPresent()) {
            Challenge challenge = challengeOpt.get();
            Map<String, Integer> pointsMap = challenge.getStudentPoints();
            if (pointsMap == null) {
                pointsMap = new HashMap<>();
            }
            pointsMap.put(studentId, earnedPoints);
            challenge.setStudentPoints(pointsMap);
            challengeRepository.save(challenge);
            log.debug("Student {} earned {} points for challenge {}", studentId, earnedPoints, challengeId);
        } else {
            log.warn("Challenge with ID {} not found", challengeId);
        }
    }

    public Challenge getChallengeById(String challengeId) {
        return challengeRepository.findById(challengeId).orElse(null);
    }

    public List<Challenge> getAllChallenges() {
        return challengeRepository.findAll();
    }
}
