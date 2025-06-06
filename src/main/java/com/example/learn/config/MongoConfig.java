package com.example.learn.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.mongodb.core.convert.MongoCustomConversions;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

@Configuration
public class MongoConfig {
    @Bean
    public MongoCustomConversions customConversions() {
        return new MongoCustomConversions(List.of(
                new Converter<List<?>, Vector<?>>() {
                    @Override
                    public Vector<?> convert(List<?> source) {
                        return new Vector<>(source);
                    }
                },
                new Converter<Vector<?>, List<?>>() {
                    @Override
                    public List<?> convert(Vector<?> source) {
                        return new ArrayList<>(source);
                    }
                }
        ));
    }
}