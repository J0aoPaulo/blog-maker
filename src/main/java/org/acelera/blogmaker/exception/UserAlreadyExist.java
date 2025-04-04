package org.acelera.blogmaker.exception;

public class UserAlreadyExist extends RuntimeException {

    public UserAlreadyExist(String message) {
        super(message);
    }
}
