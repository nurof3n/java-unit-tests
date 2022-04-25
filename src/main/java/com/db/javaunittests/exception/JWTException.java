package com.db.javaunittests.exception;

public class JWTException extends Exception {
    public JWTException(String message) {
        super("JWT token exception: " + message);
    }
}
