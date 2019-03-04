/* tslint:disable no-unused-expression */
import { browser, ExpectedConditions as ec, promise } from 'protractor';
import { NavBarPage, SignInPage } from '../../page-objects/jhi-page-objects';

import { AssetComponentsPage, AssetDeleteDialog, AssetUpdatePage } from './asset.page-object';

const expect = chai.expect;

describe('Asset e2e test', () => {
    let navBarPage: NavBarPage;
    let signInPage: SignInPage;
    let assetUpdatePage: AssetUpdatePage;
    let assetComponentsPage: AssetComponentsPage;
    /*let assetDeleteDialog: AssetDeleteDialog;*/

    before(async () => {
        await browser.get('/');
        navBarPage = new NavBarPage();
        signInPage = await navBarPage.getSignInPage();
        await signInPage.autoSignInUsing('admin', 'admin');
        await browser.wait(ec.visibilityOf(navBarPage.entityMenu), 5000);
    });

    it('should load Assets', async () => {
        await navBarPage.goToEntity('asset');
        assetComponentsPage = new AssetComponentsPage();
        await browser.wait(ec.visibilityOf(assetComponentsPage.title), 5000);
        expect(await assetComponentsPage.getTitle()).to.eq('Assets');
    });

    it('should load create Asset page', async () => {
        await assetComponentsPage.clickOnCreateButton();
        assetUpdatePage = new AssetUpdatePage();
        expect(await assetUpdatePage.getPageTitle()).to.eq('Create or edit a Asset');
        await assetUpdatePage.cancel();
    });

    /* it('should create and save Assets', async () => {
        const nbButtonsBeforeCreate = await assetComponentsPage.countDeleteButtons();

        await assetComponentsPage.clickOnCreateButton();
        await promise.all([
            assetUpdatePage.setDescriptionInput('description'),
            assetUpdatePage.setPurchaseDateInput('2000-12-31'),
            assetUpdatePage.setAssetTagInput('assetTag'),
            assetUpdatePage.setPurchaseCostInput('5'),
            assetUpdatePage.categorySelectLastOption(),
            assetUpdatePage.serviceOutletSelectLastOption(),
        ]);
        expect(await assetUpdatePage.getDescriptionInput()).to.eq('description');
        expect(await assetUpdatePage.getPurchaseDateInput()).to.eq('2000-12-31');
        expect(await assetUpdatePage.getAssetTagInput()).to.eq('assetTag');
        expect(await assetUpdatePage.getPurchaseCostInput()).to.eq('5');
        await assetUpdatePage.save();
        expect(await assetUpdatePage.getSaveButton().isPresent()).to.be.false;

        expect(await assetComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeCreate + 1);
    });*/

    /* it('should delete last Asset', async () => {
        const nbButtonsBeforeDelete = await assetComponentsPage.countDeleteButtons();
        await assetComponentsPage.clickOnLastDeleteButton();

        assetDeleteDialog = new AssetDeleteDialog();
        expect(await assetDeleteDialog.getDialogTitle())
            .to.eq('Are you sure you want to delete this Asset?');
        await assetDeleteDialog.clickOnConfirmButton();

        expect(await assetComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeDelete - 1);
    });*/

    after(async () => {
        await navBarPage.autoSignOut();
    });
});
