import { NgModule } from '@angular/core';

import { FassetsSharedLibsModule, JhiAlertComponent, JhiAlertErrorComponent } from './';

@NgModule({
    imports: [FassetsSharedLibsModule],
    declarations: [JhiAlertComponent, JhiAlertErrorComponent],
    exports: [FassetsSharedLibsModule, JhiAlertComponent, JhiAlertErrorComponent]
})
export class FassetsSharedCommonModule {}
