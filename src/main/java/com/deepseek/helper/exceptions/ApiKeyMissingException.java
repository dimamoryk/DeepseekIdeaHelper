package com.deepseek.helper.service.exceptions;

public class ApiKeyMissingException extends
        DeepSeekException {

    public ApiKeyMissingException(String message) {
        super(message);
    }

    public ApiKeyMissingException(String message, Throwable caause) {
        super(message, caause);
    }
}
