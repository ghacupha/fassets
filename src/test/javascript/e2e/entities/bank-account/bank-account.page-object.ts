import { element, by, ElementFinder } from 'protractor';

export class BankAccountComponentsPage {
    createButton = element(by.id('jh-create-entity'));
    deleteButtons = element.all(by.css('jhi-bank-account div table .btn-danger'));
    title = element.all(by.css('jhi-bank-account div h2#page-heading span')).first();

    async clickOnCreateButton() {
        await this.createButton.click();
    }

    async clickOnLastDeleteButton() {
        await this.deleteButtons.last().click();
    }

    async countDeleteButtons() {
        return this.deleteButtons.count();
    }

    async getTitle() {
        return this.title.getText();
    }
}

export class BankAccountUpdatePage {
    pageTitle = element(by.id('jhi-bank-account-heading'));
    saveButton = element(by.id('save-entity'));
    cancelButton = element(by.id('cancel-save'));
    accountNameInput = element(by.id('field_accountName'));
    accountNumberInput = element(by.id('field_accountNumber'));
    accountBalanceInput = element(by.id('field_accountBalance'));

    async getPageTitle() {
        return this.pageTitle.getText();
    }

    async setAccountNameInput(accountName) {
        await this.accountNameInput.sendKeys(accountName);
    }

    async getAccountNameInput() {
        return this.accountNameInput.getAttribute('value');
    }

    async setAccountNumberInput(accountNumber) {
        await this.accountNumberInput.sendKeys(accountNumber);
    }

    async getAccountNumberInput() {
        return this.accountNumberInput.getAttribute('value');
    }

    async setAccountBalanceInput(accountBalance) {
        await this.accountBalanceInput.sendKeys(accountBalance);
    }

    async getAccountBalanceInput() {
        return this.accountBalanceInput.getAttribute('value');
    }

    async save() {
        await this.saveButton.click();
    }

    async cancel() {
        await this.cancelButton.click();
    }

    getSaveButton(): ElementFinder {
        return this.saveButton;
    }
}

export class BankAccountDeleteDialog {
    private dialogTitle = element(by.id('jhi-delete-bankAccount-heading'));
    private confirmButton = element(by.id('jhi-confirm-delete-bankAccount'));

    async getDialogTitle() {
        return this.dialogTitle.getText();
    }

    async clickOnConfirmButton() {
        await this.confirmButton.click();
    }
}
