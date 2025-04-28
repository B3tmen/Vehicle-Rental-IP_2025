package org.unibl.etf.carrentalbackend.exception;

import io.jsonwebtoken.ExpiredJwtException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.unibl.etf.carrentalbackend.util.CustomLogger;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    public ResponseEntity<Object> handleValidationExceptions(MethodArgumentNotValidException exception){
        Map<String, String> errors = new HashMap<>();
        exception.getBindingResult().getAllErrors().forEach((error) -> {
           String fieldName = ((FieldError) error).getField();
           String errorMsg = error.getDefaultMessage();
           errors.put(fieldName, errorMsg);
        });

        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ExpiredJwtException.class)
    public ResponseEntity<?> handleExpiredJwtException(ExpiredJwtException ex){
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(UserNotActiveException.class)
    public ResponseEntity<?> handleUserNotActiveException(UserNotActiveException ex){
        CustomLogger.getInstance().warning("UserNotActiveException: " + ex.getMessage());
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(UnauthorizedException.class)
    public ResponseEntity<Object> handleUnauthorizedException(UnauthorizedException ex){
        CustomLogger.getInstance().warning("UnauthorizedException: " + ex.getMessage());
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<?> handleResourceNotFoundException(ResourceNotFoundException ex){
        CustomLogger.getInstance().warning("ResourceNotFoundException: " + ex.getMessage());
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(UsernameConflictException.class)
    public ResponseEntity<?> handleUsernameConflictException(UsernameConflictException ex){
        CustomLogger.getInstance().warning("UsernameConflictException: " + ex.getMessage());
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.CONFLICT);
    }

    @ExceptionHandler(PhoneNumberConflictException.class)
    public ResponseEntity<?> handlePhoneNumberConflictException(PhoneNumberConflictException ex){
        CustomLogger.getInstance().warning("PhoneNumberConflictException: " + ex.getMessage());
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.CONFLICT);
    }

    @ExceptionHandler(PersonalCardNumberConflictException.class)
    public ResponseEntity<?> handlePersonalCardIdConflictException(PersonalCardNumberConflictException ex){
        CustomLogger.getInstance().warning("PersonalCardIdConflictException: " + ex.getMessage());
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.CONFLICT);
    }

    @ExceptionHandler(CustomVehicleIdConflictException.class)
    public ResponseEntity<?> handleCustomVehicleIdConflictException(CustomVehicleIdConflictException ex){
        CustomLogger.getInstance().warning("CustomVehicleIdConflictException: " + ex.getMessage());
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.CONFLICT);
    }

    @ExceptionHandler(InvalidCsvFormatException.class)
    public ResponseEntity<?> handleInvalidCsvFormatException(InvalidCsvFormatException ex){
        CustomLogger.getInstance().warning("InvalidCsvFormatException: " + ex.getMessage());
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(InvalidDateTimeFormatException.class)
    public ResponseEntity<?> handleInvalidDateTimeFormatException(InvalidDateTimeFormatException ex){
        CustomLogger.getInstance().warning("InvalidDateTimeFormatException: " + ex.getMessage());
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(EmailConflictException.class)
    public ResponseEntity<?> handleEmailConflictException(EmailConflictException ex){
        CustomLogger.getInstance().warning("EmailConflictException: " + ex.getMessage());
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.CONFLICT);
    }
}
