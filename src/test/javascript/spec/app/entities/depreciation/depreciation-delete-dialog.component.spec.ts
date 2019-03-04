/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, inject, fakeAsync, tick } from '@angular/core/testing';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { Observable, of } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';

import { FassetsTestModule } from '../../../test.module';
import { DepreciationDeleteDialogComponent } from 'app/entities/depreciation/depreciation-delete-dialog.component';
import { DepreciationService } from 'app/entities/depreciation/depreciation.service';

describe('Component Tests', () => {
    describe('Depreciation Management Delete Component', () => {
        let comp: DepreciationDeleteDialogComponent;
        let fixture: ComponentFixture<DepreciationDeleteDialogComponent>;
        let service: DepreciationService;
        let mockEventManager: any;
        let mockActiveModal: any;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [FassetsTestModule],
                declarations: [DepreciationDeleteDialogComponent]
            })
                .overrideTemplate(DepreciationDeleteDialogComponent, '')
                .compileComponents();
            fixture = TestBed.createComponent(DepreciationDeleteDialogComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(DepreciationService);
            mockEventManager = fixture.debugElement.injector.get(JhiEventManager);
            mockActiveModal = fixture.debugElement.injector.get(NgbActiveModal);
        });

        describe('confirmDelete', () => {
            it('Should call delete service on confirmDelete', inject(
                [],
                fakeAsync(() => {
                    // GIVEN
                    spyOn(service, 'delete').and.returnValue(of({}));

                    // WHEN
                    comp.confirmDelete(123);
                    tick();

                    // THEN
                    expect(service.delete).toHaveBeenCalledWith(123);
                    expect(mockActiveModal.dismissSpy).toHaveBeenCalled();
                    expect(mockEventManager.broadcastSpy).toHaveBeenCalled();
                })
            ));
        });
    });
});
