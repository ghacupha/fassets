import { element, by, ElementFinder } from 'protractor';

export class AssetComponentsPage {
    createButton = element(by.id('jh-create-entity'));
    deleteButtons = element.all(by.css('jhi-asset div table .btn-danger'));
    title = element.all(by.css('jhi-asset div h2#page-heading span')).first();

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

export class AssetUpdatePage {
    pageTitle = element(by.id('jhi-asset-heading'));
    saveButton = element(by.id('save-entity'));
    cancelButton = element(by.id('cancel-save'));
    descriptionInput = element(by.id('field_description'));
    purchaseDateInput = element(by.id('field_purchaseDate'));
    assetTagInput = element(by.id('field_assetTag'));
    purchaseCostInput = element(by.id('field_purchaseCost'));
    categorySelect = element(by.id('field_category'));
    serviceOutletSelect = element(by.id('field_serviceOutlet'));

    async getPageTitle() {
        return this.pageTitle.getText();
    }

    async setDescriptionInput(description) {
        await this.descriptionInput.sendKeys(description);
    }

    async getDescriptionInput() {
        return this.descriptionInput.getAttribute('value');
    }

    async setPurchaseDateInput(purchaseDate) {
        await this.purchaseDateInput.sendKeys(purchaseDate);
    }

    async getPurchaseDateInput() {
        return this.purchaseDateInput.getAttribute('value');
    }

    async setAssetTagInput(assetTag) {
        await this.assetTagInput.sendKeys(assetTag);
    }

    async getAssetTagInput() {
        return this.assetTagInput.getAttribute('value');
    }

    async setPurchaseCostInput(purchaseCost) {
        await this.purchaseCostInput.sendKeys(purchaseCost);
    }

    async getPurchaseCostInput() {
        return this.purchaseCostInput.getAttribute('value');
    }

    async categorySelectLastOption() {
        await this.categorySelect
            .all(by.tagName('option'))
            .last()
            .click();
    }

    async categorySelectOption(option) {
        await this.categorySelect.sendKeys(option);
    }

    getCategorySelect(): ElementFinder {
        return this.categorySelect;
    }

    async getCategorySelectedOption() {
        return this.categorySelect.element(by.css('option:checked')).getText();
    }

    async serviceOutletSelectLastOption() {
        await this.serviceOutletSelect
            .all(by.tagName('option'))
            .last()
            .click();
    }

    async serviceOutletSelectOption(option) {
        await this.serviceOutletSelect.sendKeys(option);
    }

    getServiceOutletSelect(): ElementFinder {
        return this.serviceOutletSelect;
    }

    async getServiceOutletSelectedOption() {
        return this.serviceOutletSelect.element(by.css('option:checked')).getText();
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

export class AssetDeleteDialog {
    private dialogTitle = element(by.id('jhi-delete-asset-heading'));
    private confirmButton = element(by.id('jhi-confirm-delete-asset'));

    async getDialogTitle() {
        return this.dialogTitle.getText();
    }

    async clickOnConfirmButton() {
        await this.confirmButton.click();
    }
}
