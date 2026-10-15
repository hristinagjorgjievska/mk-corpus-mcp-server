package mk.ukim.finki.mcptoolbackend.web.handler;

import mk.ukim.finki.mcptoolbackend.model.exception.DonationBatchNotFoundException;
import mk.ukim.finki.mcptoolbackend.model.exception.InvalidDonationStateException;
import mk.ukim.finki.mcptoolbackend.model.exception.VezilkaIntegrationException;
import mk.ukim.finki.mcptoolbackend.web.controller.DonationController;
import mk.ukim.finki.mcptoolbackend.web.dto.ApiError;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice(assignableTypes = DonationController.class)
public class DonationControllerExceptionHandler {
    @ExceptionHandler(DonationBatchNotFoundException.class)
    public ResponseEntity<ApiError> handleNotFound(DonationBatchNotFoundException exception) {
        return ResponseEntity
            .status(HttpStatus.NOT_FOUND)
            .body(ApiError.of(HttpStatus.NOT_FOUND, exception.getMessage()));
    }

    @ExceptionHandler(InvalidDonationStateException.class)
    public ResponseEntity<ApiError> handleInvalidState(InvalidDonationStateException exception) {
        return ResponseEntity
            .status(HttpStatus.CONFLICT)
            .body(ApiError.of(HttpStatus.CONFLICT, exception.getMessage()));
    }

    @ExceptionHandler(VezilkaIntegrationException.class)
    public ResponseEntity<ApiError> handleVezilka(VezilkaIntegrationException exception) {
        return ResponseEntity
            .status(HttpStatus.BAD_GATEWAY)
            .body(ApiError.of(HttpStatus.BAD_GATEWAY, exception.getMessage()));
    }
}
