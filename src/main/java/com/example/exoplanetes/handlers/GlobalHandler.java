package com.example.exoplanetes.handlers;

import com.example.exoplanetes.exceptions.DesignationAlreadyExistsException;
import com.example.exoplanetes.exceptions.IllegalStatutTransitionException;
import com.example.exoplanetes.exceptions.NotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.http.*;
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

    private static final Logger logger = LoggerFactory.getLogger(GlobalHandler.class);

    @ExceptionHandler(NotFoundException.class)
    public ProblemDetail resourceNotFound(Exception e) {
        return ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, e.getMessage());
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ProblemDetail conflictDataIntegrity(Exception e) {
        return ProblemDetail.forStatusAndDetail(HttpStatus.CONFLICT, "This resource already exist.");
    }

    @ExceptionHandler(DesignationAlreadyExistsException.class)
    public ProblemDetail designationConflict(DesignationAlreadyExistsException e) {
        return ProblemDetail.forStatusAndDetail(HttpStatus.CONFLICT, e.getMessage());
    }

    @ExceptionHandler(IllegalStatutTransitionException.class)
    public ProblemDetail handleIllegalTransition(IllegalStatutTransitionException e) {
        return ProblemDetail.forStatusAndDetail(HttpStatus.UNPROCESSABLE_ENTITY, e.getMessage());
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
                                                                  HttpHeaders headers,
                                                                  HttpStatusCode status,
                                                                  WebRequest request) {

        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, "One or more fields are missing.");
        List<FieldError> fields = ex.getBindingResult().getFieldErrors();
        Map<String, String> mapField = new HashMap<>();

        for (FieldError field : fields) {
            mapField.put(field.getField(), field.getDefaultMessage());
        }

        problemDetail.setProperty("errors", mapField);
        return handleExceptionInternal(ex, problemDetail, headers, status, request);
    }

    @ExceptionHandler(OptimisticLockingFailureException.class)
    public ProblemDetail handleOptimisticLock(OptimisticLockingFailureException e) {
        return ProblemDetail.forStatusAndDetail(
                HttpStatus.CONFLICT,
                "The resource has been modified by another request. Reload and try again."
        );
    }

    @ExceptionHandler(Exception.class)
    public ProblemDetail handleDefaultException(Exception e) {
        logger.error("Unexpected error",e);
        return ProblemDetail.forStatusAndDetail(HttpStatus.INTERNAL_SERVER_ERROR, "An internal error has occurred");
    }
}
