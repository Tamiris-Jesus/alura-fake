package br.com.alura.AluraFake.infra.exception;

import com.auth0.jwt.exceptions.JWTDecodeException;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class RestExceptionHandler {

    @ExceptionHandler(ValidationException.class)
    public ResponseEntity handleBusinessRuleException(ValidationException ex) {
        return ResponseEntity.badRequest().body(ex.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity handleValidationError(MethodArgumentNotValidException ex) {
        var errors = ex.getFieldErrors();
        return ResponseEntity.badRequest().body(errors.stream().map(ExceptionMessageDTO::new).toList());
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity handleNotFoundException() {
        return ResponseEntity.notFound().build();
    }

    @ExceptionHandler(JWTDecodeException.class)
    public ResponseEntity handleJwtExpiredException(JWTDecodeException ex) {
        return ResponseEntity.badRequest().body(ex.getMessage());
    }


    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity handleInvalidFormatException(HttpMessageNotReadableException ex) {
        return ResponseEntity.badRequest().body("Requisição mal formatada: " + ex.getMessage());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity handleInternalServerError(Exception ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro interno: " + ex.getLocalizedMessage());
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity handleForeignKeyViolation(Exception ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                "Ocorreu um erro de integridade. Verifique os valores das chaves estrangeiras enviadas: " + ex.getLocalizedMessage());
    }
}
