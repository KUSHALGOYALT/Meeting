package com.example.learn.exception;

public class OfflineSyncException extends RuntimeException {
    public OfflineSyncException(String message) {
        super(message);
    }
}