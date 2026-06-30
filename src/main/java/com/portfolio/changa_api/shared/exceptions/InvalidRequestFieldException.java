package com.portfolio.changa_api.shared.exceptions;

public class InvalidRequestFieldException extends RuntimeException {
    public InvalidRequestFieldException(String message) {
        super(message);
    }
}
