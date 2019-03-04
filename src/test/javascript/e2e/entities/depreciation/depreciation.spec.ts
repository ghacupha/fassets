/* tslint:disable no-unused-expression */
import { browser, ExpectedConditions as ec, promise } from 'protractor';
import { NavBarPage, SignInPage } from '../../page-objects/jhi-page-objects';

import { DepreciationComponentsPage, DepreciationDeleteDialog, DepreciationUpdatePage } from './depreciation.page-object';

const expect = chai.expect;

describe('Depreciation e2e test', () => {
    let navBarPage: NavBarPage;
    let signInPage: SignInPage;
    let depreciationUpdatePage: DepreciationUpdatePage;
    let depreciationComponentsPage: DepreciationComponentsPage;
    let depreciationDeleteDialog: DepreciationDeleteDialog;

    before(async () => {
        await browser.get('/');
        navBarPage = new NavBarPage();
        signInPage = await navBarPage.getSignInPage();
        await signInPage.autoSignInUsing('admin', 'admin');
        await browser.wait(ec.visibilityOf(navBarPage.entityMenu), 5000);
    });

    it('should load Depreciations', async () => {
        await navBarPage.goToEntity('depreciation');
        depreciationComponentsPage = new DepreciationComponentsPage();
        await browser.wait(ec.visibilityOf(depreciationComponentsPage.title), 5000);
        expect(await depreciationComponentsPage.getTitle()).to.eq('Depreciations');
    });

    it('should load create Depreciation page', async () => {
        await depreciationComponentsPage.clickOnCreateButton();
        depreciationUpdatePage = new DepreciationUpdatePage();
        expect(await depreciationUpdatePage.getPageTitle()).to.eq('Create or edit a Depreciation');
        await depreciationUpdatePage.cancel();
    });

    it('should create and save Depreciations', async () => {
        const nbButtonsBeforeCreate = await depreciationComponentsPage.countDeleteButtons();

        await depreciationComponentsPage.clickOnCreateButton();
        await promise.all([depreciationUpdatePage.setTypeOfDepreciationInput('typeOfDepreciation')]);
        expect(await depreciationUpdatePage.getTypeOfDepreciationInput()).to.eq('typeOfDepreciation');
        await depreciationUpdatePage.save();
        expect(await depreciationUpdatePage.getSaveButton().isPresent()).to.be.false;

        expect(await depreciationComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeCreate + 1);
    });

    it('should delete last Depreciation', async () => {
        const nbButtonsBeforeDelete = await depreciationComponentsPage.countDeleteButtons();
        await depreciationComponentsPage.clickOnLastDeleteButton();

        depreciationDeleteDialog = new DepreciationDeleteDialog();
        expect(await depreciationDeleteDialog.getDialogTitle()).to.eq('Are you sure you want to delete this Depreciation?');
        await depreciationDeleteDialog.clickOnConfirmButton();

        expect(await depreciationComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeDelete - 1);
    });

    after(async () => {
        await navBarPage.autoSignOut();
    });
});
