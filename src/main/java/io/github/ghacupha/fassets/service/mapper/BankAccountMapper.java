package io.github.ghacupha.fassets.service.mapper;

import io.github.ghacupha.fassets.domain.*;
import io.github.ghacupha.fassets.service.dto.BankAccountDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity BankAccount and its DTO BankAccountDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface BankAccountMapper extends EntityMapper<BankAccountDTO, BankAccount> {


    @Mapping(target = "category", ignore = true)
    BankAccount toEntity(BankAccountDTO bankAccountDTO);

    default BankAccount fromId(Long id) {
        if (id == null) {
            return null;
        }
        BankAccount bankAccount = new BankAccount();
        bankAccount.setId(id);
        return bankAccount;
    }
}
