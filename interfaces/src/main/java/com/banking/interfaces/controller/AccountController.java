package com.banking.interfaces.controller;

import com.banking.application.query.GetAccountByIdQuery;
import com.banking.application.query.GetAccountByNumberQuery;
import com.banking.application.query.GetAllAccountsQuery;
import com.banking.application.service.AccountApplicationService;
import com.banking.domain.entity.Account;
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

    private final AccountApplicationService accountApplicationService;
    private final InterfaceMapper interfaceMapper;

    @Autowired
    public AccountController(AccountApplicationService accountApplicationService, 
                           InterfaceMapper interfaceMapper) {
        this.accountApplicationService = accountApplicationService;
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
            var createAccountDTO = interfaceMapper.toCreateAccountDTO(request);
            Account account = accountApplicationService.createAccount(createAccountDTO);
            AccountResponse response = interfaceMapper.toAccountResponse(account);
            
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(ApiResponse.success("Conta criada com sucesso", response));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponse.error("Erro ao criar conta: " + e.getMessage()));
        }
    }

    /**
     * Busca uma conta por ID
     */
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
     * Busca uma conta por número
     */
    @GetMapping("/number/{accountNumber}")
    @Operation(summary = "Buscar conta por número", 
               description = "Retorna os dados de uma conta específica pelo seu número")
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Conta encontrada"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Conta não encontrada")
    })
    public ResponseEntity<ApiResponse<AccountResponse>> getAccountByNumber(
            @Parameter(description = "Número da conta") @PathVariable String accountNumber) {
        
        try {
            var query = new GetAccountByNumberQuery(accountNumber);
            Account account = accountApplicationService.getAccountByNumber(query);
            AccountResponse response = interfaceMapper.toAccountResponse(account);
            
            return ResponseEntity.ok(ApiResponse.success(response));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.error("Conta não encontrada: " + e.getMessage()));
        }
    }

    /**
     * Lista todas as contas
     */
    @GetMapping
    @Operation(summary = "Listar todas as contas", 
               description = "Retorna a lista de todas as contas bancárias")
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Lista de contas retornada com sucesso")
    })
    public ResponseEntity<ApiResponse<List<AccountResponse>>> getAllAccounts() {
        
        try {
            var query = new GetAllAccountsQuery();
            List<Account> accounts = accountApplicationService.getAllAccounts(query);
            List<AccountResponse> responses = accounts.stream()
                    .map(interfaceMapper::toAccountResponse)
                    .collect(Collectors.toList());
            
            return ResponseEntity.ok(ApiResponse.success(responses));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("Erro ao buscar contas: " + e.getMessage()));
        }
    }

    /**
     * Busca contas ativas
     */
    @GetMapping("/active")
    @Operation(summary = "Listar contas ativas", 
               description = "Retorna a lista de contas bancárias ativas")
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Lista de contas ativas retornada com sucesso")
    })
    public ResponseEntity<ApiResponse<List<AccountResponse>>> getActiveAccounts() {
        
        try {
            List<Account> accounts = accountApplicationService.getActiveAccounts();
            List<AccountResponse> responses = accounts.stream()
                    .map(interfaceMapper::toAccountResponse)
                    .collect(Collectors.toList());
            
            return ResponseEntity.ok(ApiResponse.success(responses));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("Erro ao buscar contas ativas: " + e.getMessage()));
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