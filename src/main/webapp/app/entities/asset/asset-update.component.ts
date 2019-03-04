import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import * as moment from 'moment';
import { JhiAlertService } from 'ng-jhipster';
import { IAsset } from 'app/shared/model/asset.model';
import { AssetService } from './asset.service';
import { ICategory } from 'app/shared/model/category.model';
import { CategoryService } from 'app/entities/category';
import { IServiceOutlet } from 'app/shared/model/service-outlet.model';
import { ServiceOutletService } from 'app/entities/service-outlet';

@Component({
    selector: 'jhi-asset-update',
    templateUrl: './asset-update.component.html'
})
export class AssetUpdateComponent implements OnInit {
    asset: IAsset;
    isSaving: boolean;

    categories: ICategory[];

    serviceoutlets: IServiceOutlet[];
    purchaseDateDp: any;

    constructor(
        protected jhiAlertService: JhiAlertService,
        protected assetService: AssetService,
        protected categoryService: CategoryService,
        protected serviceOutletService: ServiceOutletService,
        protected activatedRoute: ActivatedRoute
    ) {}

    ngOnInit() {
        this.isSaving = false;
        this.activatedRoute.data.subscribe(({ asset }) => {
            this.asset = asset;
        });
        this.categoryService
            .query()
            .pipe(
                filter((mayBeOk: HttpResponse<ICategory[]>) => mayBeOk.ok),
                map((response: HttpResponse<ICategory[]>) => response.body)
            )
            .subscribe((res: ICategory[]) => (this.categories = res), (res: HttpErrorResponse) => this.onError(res.message));
        this.serviceOutletService
            .query()
            .pipe(
                filter((mayBeOk: HttpResponse<IServiceOutlet[]>) => mayBeOk.ok),
                map((response: HttpResponse<IServiceOutlet[]>) => response.body)
            )
            .subscribe((res: IServiceOutlet[]) => (this.serviceoutlets = res), (res: HttpErrorResponse) => this.onError(res.message));
    }

    previousState() {
        window.history.back();
    }

    save() {
        this.isSaving = true;
        if (this.asset.id !== undefined) {
            this.subscribeToSaveResponse(this.assetService.update(this.asset));
        } else {
            this.subscribeToSaveResponse(this.assetService.create(this.asset));
        }
    }

    protected subscribeToSaveResponse(result: Observable<HttpResponse<IAsset>>) {
        result.subscribe((res: HttpResponse<IAsset>) => this.onSaveSuccess(), (res: HttpErrorResponse) => this.onSaveError());
    }

    protected onSaveSuccess() {
        this.isSaving = false;
        this.previousState();
    }

    protected onSaveError() {
        this.isSaving = false;
    }

    protected onError(errorMessage: string) {
        this.jhiAlertService.error(errorMessage, null, null);
    }

    trackCategoryById(index: number, item: ICategory) {
        return item.id;
    }

    trackServiceOutletById(index: number, item: IServiceOutlet) {
        return item.id;
    }
}
