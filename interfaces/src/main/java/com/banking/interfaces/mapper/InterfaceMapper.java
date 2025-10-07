package com.banking.interfaces.mapper;

import com.banking.application.transfer.dto.ProcessTransferRequest;
import com.banking.domain.account.entity.Account;
import com.banking.domain.transfer.entity.Transfer;
import com.banking.interfaces.dto.request.CreateAccountRequest;
import com.banking.interfaces.dto.request.TransferRequest;
import com.banking.interfaces.dto.request.CreateAccountRequest;
import com.banking.interfaces.dto.response.AccountResponse;
import com.banking.interfaces.dto.response.TransferResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

/**
 * Mapper para conversão entre DTOs de interface e DTOs de aplicação.
 * Usa MapStruct para geração automática de código de mapeamento.
 */
@Mapper(componentModel = "spring")
public interface InterfaceMapper {

    InterfaceMapper INSTANCE = Mappers.getMapper(InterfaceMapper.class);

    // Account mappings
    
    /**
     * Converte CreateAccountRequest para CreateAccountRequest (aplicação)
     */
    com.banking.application.account.dto.CreateAccountRequest toCreateAccountDTO(CreateAccountRequest request);

    /**
     * Converte Account (domínio) para AccountResponse
     */
        @Mapping(source = "id.value", target = "id")
    @Mapping(source = "accountNumber.value", target = "accountNumber")
    @Mapping(source = "holderName.value", target = "holderName")
    @Mapping(source = "holderCpf.value", target = "holderCpf")
    @Mapping(source = "balance.amount", target = "balance")
    @Mapping(source = "active", target = "isActive")
    @Mapping(source = "createdAt", target = "createdAt")
    AccountResponse toAccountResponse(Account account);

    // Transfer mappings
    
    /**
     * Converte TransferRequest para ProcessTransferRequest
     */
    ProcessTransferRequest toTransferDTO(com.banking.interfaces.dto.request.TransferRequest request);

    /**
     * Converte Transfer (domínio) para TransferResponse (versão simples)
     */
    @Mapping(source = "id.value", target = "id")
    @Mapping(source = "fromAccountId.value", target = "fromAccountId")
    @Mapping(source = "toAccountId.value", target = "toAccountId")
    @Mapping(source = "amount.value", target = "amount")
    @Mapping(source = "idempotencyKey.value", target = "idempotencyKey")
    @Mapping(source = "status", target = "status")
    com.banking.interfaces.dto.response.TransferResponse toTransferResponse(Transfer transfer);

    /**
     * Converte Transfer (domínio) para TransferResponse (versão completa com dados das contas)
     */
    @Mapping(source = "transfer.id.value", target = "id")
    @Mapping(source = "transfer.fromAccountId.value", target = "fromAccountId")
    @Mapping(source = "fromAccount.accountNumber.value", target = "fromAccountNumber")
    @Mapping(source = "fromAccount.holderName.value", target = "fromAccountHolderName")
    @Mapping(source = "transfer.toAccountId.value", target = "toAccountId")
    @Mapping(source = "toAccount.accountNumber.value", target = "toAccountNumber")
    @Mapping(source = "toAccount.holderName.value", target = "toAccountHolderName")
    @Mapping(source = "transfer.amount.value", target = "amount")
    @Mapping(source = "transfer.description", target = "description")
    @Mapping(source = "transfer.idempotencyKey.value", target = "idempotencyKey")
    @Mapping(source = "transfer.status", target = "status")
    @Mapping(source = "transfer.createdAt", target = "createdAt")
    com.banking.interfaces.dto.response.TransferResponse toTransferResponseWithAccounts(Transfer transfer, Account fromAccount, Account toAccount);

    // Value object mappings
    
    /**
     * Converte TransferId para String
     */
    default String map(com.banking.domain.transfer.valueobject.TransferId transferId) {
        return transferId != null ? transferId.getValue() : null;
    }

    /**
     * Converte AccountId para String
     */
    default String map(com.banking.domain.account.valueobject.AccountId accountId) {
        return accountId != null ? accountId.getValue() : null;
    }

    /**
     * Converte HolderName para String
     */
    default String map(com.banking.domain.account.valueobject.HolderName holderName) {
        return holderName != null ? holderName.getValue() : null;
    }

    // Conversion from Application DTOs to Interface DTOs
    
    /**
     * Converte AccountResponse da aplicação para AccountResponse da interface
     */
    @Mapping(source = "active", target = "isActive")
    @Mapping(source = "createdAt", target = "createdAt")
    @Mapping(target = "holderCpf", source = "holderCpf") // Direct mapping
    AccountResponse fromApplication(com.banking.application.account.dto.AccountResponse applicationResponse);

    /**
     * Converte TransferResponse da aplicação para TransferResponse da interface
     */
    @Mapping(source = "status", target = "status", qualifiedByName = "transferStatusToString")
    com.banking.interfaces.dto.response.TransferResponse fromApplication(com.banking.application.transfer.dto.TransferResponse applicationResponse);

    /**
     * Converte TransferStatus para String
     */
    @Named("transferStatusToString")
    default String transferStatusToString(com.banking.domain.transfer.valueobject.TransferStatus status) {
        return status != null ? status.name() : null;
    }

    /**
     * Converte status de Transfer (enum do domínio) para String
     */
    default String mapTransferStatus(com.banking.domain.transfer.valueobject.TransferStatus status) {
        return status != null ? status.name() : null;
    }
}