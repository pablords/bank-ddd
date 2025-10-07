package com.banking.interfaces.controller;

import com.banking.application.query.GetTransferByIdQuery;
import com.banking.application.query.GetTransferHistoryQuery;
import com.banking.application.service.TransferApplicationService;
import com.banking.domain.entity.Transfer;
import com.banking.interfaces.dto.request.TransferRequest;
import com.banking.interfaces.dto.response.TransferResponse;
import com.banking.interfaces.dto.response.ApiResponse;
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

import java.util.List;
import java.util.stream.Collectors;

/**
 * Controller REST para operações de transferências bancárias.
 * Fornece endpoints para processar e consultar transferências.
 */
@RestController
@RequestMapping("/api/v1/transfers")
@Tag(name = "Transfers", description = "Operações relacionadas a transferências bancárias")
public class TransferController {

    private final TransferApplicationService transferApplicationService;
    private final InterfaceMapper interfaceMapper;

    @Autowired
    public TransferController(TransferApplicationService transferApplicationService, 
                            InterfaceMapper interfaceMapper) {
        this.transferApplicationService = transferApplicationService;
        this.interfaceMapper = interfaceMapper;
    }

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
            Transfer transfer = transferApplicationService.processTransfer(transferDTO);
            TransferResponse response = interfaceMapper.toTransferResponse(transfer);
            
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
     * Busca uma transferência por ID
     */
    @GetMapping("/{id}")
    @Operation(summary = "Buscar transferência por ID", 
               description = "Retorna os dados de uma transferência específica pelo seu ID")
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Transferência encontrada"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Transferência não encontrada")
    })
    public ResponseEntity<ApiResponse<TransferResponse>> getTransferById(
            @Parameter(description = "ID da transferência") @PathVariable String id) {
        
        try {
            var query = new GetTransferByIdQuery(id);
            Transfer transfer = transferApplicationService.getTransferById(query);
            TransferResponse response = interfaceMapper.toTransferResponse(transfer);
            
            return ResponseEntity.ok(ApiResponse.success(response));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.error("Transferência não encontrada: " + e.getMessage()));
        }
    }

    /**
     * Busca histórico de transferências de uma conta
     */
    @GetMapping("/history/{accountId}")
    @Operation(summary = "Buscar histórico de transferências", 
               description = "Retorna o histórico de transferências de uma conta específica")
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Histórico retornado com sucesso"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Conta não encontrada")
    })
    public ResponseEntity<ApiResponse<List<TransferResponse>>> getTransferHistory(
            @Parameter(description = "ID da conta") @PathVariable String accountId) {
        
        try {
            var query = new GetTransferHistoryQuery(accountId);
            List<Transfer> transfers = transferApplicationService.getTransferHistory(query);
            List<TransferResponse> responses = transfers.stream()
                    .map(interfaceMapper::toTransferResponse)
                    .collect(Collectors.toList());
            
            return ResponseEntity.ok(ApiResponse.success(responses));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.error("Erro ao buscar histórico: " + e.getMessage()));
        }
    }

    /**
     * Busca transferências enviadas por uma conta
     */
    @GetMapping("/sent/{accountId}")
    @Operation(summary = "Buscar transferências enviadas", 
               description = "Retorna as transferências enviadas por uma conta específica")
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Transferências encontradas"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Conta não encontrada")
    })
    public ResponseEntity<ApiResponse<List<TransferResponse>>> getSentTransfers(
            @Parameter(description = "ID da conta") @PathVariable String accountId) {
        
        try {
            List<Transfer> transfers = transferApplicationService.getSentTransfers(accountId);
            List<TransferResponse> responses = transfers.stream()
                    .map(interfaceMapper::toTransferResponse)
                    .collect(Collectors.toList());
            
            return ResponseEntity.ok(ApiResponse.success(responses));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.error("Erro ao buscar transferências enviadas: " + e.getMessage()));
        }
    }

    /**
     * Busca transferências recebidas por uma conta
     */
    @GetMapping("/received/{accountId}")
    @Operation(summary = "Buscar transferências recebidas", 
               description = "Retorna as transferências recebidas por uma conta específica")
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Transferências encontradas"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Conta não encontrada")
    })
    public ResponseEntity<ApiResponse<List<TransferResponse>>> getReceivedTransfers(
            @Parameter(description = "ID da conta") @PathVariable String accountId) {
        
        try {
            List<Transfer> transfers = transferApplicationService.getReceivedTransfers(accountId);
            List<TransferResponse> responses = transfers.stream()
                    .map(interfaceMapper::toTransferResponse)
                    .collect(Collectors.toList());
            
            return ResponseEntity.ok(ApiResponse.success(responses));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.error("Erro ao buscar transferências recebidas: " + e.getMessage()));
        }
    }

    /**
     * Verifica status de uma transferência por chave de idempotência
     */
    @GetMapping("/idempotency/{idempotencyKey}")
    @Operation(summary = "Verificar status por idempotência", 
               description = "Verifica o status de uma transferência pela chave de idempotência")
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Status encontrado"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Transferência não encontrada")
    })
    public ResponseEntity<ApiResponse<TransferResponse>> getTransferByIdempotencyKey(
            @Parameter(description = "Chave de idempotência") @PathVariable String idempotencyKey) {
        
        try {
            Transfer transfer = transferApplicationService.getTransferByIdempotencyKey(idempotencyKey);
            TransferResponse response = interfaceMapper.toTransferResponse(transfer);
            
            return ResponseEntity.ok(ApiResponse.success(response));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.error("Transferência não encontrada: " + e.getMessage()));
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