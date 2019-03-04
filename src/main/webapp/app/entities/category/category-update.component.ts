import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { JhiAlertService } from 'ng-jhipster';
import { ICategory } from 'app/shared/model/category.model';
import { CategoryService } from './category.service';
import { IBankAccount } from 'app/shared/model/bank-account.model';
import { BankAccountService } from 'app/entities/bank-account';
import { IDepreciation } from 'app/shared/model/depreciation.model';
import { DepreciationService } from 'app/entities/depreciation';

@Component({
    selector: 'jhi-category-update',
    templateUrl: './category-update.component.html'
})
export class CategoryUpdateComponent implements OnInit {
    category: ICategory;
    isSaving: boolean;

    bankaccounts: IBankAccount[];

    depreciations: IDepreciation[];

    constructor(
        protected jhiAlertService: JhiAlertService,
        protected categoryService: CategoryService,
        protected bankAccountService: BankAccountService,
        protected depreciationService: DepreciationService,
        protected activatedRoute: ActivatedRoute
    ) {}

    ngOnInit() {
        this.isSaving = false;
        this.activatedRoute.data.subscribe(({ category }) => {
            this.category = category;
        });
        this.bankAccountService
            .query({ 'categoryId.specified': 'false' })
            .pipe(
                filter((mayBeOk: HttpResponse<IBankAccount[]>) => mayBeOk.ok),
                map((response: HttpResponse<IBankAccount[]>) => response.body)
            )
            .subscribe(
                (res: IBankAccount[]) => {
                    if (!this.category.bankAccountId) {
                        this.bankaccounts = res;
                    } else {
                        this.bankAccountService
                            .find(this.category.bankAccountId)
                            .pipe(
                                filter((subResMayBeOk: HttpResponse<IBankAccount>) => subResMayBeOk.ok),
                                map((subResponse: HttpResponse<IBankAccount>) => subResponse.body)
                            )
                            .subscribe(
                                (subRes: IBankAccount) => (this.bankaccounts = [subRes].concat(res)),
                                (subRes: HttpErrorResponse) => this.onError(subRes.message)
                            );
                    }
                },
                (res: HttpErrorResponse) => this.onError(res.message)
            );
        this.depreciationService
            .query({ 'categoryId.specified': 'false' })
            .pipe(
                filter((mayBeOk: HttpResponse<IDepreciation[]>) => mayBeOk.ok),
                map((response: HttpResponse<IDepreciation[]>) => response.body)
            )
            .subscribe(
                (res: IDepreciation[]) => {
                    if (!this.category.depreciationId) {
                        this.depreciations = res;
                    } else {
                        this.depreciationService
                            .find(this.category.depreciationId)
                            .pipe(
                                filter((subResMayBeOk: HttpResponse<IDepreciation>) => subResMayBeOk.ok),
                                map((subResponse: HttpResponse<IDepreciation>) => subResponse.body)
                            )
                            .subscribe(
                                (subRes: IDepreciation) => (this.depreciations = [subRes].concat(res)),
                                (subRes: HttpErrorResponse) => this.onError(subRes.message)
                            );
                    }
                },
                (res: HttpErrorResponse) => this.onError(res.message)
            );
    }

    previousState() {
        window.history.back();
    }

    save() {
        this.isSaving = true;
        if (this.category.id !== undefined) {
            this.subscribeToSaveResponse(this.categoryService.update(this.category));
        } else {
            this.subscribeToSaveResponse(this.categoryService.create(this.category));
        }
    }

    protected subscribeToSaveResponse(result: Observable<HttpResponse<ICategory>>) {
        result.subscribe((res: HttpResponse<ICategory>) => this.onSaveSuccess(), (res: HttpErrorResponse) => this.onSaveError());
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

    trackBankAccountById(index: number, item: IBankAccount) {
        return item.id;
    }

    trackDepreciationById(index: number, item: IDepreciation) {
        return item.id;
    }
}
