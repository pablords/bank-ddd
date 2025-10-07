package com.banking.interfaces.controller;

import com.banking.application.account.query.GetAccountQuery;
import com.banking.application.account.query.GetAccountHandler;
import com.banking.application.account.command.CreateAccountCommand;
import com.banking.application.account.command.CreateAccountHandler;
import com.banking.domain.account.entity.Account;
import com.banking.interfaces.dto.request.CreateAccountRequest;
import com.banking.interfaces.dto.response.AccountResponse;
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
 * Controller REST para operações de contas bancárias.
 * Fornece endpoints para criação, consulta e gerenciamento de contas.
 */
@RestController
@RequestMapping("/api/v1/accounts")
@Tag(name = "Accounts", description = "Operações relacionadas a contas bancárias")
public class AccountController {

    private final CreateAccountHandler createAccountHandler;
    private final GetAccountHandler getAccountHandler;
    private final InterfaceMapper interfaceMapper;

    @Autowired
    public AccountController(CreateAccountHandler createAccountHandler,
                           GetAccountHandler getAccountHandler,
                           InterfaceMapper interfaceMapper) {
        this.createAccountHandler = createAccountHandler;
        this.getAccountHandler = getAccountHandler;
        this.interfaceMapper = interfaceMapper;
    }

    /**
     * Cria uma nova conta bancária
     */
    @PostMapping
    @Operation(summary = "Criar nova conta", 
               description = "Cria uma nova conta bancária com os dados fornecidos")
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "201", description = "Conta criada com sucesso"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Dados inválidos"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "409", description = "CPF já cadastrado")
    })
    public ResponseEntity<ApiResponse<AccountResponse>> createAccount(
            @Valid @RequestBody CreateAccountRequest request) {
        
        try {
            var accountDTO = interfaceMapper.toCreateAccountDTO(request);
            var command = CreateAccountCommand.from(accountDTO);
            var applicationResponse = createAccountHandler.handle(command);
            
            // Convert application DTO to interface DTO
            var response = interfaceMapper.fromApplication(applicationResponse);
            
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(ApiResponse.success("Conta criada com sucesso", response));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponse.error("Erro ao criar conta: " + e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("Erro interno: " + e.getMessage()));
        }
    }

    /**
     * Busca uma conta por ID
     */
    /*
    @GetMapping("/{id}")
    @Operation(summary = "Buscar conta por ID", 
               description = "Retorna os dados de uma conta específica pelo seu ID")
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Conta encontrada"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Conta não encontrada")
    })
    public ResponseEntity<ApiResponse<AccountResponse>> getAccountById(
            @Parameter(description = "ID da conta") @PathVariable String id) {
        
        try {
            var query = new GetAccountByIdQuery(id);
            Account account = accountApplicationService.getAccountById(query);
            AccountResponse response = interfaceMapper.toAccountResponse(account);
            
            return ResponseEntity.ok(ApiResponse.success(response));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.error("Conta não encontrada: " + e.getMessage()));
        }
    }

    /**
     * Endpoint de health check
     */
    @GetMapping("/health")
    @Operation(summary = "Health check", description = "Verifica se o serviço de contas está funcionando")
    public ResponseEntity<ApiResponse<String>> healthCheck() {
        return ResponseEntity.ok(ApiResponse.success("Serviço de contas funcionando corretamente"));
    }
}