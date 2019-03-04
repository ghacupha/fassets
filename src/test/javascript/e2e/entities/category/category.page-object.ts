import { element, by, ElementFinder } from 'protractor';

export class CategoryComponentsPage {
    createButton = element(by.id('jh-create-entity'));
    deleteButtons = element.all(by.css('jhi-category div table .btn-danger'));
    title = element.all(by.css('jhi-category div h2#page-heading span')).first();

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

export class CategoryUpdatePage {
    pageTitle = element(by.id('jhi-category-heading'));
    saveButton = element(by.id('save-entity'));
    cancelButton = element(by.id('cancel-save'));
    categoryInput = element(by.id('field_category'));
    bankAccountSelect = element(by.id('field_bankAccount'));
    depreciationSelect = element(by.id('field_depreciation'));

    async getPageTitle() {
        return this.pageTitle.getText();
    }

    async setCategoryInput(category) {
        await this.categoryInput.sendKeys(category);
    }

    async getCategoryInput() {
        return this.categoryInput.getAttribute('value');
    }

    async bankAccountSelectLastOption() {
        await this.bankAccountSelect
            .all(by.tagName('option'))
            .last()
            .click();
    }

    async bankAccountSelectOption(option) {
        await this.bankAccountSelect.sendKeys(option);
    }

    getBankAccountSelect(): ElementFinder {
        return this.bankAccountSelect;
    }

    async getBankAccountSelectedOption() {
        return this.bankAccountSelect.element(by.css('option:checked')).getText();
    }

    async depreciationSelectLastOption() {
        await this.depreciationSelect
            .all(by.tagName('option'))
            .last()
            .click();
    }

    async depreciationSelectOption(option) {
        await this.depreciationSelect.sendKeys(option);
    }

    getDepreciationSelect(): ElementFinder {
        return this.depreciationSelect;
    }

    async getDepreciationSelectedOption() {
        return this.depreciationSelect.element(by.css('option:checked')).getText();
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

export class CategoryDeleteDialog {
    private dialogTitle = element(by.id('jhi-delete-category-heading'));
    private confirmButton = element(by.id('jhi-confirm-delete-category'));

    async getDialogTitle() {
        return this.dialogTitle.getText();
    }

    async clickOnConfirmButton() {
        await this.confirmButton.click();
    }
}
