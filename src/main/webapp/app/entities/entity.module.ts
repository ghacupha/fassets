import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

@NgModule({
    imports: [
        RouterModule.forChild([
            {
                path: 'asset',
                loadChildren: './asset/asset.module#FassetsAssetModule'
            },
            {
                path: 'asset',
                loadChildren: './asset/asset.module#FassetsAssetModule'
            },
            {
                path: 'asset',
                loadChildren: './asset/asset.module#FassetsAssetModule'
            },
            {
                path: 'asset',
                loadChildren: './asset/asset.module#FassetsAssetModule'
            },
            {
                path: 'service-outlet',
                loadChildren: './service-outlet/service-outlet.module#FassetsServiceOutletModule'
            },
            {
                path: 'category',
                loadChildren: './category/category.module#FassetsCategoryModule'
            },
            {
                path: 'bank-account',
                loadChildren: './bank-account/bank-account.module#FassetsBankAccountModule'
            },
            {
                path: 'depreciation',
                loadChildren: './depreciation/depreciation.module#FassetsDepreciationModule'
            },
            {
                path: 'asset',
                loadChildren: './asset/asset.module#FassetsAssetModule'
            },
            {
                path: 'bank-account',
                loadChildren: './bank-account/bank-account.module#FassetsBankAccountModule'
            },
            {
                path: 'bank-account',
                loadChildren: './bank-account/bank-account.module#FassetsBankAccountModule'
            }
            /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
        ])
    ],
    declarations: [],
    entryComponents: [],
    providers: [],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class FassetsEntityModule {}
