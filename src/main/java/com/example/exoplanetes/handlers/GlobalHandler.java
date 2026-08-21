package com.example.exoplanetes.handlers;

import com.example.exoplanetes.exceptions.ObservatoireNotFound;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.*;
import org.springframework.lang.Nullable;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestControllerAdvice
public class GlobalHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(ObservatoireNotFound.class)
    public ProblemDetail ResourceNotFound(Exception e) {
        return ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, e.getMessage());
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ProblemDetail ConflictDataIntegrity(Exception e) {
        return ProblemDetail.forStatusAndDetail(HttpStatus.CONFLICT, "This resource already exist.");
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
                                                                  HttpHeaders headers,
                                                                  HttpStatusCode status,
                                                                  WebRequest request) {

        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, "One or more fields are missing.");
        List<FieldError> fields = ex.getBindingResult().getFieldErrors();
        Map<String, String> mapField = new HashMap<>();

        for(FieldError field : fields) {
            mapField.put(field.getField(), field.getDefaultMessage());
        }

        problemDetail.setProperty("errors", mapField);
        return handleExceptionInternal(ex, problemDetail, headers, status, request);
    }
}
