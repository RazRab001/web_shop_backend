package project.onlineshop.utils.exceptions;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@ControllerAdvice
public class ApiExceptionHandler {
    HttpStatus badRequest = HttpStatus.BAD_REQUEST;
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Object> handleValidationExceptions(
            MethodArgumentNotValidException ex) {
        List<ApiException> errors = new ArrayList<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            errors.add(new ApiException(error.getDefaultMessage(),
                    badRequest,
                    ZonedDateTime.now(ZoneId.of("Z"))));
        });
        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler(ApiRequestException.class)
    public ResponseEntity<Object> handleApiRequestExceptions(ApiRequestException ex) {

        ApiException apiException = new ApiException(
                ex.getMessage(),
                badRequest,
                ZonedDateTime.now(ZoneId.of("Z"))
        );

        return new ResponseEntity<>(apiException, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<Object> handleConstraintViolationException(ConstraintViolationException ex) {
        List<ApiException> errors = new ArrayList<>();
        ex.getConstraintViolations().forEach((error) -> {
            errors.add(new ApiException(error.getMessage(),
                    badRequest,
                    ZonedDateTime.now(ZoneId.of("Z"))));
        });
        return ResponseEntity.badRequest().body(errors);
    }

    @ExceptionHandler(BindException.class)
    public ResponseEntity<Object> handleBindException(BindException ex) {
        List<ApiException> errors = new ArrayList<>();
        ex.getFieldErrors().forEach((error) -> {
            errors.add(new ApiException(error.getDefaultMessage(),
                    badRequest,
                    ZonedDateTime.now(ZoneId.of("Z"))));
        });
        return ResponseEntity.badRequest().body(errors);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<Object> handleMethodArgumentTypeMismatch(MethodArgumentTypeMismatchException ex) {
        ApiException apiException = new ApiException(
                "Invalid argument type: " + ex.getName(),
                badRequest,
                ZonedDateTime.now(ZoneId.of("Z"))
        );
        return ResponseEntity.badRequest().body(apiException);
    }
}
