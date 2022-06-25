package com.shashi.bol.mancala.web.game.v1.exceptions;

public class ApiConnectionException extends RuntimeException {
    public ApiConnectionException(String message, Exception cause) {
        super(message, cause);
    }
    public ApiConnectionException(String message) {
        super(message);
    }
}
