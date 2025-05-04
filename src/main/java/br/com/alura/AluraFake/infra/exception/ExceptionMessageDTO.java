package br.com.alura.AluraFake.infra.exception;

import org.springframework.validation.FieldError;

public record ExceptionMessageDTO(String field, String message) {
    public ExceptionMessageDTO(FieldError error) {
        this(error.getField(), error.getDefaultMessage());
    }
}
