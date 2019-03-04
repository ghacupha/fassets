/* tslint:disable no-unused-expression */
import { browser, ExpectedConditions as ec, promise } from 'protractor';
import { NavBarPage, SignInPage } from '../../page-objects/jhi-page-objects';

import { ServiceOutletComponentsPage, ServiceOutletDeleteDialog, ServiceOutletUpdatePage } from './service-outlet.page-object';

const expect = chai.expect;

describe('ServiceOutlet e2e test', () => {
    let navBarPage: NavBarPage;
    let signInPage: SignInPage;
    let serviceOutletUpdatePage: ServiceOutletUpdatePage;
    let serviceOutletComponentsPage: ServiceOutletComponentsPage;
    let serviceOutletDeleteDialog: ServiceOutletDeleteDialog;

    before(async () => {
        await browser.get('/');
        navBarPage = new NavBarPage();
        signInPage = await navBarPage.getSignInPage();
        await signInPage.autoSignInUsing('admin', 'admin');
        await browser.wait(ec.visibilityOf(navBarPage.entityMenu), 5000);
    });

    it('should load ServiceOutlets', async () => {
        await navBarPage.goToEntity('service-outlet');
        serviceOutletComponentsPage = new ServiceOutletComponentsPage();
        await browser.wait(ec.visibilityOf(serviceOutletComponentsPage.title), 5000);
        expect(await serviceOutletComponentsPage.getTitle()).to.eq('Service Outlets');
    });

    it('should load create ServiceOutlet page', async () => {
        await serviceOutletComponentsPage.clickOnCreateButton();
        serviceOutletUpdatePage = new ServiceOutletUpdatePage();
        expect(await serviceOutletUpdatePage.getPageTitle()).to.eq('Create or edit a Service Outlet');
        await serviceOutletUpdatePage.cancel();
    });

    it('should create and save ServiceOutlets', async () => {
        const nbButtonsBeforeCreate = await serviceOutletComponentsPage.countDeleteButtons();

        await serviceOutletComponentsPage.clickOnCreateButton();
        await promise.all([
            serviceOutletUpdatePage.setServiceOutletInput('serviceOutlet'),
            serviceOutletUpdatePage.setServiceOutletCodeInput('serviceOutletCode')
        ]);
        expect(await serviceOutletUpdatePage.getServiceOutletInput()).to.eq('serviceOutlet');
        expect(await serviceOutletUpdatePage.getServiceOutletCodeInput()).to.eq('serviceOutletCode');
        await serviceOutletUpdatePage.save();
        expect(await serviceOutletUpdatePage.getSaveButton().isPresent()).to.be.false;

        expect(await serviceOutletComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeCreate + 1);
    });

    it('should delete last ServiceOutlet', async () => {
        const nbButtonsBeforeDelete = await serviceOutletComponentsPage.countDeleteButtons();
        await serviceOutletComponentsPage.clickOnLastDeleteButton();

        serviceOutletDeleteDialog = new ServiceOutletDeleteDialog();
        expect(await serviceOutletDeleteDialog.getDialogTitle()).to.eq('Are you sure you want to delete this Service Outlet?');
        await serviceOutletDeleteDialog.clickOnConfirmButton();

        expect(await serviceOutletComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeDelete - 1);
    });

    after(async () => {
        await navBarPage.autoSignOut();
    });
});
