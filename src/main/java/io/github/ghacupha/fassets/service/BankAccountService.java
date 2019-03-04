package io.github.ghacupha.fassets.service;

import io.github.ghacupha.fassets.service.dto.BankAccountDTO;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing BankAccount.
 */
public interface BankAccountService {

    /**
     * Save a bankAccount.
     *
     * @param bankAccountDTO the entity to save
     * @return the persisted entity
     */
    BankAccountDTO save(BankAccountDTO bankAccountDTO);

    /**
     * Get all the bankAccounts.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<BankAccountDTO> findAll(Pageable pageable);
    /**
     * Get all the BankAccountDTO where Category is null.
     *
     * @return the list of entities
     */
    List<BankAccountDTO> findAllWhereCategoryIsNull();


    /**
     * Get the "id" bankAccount.
     *
     * @param id the id of the entity
     * @return the entity
     */
    Optional<BankAccountDTO> findOne(Long id);

    /**
     * Delete the "id" bankAccount.
     *
     * @param id the id of the entity
     */
    void delete(Long id);
}
