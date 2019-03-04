import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { IServiceOutlet } from 'app/shared/model/service-outlet.model';
import { ServiceOutletService } from './service-outlet.service';

@Component({
    selector: 'jhi-service-outlet-update',
    templateUrl: './service-outlet-update.component.html'
})
export class ServiceOutletUpdateComponent implements OnInit {
    serviceOutlet: IServiceOutlet;
    isSaving: boolean;

    constructor(protected serviceOutletService: ServiceOutletService, protected activatedRoute: ActivatedRoute) {}

    ngOnInit() {
        this.isSaving = false;
        this.activatedRoute.data.subscribe(({ serviceOutlet }) => {
            this.serviceOutlet = serviceOutlet;
        });
    }

    previousState() {
        window.history.back();
    }

    save() {
        this.isSaving = true;
        if (this.serviceOutlet.id !== undefined) {
            this.subscribeToSaveResponse(this.serviceOutletService.update(this.serviceOutlet));
        } else {
            this.subscribeToSaveResponse(this.serviceOutletService.create(this.serviceOutlet));
        }
    }

    protected subscribeToSaveResponse(result: Observable<HttpResponse<IServiceOutlet>>) {
        result.subscribe((res: HttpResponse<IServiceOutlet>) => this.onSaveSuccess(), (res: HttpErrorResponse) => this.onSaveError());
    }

    protected onSaveSuccess() {
        this.isSaving = false;
        this.previousState();
    }

    protected onSaveError() {
        this.isSaving = false;
    }
}
