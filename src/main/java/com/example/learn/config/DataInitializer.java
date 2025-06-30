package com.example.learn.config;

import com.example.learn.model.*;
import com.example.learn.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import java.util.Arrays;
import java.util.List;

@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private MeetingRepository meetingRepository;

    @Autowired
    private SwotAnalysisRepository swotRepository;

    @Override
    public void run(String... args) throws Exception {
        // Check if data already exists
        if (userRepository.count() > 0) {
            return; // Data already initialized
        }

        // Create sample students
        List<User> students = Arrays.asList(
                new User("Rahul Sharma", "rahul@example.com", "STUDENT"),
                new User("Priya Patel", "priya@example.com", "STUDENT"),
                new User("Amit Kumar", "amit@example.com", "STUDENT"),
                new User("Sneha Gupta", "sneha@example.com", "STUDENT"),
                new User("Arjun Singh", "arjun@example.com", "STUDENT")
        );
        userRepository.saveAll(students);

        // Create sample teachers
        List<User> teachers = Arrays.asList(
                new User("Dr. Sunita Mehta", "sunita@example.com", "TEACHER"),
                new User("Prof. Rajesh Verma", "rajesh@example.com", "TEACHER")
        );
        userRepository.saveAll(teachers);

        // Create admin
        User admin = new User("Admin User", "admin@example.com", "ADMIN");
        userRepository.save(admin);

        // Create sample meetings
        List<Meeting> meetings = Arrays.asList(
                new Meeting("Mathematics Class", teachers.get(0).getId(), "math-room-101"),
                new Meeting("Science Lab Session", teachers.get(1).getId(), "science-lab-202"),
                new Meeting("English Literature", teachers.get(0).getId(), "english-room-103")
        );
        meetingRepository.saveAll(meetings);

        // Create sample SWOT analyses
        SwotAnalysis swot1 = new SwotAnalysis(students.get(0).getId(), "Mathematics");
        swot1.setStrengths(Arrays.asList("Good analytical skills", "Quick problem solving"));
        swot1.setWeaknesses(Arrays.asList("Struggles with complex equations"));
        swot1.setOpportunities(Arrays.asList("Extra practice sessions", "Peer tutoring"));
        swot1.setThreats(Arrays.asList("Time management issues"));
        swot1.setAiRecommendations("Focus on step-by-step problem solving approach");

        SwotAnalysis swot2 = new SwotAnalysis(students.get(1).getId(), "Science");
        swot2.setStrengths(Arrays.asList("Excellent memory", "Strong theoretical knowledge"));
        swot2.setWeaknesses(Arrays.asList("Lacks practical application skills"));
        swot2.setOpportunities(Arrays.asList("More lab sessions", "Hands-on experiments"));
        swot2.setThreats(Arrays.asList("Overconfidence in theory"));
        swot2.setAiRecommendations("Balance theory with practical applications");

        swotRepository.saveAll(Arrays.asList(swot1, swot2));

        System.out.println("Sample data initialized successfully!");
        System.out.println("Students: " + students.size());
        System.out.println("Teachers: " + teachers.size());
        System.out.println("Meetings: " + meetings.size());
    }
}