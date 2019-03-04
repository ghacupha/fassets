import { element, by, ElementFinder } from 'protractor';

export class ServiceOutletComponentsPage {
    createButton = element(by.id('jh-create-entity'));
    deleteButtons = element.all(by.css('jhi-service-outlet div table .btn-danger'));
    title = element.all(by.css('jhi-service-outlet div h2#page-heading span')).first();

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

export class ServiceOutletUpdatePage {
    pageTitle = element(by.id('jhi-service-outlet-heading'));
    saveButton = element(by.id('save-entity'));
    cancelButton = element(by.id('cancel-save'));
    serviceOutletInput = element(by.id('field_serviceOutlet'));
    serviceOutletCodeInput = element(by.id('field_serviceOutletCode'));

    async getPageTitle() {
        return this.pageTitle.getText();
    }

    async setServiceOutletInput(serviceOutlet) {
        await this.serviceOutletInput.sendKeys(serviceOutlet);
    }

    async getServiceOutletInput() {
        return this.serviceOutletInput.getAttribute('value');
    }

    async setServiceOutletCodeInput(serviceOutletCode) {
        await this.serviceOutletCodeInput.sendKeys(serviceOutletCode);
    }

    async getServiceOutletCodeInput() {
        return this.serviceOutletCodeInput.getAttribute('value');
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

export class ServiceOutletDeleteDialog {
    private dialogTitle = element(by.id('jhi-delete-serviceOutlet-heading'));
    private confirmButton = element(by.id('jhi-confirm-delete-serviceOutlet'));

    async getDialogTitle() {
        return this.dialogTitle.getText();
    }

    async clickOnConfirmButton() {
        await this.confirmButton.click();
    }
}
