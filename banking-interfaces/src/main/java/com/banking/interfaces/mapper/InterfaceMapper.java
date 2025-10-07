package com.banking.interfaces.mapper;

import com.banking.application.dto.CreateAccountDTO;
import com.banking.application.dto.TransferDTO;
import com.banking.domain.entity.Account;
import com.banking.domain.entity.Transfer;
import com.banking.interfaces.dto.request.CreateAccountRequest;
import com.banking.interfaces.dto.request.TransferRequest;
import com.banking.interfaces.dto.response.AccountResponse;
import com.banking.interfaces.dto.response.TransferResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
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
     * Converte CreateAccountRequest para CreateAccountDTO
     */
    CreateAccountDTO toCreateAccountDTO(CreateAccountRequest request);

    /**
     * Converte Account (domínio) para AccountResponse
     */
    @Mapping(source = "accountNumber.value", target = "accountNumber")
    @Mapping(source = "holderCpf.value", target = "holderCpf")
    @Mapping(source = "balance.value", target = "balance")
    AccountResponse toAccountResponse(Account account);

    // Transfer mappings
    
    /**
     * Converte TransferRequest para TransferDTO
     */
    TransferDTO toTransferDTO(TransferRequest request);

    /**
     * Converte Transfer (domínio) para TransferResponse (versão simples)
     */
    @Mapping(source = "amount.value", target = "amount")
    @Mapping(source = "idempotencyKey.value", target = "idempotencyKey")
    @Mapping(source = "status", target = "status")
    TransferResponse toTransferResponse(Transfer transfer);

    /**
     * Converte Transfer (domínio) para TransferResponse (versão completa com dados das contas)
     */
    @Mapping(source = "transfer.id", target = "id")
    @Mapping(source = "transfer.fromAccountId", target = "fromAccountId")
    @Mapping(source = "fromAccount.accountNumber.value", target = "fromAccountNumber")
    @Mapping(source = "fromAccount.holderName", target = "fromAccountHolderName")
    @Mapping(source = "transfer.toAccountId", target = "toAccountId")
    @Mapping(source = "toAccount.accountNumber.value", target = "toAccountNumber")
    @Mapping(source = "toAccount.holderName", target = "toAccountHolderName")
    @Mapping(source = "transfer.amount.value", target = "amount")
    @Mapping(source = "transfer.description", target = "description")
    @Mapping(source = "transfer.idempotencyKey.value", target = "idempotencyKey")
    @Mapping(source = "transfer.status", target = "status")
    @Mapping(source = "transfer.createdAt", target = "createdAt")
    TransferResponse toTransferResponseWithAccounts(Transfer transfer, Account fromAccount, Account toAccount);

    /**
     * Converte status de Transfer (enum do domínio) para String
     */
    default String mapTransferStatus(Transfer.TransferStatus status) {
        return status != null ? status.name() : null;
    }
}