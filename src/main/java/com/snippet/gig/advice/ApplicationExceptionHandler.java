package com.snippet.gig.advice;

import com.snippet.gig.exception.AlreadyExistsException;
import com.snippet.gig.exception.BadRequestException;
import com.snippet.gig.exception.EmailDeliveringException;
import com.snippet.gig.exception.ResourceNotFoundException;
import com.snippet.gig.response.ErrorResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

import static org.springframework.http.HttpStatus.*;

@RestControllerAdvice
public class ApplicationExceptionHandler {

    @ResponseStatus(BAD_REQUEST)
    @ExceptionHandler({MethodArgumentNotValidException.class})
    public ResponseEntity<ErrorResponse> handleInvalidArgument(MethodArgumentNotValidException exception) {
        Map<String, String> errorMap = new HashMap<>();
        exception.getBindingResult().getFieldErrors().forEach(
                fieldError -> {
                    errorMap.put(fieldError.getField(), fieldError.getDefaultMessage());
                }
        );

        return ResponseEntity
                .status(BAD_REQUEST)
                .body(new ErrorResponse(BAD_REQUEST.value(), "Methods argument not valid", errorMap));
    }

    @ResponseStatus(NOT_FOUND)
    @ExceptionHandler({ResourceNotFoundException.class})
    public ResponseEntity<ErrorResponse> handleResourceNotFound(ResourceNotFoundException exception) {
        return ResponseEntity
                .status(NOT_FOUND)
                .body(new ErrorResponse(NOT_FOUND.value(), exception.getMessage()));
    }

    @ResponseStatus(FORBIDDEN)
    @ExceptionHandler({AlreadyExistsException.class})
    public ResponseEntity<ErrorResponse> handleResourceAlreadyExist(AlreadyExistsException exception) {
        return ResponseEntity
                .status(FORBIDDEN)
                .body(new ErrorResponse(FORBIDDEN.value(), exception.getMessage()));
    }

    @ResponseStatus(BAD_REQUEST)
    @ExceptionHandler({BadRequestException.class})
    public ResponseEntity<ErrorResponse> handleBadRequest(BadRequestException exception) {
        return ResponseEntity
                .status(BAD_REQUEST)
                .body(new ErrorResponse(BAD_REQUEST.value(), exception.getMessage()));
    }

    @ResponseStatus(INTERNAL_SERVER_ERROR)
    @ExceptionHandler({EmailDeliveringException.class})
    public ResponseEntity<ErrorResponse> handleEmailDeliveringException(EmailDeliveringException exception) {
        return ResponseEntity
                .status(INTERNAL_SERVER_ERROR)
                .body(new ErrorResponse(INTERNAL_SERVER_ERROR.value(), exception.getMessage()));
    }

    @ResponseStatus(INTERNAL_SERVER_ERROR)
    @ExceptionHandler({Exception.class})
    public ResponseEntity<ErrorResponse> globalExceptionHandle(Exception exception) {
        return ResponseEntity
                .status(INTERNAL_SERVER_ERROR)
                .body(new ErrorResponse(INTERNAL_SERVER_ERROR.value(), exception.getMessage()));
    }
}
