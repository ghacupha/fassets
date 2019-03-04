import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IDepreciation } from 'app/shared/model/depreciation.model';

@Component({
    selector: 'jhi-depreciation-detail',
    templateUrl: './depreciation-detail.component.html'
})
export class DepreciationDetailComponent implements OnInit {
    depreciation: IDepreciation;

    constructor(protected activatedRoute: ActivatedRoute) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ depreciation }) => {
            this.depreciation = depreciation;
        });
    }

    previousState() {
        window.history.back();
    }
}
