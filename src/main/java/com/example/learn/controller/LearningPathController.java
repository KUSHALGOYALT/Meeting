package com.example.learn.controller;

import com.example.learn.service.SessionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class LearningPathController {
    private final SessionService sessionService;

    @Autowired
    public LearningPathController(SessionService sessionService) {
        this.sessionService = sessionService;
    }

    @GetMapping("/learning-path/{sessionId}")
    public EntityModel<String> getLearningPath(@PathVariable String sessionId) {
        // Example: Generate a quiz as a learning path
        List<String> quiz = sessionService.generateSessionQuiz(sessionId, "student1");
        String path = String.join("\n", quiz);
        EntityModel<String> model = EntityModel.of(path);
        model.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(LearningPathController.class).getLearningPath(sessionId)).withSelfRel());
        return model;
    }
}