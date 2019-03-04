package io.github.ghacupha.fassets.service.dto;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Objects;

/**
 * A DTO for the BankAccount entity.
 */
public class BankAccountDTO implements Serializable {

    private Long id;

    @NotNull
    private String accountName;

    @NotNull
    private String accountNumber;

    @NotNull
    private BigDecimal accountBalance;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public BigDecimal getAccountBalance() {
        return accountBalance;
    }

    public void setAccountBalance(BigDecimal accountBalance) {
        this.accountBalance = accountBalance;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        BankAccountDTO bankAccountDTO = (BankAccountDTO) o;
        if (bankAccountDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), bankAccountDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "BankAccountDTO{" +
            "id=" + getId() +
            ", accountName='" + getAccountName() + "'" +
            ", accountNumber='" + getAccountNumber() + "'" +
            ", accountBalance=" + getAccountBalance() +
            "}";
    }
}
