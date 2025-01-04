package com.trickytechies.foodapi.exception;

// BranchNotFoundException.java
public class BranchNotFoundException extends RuntimeException {
    public BranchNotFoundException(String message) {
        super(message);
    }
}
