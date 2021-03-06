/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { Observable, of } from 'rxjs';

import { FassetsTestModule } from '../../../test.module';
import { ServiceOutletUpdateComponent } from 'app/entities/service-outlet/service-outlet-update.component';
import { ServiceOutletService } from 'app/entities/service-outlet/service-outlet.service';
import { ServiceOutlet } from 'app/shared/model/service-outlet.model';

describe('Component Tests', () => {
    describe('ServiceOutlet Management Update Component', () => {
        let comp: ServiceOutletUpdateComponent;
        let fixture: ComponentFixture<ServiceOutletUpdateComponent>;
        let service: ServiceOutletService;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [FassetsTestModule],
                declarations: [ServiceOutletUpdateComponent]
            })
                .overrideTemplate(ServiceOutletUpdateComponent, '')
                .compileComponents();

            fixture = TestBed.createComponent(ServiceOutletUpdateComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(ServiceOutletService);
        });

        describe('save', () => {
            it('Should call update service on save for existing entity', fakeAsync(() => {
                // GIVEN
                const entity = new ServiceOutlet(123);
                spyOn(service, 'update').and.returnValue(of(new HttpResponse({ body: entity })));
                comp.serviceOutlet = entity;
                // WHEN
                comp.save();
                tick(); // simulate async

                // THEN
                expect(service.update).toHaveBeenCalledWith(entity);
                expect(comp.isSaving).toEqual(false);
            }));

            it('Should call create service on save for new entity', fakeAsync(() => {
                // GIVEN
                const entity = new ServiceOutlet();
                spyOn(service, 'create').and.returnValue(of(new HttpResponse({ body: entity })));
                comp.serviceOutlet = entity;
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
