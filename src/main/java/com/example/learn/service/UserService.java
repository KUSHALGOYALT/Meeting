package com.example.learn.service;
import com.example.learn.model.*;
import com.example.learn.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public List<User> getStudents() {
        return userRepository.findByRole("STUDENT");
    }

    public List<User> getTeachers() {
        return userRepository.findByRole("TEACHER");
    }

    public User createUser(User user) {
        return userRepository.save(user);
    }

    public Optional<User> getUserById(String id) {
        return userRepository.findById(id);
    }
}

