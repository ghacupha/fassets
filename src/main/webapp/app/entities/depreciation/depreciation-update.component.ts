import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { JhiAlertService } from 'ng-jhipster';
import { IDepreciation } from 'app/shared/model/depreciation.model';
import { DepreciationService } from './depreciation.service';
import { ICategory } from 'app/shared/model/category.model';
import { CategoryService } from 'app/entities/category';

@Component({
    selector: 'jhi-depreciation-update',
    templateUrl: './depreciation-update.component.html'
})
export class DepreciationUpdateComponent implements OnInit {
    depreciation: IDepreciation;
    isSaving: boolean;

    categories: ICategory[];

    constructor(
        protected jhiAlertService: JhiAlertService,
        protected depreciationService: DepreciationService,
        protected categoryService: CategoryService,
        protected activatedRoute: ActivatedRoute
    ) {}

    ngOnInit() {
        this.isSaving = false;
        this.activatedRoute.data.subscribe(({ depreciation }) => {
            this.depreciation = depreciation;
        });
        this.categoryService
            .query()
            .pipe(
                filter((mayBeOk: HttpResponse<ICategory[]>) => mayBeOk.ok),
                map((response: HttpResponse<ICategory[]>) => response.body)
            )
            .subscribe((res: ICategory[]) => (this.categories = res), (res: HttpErrorResponse) => this.onError(res.message));
    }

    previousState() {
        window.history.back();
    }

    save() {
        this.isSaving = true;
        if (this.depreciation.id !== undefined) {
            this.subscribeToSaveResponse(this.depreciationService.update(this.depreciation));
        } else {
            this.subscribeToSaveResponse(this.depreciationService.create(this.depreciation));
        }
    }

    protected subscribeToSaveResponse(result: Observable<HttpResponse<IDepreciation>>) {
        result.subscribe((res: HttpResponse<IDepreciation>) => this.onSaveSuccess(), (res: HttpErrorResponse) => this.onSaveError());
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
}
