package com.mabawa.nnpdairy.models;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;

import javax.persistence.EntityNotFoundException;
import java.util.List;

public class AdviceResponse {
    private HttpStatus status;
    private String message;
    private String debugMessage;

    public AdviceResponse(HttpStatus httpStatus) {
        this.status = httpStatus;
    }

    public AdviceResponse(HttpStatus conflict, String msg, Throwable cause) {
        this.status = conflict;
        this.message = msg;
        this.debugMessage = cause.getLocalizedMessage();
    }

    public AdviceResponse(HttpStatus notFound, EntityNotFoundException ex) {
        this.status = notFound;
    }

    public AdviceResponse(HttpStatus internalServerError, DataIntegrityViolationException ex) {
        this.status = internalServerError;
    }

    public HttpStatus getStatus() {
        return status;
    }
    public void setStatus(HttpStatus status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }
    public void setMessage(String message) {
        this.message = message;
    }

    public String getDebugMessage() {
        return debugMessage;
    }
    public void setDebugMessage(String debugMessage) {
        this.debugMessage = debugMessage;
    }

    public void addValidationErrors(List<FieldError> constraintViolations) {
    }
}
