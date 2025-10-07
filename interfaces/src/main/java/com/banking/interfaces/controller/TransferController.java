package com.banking.interfaces.controller;

import com.banking.application.transfer.command.ProcessTransferCommand;
import com.banking.application.transfer.command.ProcessTransferHandler;
import com.banking.interfaces.dto.request.TransferRequest;
import com.banking.interfaces.dto.response.ApiResponse;
import com.banking.interfaces.dto.response.TransferResponse;
import com.banking.interfaces.mapper.InterfaceMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Controller REST para gerenciamento de transferências bancárias
 * Implementa operações de transferência seguindo padrões CQRS
 */
@RestController
@RequestMapping("/api/v1/transfers")
@Tag(name = "Transferências", description = "API para gerenciamento de transferências bancárias")
public class TransferController {

    @Autowired
    private ProcessTransferHandler processTransferHandler;

    @Autowired
    private InterfaceMapper interfaceMapper;

    /**
     * Processa uma nova transferência bancária
     */
    @PostMapping
    @Operation(summary = "Processar transferência", 
               description = "Processa uma nova transferência bancária entre contas")
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "201", description = "Transferência processada com sucesso"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Dados inválidos"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "409", description = "Transferência duplicada (idempotência)"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "422", description = "Saldo insuficiente ou conta inválida")
    })
    public ResponseEntity<ApiResponse<TransferResponse>> processTransfer(
            @Valid @RequestBody TransferRequest request) {
        
        try {
            var transferDTO = interfaceMapper.toTransferDTO(request);
            var command = ProcessTransferCommand.from(transferDTO);
            var applicationResponse = processTransferHandler.handle(command);
            
            // Convert application DTO to interface DTO
            var response = interfaceMapper.fromApplication(applicationResponse);
            
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(ApiResponse.success("Transferência processada com sucesso", response));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponse.error("Dados inválidos: " + e.getMessage()));
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY)
                    .body(ApiResponse.error("Erro de negócio: " + e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("Erro interno: " + e.getMessage()));
        }
    }

    /**
     * Endpoint de health check
     */
    @GetMapping("/health")
    @Operation(summary = "Health check", description = "Verifica se o serviço de transferências está funcionando")
    public ResponseEntity<ApiResponse<String>> healthCheck() {
        return ResponseEntity.ok(ApiResponse.success("Serviço de transferências funcionando corretamente"));
    }
}