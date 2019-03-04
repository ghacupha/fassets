import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { JhiPaginationUtil, JhiResolvePagingParams } from 'ng-jhipster';
import { UserRouteAccessService } from 'app/core';
import { Observable, of } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { Depreciation } from 'app/shared/model/depreciation.model';
import { DepreciationService } from './depreciation.service';
import { DepreciationComponent } from './depreciation.component';
import { DepreciationDetailComponent } from './depreciation-detail.component';
import { DepreciationUpdateComponent } from './depreciation-update.component';
import { DepreciationDeletePopupComponent } from './depreciation-delete-dialog.component';
import { IDepreciation } from 'app/shared/model/depreciation.model';

@Injectable({ providedIn: 'root' })
export class DepreciationResolve implements Resolve<IDepreciation> {
    constructor(private service: DepreciationService) {}

    resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<IDepreciation> {
        const id = route.params['id'] ? route.params['id'] : null;
        if (id) {
            return this.service.find(id).pipe(
                filter((response: HttpResponse<Depreciation>) => response.ok),
                map((depreciation: HttpResponse<Depreciation>) => depreciation.body)
            );
        }
        return of(new Depreciation());
    }
}

export const depreciationRoute: Routes = [
    {
        path: '',
        component: DepreciationComponent,
        resolve: {
            pagingParams: JhiResolvePagingParams
        },
        data: {
            authorities: ['ROLE_USER'],
            defaultSort: 'id,asc',
            pageTitle: 'Depreciations'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: ':id/view',
        component: DepreciationDetailComponent,
        resolve: {
            depreciation: DepreciationResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Depreciations'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'new',
        component: DepreciationUpdateComponent,
        resolve: {
            depreciation: DepreciationResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Depreciations'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: ':id/edit',
        component: DepreciationUpdateComponent,
        resolve: {
            depreciation: DepreciationResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Depreciations'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const depreciationPopupRoute: Routes = [
    {
        path: ':id/delete',
        component: DepreciationDeletePopupComponent,
        resolve: {
            depreciation: DepreciationResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Depreciations'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
