package com.hexaware.carconnect.exception;
import java.io.Serializable;
public class ReservationException extends Exception implements Serializable {
    private static final long serialVersionUID = 1L;
    public ReservationException(String message) {
        super(message);
    }
}
