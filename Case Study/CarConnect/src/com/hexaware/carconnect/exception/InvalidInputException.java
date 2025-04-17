package com.hexaware.carconnect.exception;
import java.io.Serializable;
public class InvalidInputException extends Exception implements Serializable {
    private static final long serialVersionUID = 1L;
    public InvalidInputException(String message) {
        super(message);
    }
}
