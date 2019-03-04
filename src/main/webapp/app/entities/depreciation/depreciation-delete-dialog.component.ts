import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { NgbActiveModal, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IDepreciation } from 'app/shared/model/depreciation.model';
import { DepreciationService } from './depreciation.service';

@Component({
    selector: 'jhi-depreciation-delete-dialog',
    templateUrl: './depreciation-delete-dialog.component.html'
})
export class DepreciationDeleteDialogComponent {
    depreciation: IDepreciation;

    constructor(
        protected depreciationService: DepreciationService,
        public activeModal: NgbActiveModal,
        protected eventManager: JhiEventManager
    ) {}

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.depreciationService.delete(id).subscribe(response => {
            this.eventManager.broadcast({
                name: 'depreciationListModification',
                content: 'Deleted an depreciation'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-depreciation-delete-popup',
    template: ''
})
export class DepreciationDeletePopupComponent implements OnInit, OnDestroy {
    protected ngbModalRef: NgbModalRef;

    constructor(protected activatedRoute: ActivatedRoute, protected router: Router, protected modalService: NgbModal) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ depreciation }) => {
            setTimeout(() => {
                this.ngbModalRef = this.modalService.open(DepreciationDeleteDialogComponent as Component, {
                    size: 'lg',
                    backdrop: 'static'
                });
                this.ngbModalRef.componentInstance.depreciation = depreciation;
                this.ngbModalRef.result.then(
                    result => {
                        this.router.navigate(['/depreciation', { outlets: { popup: null } }]);
                        this.ngbModalRef = null;
                    },
                    reason => {
                        this.router.navigate(['/depreciation', { outlets: { popup: null } }]);
                        this.ngbModalRef = null;
                    }
                );
            }, 0);
        });
    }

    ngOnDestroy() {
        this.ngbModalRef = null;
    }
}
