package com.banking.interfaces.exception;

import com.banking.interfaces.dto.response.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import java.util.ArrayList;
import java.util.List;

/**
 * Handler global para tratamento de exceções da API.
 * Centraliza o tratamento de erros e padroniza as respostas.
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Trata erros de validação de dados de entrada
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<Object>> handleValidationExceptions(
            MethodArgumentNotValidException ex, WebRequest request) {
        
        List<String> errors = new ArrayList<>();
        for (FieldError error : ex.getBindingResult().getFieldErrors()) {
            errors.add(error.getField() + ": " + error.getDefaultMessage());
        }
        
        ApiResponse<Object> response = ApiResponse.error("Dados de entrada inválidos", errors);
        response.setStatusCode(HttpStatus.BAD_REQUEST.value());
        response.setPath(request.getDescription(false).replace("uri=", ""));
        
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    /**
     * Trata argumentos ilegais (dados de negócio inválidos)
     */
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ApiResponse<Object>> handleIllegalArgumentException(
            IllegalArgumentException ex, WebRequest request) {
        
        ApiResponse<Object> response = ApiResponse.error(ex.getMessage());
        response.setStatusCode(HttpStatus.BAD_REQUEST.value());
        response.setPath(request.getDescription(false).replace("uri=", ""));
        
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    /**
     * Trata estados ilegais (regras de negócio violadas)
     */
    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<ApiResponse<Object>> handleIllegalStateException(
            IllegalStateException ex, WebRequest request) {
        
        ApiResponse<Object> response = ApiResponse.error(ex.getMessage());
        response.setStatusCode(HttpStatus.UNPROCESSABLE_ENTITY.value());
        response.setPath(request.getDescription(false).replace("uri=", ""));
        
        return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(response);
    }

    /**
     * Trata recursos não encontrados
     */
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ApiResponse<Object>> handleRuntimeException(
            RuntimeException ex, WebRequest request) {
        
        // Verifica se é uma exceção de "não encontrado" baseada na mensagem
        if (ex.getMessage() != null && 
            (ex.getMessage().toLowerCase().contains("not found") || 
             ex.getMessage().toLowerCase().contains("não encontrad"))) {
            
            ApiResponse<Object> response = ApiResponse.error(ex.getMessage());
            response.setStatusCode(HttpStatus.NOT_FOUND.value());
            response.setPath(request.getDescription(false).replace("uri=", ""));
            
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
        
        // Para outras RuntimeExceptions, retorna erro interno
        ApiResponse<Object> response = ApiResponse.error("Erro interno do servidor");
        response.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
        response.setPath(request.getDescription(false).replace("uri=", ""));
        
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }

    /**
     * Trata exceções genéricas não mapeadas
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<Object>> handleGenericException(
            Exception ex, WebRequest request) {
        
        // Verifica se é uma exceção de conflito baseada na mensagem
        if (ex.getMessage() != null && 
            (ex.getMessage().toLowerCase().contains("duplicate") || 
             ex.getMessage().toLowerCase().contains("já existe") ||
             ex.getMessage().toLowerCase().contains("conflict"))) {
            
            ApiResponse<Object> response = ApiResponse.error(ex.getMessage());
            response.setStatusCode(HttpStatus.CONFLICT.value());
            response.setPath(request.getDescription(false).replace("uri=", ""));
            
            return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
        }
        
        ApiResponse<Object> response = ApiResponse.error("Erro interno do servidor");
        response.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
        response.setPath(request.getDescription(false).replace("uri=", ""));
        
        // Log do erro para debugging (em produção usar logger apropriado)
        System.err.println("Erro não tratado: " + ex.getClass().getSimpleName() + " - " + ex.getMessage());
        ex.printStackTrace();
        
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }
}