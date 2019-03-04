import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { FassetsSharedModule } from 'app/shared';
import {
    DepreciationComponent,
    DepreciationDetailComponent,
    DepreciationUpdateComponent,
    DepreciationDeletePopupComponent,
    DepreciationDeleteDialogComponent,
    depreciationRoute,
    depreciationPopupRoute
} from './';

const ENTITY_STATES = [...depreciationRoute, ...depreciationPopupRoute];

@NgModule({
    imports: [FassetsSharedModule, RouterModule.forChild(ENTITY_STATES)],
    declarations: [
        DepreciationComponent,
        DepreciationDetailComponent,
        DepreciationUpdateComponent,
        DepreciationDeleteDialogComponent,
        DepreciationDeletePopupComponent
    ],
    entryComponents: [
        DepreciationComponent,
        DepreciationUpdateComponent,
        DepreciationDeleteDialogComponent,
        DepreciationDeletePopupComponent
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class FassetsDepreciationModule {}
