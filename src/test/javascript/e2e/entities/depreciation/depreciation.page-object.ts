import { element, by, ElementFinder } from 'protractor';

export class DepreciationComponentsPage {
    createButton = element(by.id('jh-create-entity'));
    deleteButtons = element.all(by.css('jhi-depreciation div table .btn-danger'));
    title = element.all(by.css('jhi-depreciation div h2#page-heading span')).first();

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

export class DepreciationUpdatePage {
    pageTitle = element(by.id('jhi-depreciation-heading'));
    saveButton = element(by.id('save-entity'));
    cancelButton = element(by.id('cancel-save'));
    typeOfDepreciationInput = element(by.id('field_typeOfDepreciation'));

    async getPageTitle() {
        return this.pageTitle.getText();
    }

    async setTypeOfDepreciationInput(typeOfDepreciation) {
        await this.typeOfDepreciationInput.sendKeys(typeOfDepreciation);
    }

    async getTypeOfDepreciationInput() {
        return this.typeOfDepreciationInput.getAttribute('value');
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

export class DepreciationDeleteDialog {
    private dialogTitle = element(by.id('jhi-delete-depreciation-heading'));
    private confirmButton = element(by.id('jhi-confirm-delete-depreciation'));

    async getDialogTitle() {
        return this.dialogTitle.getText();
    }

    async clickOnConfirmButton() {
        await this.confirmButton.click();
    }
}
