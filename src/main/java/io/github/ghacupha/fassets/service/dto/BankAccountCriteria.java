package io.github.ghacupha.fassets.service.dto;

import java.io.Serializable;
import java.util.Objects;
import io.github.jhipster.service.filter.BooleanFilter;
import io.github.jhipster.service.filter.DoubleFilter;
import io.github.jhipster.service.filter.Filter;
import io.github.jhipster.service.filter.FloatFilter;
import io.github.jhipster.service.filter.IntegerFilter;
import io.github.jhipster.service.filter.LongFilter;
import io.github.jhipster.service.filter.StringFilter;
import io.github.jhipster.service.filter.BigDecimalFilter;

/**
 * Criteria class for the BankAccount entity. This class is used in BankAccountResource to
 * receive all the possible filtering options from the Http GET request parameters.
 * For example the following could be a valid requests:
 * <code> /bank-accounts?id.greaterThan=5&amp;attr1.contains=something&amp;attr2.specified=false</code>
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class BankAccountCriteria implements Serializable {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter accountName;

    private StringFilter accountNumber;

    private BigDecimalFilter accountBalance;

    private LongFilter categoryId;

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getAccountName() {
        return accountName;
    }

    public void setAccountName(StringFilter accountName) {
        this.accountName = accountName;
    }

    public StringFilter getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(StringFilter accountNumber) {
        this.accountNumber = accountNumber;
    }

    public BigDecimalFilter getAccountBalance() {
        return accountBalance;
    }

    public void setAccountBalance(BigDecimalFilter accountBalance) {
        this.accountBalance = accountBalance;
    }

    public LongFilter getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(LongFilter categoryId) {
        this.categoryId = categoryId;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final BankAccountCriteria that = (BankAccountCriteria) o;
        return
            Objects.equals(id, that.id) &&
            Objects.equals(accountName, that.accountName) &&
            Objects.equals(accountNumber, that.accountNumber) &&
            Objects.equals(accountBalance, that.accountBalance) &&
            Objects.equals(categoryId, that.categoryId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id,
        accountName,
        accountNumber,
        accountBalance,
        categoryId
        );
    }

    @Override
    public String toString() {
        return "BankAccountCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (accountName != null ? "accountName=" + accountName + ", " : "") +
                (accountNumber != null ? "accountNumber=" + accountNumber + ", " : "") +
                (accountBalance != null ? "accountBalance=" + accountBalance + ", " : "") +
                (categoryId != null ? "categoryId=" + categoryId + ", " : "") +
            "}";
    }

}
