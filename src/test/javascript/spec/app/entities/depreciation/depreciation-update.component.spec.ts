/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { Observable, of } from 'rxjs';

import { FassetsTestModule } from '../../../test.module';
import { DepreciationUpdateComponent } from 'app/entities/depreciation/depreciation-update.component';
import { DepreciationService } from 'app/entities/depreciation/depreciation.service';
import { Depreciation } from 'app/shared/model/depreciation.model';

describe('Component Tests', () => {
    describe('Depreciation Management Update Component', () => {
        let comp: DepreciationUpdateComponent;
        let fixture: ComponentFixture<DepreciationUpdateComponent>;
        let service: DepreciationService;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [FassetsTestModule],
                declarations: [DepreciationUpdateComponent]
            })
                .overrideTemplate(DepreciationUpdateComponent, '')
                .compileComponents();

            fixture = TestBed.createComponent(DepreciationUpdateComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(DepreciationService);
        });

        describe('save', () => {
            it('Should call update service on save for existing entity', fakeAsync(() => {
                // GIVEN
                const entity = new Depreciation(123);
                spyOn(service, 'update').and.returnValue(of(new HttpResponse({ body: entity })));
                comp.depreciation = entity;
                // WHEN
                comp.save();
                tick(); // simulate async

                // THEN
                expect(service.update).toHaveBeenCalledWith(entity);
                expect(comp.isSaving).toEqual(false);
            }));

            it('Should call create service on save for new entity', fakeAsync(() => {
                // GIVEN
                const entity = new Depreciation();
                spyOn(service, 'create').and.returnValue(of(new HttpResponse({ body: entity })));
                comp.depreciation = entity;
                // WHEN
                comp.save();
                tick(); // simulate async

                // THEN
                expect(service.create).toHaveBeenCalledWith(entity);
                expect(comp.isSaving).toEqual(false);
            }));
        });
    });
});
